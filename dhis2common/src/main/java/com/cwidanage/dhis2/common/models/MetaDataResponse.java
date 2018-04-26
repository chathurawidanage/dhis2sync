package com.cwidanage.dhis2.common.models;

import com.cwidanage.dhis2.common.models.DataElement;

import java.util.List;

public class MetaDataResponse {

    private String instanceId;
    private List<DataElement> dataElements;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public List<DataElement> getDataElements() {
        return dataElements;
    }

    public void setDataElements(List<DataElement> dataElements) {
        this.dataElements = dataElements;
    }
}
