package com.cwidanage.dhis2.publisher.services;

import com.cwidanage.dhis2.common.models.MetaDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetaDataService {

    @Autowired
    private DataElementService dataElementService;

    public MetaDataResponse requestAllMetaData() {
        MetaDataResponse metaDataResponse = new MetaDataResponse();
        metaDataResponse.setDataElements(dataElementService.getAllDataElements());
        return metaDataResponse;
    }
}
