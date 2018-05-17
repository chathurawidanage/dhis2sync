package com.cwidanage.dhis2.common.repositories;

import com.cwidanage.dhis2.common.models.sync.EventRoute;
import org.springframework.data.repository.CrudRepository;

public interface EventRouteRepository extends CrudRepository<EventRoute, String> {

    Iterable<EventRoute> findAllBySource_Dhis2Instance_IdAndSource_Id(String sourceInstanceId, String programStageIdInSource);
}
