package com.cwidanage.dhis2.common.services;

import com.cwidanage.dhis2.common.exceptions.ValidationException;
import com.cwidanage.dhis2.common.models.sync.EventRoute;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceProgramStage;
import com.cwidanage.dhis2.common.repositories.EventRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventRouteService {

    @Autowired
    private EventRouteRepository repository;

    @Autowired
    private DHIS2InstanceProgramStageService d2iProgramStageService;

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

    public EventRoute createAndSaveEventRoute(String sourceProgramStageId, String destinationProgramStageId) {
        return this.save(this.createEventRoute(sourceProgramStageId, destinationProgramStageId));
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

}
