package com.cwidanage.dhis2.common.models.rest;

import com.cwidanage.dhis2.common.models.dhis2.TrackedEntityInstance;

/**
 * @author Chathura Widanage
 */
public class TrackedEntityInstanceQueryResponse {

    private String identifier;
    private TrackedEntityInstance trackedEntityInstance;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public TrackedEntityInstance getTrackedEntityInstance() {
        return trackedEntityInstance;
    }

    public void setTrackedEntityInstance(TrackedEntityInstance trackedEntityInstance) {
        this.trackedEntityInstance = trackedEntityInstance;
    }
}
