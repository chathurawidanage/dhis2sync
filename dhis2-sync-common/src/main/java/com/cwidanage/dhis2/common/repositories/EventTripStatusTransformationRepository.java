package com.cwidanage.dhis2.common.repositories;

import com.cwidanage.dhis2.common.models.sync.EventTripStatusTransformation;
import org.springframework.data.repository.CrudRepository;

public interface EventTripStatusTransformationRepository extends CrudRepository<EventTripStatusTransformation, String> {
}
