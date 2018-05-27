package com.cwidanage.dhis2.acceptor.controllers;

import com.cwidanage.dhis2.common.constants.EventTripStatus;
import com.cwidanage.dhis2.common.models.sync.EventTrip;
import com.cwidanage.dhis2.common.services.EventTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("eventTrips")
public class EventTripController {

    @Autowired
    private EventTripService eventTripService;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<EventTrip> getAll(@RequestParam("status") EventTripStatus eventTripStatus,
                                  @RequestParam("page") int page,
                                  @RequestParam("routeId") String routeId) {
        return eventTripService.queryEventTrips(routeId, eventTripStatus, page);
    }

    @RequestMapping(value = {"{eventTripId}/reinitialize", "{eventTripId}/reinitialize/"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public EventTrip reinitialize(@PathVariable("eventTripId") String eventTripId) {
        return this.eventTripService.reInitializeTrip(eventTripId);
    }

    @RequestMapping(value = {"count", "count/"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<EventTripStatus, Long> countRoutesWithStatus(@RequestParam("routeId") String routeId) {
        return this.eventTripService.countEventTrips(routeId);
    }
}
