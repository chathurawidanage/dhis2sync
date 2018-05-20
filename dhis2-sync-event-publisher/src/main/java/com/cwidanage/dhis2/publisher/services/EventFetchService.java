package com.cwidanage.dhis2.publisher.services;

import com.cwidanage.dhis2.common.models.Setting;
import com.cwidanage.dhis2.common.models.TransmittableEvent;
import com.cwidanage.dhis2.common.repositories.EventRepository;
import com.cwidanage.dhis2.common.repositories.TransmittableEventRepository;
import com.cwidanage.dhis2.common.services.SettingService;
import com.cwidanage.dhis2.common.services.TransmittableEventService;
import com.cwidanage.dhis2.publisher.Configuration;
import com.cwidanage.dhis2.publisher.models.EventsResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class EventFetchService {

    private final static Logger logger = LogManager.getLogger(EventFetchService.class);
    private final static String SETTING_LAST_FETCHED_DATE = "SETTING_LAST_FETCHED_DATE";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private RestTemplate restTemplate;

    @Value("${dhis2.apiEndPoint}")
    String dhis2ApiEndpoint;

    @Autowired
    private Configuration configuration;

    @Autowired
    private TransmittableEventService transmittableEventService;

    @Autowired
    private SettingService settingService;

    private Cache<String, Boolean> fetchedEventsCache = new Cache2kBuilder<String, Boolean>() {
    }
            .expireAfterWrite(1, TimeUnit.DAYS)
            .resilienceDuration(30, TimeUnit.SECONDS)
            .build();

    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 2)
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
                final String lastFetchedDate = lastFetchedDateSetting != null ? lastFetchedDateSetting.getValue() : "2018-05-15";
                String nextFetchDate = simpleDateFormat.format(new Date());
                logger.debug("Last fetch date of events of {} : {}", programStage.getId(), lastFetchedDate);

                logger.debug("Scanning events for program stage {} of {}", programStage.getId(), program.getId());
                uriComponentsBuilder.replaceQueryParam("programStage", programStage.getId());
                uriComponentsBuilder.replaceQueryParam("startDate", lastFetchedDate);

                int pageToFetch = 1;
                int totalPages;
                do {
                    logger.debug("Fetching page {} of events from {}", pageToFetch, lastFetchedDate);
                    EventsResponse eventsResponse = this.fetchPage(uriComponentsBuilder, pageToFetch);
                    totalPages = eventsResponse.getPager().getPageCount();
                    pageToFetch = eventsResponse.getPager().getPage() + 1;
                    List<TransmittableEvent> transmittableEvents = eventsResponse.getEvents()
                            .stream()
                            .filter(event -> !fetchedEventsCache.containsKey(event.getEvent()))
                            .map(event -> new TransmittableEvent(event, this.configuration.getInstanceId()))
                            .collect(Collectors.toList());
                    logger.debug("Filtered out {} already sent events", transmittableEvents.size() - eventsResponse.getEvents().size());
                    transmittableEventService.save(transmittableEvents);
                    transmittableEvents.forEach(transmittableEvent -> {
                        fetchedEventsCache.put(transmittableEvent.getEvent().getEvent(), true);
                    });
                } while (pageToFetch < totalPages && pageToFetch < 5);

                logger.debug("Setting next fetch date of {} to {}", programStage.getId(), nextFetchDate);
                settingService.setValue(SETTING_LAST_FETCHED_DATE + "_" + programStage.getId(), nextFetchDate);
            });
        });
    }

    private EventsResponse fetchPage(UriComponentsBuilder uriComponentsBuilder, int page) {
        uriComponentsBuilder.replaceQueryParam("page", page);
        ResponseEntity<EventsResponse> responseEntity = restTemplate.getForEntity(uriComponentsBuilder.toUriString(), EventsResponse.class);
        return responseEntity.getBody();
    }
}
