package com.cwidanage.dhis2.common.models;

import com.cwidanage.dhis2.common.models.dhis2.DataElement;
import com.cwidanage.dhis2.common.models.dhis2.ProgramStage;

import java.util.ArrayList;
import java.util.List;

public class MetaDataResponse {

    private String instanceId;
    private List<DataElement> dataElements = new ArrayList<>();
    private List<ProgramStage> programStages = new ArrayList<>();

    public List<ProgramStage> getProgramStages() {
        return programStages;
    }

    public void setProgramStages(List<ProgramStage> programStages) {
        this.programStages = programStages;
    }

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
