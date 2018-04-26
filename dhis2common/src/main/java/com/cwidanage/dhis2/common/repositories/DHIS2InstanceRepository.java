package com.cwidanage.dhis2.common.repositories;

import com.cwidanage.dhis2.common.models.DHIS2Instance;
import org.springframework.data.repository.CrudRepository;

public interface DHIS2InstanceRepository extends CrudRepository<DHIS2Instance, String> {
}
