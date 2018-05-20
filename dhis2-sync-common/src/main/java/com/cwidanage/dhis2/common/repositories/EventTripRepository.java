package com.cwidanage.dhis2.common.repositories;

import com.cwidanage.dhis2.common.constants.EventTripStatus;
import com.cwidanage.dhis2.common.models.sync.EventTrip;
import org.springframework.data.repository.CrudRepository;

public interface EventTripRepository extends CrudRepository<EventTrip, String> {

    Iterable<EventTrip> findTop20ByLatestTransformation_CurrentStatusOrderByLastUpdate(EventTripStatus eventTripStatus);

}
