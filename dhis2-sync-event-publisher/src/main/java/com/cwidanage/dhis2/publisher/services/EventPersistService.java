package com.cwidanage.dhis2.publisher.services;

import com.cwidanage.dhis2.common.models.Event;
import com.cwidanage.dhis2.common.models.rest.eventPersistResponse.EventPersistResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Service
public class EventPersistService {

    private final static Logger logger = LogManager.getLogger(EventPersistService.class);

    @Autowired
    @Qualifier("eventPostRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("eventPostResponseMapper")
    private ObjectMapper objectMapper;

    @Value("${dhis2.apiEndPoint}")
    String dhis2ApiEndpoint;

    public EventPersistResponse persist(Event event) {
        logger.debug("Received persist request for event {}", event.getEvent());
        if (logger.isTraceEnabled()) {
            logger.trace("Received persist request for event {}", event);
        }
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(dhis2ApiEndpoint);
        uriComponentsBuilder.path("events.json");

        try {
            ResponseEntity<EventPersistResponse> eventPersistResponseResponseEntity = restTemplate.postForEntity(uriComponentsBuilder.toUriString(), event, EventPersistResponse.class);
            logger.debug("Event persisted {}", eventPersistResponseResponseEntity.getBody());
            return eventPersistResponseResponseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException exe) {
            logger.error("Error occurred when persisting event : {}", exe.getResponseBodyAsString(), exe);
            try {
                //try to create a response object
                return objectMapper.readValue(exe.getResponseBodyAsString(), EventPersistResponse.class);
            } catch (IOException e) {
                EventPersistResponse eventPersistResponse = new EventPersistResponse();
                eventPersistResponse.setHttpStatusCode(exe.getRawStatusCode());
                eventPersistResponse.setMessage(exe.getResponseBodyAsString());
                return eventPersistResponse;
            }
        }
    }
}
