package com.cwidanage.dhis2.acceptor.services;

import com.cwidanage.dhis2.common.models.MetaDataResponse;
import com.cwidanage.dhis2.common.models.dhis2.DataElement;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceDataElement;
import com.cwidanage.dhis2.common.repositories.DHIS2InstanceRepository;
import com.cwidanage.dhis2.common.services.DHIS2InstanceDataElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DHIS2InstanceService {

    @Autowired
    private DHIS2InstanceRepository dhis2InstanceRepository;

    @Autowired
    private DHIS2InstanceDataElementService dhis2InstanceDataElementService;

    public MetaDataResponse fetchMetaData(DHIS2Instance dhis2Instance) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(dhis2Instance.getUrl());
        uriComponentsBuilder.path("metadata");
        ResponseEntity<MetaDataResponse> instanceMetaData = restTemplate.getForEntity(uriComponentsBuilder.toUriString(), MetaDataResponse.class);
        return instanceMetaData.getBody();
    }

    @Transactional
    public DHIS2Instance verifyAndSave(DHIS2Instance dhis2Instance) {
        this.syncMetaData(this.save(dhis2Instance));
        dhis2Instance.setMetaDataSynced(true);
        return this.save(dhis2Instance);
    }

    @Transactional
    public void syncMetaData(DHIS2Instance dhis2Instance) {
        MetaDataResponse metaDataResponse = this.fetchMetaData(dhis2Instance);

        //SYNCING PROGRAM STAGES


        //SYNCING DATA ELEMENTS
        Map<String, DHIS2InstanceDataElement> dataElementMap = this.dhis2InstanceDataElementService.getDataElementMap(dhis2Instance);
        List<DataElement> dataElementsResponse = metaDataResponse.getDataElements();
        List<DHIS2InstanceDataElement> dataElementsToSave = new ArrayList<>();
        dataElementsResponse.forEach(dataElement -> {
            String identifier = dhis2InstanceDataElementService.generateIdentifier(dhis2Instance, dataElement);
            if (!dataElementMap.containsKey(identifier)) {
                DHIS2InstanceDataElement dhis2InstanceDataElement = this.dhis2InstanceDataElementService
                        .createDHIS2InstanceDataElement(dhis2Instance, dataElement);
                dataElementsToSave.add(dhis2InstanceDataElement);
            } else {
                dataElementMap.remove(identifier);
            }
        });

        //the remaining in dataElementMap are the data elements which were turned off by respective DHIS2 admin
        List<DHIS2InstanceDataElement> turnedOffDataElements = dataElementMap.values().stream().map(dataElementMapValue -> {
            dataElementMapValue.getSyncability().setEnabledBySource(false);
            return dataElementMapValue;
        }).collect(Collectors.toList());
        dataElementsToSave.addAll(turnedOffDataElements);
        this.dhis2InstanceDataElementService.save(dataElementsToSave);


    }

    public DHIS2Instance save(DHIS2Instance dhis2Instance) {
        return this.dhis2InstanceRepository.save(dhis2Instance);
    }

    public Iterable<DHIS2Instance> getAll() {
        return this.dhis2InstanceRepository.findAll();
    }
}
