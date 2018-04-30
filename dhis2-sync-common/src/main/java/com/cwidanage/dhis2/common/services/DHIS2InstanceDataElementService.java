package com.cwidanage.dhis2.common.services;

import com.cwidanage.dhis2.common.models.dhis2.DataElement;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.SyncDataElement;
import com.cwidanage.dhis2.common.models.sync.Syncability;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceDataElement;
import com.cwidanage.dhis2.common.repositories.dhis2.DHIS2InstanceDataElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.Oneway;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Chathura Widanage
 */
@Component
public class DHIS2InstanceDataElementService {

    @Autowired
    private DHIS2InstanceDataElementRepository repository;

    @Autowired
    private SyncDataElementService syncDataElementService;

    public Map<String, DHIS2InstanceDataElement> getDataElementMap(DHIS2Instance dhis2Instance) {
        Iterable<DHIS2InstanceDataElement> dataElementIterable = this.repository.findByDhis2Instance(dhis2Instance);
        final Map<String, DHIS2InstanceDataElement> dataElementMap = new HashMap<>();
        dataElementIterable.forEach(de -> dataElementMap.put(de.getIdentifier(), de));
        return dataElementMap;
    }

    public String generateIdentifier(DHIS2Instance dhis2Instance, DataElement dataElement) {
        return String.format("%s_%s", dhis2Instance.getId(), dataElement.getId());
    }

    public Iterable<DHIS2InstanceDataElement> save(Set<DHIS2InstanceDataElement> dataElementList) {
        return this.repository.save(dataElementList);
    }

    public DHIS2InstanceDataElement createDHIS2InstanceDataElement(DHIS2Instance dhis2Instance, DataElement dataElement) {
        DHIS2InstanceDataElement d2iDataElement = new DHIS2InstanceDataElement();
        d2iDataElement.setDisplayName(dataElement.getDisplayName());
        d2iDataElement.setId(dataElement.getId());
        d2iDataElement.setSyncability(new Syncability());
        d2iDataElement.setDhis2Instance(dhis2Instance);
        d2iDataElement.setIdentifier(this.generateIdentifier(dhis2Instance, dataElement));
        return d2iDataElement;
    }

    public DHIS2InstanceDataElement setSyncDataElement(String d2iDataElementId, String syncDeId) {
        DHIS2InstanceDataElement d2iDataElement = this.repository.findOne(d2iDataElementId);
        SyncDataElement syncDataElement = this.syncDataElementService.getById(syncDeId);
        d2iDataElement.setSyncDataElement(syncDataElement);
        return this.repository.save(d2iDataElement);
    }
}
