package com.cwidanage.dhis2.common.models.sync;

import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceProgramStage;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class EventRoute {

    @Id
    private String id;

    @ManyToOne(optional = false)
    private DHIS2InstanceProgramStage source;

    @ManyToOne(optional = false)
    private DHIS2InstanceProgramStage destination;

    private boolean enableSync;

    public boolean isEnableSync() {
        return enableSync;
    }

    public void setEnableSync(boolean enableSync) {
        this.enableSync = enableSync;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DHIS2InstanceProgramStage getSource() {
        return source;
    }

    public void setSource(DHIS2InstanceProgramStage source) {
        this.source = source;
    }

    public DHIS2InstanceProgramStage getDestination() {
        return destination;
    }

    public void setDestination(DHIS2InstanceProgramStage destination) {
        this.destination = destination;
    }
}
