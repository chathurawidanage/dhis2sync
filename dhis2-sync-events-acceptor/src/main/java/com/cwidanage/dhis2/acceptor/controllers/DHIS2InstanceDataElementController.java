package com.cwidanage.dhis2.acceptor.controllers;

import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceDataElement;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2InstanceDataElementService;
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
@RequestMapping("/d2iDataElements")
public class DHIS2InstanceDataElementController {

    @Autowired
    private DHIS2InstanceDataElementService d2iDataElementService;

    @RequestMapping(value = "/{d2iDataElementId}/{syncDataElementId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public DHIS2InstanceDataElement createMap(@PathVariable("d2iDataElementId") String d2iDataElementId,
                                              @PathVariable("syncDataElementId") String syncDataElementId) {
        return d2iDataElementService.setSyncDataElement(d2iDataElementId, syncDataElementId);
    }
}
