package com.cwidanage.dhis2.common.repositories;

import com.cwidanage.dhis2.common.models.EventStatusTransformation;
import org.springframework.data.repository.CrudRepository;

public interface EventStatusTransformationRepository extends CrudRepository<EventStatusTransformation, String> {
}
