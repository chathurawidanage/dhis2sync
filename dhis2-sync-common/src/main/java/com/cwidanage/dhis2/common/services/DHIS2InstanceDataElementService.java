package com.cwidanage.dhis2.common.services;

import com.cwidanage.dhis2.common.models.dhis2.DataElement;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.Syncability;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceDataElement;
import com.cwidanage.dhis2.common.repositories.dhis2.DHIS2InstanceDataElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chathura Widanage
 */
@Component
public class DHIS2InstanceDataElementService {

    @Autowired
    private DHIS2InstanceDataElementRepository repository;

    public Map<String, DHIS2InstanceDataElement> getDataElementMap(DHIS2Instance dhis2Instance) {
        Iterable<DHIS2InstanceDataElement> dataElementIterable = this.repository.findByDhis2Instance(dhis2Instance);
        final Map<String, DHIS2InstanceDataElement> dataElementMap = new HashMap<>();
        dataElementIterable.forEach(de -> dataElementMap.put(de.getIdentifier(), de));
        return dataElementMap;
    }

    public String generateIdentifier(DHIS2Instance dhis2Instance, DataElement dataElement) {
        return String.format("%s_%s", dhis2Instance.getId(), dataElement.getId());
    }

    public Iterable<DHIS2InstanceDataElement> save(List<DHIS2InstanceDataElement> dataElementList) {
        return this.repository.save(dataElementList);
    }

    public DHIS2InstanceDataElement createDHIS2InstanceDataElement(DHIS2Instance dhis2Instance, DataElement dataElement) {
        DHIS2InstanceDataElement dhis2InstanceDataElement = new DHIS2InstanceDataElement();
        dhis2InstanceDataElement.setDisplayName(dataElement.getDisplayName());
        dhis2InstanceDataElement.setId(dataElement.getId());
        dhis2InstanceDataElement.setSyncability(new Syncability());
        dhis2InstanceDataElement.setDhis2Instance(dhis2Instance);
        dhis2InstanceDataElement.setId(this.generateIdentifier(dhis2Instance, dataElement));
        return dhis2InstanceDataElement;
    }
}
