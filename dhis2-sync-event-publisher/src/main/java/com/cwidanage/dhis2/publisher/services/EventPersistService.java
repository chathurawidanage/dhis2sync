package com.cwidanage.dhis2.publisher.services;

import com.cwidanage.dhis2.common.models.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class EventPersistService {

    private final static Logger logger = LogManager.getLogger(EventPersistService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${dhis2.apiEndPoint}")
    String dhis2ApiEndpoint;

    public Event persist(Event event) {
        logger.debug("Received persist request for event {}", event);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(dhis2ApiEndpoint);
        uriComponentsBuilder.path("events.json");
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(uriComponentsBuilder.toUriString(), event, String.class);
        System.out.println(stringResponseEntity.getBody());
        event.setEvent("HIII");
        return event;
    }
}
