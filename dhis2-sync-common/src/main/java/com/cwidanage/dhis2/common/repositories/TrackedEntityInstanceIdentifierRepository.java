package com.cwidanage.dhis2.common.repositories;

import com.cwidanage.dhis2.common.models.sync.TrackedEntityInstanceIdentifier;
import org.springframework.data.repository.CrudRepository;

public interface TrackedEntityInstanceIdentifierRepository extends CrudRepository<TrackedEntityInstanceIdentifier, String> {

}
