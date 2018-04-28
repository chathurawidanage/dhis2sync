package com.cwidanage.dhis2.common.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Chathura Widanage
 */
@Entity
public class SyncDataElementMap {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    private String syncDataElementCode;
    private String dhis2DataElementId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSyncDataElementCode() {
        return syncDataElementCode;
    }

    public void setSyncDataElementCode(String syncDataElementCode) {
        this.syncDataElementCode = syncDataElementCode;
    }

    public String getDhis2DataElementId() {
        return dhis2DataElementId;
    }

    public void setDhis2DataElementId(String dhis2DataElementId) {
        this.dhis2DataElementId = dhis2DataElementId;
    }
}
