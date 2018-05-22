package com.cwidanage.dhis2.common.models.rest;

import com.cwidanage.dhis2.common.models.dhis2.DataElement;
import com.cwidanage.dhis2.common.models.dhis2.Program;
import com.cwidanage.dhis2.common.models.dhis2.ProgramStage;

import java.util.ArrayList;
import java.util.List;

public class MetaDataResponse {

    private String instanceId;
    private List<DataElement> dataElements = new ArrayList<>();
    private List<ProgramStage> programStages = new ArrayList<>();
    private List<Program> programs = new ArrayList<>();

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }

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
