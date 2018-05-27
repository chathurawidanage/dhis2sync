package com.cwidanage.dhis2.publisher.controllers;

import com.cwidanage.dhis2.common.models.Event;
import com.cwidanage.dhis2.common.models.rest.eventPersistResponse.EventPersistResponse;
import com.cwidanage.dhis2.publisher.services.EventPersistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventPersistService eventPersistService;

    @RequestMapping(path = {"", "/"}, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public EventPersistResponse persistEvent(@RequestBody Event event) {
        return this.eventPersistService.persist(event);
    }
}
