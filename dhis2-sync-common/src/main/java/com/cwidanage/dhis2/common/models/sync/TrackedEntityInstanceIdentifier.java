package com.cwidanage.dhis2.common.models.sync;

import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceTrackedEntityInstance;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chathura Widanage
 */
@Entity
public class TrackedEntityInstanceIdentifier {

    @Id
    private String id;

    @OneToMany(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "dhis2Instance")
    private Map<DHIS2Instance, DHIS2InstanceTrackedEntityInstance> instancesMap = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<DHIS2Instance, DHIS2InstanceTrackedEntityInstance> getInstancesMap() {
        return instancesMap;
    }

    public void setInstancesMap(Map<DHIS2Instance, DHIS2InstanceTrackedEntityInstance> instancesMap) {
        this.instancesMap = instancesMap;
    }
}
