package com.cwidanage.dhis2.publisher.models;

import com.cwidanage.dhis2.common.models.dhis2.Program;

import java.util.List;

/**
 * @author Chathura Widanage
 */
public class ProgramResponse {

    private Pager pager;
    private List<Program> programs;

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }
}
