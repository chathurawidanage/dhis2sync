package com.cwidanage.dhis2.common.models.sync;

import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceDataElement;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceProgramStage;

import javax.persistence.*;
import java.util.Set;

@Entity
public class DHIS2Instance {

    @Id
    private String id;

    private String url;

    private String description;

    public boolean metaDataSynced;

    public Boolean syncEnabled;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "dhis2Instance")
    private Set<DHIS2InstanceDataElement> dataElements;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "dhis2Instance")
    private Set<DHIS2InstanceProgramStage> programStages;

    public Boolean isSyncEnabled() {
        return syncEnabled;
    }

    public void setSyncEnabled(Boolean syncEnabled) {
        this.syncEnabled = syncEnabled;
    }

    public boolean isMetaDataSynced() {
        return metaDataSynced;
    }

    public void setMetaDataSynced(boolean metaDataSynced) {
        this.metaDataSynced = metaDataSynced;
    }

    public Set<DHIS2InstanceProgramStage> getProgramStages() {
        return programStages;
    }

    public void setProgramStages(Set<DHIS2InstanceProgramStage> programStages) {
        this.programStages = programStages;
    }

    public Set<DHIS2InstanceDataElement> getDataElements() {
        return dataElements;
    }

    public void setDataElements(Set<DHIS2InstanceDataElement> dataElements) {
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
