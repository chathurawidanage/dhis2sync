package com.cwidanage.dhis2.common.models.sync;

import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceDataElement;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chathura Widanage
 */
@Entity
public class SyncDataElement {

    @Id
    private String code;

    private String displayName;
//
//    @OneToMany(fetch = FetchType.EAGER)
//    @MapKeyColumn(name = "dhis2Instance")
//    private Map<DHIS2Instance, DHIS2InstanceDataElement> d2InstanceDEMap;

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

//    public Map<DHIS2Instance, DHIS2InstanceDataElement> getD2InstanceDEMap() {
//        return d2InstanceDEMap;
//    }
//
//    public void setD2InstanceDEMap(Map<DHIS2Instance, DHIS2InstanceDataElement> d2InstanceDEMap) {
//        this.d2InstanceDEMap = d2InstanceDEMap;
//    }
}
