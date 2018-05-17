package com.cwidanage.dhis2.acceptor.services;

import com.cwidanage.dhis2.common.constants.EventTripStatus;
import com.cwidanage.dhis2.common.constants.TransmittableEventStatus;
import com.cwidanage.dhis2.common.models.TransmittableEvent;
import com.cwidanage.dhis2.common.models.sync.EventRoute;
import com.cwidanage.dhis2.common.models.sync.EventTrip;
import com.cwidanage.dhis2.common.services.EventRouteService;
import com.cwidanage.dhis2.common.services.EventTripService;
import com.cwidanage.dhis2.common.services.TransmittableEventService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Chathura Widanage
 */
@Component
public class IncomingEventListener {

    private final static Logger logger = LogManager.getLogger(IncomingEventListener.class);

    @Autowired
    private TransmittableEventService transmittableEventService;

    @Autowired
    private EventTripService eventTripService;

    @Autowired
    private EventRouteService eventRouteService;

    @JmsListener(destination = "incoming_events", containerFactory = "jmsListenerFactory")
    public void onEvent(TransmittableEvent transmittableEvent) {
        logger.debug("Incoming event {} received from {}", transmittableEvent.getId(), transmittableEvent.getInstanceId());
        if (logger.isTraceEnabled()) {
            logger.trace("Incoming event received {}", transmittableEvent);
        }
        //check if this is an update
        TransmittableEvent subject = transmittableEventService.getById(transmittableEvent.getId());
        if (subject != null) {
            logger.debug("Received event {} is existing", transmittableEvent.getId());
            subject.setEvent(transmittableEvent.getEvent());
            this.transmittableEventService.transformStatus(subject, TransmittableEventStatus.UPDATED, null);
            //reset trips
            logger.debug("Resetting {} event trips of {}", subject.getEventTrips().size(), subject.getId());
            subject.getEventTrips().forEach(eventTrip -> eventTripService.transformStatus(
                    eventTrip, EventTripStatus.INITIALIZED, "Reinitializing due to an update")
            );
        } else {
            logger.debug("Received event {} is new", transmittableEvent.getId());
            //new event
            subject = transmittableEvent;
        }

        this.addMissingTrips(subject);

        logger.debug("Accepting event {} to system", subject.getId());
        this.transmittableEventService.transformStatus(subject, TransmittableEventStatus.ACCEPTED_BY_UPSTREAM, null);
        transmittableEventService.save(subject);
    }

    private void addMissingTrips(TransmittableEvent transmittableEvent) {
        //create event trips
        Iterable<EventRoute> routesForSource = eventRouteService.getRoutesForSource(
                transmittableEvent.getInstanceId(),
                transmittableEvent.getEvent().getProgramStage()
        );

        logger.debug("Found possible routes for event {}", transmittableEvent.getId());

        Set<EventRoute> existingEventRoutes = transmittableEvent.getEventTrips().stream()
                .map(EventTrip::getEventRoute).collect(Collectors.toSet());

        logger.debug("Found {} existing routes for event {}", existingEventRoutes.size(), transmittableEvent.getId());

        routesForSource.forEach(eventRoute -> {
            if (!existingEventRoutes.contains(eventRoute)) {
                logger.debug("Creating trip for event {} with route {}", transmittableEvent.getId(), eventRoute.getId());
                EventTrip eventTrip = eventTripService.createEventTrip(transmittableEvent, eventRoute);
                transmittableEvent.getEventTrips().add(eventTrip);
            } else {
                logger.debug("Skipping route {} for event {} since, already exists", eventRoute.getId(), transmittableEvent.getId());
            }
        });
    }
}
