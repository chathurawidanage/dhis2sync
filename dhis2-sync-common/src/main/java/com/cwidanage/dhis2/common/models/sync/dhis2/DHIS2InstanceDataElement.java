package com.cwidanage.dhis2.common.models.sync.dhis2;

import com.cwidanage.dhis2.common.models.dhis2.DataElement;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.SyncDataElement;
import com.cwidanage.dhis2.common.models.sync.Syncability;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * @author Chathura Widanage
 */
@Entity
public class DHIS2InstanceDataElement extends DataElement {

    @Id
    private String identifier;

    @JsonIgnore
    @ManyToOne(optional = false)
    private DHIS2Instance dhis2Instance;

    @OneToOne(optional = false, orphanRemoval = true, cascade = CascadeType.ALL)
    private Syncability syncability;

    @ManyToOne(optional = true)
    private SyncDataElement syncDataElement;

    public SyncDataElement getSyncDataElement() {
        return syncDataElement;
    }

    public void setSyncDataElement(SyncDataElement syncDataElement) {
        this.syncDataElement = syncDataElement;
    }

    public Syncability getSyncability() {
        return syncability;
    }

    public void setSyncability(Syncability syncability) {
        this.syncability = syncability;
    }

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

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DHIS2InstanceDataElement that = (DHIS2InstanceDataElement) o;

        return identifier.equals(that.identifier);
    }
}
