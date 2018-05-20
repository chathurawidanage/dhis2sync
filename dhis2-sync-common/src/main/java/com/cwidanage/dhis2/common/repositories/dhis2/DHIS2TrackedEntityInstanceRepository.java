package com.cwidanage.dhis2.common.repositories.dhis2;

import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceTrackedEntityInstance;
import org.springframework.data.repository.CrudRepository;

public interface DHIS2TrackedEntityInstanceRepository extends CrudRepository<DHIS2InstanceTrackedEntityInstance, String> {

    DHIS2InstanceTrackedEntityInstance findDistinctByDhis2InstanceAndTrackedEntityInstanceIdentifier_Id(DHIS2Instance dhis2Instance, String attributeValue);

}
