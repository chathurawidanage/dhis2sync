package com.cwidanage.dhis2.common.services;

import com.cwidanage.dhis2.common.constants.TransmittableEventStatus;
import com.cwidanage.dhis2.common.models.EventStatusTransformation;
import com.cwidanage.dhis2.common.models.TransmittableEvent;
import com.cwidanage.dhis2.common.repositories.EventStatusTransformationRepository;
import com.cwidanage.dhis2.common.repositories.TransmittableEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransmittableEventService {

    @Autowired
    private TransmittableEventRepository repository;

    @Autowired
    private EventStatusTransformationRepository eventStatusTransformationRepository;

    public Iterable<TransmittableEvent> save(Iterable<TransmittableEvent> transmittableEvents) {
        return this.repository.save(transmittableEvents);
    }

    public TransmittableEvent save(TransmittableEvent transmittableEvent) {
        return this.repository.save(transmittableEvent);
    }

    public TransmittableEvent getById(String id) {
        return this.repository.findOne(id);
    }

    public Iterable<TransmittableEvent> getEventsWithStatus(TransmittableEventStatus eventStatus) {
        return this.repository.findAllByLatestTransformation_CurrentStatus(eventStatus);
    }

    public void transformStatus(TransmittableEvent transmittableEvent, TransmittableEventStatus transmittableEventStatus, String message) {
        EventStatusTransformation latestTransformation = new EventStatusTransformation();
        latestTransformation.setCurrentStatus(transmittableEventStatus);
        latestTransformation.setMessage(message);
        if (transmittableEvent.getLatestTransformation() != null) {
            latestTransformation.setPreviousStatus(transmittableEvent.getLatestTransformation().getCurrentStatus());
        }

        //saving transformation
        //transmittableEvent.getStatusTransformations().add(latestTransformation);
        latestTransformation = eventStatusTransformationRepository.save(latestTransformation);

        transmittableEvent.setLatestTransformation(latestTransformation);
    }
}
