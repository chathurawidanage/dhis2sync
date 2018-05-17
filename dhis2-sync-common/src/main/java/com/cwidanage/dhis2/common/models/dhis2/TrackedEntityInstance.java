package com.cwidanage.dhis2.common.models.dhis2;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Chathura Widanage
 */
@MappedSuperclass
public class TrackedEntityInstance {

    private String trackedEntityInstance;
    private String trackedEntity;
    private String orgUnit;

    @Transient
    private Set<TrackedEntityAttribute> attributes = new HashSet<>();

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

    public Set<TrackedEntityAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<TrackedEntityAttribute> attributes) {
        this.attributes = attributes;
    }
}
