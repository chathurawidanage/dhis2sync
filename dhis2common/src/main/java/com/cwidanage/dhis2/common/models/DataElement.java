package com.cwidanage.dhis2.common.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DataElement {

    @Id
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
