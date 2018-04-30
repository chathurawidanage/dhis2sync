package com.cwidanage.dhis2.acceptor.controllers;


import com.cwidanage.dhis2.acceptor.services.DHIS2InstanceService;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
        return this.dhis2InstanceService.verifyAndSave(dhis2Instance);
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
