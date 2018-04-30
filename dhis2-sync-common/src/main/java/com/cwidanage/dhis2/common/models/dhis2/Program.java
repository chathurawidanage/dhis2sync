package com.cwidanage.dhis2.common.models.dhis2;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.List;

/**
 * @author Chathura Widanage
 */
@MappedSuperclass
public class Program {

    private String id;
    private String displayName;

    @Transient
    private List<ProgramStage> programStages;

    public List<ProgramStage> getProgramStages() {
        return programStages;
    }

    public void setProgramStages(List<ProgramStage> programStages) {
        this.programStages = programStages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
