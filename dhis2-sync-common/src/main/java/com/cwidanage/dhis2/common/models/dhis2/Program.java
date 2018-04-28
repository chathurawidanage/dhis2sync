package com.cwidanage.dhis2.common.models.dhis2;

import java.util.List;

/**
 * @author Chathura Widanage
 */
public class Program {

    private String id;
    private String displayName;
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
