package com.cwidanage.dhis2.publisher.controllers;

import com.cwidanage.dhis2.common.exceptions.CommunicationException;
import com.cwidanage.dhis2.common.models.rest.MetaDataResponse;
import com.cwidanage.dhis2.publisher.services.MetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import java.net.ConnectException;

/**
 * @author Chathura Widanage
 */
@RestController
@RequestMapping("/metadata")
public class MetaDataController {

    @Autowired
    private MetaDataService metaDataService;

    @RequestMapping(path = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public MetaDataResponse requestAllMetaData() {
        try {
            return metaDataService.requestAllMetaData();
        } catch (RestClientException restCEx) {
            throw new CommunicationException("Client instance couldn't communicate with DHIS2 server");
        }
    }
}
