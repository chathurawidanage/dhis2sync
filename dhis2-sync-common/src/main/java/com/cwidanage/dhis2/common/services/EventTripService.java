package com.cwidanage.dhis2.common.services;

import com.cwidanage.dhis2.common.constants.EventTripStatus;
import com.cwidanage.dhis2.common.models.TransmittableEvent;
import com.cwidanage.dhis2.common.models.sync.EventRoute;
import com.cwidanage.dhis2.common.models.sync.EventTrip;
import com.cwidanage.dhis2.common.models.sync.EventTripStatusTransformation;
import org.springframework.stereotype.Component;

@Component
public class EventTripService {

    public void transformStatus(EventTrip eventTrip, EventTripStatus eventTripStatus, String message) {
        EventTripStatusTransformation eventTripStatusTransformation = new EventTripStatusTransformation();
        eventTripStatusTransformation.setCurrentStatus(eventTripStatus);
        eventTripStatusTransformation.setMessage(message);

        if (eventTrip.getLatestTransformation() != null) {
            eventTripStatusTransformation.setPreviousStatus(eventTrip.getLatestTransformation().getCurrentStatus());
        }

        eventTrip.setLatestTransformation(eventTripStatusTransformation);
        eventTrip.getEventTripStatusTransformations().add(eventTripStatusTransformation);
    }

    public EventTrip createEventTrip(TransmittableEvent transmittableEvent, EventRoute eventRoute) {
        EventTrip eventTrip = new EventTrip();
        eventTrip.setTransmittableEvent(transmittableEvent);
        eventTrip.setEventRoute(eventRoute);
        eventTrip.setId(String.format("%s_%s", transmittableEvent.getId(), eventRoute.getId()));

        this.transformStatus(eventTrip, EventTripStatus.INITIALIZED, null);
        return eventTrip;
    }
}
