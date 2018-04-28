package com.cwidanage.dhis2.acceptor.controllers;


import com.cwidanage.dhis2.common.models.DHIS2Instance;
import com.cwidanage.dhis2.common.models.SyncDataElement;
import com.cwidanage.dhis2.common.services.DHIS2InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dhis2Instances")
public class DHIS2InstanceController {

    @Autowired
    private DHIS2InstanceService dhis2InstanceService;

    @RequestMapping(path = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<DHIS2Instance> listAll() {
        return this.dhis2InstanceService.getAll();
    }

    @RequestMapping(path = {"", "/"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public DHIS2Instance save(@RequestBody DHIS2Instance dhis2Instance) {
        //todo get metadata
        return this.dhis2InstanceService.save(dhis2Instance);
    }
}
