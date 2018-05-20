package com.cwidanage.dhis2.publisher.models;

import com.cwidanage.dhis2.common.models.dhis2.TrackedEntityInstance;

import java.util.List;

/**
 * @author Chathura Widanage
 */
public class TrackedEntityInstanceResponse {

    private List<TrackedEntityInstance> trackedEntityInstances;

    public List<TrackedEntityInstance> getTrackedEntityInstances() {
        return trackedEntityInstances;
    }

    public void setTrackedEntityInstances(List<TrackedEntityInstance> trackedEntityInstances) {
        this.trackedEntityInstances = trackedEntityInstances;
    }
}
