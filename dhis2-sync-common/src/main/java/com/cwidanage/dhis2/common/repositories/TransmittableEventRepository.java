package com.cwidanage.dhis2.common.repositories;

import com.cwidanage.dhis2.common.constants.TransmittableEventStatus;
import com.cwidanage.dhis2.common.models.TransmittableEvent;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface TransmittableEventRepository extends CrudRepository<TransmittableEvent, String> {

    Iterable<TransmittableEvent> findTop100ByLatestTransformation_CurrentStatus(TransmittableEventStatus transmittableEventStatus);

    Stream<TransmittableEvent> streamAllByLatestTransformation_CurrentStatus(TransmittableEventStatus transmittableEventStatus);

    Stream<TransmittableEvent> streamAllByInstanceIdAndEvent_ProgramStage(String instanceId, String programStageId);
}
