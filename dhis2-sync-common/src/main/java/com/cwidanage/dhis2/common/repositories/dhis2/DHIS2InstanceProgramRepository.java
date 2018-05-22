package com.cwidanage.dhis2.common.repositories.dhis2;

import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceProgram;
import org.springframework.data.repository.CrudRepository;

public interface DHIS2InstanceProgramRepository extends CrudRepository<DHIS2InstanceProgram, String> {

    Iterable<DHIS2InstanceProgram> findAllByDhis2Instance(DHIS2Instance dhis2Instance);
}
