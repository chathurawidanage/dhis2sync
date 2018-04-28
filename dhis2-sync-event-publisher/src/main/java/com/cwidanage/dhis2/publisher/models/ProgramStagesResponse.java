package com.cwidanage.dhis2.publisher.models;

import com.cwidanage.dhis2.common.models.dhis2.ProgramStage;

import java.util.List;

/**
 * @author Chathura Widanage
 */
public class ProgramStagesResponse {
    private Pager pager;
    private List<ProgramStage> programStages;

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public List<ProgramStage> getProgramStages() {
        return programStages;
    }

    public void setProgramStages(List<ProgramStage> programStages) {
        this.programStages = programStages;
    }
}
