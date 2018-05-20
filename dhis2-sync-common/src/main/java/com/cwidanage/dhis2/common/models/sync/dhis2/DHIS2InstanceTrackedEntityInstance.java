package com.cwidanage.dhis2.common.models.sync.dhis2;

import com.cwidanage.dhis2.common.models.dhis2.TrackedEntityInstance;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.TrackedEntityInstanceIdentifier;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author Chathura Widanage
 */
@Entity
public class DHIS2InstanceTrackedEntityInstance extends TrackedEntityInstance {

    @Id
    private String identifier;//dhis2InstanceId_trackedEntityInstance

    @ManyToOne(optional = false)
    private DHIS2Instance dhis2Instance;

    @ManyToOne(optional = false)
    private TrackedEntityInstanceIdentifier trackedEntityInstanceIdentifier;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public DHIS2Instance getDhis2Instance() {
        return dhis2Instance;
    }

    public void setDhis2Instance(DHIS2Instance dhis2Instance) {
        this.dhis2Instance = dhis2Instance;
    }

    public TrackedEntityInstanceIdentifier getTrackedEntityInstanceIdentifier() {
        return trackedEntityInstanceIdentifier;
    }

    public void setTrackedEntityInstanceIdentifier(TrackedEntityInstanceIdentifier trackedEntityInstanceIdentifier) {
        this.trackedEntityInstanceIdentifier = trackedEntityInstanceIdentifier;
    }
}
