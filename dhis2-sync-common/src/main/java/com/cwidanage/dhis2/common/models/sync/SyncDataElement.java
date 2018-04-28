package com.cwidanage.dhis2.common.models.sync;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Chathura Widanage
 */
@Entity
public class SyncDataElement {

    @Id
    private String code;

    private String displayName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
