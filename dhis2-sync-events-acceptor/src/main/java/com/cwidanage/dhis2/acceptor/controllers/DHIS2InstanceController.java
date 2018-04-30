package com.cwidanage.dhis2.acceptor.controllers;


import com.cwidanage.dhis2.acceptor.services.DHIS2InstanceService;
import com.cwidanage.dhis2.common.exceptions.CommunicationException;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.net.ConnectException;

@RestController
@RequestMapping("/dhis2Instances")
public class DHIS2InstanceController {

    @Autowired
    private DHIS2InstanceService dhis2InstanceService;

    @RequestMapping(path = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<DHIS2Instance> listAll() {
        return this.dhis2InstanceService.getAll();
    }

    @RequestMapping(path = {"", "/"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DHIS2Instance save(@RequestBody DHIS2Instance dhis2Instance) {
        try {
            return this.dhis2InstanceService.verifyAndSave(dhis2Instance);
        } catch (RestClientException conex) {
            throw new CommunicationException("Couldn't establish a connection with the Sync client of " + dhis2Instance.getId());
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DHIS2Instance getById(@PathVariable("id") String id) {
        return this.dhis2InstanceService.getById(id);
    }

    @RequestMapping(path = "/{id}/syncMetadata", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public DHIS2Instance syncMetadata(@PathVariable("id") String id) {
        this.dhis2InstanceService.syncMetaData(id);
        return this.dhis2InstanceService.getById(id);
    }

    @RequestMapping(path = "/{id}/start", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void start(@PathVariable("id") String id) {
        this.dhis2InstanceService.setSyncEnabled(id, true);
    }

    @RequestMapping(path = "/{id}/stop", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void stop(@PathVariable("id") String id) {
        this.dhis2InstanceService.setSyncEnabled(id, false);
    }
}
