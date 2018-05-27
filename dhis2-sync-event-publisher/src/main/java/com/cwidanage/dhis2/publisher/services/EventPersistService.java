package com.cwidanage.dhis2.publisher.services;

import com.cwidanage.dhis2.common.models.Event;
import com.cwidanage.dhis2.common.models.rest.eventPersistResponse.EventPersistResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class EventPersistService {

    private final static Logger logger = LogManager.getLogger(EventPersistService.class);

    @Autowired
    @Qualifier("eventPostRestTemplate")
    private RestTemplate restTemplate;

    @Value("${dhis2.apiEndPoint}")
    String dhis2ApiEndpoint;

    public EventPersistResponse persist(Event event) {
        if (event.getEvent() != null && event.getEvent().equals("HIII")) {
            event.setEvent(null);
        }
        logger.debug("Received persist request for event {}", event.getEvent());
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(dhis2ApiEndpoint);
        uriComponentsBuilder.path("events.json");

        ResponseEntity<EventPersistResponse> eventPersistResponseResponseEntity = restTemplate.postForEntity(uriComponentsBuilder.toUriString(), event, EventPersistResponse.class);

        System.out.println(eventPersistResponseResponseEntity.getBody());
        return eventPersistResponseResponseEntity.getBody();
    }
}
