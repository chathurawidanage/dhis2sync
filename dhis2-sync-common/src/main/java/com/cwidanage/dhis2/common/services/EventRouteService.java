package com.cwidanage.dhis2.common.services;

import com.cwidanage.dhis2.common.exceptions.ValidationException;
import com.cwidanage.dhis2.common.models.TransmittableEvent;
import com.cwidanage.dhis2.common.models.sync.EventRoute;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceProgramStage;
import com.cwidanage.dhis2.common.repositories.EventRouteRepository;
import com.cwidanage.dhis2.common.repositories.TransmittableEventRepository;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2InstanceProgramStageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Component
public class EventRouteService {

    private final Logger logger = LogManager.getLogger(EventTripService.class);

    @Autowired
    private EventRouteRepository repository;

    @Autowired
    private DHIS2InstanceProgramStageService d2iProgramStageService;

    @Autowired
    private TransmittableEventService transmittableEventService;

    @Autowired
    private EventTripService eventTripService;

    @Autowired
    private TransmittableEventRepository transmittableEventRepository;

    public Iterable<EventRoute> getAll() {
        return repository.findAll();
    }

    public EventRoute save(EventRoute eventRoute) {
        return this.repository.save(eventRoute);
    }

    public String generateIdentifier(EventRoute eventRoute) {
        if (eventRoute.getSource() == null) {
            throw new ValidationException("Couldn't generate Event Route identifier. Source Program Stage can't be null");
        } else if (eventRoute.getDestination() == null) {
            throw new ValidationException("Couldn't generate Event Route identifier. Destination Program Stage can't be null");
        }
        return String.format("%s_%s", eventRoute.getSource().getIdentifier(), eventRoute.getDestination().getIdentifier());
    }


    @Transactional(readOnly = true)
    public EventRoute createAndSaveEventRoute(String sourceProgramStageId, String destinationProgramStageId) {
        EventRoute eventRoute = this.save(this.createEventRoute(sourceProgramStageId, destinationProgramStageId));
        this.createTripsForRoute(eventRoute);
        return eventRoute;
    }

    public void createTripsForRoute(EventRoute eventRoute) {
        logger.debug("Creating trips for route {}", eventRoute.getId());
        try (Stream<TransmittableEvent> eventsStrean = transmittableEventRepository.streamAllByInstanceIdAndEvent_ProgramStage(
                eventRoute.getSource().getDhis2Instance().getId(),
                eventRoute.getSource().getId())) {
            eventsStrean.forEach(transmittableEvent -> {
                logger.debug("Creating event trip for ", transmittableEvent.getId());
                eventTripService.createAndSaveEventTrip(transmittableEvent, eventRoute);
            });
        }
    }

    public EventRoute createEventRoute(String sourceProgramStageId, String destinationProgramStageId) {
        EventRoute eventRoute = new EventRoute();
        DHIS2InstanceProgramStage sourceProgramStage = d2iProgramStageService.getByIdentifier(sourceProgramStageId);
        if (sourceProgramStage == null) {
            throw new ValidationException("Selected source Program Stage is invalid");
        }

        DHIS2InstanceProgramStage destinationProgramStage = d2iProgramStageService.getByIdentifier(destinationProgramStageId);
        if (destinationProgramStage == null) {
            throw new ValidationException("Selected destination Program Stage is invalid");
        }

        eventRoute.setSource(sourceProgramStage);
        eventRoute.setDestination(destinationProgramStage);
        eventRoute.setId(this.generateIdentifier(eventRoute));
        return eventRoute;
    }

    public EventRoute toggleSync(String routeId) {
        EventRoute eventRoute = this.repository.findOne(routeId);
        eventRoute.setEnableSync(!eventRoute.isEnableSync());
        return this.repository.save(eventRoute);
    }

    public Iterable<EventRoute> getRoutesForSource(String sourceInstanceId, String sourceProgramStageId) {
        return this.repository.findAllBySource_Dhis2Instance_IdAndSource_Id(sourceInstanceId, sourceProgramStageId);
    }
}
