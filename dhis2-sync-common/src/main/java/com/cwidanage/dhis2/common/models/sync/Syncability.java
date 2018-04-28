package com.cwidanage.dhis2.common.models.sync;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Syncability {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    private boolean enabledBySource;
    private boolean enabledByServer;

    public Syncability() {
        this.enabledBySource = true;
        this.enabledByServer = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return this.enabledByServer && this.enabledBySource;
    }

    public boolean isEnabledBySource() {
        return enabledBySource;
    }

    public void setEnabledBySource(boolean enabledBySource) {
        this.enabledBySource = enabledBySource;
    }

    public boolean isEnabledByServer() {
        return enabledByServer;
    }

    public void setEnabledByServer(boolean enabledByServer) {
        this.enabledByServer = enabledByServer;
    }
}
