package com.cwidanage.dhis2.acceptor.services;

import com.cwidanage.dhis2.common.exceptions.ValidationException;
import com.cwidanage.dhis2.common.models.rest.MetaDataResponse;
import com.cwidanage.dhis2.common.models.dhis2.DataElement;
import com.cwidanage.dhis2.common.models.dhis2.ProgramStage;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceDataElement;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceProgramStage;
import com.cwidanage.dhis2.common.repositories.DHIS2InstanceRepository;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2InstanceDataElementService;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2InstanceProgramStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author Chathura Widanage
 */
@Component
public class DHIS2InstanceService {

    @Autowired
    private DHIS2InstanceRepository d2iRepository;

    @Autowired
    private DHIS2InstanceDataElementService d2iDataElementService;

    @Autowired
    private DHIS2InstanceProgramStageService di2ProgramStagesService;

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

        //check whether instance IDs map
        if (!dhis2Instance.getId().equals(metaDataResponse.getInstanceId())) {
            throw new ValidationException(String.format(
                    "Instance identifier mismatch. Found %s in target, Expected %s.",
                    metaDataResponse.getInstanceId(),
                    dhis2Instance.getId()
            ));
        }

        //SYNCING PROGRAM STAGES
        Map<String, DHIS2InstanceProgramStage> programStagesMap = this.di2ProgramStagesService.getProgramStagesMap(dhis2Instance);
        List<ProgramStage> programStagesResponse = metaDataResponse.getProgramStages();
        programStagesResponse.forEach(programStage -> {
            String identifier = DHIS2InstanceProgramStageService.generateIdentifier(dhis2Instance, programStage);
            if (!programStagesMap.containsKey(identifier)) {
                DHIS2InstanceProgramStage d2iProgramStage = DHIS2InstanceProgramStageService.createDHIS2InstanceProgramStage(
                        dhis2Instance, programStage
                );
                programStagesMap.put(identifier, d2iProgramStage);
            } else if (!programStagesMap.get(identifier).getSyncability().isEnabledBySource()) {
                programStagesMap.get(identifier).getSyncability().setEnabledBySource(true);
            } else {
                programStagesMap.remove(identifier);
            }
        });
        this.di2ProgramStagesService.save(new HashSet<>(programStagesMap.values()));
        //DONE SYNCING PROGRAM STAGES

        //SYNCING DATA ELEMENTS
        Map<String, DHIS2InstanceDataElement> dataElementMap = this.d2iDataElementService.getDataElementMap(dhis2Instance);
        List<DataElement> dataElementsResponse = metaDataResponse.getDataElements();
        dataElementsResponse.forEach(dataElement -> {
            String identifier = DHIS2InstanceDataElementService.generateIdentifier(dhis2Instance, dataElement);
            if (!dataElementMap.containsKey(identifier)) {
                DHIS2InstanceDataElement d2iDataElement = DHIS2InstanceDataElementService.createDHIS2InstanceDataElement(
                        dhis2Instance, dataElement
                );
                dataElementMap.put(identifier, d2iDataElement);
            } else if (!dataElementMap.get(identifier).getSyncability().isEnabledBySource()) {
                dataElementMap.get(identifier).getSyncability().setEnabledBySource(true);
            } else {
                dataElementMap.remove(identifier);
            }
        });

        this.d2iDataElementService.save(new HashSet<>(dataElementMap.values()));
        //DONE SYNCING DATA ELEMENTS
    }

    @Transactional
    public void syncMetaData(String id) {
        this.syncMetaData(this.getById(id));
    }

    public void setSyncEnabled(String id, boolean enabled) {
        DHIS2Instance instance = this.getById(id);
        //todo check requirements if enabled=true
        instance.setSyncEnabled(enabled);
        this.d2iRepository.save(instance);
    }


    public DHIS2Instance save(DHIS2Instance dhis2Instance) {
        return this.d2iRepository.save(dhis2Instance);
    }

    public Iterable<DHIS2Instance> getAll() {
        return this.d2iRepository.findAll();
    }

    public DHIS2Instance getById(String id) {
        return this.d2iRepository.findOne(id);
    }
}
