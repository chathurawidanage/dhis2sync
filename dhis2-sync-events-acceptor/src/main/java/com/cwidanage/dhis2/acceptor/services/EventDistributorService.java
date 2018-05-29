package com.cwidanage.dhis2.acceptor.services;

import com.cwidanage.dhis2.common.constants.EventTripStatus;
import com.cwidanage.dhis2.common.models.sync.EventTrip;
import com.cwidanage.dhis2.common.services.EventTripService;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2EventService;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2InstanceDataElementService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Chathura Widanage
 */
@Service
@EnableScheduling
@EnableAsync
public class EventDistributorService {

    private final Logger logger = LogManager.getLogger(EventDistributorService.class);

    @Autowired
    private EventTripService eventTripService;

    @Autowired
    private TrackedEntityInstanceService trackedEntityInstanceService;

    @Autowired
    private DHIS2EventService dhis2EventService;

    @Autowired
    private DHIS2InstanceDataElementService dhis2InstanceDataElementService;

    @Scheduled(fixedDelay = 1000)
    public void distributeNewEvent() {
        Iterable<EventTrip> newEventTrips = this.eventTripService.getNewEventTrips();
        newEventTrips.forEach(this::processTrip);
    }

    @Async
    public void processTrip(EventTrip eventTrip) {
        AsyncEventTripHandler asyncEventTripHandler = new AsyncEventTripHandler(
                eventTrip,
                trackedEntityInstanceService,
                eventTripService,
                dhis2EventService,
                dhis2InstanceDataElementService
        );
        try {
            EventTrip handledEvent = asyncEventTripHandler.handle();
            logger.debug("Event trip processing handler  of {} returned without issues. Final status {}",
                    handledEvent.getId(), handledEvent.getLatestTransformation().getCurrentStatus());
        } catch (Exception e) {
            logger.error("Error in handling event trip {}", eventTrip.getId(), e);
            eventTripService.transformStatus(eventTrip, EventTripStatus.ERROR_IN_HANDLING_TRIP, e.getMessage());
            eventTripService.save(eventTrip);
        }
    }
}
