package com.cwidanage.dhis2.publisher.services;

import com.cwidanage.dhis2.common.models.rest.MetaDataResponse;
import com.cwidanage.dhis2.publisher.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetaDataService {

    @Autowired
    private DataElementService dataElementService;

    @Autowired
    private ProgramStageService programStageService;

    @Autowired
    private ProgramService programService;

    @Autowired
    private Configuration configuration;

    public MetaDataResponse requestAllMetaData() {
        MetaDataResponse metaDataResponse = new MetaDataResponse();
        metaDataResponse.setDataElements(dataElementService.getAllDataElements());
        metaDataResponse.setProgramStages(programStageService.getAllProgramStages());
        metaDataResponse.setPrograms(programService.getAllPrograms());
        metaDataResponse.setInstanceId(configuration.getInstanceId());
        return metaDataResponse;
    }
}
