package com.cwidanage.dhis2.publisher.services;

import com.cwidanage.dhis2.common.repositories.EventRepository;
import com.cwidanage.dhis2.publisher.Configuration;
import com.cwidanage.dhis2.publisher.models.EventsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@EnableScheduling
public class PublisherService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${dhis2.apiEndPoint}")
    String dhis2ApiEndpoint;

    @Autowired
    private Configuration configuration;

    @Autowired
    private EventRepository eventRepository;


    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    public void fetch() {
        configuration.getPrograms().forEach(program -> {

        });
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(dhis2ApiEndpoint);
        uriComponentsBuilder.path("events.json");

        uriComponentsBuilder.queryParam("program", "MmGml1Gyb7K");
        uriComponentsBuilder.queryParam("skipPaging", "false");
        uriComponentsBuilder.queryParam("totalPages", "true");
        uriComponentsBuilder.queryParam("startDate", "2017-04-01");
        uriComponentsBuilder.queryParam("page", "1");

        EventsResponse eventsResponse = this.fetchPage(uriComponentsBuilder, 1);
        eventRepository.save(eventsResponse.getEvents());
        System.out.println(eventsResponse);
    }

    private EventsResponse fetchPage(UriComponentsBuilder uriComponentsBuilder, int page) {
        if (page > 5) {
            return null;
        }
        uriComponentsBuilder.replaceQueryParam("page", page);
        ResponseEntity<EventsResponse> responseEntity = restTemplate.getForEntity(uriComponentsBuilder.toUriString(), EventsResponse.class);
        return responseEntity.getBody();
    }
}
