package com.cwidanage.dhis2.publisher.services;

import com.cwidanage.dhis2.common.models.Event;
import com.cwidanage.dhis2.common.models.Setting;
import com.cwidanage.dhis2.common.models.TransmittableEvent;
import com.cwidanage.dhis2.common.services.SettingService;
import com.cwidanage.dhis2.common.services.TransmittableEventService;
import com.cwidanage.dhis2.publisher.Configuration;
import com.cwidanage.dhis2.publisher.models.EventsResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PreDestroy;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@EnableScheduling
@EnableAsync
public class EventFetchService {

    private final static Logger logger = LogManager.getLogger(EventFetchService.class);
    private final static String SETTING_LAST_FETCHED_DATE = "SETTING_LAST_FETCHED_DATE";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Value("${dhis2.username}")
    private String dhis2Username;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${dhis2.apiEndPoint}")
    private String dhis2ApiEndpoint;

    @Autowired
    private Configuration configuration;

    @Autowired
    private TransmittableEventService transmittableEventService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private DHIS2ConnectionTrottler dhis2ConnectionTrottler;

    private DB slowDB = DBMaker
            .fileDB("fetchedEventsCache").make();


    private HTreeMap.KeySet<String> slowEventIdSet = slowDB.hashSet("slowEventIdSet", Serializer.STRING)
            .expireAfterCreate(1, TimeUnit.DAYS)
            .create();

    private ExecutorService executor = Executors.newCachedThreadPool();

    @PreDestroy
    public void onDestroy() {
        this.slowDB.close();
    }

    @Scheduled(fixedDelay = 1000 * 30)
    public void fetch() {

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(dhis2ApiEndpoint);
        uriComponentsBuilder.path("events.json");

        uriComponentsBuilder.queryParam("program", "");
        uriComponentsBuilder.queryParam("programStage", "");

        uriComponentsBuilder.queryParam("skipPaging", "false");
        uriComponentsBuilder.queryParam("totalPages", "true");
        uriComponentsBuilder.queryParam("page", "1");

        configuration.getPrograms().forEach(program -> {
            logger.debug("Scanning events for program {}", program.getId());
            uriComponentsBuilder.replaceQueryParam("program", program.getId());

            program.getProgramStages().forEach(programStage -> {

                Setting lastFetchedDateSetting = this.settingService.getValue(SETTING_LAST_FETCHED_DATE + "_" + programStage.getId());
                final String lastFetchedDate = lastFetchedDateSetting != null ? lastFetchedDateSetting.getValue() : "2010-05-15";
                String nextFetchDate = simpleDateFormat.format(this.getOneDayLessToday());
                logger.debug("Last fetch date of events of {} : {}", programStage.getId(), lastFetchedDate);

                logger.debug("Scanning events for program stage {} of {}", programStage.getId(), program.getId());
                uriComponentsBuilder.replaceQueryParam("programStage", programStage.getId());
                uriComponentsBuilder.replaceQueryParam("lastUpdatedStartDate", lastFetchedDate);

                int pageToFetch = 1;
                int totalPages = 0;
                do {
                    try {
                        this.dhis2ConnectionTrottler.acquire();
                        logger.debug("Fetching page {} / {} of events from {}", pageToFetch, totalPages, lastFetchedDate);
                        EventsResponse eventsResponse = this.fetchPage(uriComponentsBuilder, pageToFetch);
                        totalPages = eventsResponse.getPager().getPageCount();
                        pageToFetch = eventsResponse.getPager().getPage() + 1;
                        processEventResponse(eventsResponse);
                    } catch (InterruptedException e) {
                        logger.error("Error in fetching events of page {} of {}. Will retry the same page...", pageToFetch, programStage.getId());
                    } finally {
                        this.dhis2ConnectionTrottler.release();
                    }
                } while (pageToFetch < totalPages);

                logger.debug("Setting next fetch date of {} to {}", programStage.getId(), nextFetchDate);
                settingService.setValue(SETTING_LAST_FETCHED_DATE + "_" + programStage.getId(), nextFetchDate);
            });
        });
    }

    private boolean shouldProcessEvent(Event event) {
        return !dhis2Username.equals(event.getStoredBy()) && !this.slowEventIdSet.contains(event.getEvent());
    }

    public void putInCache(String eventId) {
        this.slowEventIdSet.add(eventId);
    }

    public void processEventResponse(EventsResponse eventsResponse) {
        executor.submit(() -> {
            List<TransmittableEvent> transmittableEvents = eventsResponse.getEvents()
                    .stream()
                    .filter(this::shouldProcessEvent)
                    .map(event -> new TransmittableEvent(event, this.configuration.getInstanceId()))
                    .collect(Collectors.toList());
            logger.debug("Filtered out {} already sent events out of {}", eventsResponse.getEvents().size() - transmittableEvents.size(), eventsResponse.getEvents().size());
            transmittableEventService.save(transmittableEvents);

            //adding to cache
            transmittableEvents.forEach(transmittableEvent -> this.putInCache(transmittableEvent.getEvent().getEvent()));
        });
    }

    private Date getOneDayLessToday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    private EventsResponse fetchPage(UriComponentsBuilder uriComponentsBuilder, int page) {
        uriComponentsBuilder.replaceQueryParam("page", page);
        ResponseEntity<EventsResponse> responseEntity = restTemplate.getForEntity(uriComponentsBuilder.toUriString(), EventsResponse.class);
        return responseEntity.getBody();
    }
}
