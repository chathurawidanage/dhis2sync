package com.cwidanage.dhis2.common.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DHIS2Instance {

    @Id
    private String id;

    private String url;

    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
