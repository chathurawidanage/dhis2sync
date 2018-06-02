package com.cwidanage.dhis2.common.services;

import com.cwidanage.dhis2.common.constants.EventTripStatus;
import com.cwidanage.dhis2.common.models.TransmittableEvent;
import com.cwidanage.dhis2.common.models.sync.EventRoute;
import com.cwidanage.dhis2.common.models.sync.EventTrip;
import com.cwidanage.dhis2.common.models.sync.EventTripStatusTransformation;
import com.cwidanage.dhis2.common.repositories.EventTripRepository;
import com.cwidanage.dhis2.common.repositories.EventTripStatusTransformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@Component
public class EventTripService {

    @Autowired
    private EventTripRepository repository;

    @Autowired
    private EventTripStatusTransformationRepository statusTransformationRepository;

    public void transformStatus(EventTrip eventTrip, EventTripStatus eventTripStatus, String message) {
        EventTripStatusTransformation eventTripStatusTransformation = new EventTripStatusTransformation();
        eventTripStatusTransformation.setCurrentStatus(eventTripStatus);
        eventTripStatusTransformation.setMessage(message);

        if (eventTrip.getLatestTransformation() != null) {
            eventTripStatusTransformation.setPreviousStatus(eventTrip.getLatestTransformation().getCurrentStatus());
        }

        //saving
        eventTripStatusTransformation = statusTransformationRepository.save(eventTripStatusTransformation);

        //setting
        eventTrip.setLatestTransformation(eventTripStatusTransformation);

        //eventTrip.getEventTripStatusTransformations().add(eventTripStatusTransformation);
    }

    public void reinitializeAll(EventTripStatus eventTripStatus, String routeId) {
        try (Stream<EventTrip> tripStream = this.repository
                .streamAllByLatestTransformation_CurrentStatusAndEventRoute_Id(eventTripStatus, routeId)) {
            tripStream.forEach(eventTrip -> {
                this.transformStatus(eventTrip, EventTripStatus.INITIALIZED, "Reinitialized manually");
                this.save(eventTrip);
            });
        }
    }

    /**
     * Create a new event trip or reinitialize if already existing
     *
     * @param transmittableEvent Transmittable event to create trip with
     * @param eventRoute         Route of the trip
     * @return Created or reinitialized trip
     */
    public EventTrip createEventTrip(TransmittableEvent transmittableEvent, EventRoute eventRoute) {
        String eventTripId = String.format("%s_%s", transmittableEvent.getId(), eventRoute.getId());
        EventTrip eventTrip = this.repository.findOne(eventTripId);
        if (eventTrip == null) {
            eventTrip = new EventTrip();
            eventTrip.setTransmittableEvent(transmittableEvent);
            eventTrip.setEventRoute(eventRoute);
            eventTrip.setId(eventTripId);
        }
        this.transformStatus(eventTrip, EventTripStatus.INITIALIZED, null);
        return eventTrip;
    }

    public EventTrip createAndSaveEventTrip(TransmittableEvent transmittableEvent, EventRoute eventRoute) {
        return this.repository.save(this.createEventTrip(transmittableEvent, eventRoute));
    }

    public EventTrip save(EventTrip eventTrip) {
        return this.repository.save(eventTrip);
    }

    public Iterable<EventTrip> getEventTipsWithStatus(EventTripStatus eventTripStatus) {
        return repository.findTop200ByLatestTransformation_CurrentStatusOrderByLastUpdate(eventTripStatus);
    }

    public EventTrip reInitializeTrip(String tripId) {
        EventTrip eventTrip = this.repository.findOne(tripId);
        transformStatus(eventTrip, EventTripStatus.INITIALIZED, "Reinitialized manually");
        return this.save(eventTrip);
    }

    public Page<EventTrip> queryEventTrips(String eventRouteId, EventTripStatus eventTripStatus, int page) {
        return this.repository.findAllByEventRoute_IdAndLatestTransformation_CurrentStatusOrderByLastUpdateDesc(eventRouteId, eventTripStatus, new PageRequest(page, 20));
    }

    public HashMap<EventTripStatus, Long> countEventTrips(String eventRouteId) {
        List<Object[]> objects = this.repository.countTripsOfRoute(eventRouteId);
        return objects.stream().collect(HashMap<EventTripStatus, Long>::new,
                (m, c) -> m.put((EventTripStatus) c[0], (Long) c[1]),
                (m, u) -> {
                });
    }

    public Iterable<EventTrip> getNewEventTrips() {
        return this.getEventTipsWithStatus(EventTripStatus.INITIALIZED);
    }
}
