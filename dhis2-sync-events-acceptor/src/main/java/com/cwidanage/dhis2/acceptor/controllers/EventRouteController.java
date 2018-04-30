package com.cwidanage.dhis2.acceptor.controllers;

import com.cwidanage.dhis2.common.models.sync.EventRoute;
import com.cwidanage.dhis2.common.services.EventRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chathura Widanage
 */
@RestController
@RequestMapping("/eventRoutes")
public class EventRouteController {

    @Autowired
    private EventRouteService eventRouteService;

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public EventRoute create(@PathVariable("sourceProgramStage") String sourceProgramStage,
                             @PathVariable("destinationProgramStage") String destinationProgramStage) {
        return eventRouteService.createAndSaveEventRoute(sourceProgramStage, destinationProgramStage);
    }
}
