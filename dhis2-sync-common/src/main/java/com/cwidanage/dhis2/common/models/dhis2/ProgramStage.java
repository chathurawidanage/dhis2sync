package com.cwidanage.dhis2.common.models.dhis2;

import javax.persistence.MappedSuperclass;

/**
 * @author Chathura Widanage
 */
@MappedSuperclass
public class ProgramStage {

    private String id;
    private String displayName;

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
