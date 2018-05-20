package com.cwidanage.dhis2.common.repositories.dhis2;

import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.SyncDataElement;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceDataElement;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.springframework.data.repository.CrudRepository;

public interface DHIS2InstanceDataElementRepository extends CrudRepository<DHIS2InstanceDataElement, String> {

    Iterable<DHIS2InstanceDataElement> findByDhis2Instance(DHIS2Instance dhis2Instance);

    DHIS2InstanceDataElement findDistinctByDhis2InstanceAndId(DHIS2Instance dhis2Instance, String dataElementId);

    DHIS2InstanceDataElement findDistinctByDhis2InstanceAndSyncDataElement(DHIS2Instance dhis2Instance, SyncDataElement syncDataElement);
}
