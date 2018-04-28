package com.cwidanage.dhis2.common.repositories.dhis2;

import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceProgramStage;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Chathura Widanage
 */
public interface DHIS2InstanceProgramStageRepository extends CrudRepository<DHIS2InstanceProgramStage, String> {

    Iterable<DHIS2InstanceProgramStage> findByDhis2Instance(DHIS2Instance dhis2Instance);
}
