package com.cwidanage.dhis2.common.services.dhis2;

import com.cwidanage.dhis2.common.models.dhis2.DataElement;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.SyncDataElement;
import com.cwidanage.dhis2.common.models.sync.Syncability;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceDataElement;
import com.cwidanage.dhis2.common.repositories.dhis2.DHIS2InstanceDataElementRepository;
import com.cwidanage.dhis2.common.services.SyncDataElementService;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Chathura Widanage
 */
@Component
public class DHIS2InstanceDataElementService {

    @Autowired
    private DHIS2InstanceDataElementRepository repository;

    @Autowired
    private SyncDataElementService syncDataElementService;

    private DB dataElementDB = DBMaker
            .memoryDB().make();


    private HTreeMap<String, String> dataElementsMap = dataElementDB.hashMap("dataElementsMap", Serializer.STRING, Serializer.STRING)
            .expireMaxSize(1000)
            .create();

    @PreDestroy
    public void destroy() {
        this.dataElementDB.close();
    }


    public Map<String, DHIS2InstanceDataElement> getDataElementMap(DHIS2Instance dhis2Instance) {
        Iterable<DHIS2InstanceDataElement> dataElementIterable = this.repository.findByDhis2Instance(dhis2Instance);
        final Map<String, DHIS2InstanceDataElement> dataElementMap = new HashMap<>();
        dataElementIterable.forEach(de -> dataElementMap.put(de.getIdentifier(), de));
        return dataElementMap;
    }

    public DHIS2InstanceDataElement getDestinationDHIS2DataElement(DHIS2Instance sourceInstance,
                                                                   String sourceDataElementId,
                                                                   DHIS2Instance destinationInstance) {

        String uniqueKey = String.format("%s_%s_%s", sourceInstance.getId(), sourceDataElementId, destinationInstance.getId());
        if (dataElementsMap.containsKey(uniqueKey)) {
            return this.repository.findOne(dataElementsMap.get(uniqueKey));
        }

        DHIS2InstanceDataElement sourceDataElement = this.repository.findDistinctByDhis2InstanceAndId(sourceInstance, sourceDataElementId);
        if (sourceDataElement != null && sourceDataElement.getSyncDataElement() != null) {
            DHIS2InstanceDataElement dataElement = this.repository.findDistinctByDhis2InstanceAndSyncDataElement(destinationInstance, sourceDataElement.getSyncDataElement());
            if (dataElement != null) {
                dataElementsMap.put(uniqueKey, dataElement.getIdentifier());
                return dataElement;
            }
        }

        return null;
    }

    public static String generateIdentifier(DHIS2Instance dhis2Instance, DataElement dataElement) {
        return String.format("%s_%s", dhis2Instance.getId(), dataElement.getId());
    }

    public Iterable<DHIS2InstanceDataElement> save(Set<DHIS2InstanceDataElement> dataElementList) {
        return this.repository.save(dataElementList);
    }

    public static DHIS2InstanceDataElement createDHIS2InstanceDataElement(DHIS2Instance dhis2Instance, DataElement dataElement) {
        DHIS2InstanceDataElement d2iDataElement = new DHIS2InstanceDataElement();
        d2iDataElement.setDisplayName(dataElement.getDisplayName());
        d2iDataElement.setId(dataElement.getId());
        d2iDataElement.setSyncability(new Syncability());
        d2iDataElement.setDhis2Instance(dhis2Instance);
        d2iDataElement.setIdentifier(generateIdentifier(dhis2Instance, dataElement));
        return d2iDataElement;
    }

    public DHIS2InstanceDataElement setSyncDataElement(String d2iDataElementId, String syncDeId) {
        DHIS2InstanceDataElement d2iDataElement = this.repository.findOne(d2iDataElementId);
        SyncDataElement syncDataElement = this.syncDataElementService.getById(syncDeId);
        d2iDataElement.setSyncDataElement(syncDataElement);
        return this.repository.save(d2iDataElement);
    }
}
