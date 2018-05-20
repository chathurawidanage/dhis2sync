package com.cwidanage.dhis2.common.models.sync.dhis2;

import com.cwidanage.dhis2.common.models.dhis2.ProgramStage;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.Syncability;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * @author Chathura Widanage
 */
@Entity
public class DHIS2InstanceProgramStage extends ProgramStage {

    @Id
    private String identifier;

    @JsonIgnore
    @ManyToOne(optional = false)
    private DHIS2Instance dhis2Instance;

    private String programId;

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

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    @PrePersist
    public void prePersist() {
        if (this.getProgram() != null) {
            this.programId = this.getProgram().getId();
        }
    }
}
