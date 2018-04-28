package com.cwidanage.dhis2.common.models.sync;

import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceDataElement;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceProgramStage;

import javax.persistence.*;
import java.util.List;

@Entity
public class DHIS2Instance {

    @Id
    private String id;

    private String url;

    private String description;

    public boolean metaDataSynced;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DHIS2InstanceDataElement> dataElements;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DHIS2InstanceProgramStage> programStages;

    public boolean isMetaDataSynced() {
        return metaDataSynced;
    }

    public void setMetaDataSynced(boolean metaDataSynced) {
        this.metaDataSynced = metaDataSynced;
    }

    public List<DHIS2InstanceProgramStage> getProgramStages() {
        return programStages;
    }

    public void setProgramStages(List<DHIS2InstanceProgramStage> programStages) {
        this.programStages = programStages;
    }

    public List<DHIS2InstanceDataElement> getDataElements() {
        return dataElements;
    }

    public void setDataElements(List<DHIS2InstanceDataElement> dataElements) {
        this.dataElements = dataElements;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
