package com.cwidanage.dhis2.acceptor.controllers;

import com.cwidanage.dhis2.acceptor.models.EventRouteCreateRequest;
import com.cwidanage.dhis2.common.models.sync.EventRoute;
import com.cwidanage.dhis2.common.services.EventRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Chathura Widanage
 */
@RestController
@RequestMapping("/eventRoutes")
public class EventRouteController {

    @Autowired
    private EventRouteService eventRouteService;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<EventRoute> getAll() {
        return eventRouteService.getAll();
    }

    @RequestMapping(value = {"{routeId}", "{routeId}/"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public EventRoute getById(@PathVariable("routeId") String routeId) {
        return eventRouteService.getById(routeId);
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public EventRoute create(@RequestBody EventRouteCreateRequest eventRouteCreateRequest) {
        return eventRouteService.createAndSaveEventRoute(
                eventRouteCreateRequest.getSourceProgramStage(),
                eventRouteCreateRequest.getDestinationProgramStage()
        );
    }


    @RequestMapping(value = {"/{routeId}/toggleSync/", "/{routeId}/toggleSync"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public EventRoute toggleSync(@PathVariable("routeId") String routeId) {
        return this.eventRouteService.toggleSync(routeId);
    }
}
