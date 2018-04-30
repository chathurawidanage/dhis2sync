package com.cwidanage.dhis2.acceptor.services;

import com.cwidanage.dhis2.common.constants.EventTripStatus;
import com.cwidanage.dhis2.common.constants.TransmittableEventStatus;
import com.cwidanage.dhis2.common.models.TransmittableEvent;
import com.cwidanage.dhis2.common.services.EventTripService;
import com.cwidanage.dhis2.common.services.TransmittableEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author Chathura Widanage
 */
@Component
public class IncomingEventListener {

    @Autowired
    private TransmittableEventService transmittableEventService;

    @Autowired
    private EventTripService eventTripService;

    @JmsListener(destination = "incoming_events", containerFactory = "jmsListenerFactory")
    public void onEvent(TransmittableEvent transmittableEvent) {
        //check if this is an update
        TransmittableEvent subject = transmittableEventService.getById(transmittableEvent.getId());
        if (subject != null) {
            subject.setEvent(transmittableEvent.getEvent());
            this.transmittableEventService.transformStatus(subject, TransmittableEventStatus.UPDATED, null);
            //reset trips
            subject.getEventTrips().forEach(eventTrip -> eventTripService.transformStatus(
                    eventTrip, EventTripStatus.INITIALIZED, "Reinitializing due to an update")
            );
        } else {
            subject = transmittableEvent;
        }
        this.transmittableEventService.transformStatus(subject, TransmittableEventStatus.ACCEPTED_BY_UPSTREAM, null);
        transmittableEventService.save(subject);
    }
}
