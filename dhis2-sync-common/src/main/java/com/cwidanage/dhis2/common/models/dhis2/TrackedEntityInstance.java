package com.cwidanage.dhis2.common.models.dhis2;

/**
 * @author Chathura Widanage
 */
public class TrackedEntityInstance {

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
