package com.cwidanage.dhis2.common.services;

import com.cwidanage.dhis2.common.constants.TransmittableEventStatus;
import com.cwidanage.dhis2.common.models.Event;
import com.cwidanage.dhis2.common.models.EventStatusTransformation;
import com.cwidanage.dhis2.common.models.TransmittableEvent;
import com.cwidanage.dhis2.common.repositories.EventStatusTransformationRepository;
import com.cwidanage.dhis2.common.repositories.TransmittableEventRepository;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Component
public class TransmittableEventService {

    @Autowired
    private TransmittableEventRepository repository;

    @Autowired
    private EventStatusTransformationRepository eventStatusTransformationRepository;

    @Autowired
    private DHIS2EventService dhis2EventService;

    public Iterable<TransmittableEvent> save(Iterable<TransmittableEvent> transmittableEvents) {
        return this.repository.save(transmittableEvents);
    }

    public TransmittableEvent save(TransmittableEvent transmittableEvent) {
        return this.repository.save(transmittableEvent);
    }

    public TransmittableEvent getById(String id) {
        return this.repository.findOne(id);
    }

    public Stream<TransmittableEvent> getEventsWithStatus(TransmittableEventStatus eventStatus) {
        return this.repository.streamAllByLatestTransformation_CurrentStatus(eventStatus);
    }

    @Transactional(readOnly = true)
    public Stream<TransmittableEvent> streamAllForProgramStage(String instanceId, String programStage) {
        return this.repository.streamAllByInstanceIdAndEvent_ProgramStage(instanceId, programStage);
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
