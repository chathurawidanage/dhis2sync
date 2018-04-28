package com.cwidanage.dhis2.common.models.sync.dhis2;

import com.cwidanage.dhis2.common.models.dhis2.Program;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.Syncability;

import javax.persistence.*;

/**
 * @author Chathura Widanage
 */
@Entity
public class DHIS2InstanceProgram extends Program {

    @Id
    private String identifier;

    @ManyToOne(optional = false)
    private DHIS2Instance dhis2Instance;

    @OneToOne(optional = false, orphanRemoval = true, cascade = CascadeType.ALL)
    private Syncability syncability;

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
}
