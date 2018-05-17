package com.cwidanage.dhis2.publisher.controllers;

import com.cwidanage.dhis2.common.models.rest.TrackedEntityInstanceResponse;
import com.cwidanage.dhis2.publisher.services.TrackedEntityInstanceFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chathura Widanage
 */
@RestController
@RequestMapping("/trackedEntityInstances")
public class TrackedEntityInstanceController {

    @Autowired
    private TrackedEntityInstanceFetchService teiService;

    @RequestMapping(path = {"/{tei}", "/{tei}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public TrackedEntityInstanceResponse getTEI(@PathVariable("tei") String tei) {
        return teiService.fetchById(tei);
    }

}
