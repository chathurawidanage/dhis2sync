package com.cwidanage.dhis2.common.models.sync;

import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceDataElement;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author Chathura Widanage
 */
@Entity
public class DataElementMapping {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @ManyToOne
    private SyncDataElement syncDataElement;

    @ManyToOne
    private DHIS2InstanceDataElement dhis2InstanceDataElement;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SyncDataElement getSyncDataElement() {
        return syncDataElement;
    }

    public void setSyncDataElement(SyncDataElement syncDataElement) {
        this.syncDataElement = syncDataElement;
    }

    public DHIS2InstanceDataElement getDhis2InstanceDataElement() {
        return dhis2InstanceDataElement;
    }

    public void setDhis2InstanceDataElement(DHIS2InstanceDataElement dhis2InstanceDataElement) {
        this.dhis2InstanceDataElement = dhis2InstanceDataElement;
    }
}
