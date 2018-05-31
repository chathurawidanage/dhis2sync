package com.cwidanage.dhis2.common.repositories;

import com.cwidanage.dhis2.common.constants.EventTripStatus;
import com.cwidanage.dhis2.common.models.sync.EventRoute;
import com.cwidanage.dhis2.common.models.sync.EventTrip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface EventTripRepository extends CrudRepository<EventTrip, String> {

    Iterable<EventTrip> findTop200ByLatestTransformation_CurrentStatusOrderByLastUpdate(EventTripStatus eventTripStatus);

    Stream<EventTrip> streamAllByLatestTransformation_CurrentStatusOrderByLastUpdate(EventTripStatus eventTripStatus);

    Page<EventTrip> findAllByEventRoute_IdAndLatestTransformation_CurrentStatusOrderByLastUpdateDesc(String eventRouteId, EventTripStatus eventTripStatus, Pageable pageable);

    @Query("select trip.latestTransformation.currentStatus,count(trip) from EventTrip trip where trip.eventRoute.id = :routeId group by trip.latestTransformation.currentStatus")
    List<Object[]> countTripsOfRoute(@Param("routeId") String routeId);
}
