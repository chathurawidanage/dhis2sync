package com.cwidanage.dhis2.acceptor.controllers;


import com.cwidanage.dhis2.common.models.sync.SyncDataElement;
import com.cwidanage.dhis2.common.services.SyncDataElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/syncDataElements")
public class SyncDataElementsController {

    @Autowired
    private SyncDataElementService syncDataElementService;

    @RequestMapping(path = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<SyncDataElement> listAll() {
        System.out.println("Rque rec");
        return this.syncDataElementService.getAll();
    }

    @RequestMapping(path = {"", "/"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public SyncDataElement save(@RequestBody SyncDataElement syncDataElement) {
        return this.syncDataElementService.save(syncDataElement);
    }
}
