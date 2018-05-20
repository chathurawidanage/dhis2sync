package com.cwidanage.dhis2.publisher.controllers;

import com.cwidanage.dhis2.common.models.rest.TrackedEntityInstanceQueryResponse;
import com.cwidanage.dhis2.publisher.services.TrackedEntityInstanceFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chathura Widanage
 */
@RestController
@RequestMapping("/trackedEntityInstances")
public class TrackedEntityInstanceController {

    @Autowired
    private TrackedEntityInstanceFetchService teiService;

    @RequestMapping(path = {"/query", "/query/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public TrackedEntityInstanceQueryResponse query(@RequestParam(name = "tei", required = false) String tei,
                                                    @RequestParam(name = "attributeValue", required = false) String attributeValue) {
        if (tei != null) {
            return teiService.fetchByTEIId(tei);
        }
        return teiService.fetchByAttributeValue(attributeValue);
    }
}
