package com.cwidanage.dhis2.acceptor.controllers;

import com.cwidanage.dhis2.acceptor.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping("/test")
    public String test() {
        return eventService.getTest();
    }
}
