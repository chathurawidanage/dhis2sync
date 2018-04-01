package com.cwidanage.dhis2.common.models;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Chathura Widanage
 */
@Entity
public class TrackedEntityInstance {

    @Id
    private String trackedEntityInstance;
    private String trackedEntity;
    private String orgUnit;

    public String getTrackedEntityInstance() {
        return trackedEntityInstance;
    }

    public void setTrackedEntityInstance(String trackedEntityInstance) {
        this.trackedEntityInstance = trackedEntityInstance;
    }

    public String getTrackedEntity() {
        return trackedEntity;
    }

    public void setTrackedEntity(String trackedEntity) {
        this.trackedEntity = trackedEntity;
    }

    public String getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
    }
}
