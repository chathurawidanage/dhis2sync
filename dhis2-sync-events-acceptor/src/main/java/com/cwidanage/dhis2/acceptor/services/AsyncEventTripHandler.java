package com.cwidanage.dhis2.acceptor.services;

import com.cwidanage.dhis2.common.constants.EventTripStatus;
import com.cwidanage.dhis2.common.models.DataValue;
import com.cwidanage.dhis2.common.models.Event;
import com.cwidanage.dhis2.common.models.TransmittableEvent;
import com.cwidanage.dhis2.common.models.rest.eventPersistResponse.EventPersistResponse;
import com.cwidanage.dhis2.common.models.rest.eventPersistResponse.ImportSummary;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.EventTrip;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceDataElement;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceTrackedEntityInstance;
import com.cwidanage.dhis2.common.services.EventTripService;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2EventService;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2InstanceDataElementService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class AsyncEventTripHandler implements Callable<EventTrip> {

    private final Logger logger = LogManager.getLogger(AsyncEventTripHandler.class);

    private EventTrip eventTrip;

    private RestTemplate restTemplate;

    private TrackedEntityInstanceService teiService;
    private EventTripService eventTripService;
    private DHIS2EventService dhis2EventService;
    private DHIS2InstanceDataElementService dhis2InstanceDataElementService;

    private Event sendingEvent;

    public AsyncEventTripHandler(EventTrip eventTrip,
                                 TrackedEntityInstanceService teiService,
                                 EventTripService eventTripService,
                                 DHIS2EventService dhis2EventService,
                                 DHIS2InstanceDataElementService dhis2InstanceDataElementService) {
        this.eventTrip = eventTrip;
        this.restTemplate = new RestTemplate();
        this.teiService = teiService;
        this.dhis2InstanceDataElementService = dhis2InstanceDataElementService;
        this.eventTripService = eventTripService;
        this.dhis2EventService = dhis2EventService;
        this.sendingEvent = new Event();
    }

    private boolean resolveTrackedEntityInstance() {
        logger.debug("Resolving tracked entity instance for {}", this.eventTrip.getId());
        //resolve source
        DHIS2Instance sourceInstance = this.eventTrip.getEventRoute().getSource().getDhis2Instance();
        String trackedEntityInstanceId = this.eventTrip.getTransmittableEvent().getEvent().getTrackedEntityInstance();

        DHIS2InstanceTrackedEntityInstance sourceInstanceTEI = teiService.findByTEIId(sourceInstance, trackedEntityInstanceId);

        if (sourceInstanceTEI == null) {
            logger.debug("Couldn't find unique TEI attribute in source for {}", trackedEntityInstanceId);
            this.eventTripService.transformStatus(this.eventTrip, EventTripStatus.WAITING_FOR_TEI_DATA,
                    "Couldn't find unique TEI attribute in source " + sourceInstance.getId());
            return false;
        }


        //resolve destination
        DHIS2Instance destinationInstance = this.eventTrip.getEventRoute().getDestination().getDhis2Instance();
        DHIS2InstanceTrackedEntityInstance destinationTEI = sourceInstanceTEI.getTrackedEntityInstanceIdentifier()
                .getInstancesMap()
                .getOrDefault(destinationInstance, teiService.findByAttributeValue(destinationInstance,
                        sourceInstanceTEI.getTrackedEntityInstanceIdentifier().getId()));
        if (destinationTEI == null) {
            logger.debug("Couldn't find  unique TEI attribute in destination for {} with attribute",
                    trackedEntityInstanceId,
                    sourceInstanceTEI.getTrackedEntityInstanceIdentifier().getId());

            //failed
            this.eventTripService.transformStatus(this.eventTrip, EventTripStatus.WAITING_FOR_TEI_DATA,
                    String.format("Couldn't find TEI unique attribute[%s] in destination %s",
                            sourceInstanceTEI.getTrackedEntityInstanceIdentifier().getId(), destinationInstance.getId()));
            return false;
        }
        //has all necessary values
        this.sendingEvent.setTrackedEntityInstance(destinationTEI.getTrackedEntityInstance());
        this.sendingEvent.setOrgUnit(destinationTEI.getOrgUnit());
        return true;
    }

    public boolean resolveEventAttributes() {
        TransmittableEvent transmittableEvent = this.eventTrip.getTransmittableEvent();
        //initializing lazy attributes
        Event event = transmittableEvent.getEvent();//this.dhis2EventService.loadEventAttributes(transmittableEvent.getEvent().getId());

        //data values
        List<DataValue> dataValues = event.getDataValues();
        List<DataValue> newDataValues = dataValues.stream().map(dataValue -> {
            DHIS2InstanceDataElement destinationDHIS2DataElement = dhis2InstanceDataElementService.getDestinationDHIS2DataElement(
                    this.eventTrip.getEventRoute().getSource().getDhis2Instance(),
                    dataValue.getDataElement(),
                    this.eventTrip.getEventRoute().getDestination().getDhis2Instance()
            );
            if (destinationDHIS2DataElement != null) {
                DataValue newDataValue = new DataValue();
                newDataValue.setDataElement(destinationDHIS2DataElement.getId());
                newDataValue.setValue(dataValue.getValue());
                return newDataValue;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        this.sendingEvent.setDataValues(newDataValues);

        this.sendingEvent.setCoordinate(event.getCoordinate());
        this.sendingEvent.setEventDate(event.getEventDate());
        this.sendingEvent.setLastUpdated(event.getLastUpdated());
        this.sendingEvent.setEvent(this.eventTrip.getDestinationEventId());

        return false;
    }

    public void transferEvent() {
        logger.debug("Transferring event {}", this.sendingEvent);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(
                this.eventTrip.getEventRoute().getDestination().getDhis2Instance().getUrl()
        );
        uriComponentsBuilder.path("events");

        ResponseEntity<EventPersistResponse> eventResponseEntity = this.restTemplate.postForEntity(uriComponentsBuilder.toUriString(), this.sendingEvent, EventPersistResponse.class);
        if (eventResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            EventPersistResponse eventPersistResponse = eventResponseEntity.getBody();
            if (eventPersistResponse.getHttpStatusCode() != 200) {
                logger.debug("Event trip {} rejected by down stream {}", eventTrip.getId(), eventPersistResponse.getMessage());
                this.eventTripService.transformStatus(this.eventTrip, EventTripStatus.REJECTED_BY_DOWNSTREAM, eventPersistResponse.getMessage());
            } else if (eventPersistResponse.getResponse() == null) {
                logger.debug("Event trip {} returned with a null response", eventTrip.getId());
                this.eventTripService.transformStatus(this.eventTrip, EventTripStatus.REJECTED_BY_DOWNSTREAM, "Null response in persist request");
            } else if (eventPersistResponse.getResponse().getImportSummaries() == null) {
                logger.debug("Event trip {} returned with a null import summary", eventTrip.getId());
                this.eventTripService.transformStatus(this.eventTrip, EventTripStatus.REJECTED_BY_DOWNSTREAM, "Null import summary");
            } else if (eventPersistResponse.getResponse().getImportSummaries().size() != 1) {
                logger.debug("Event trip {} returned with unexpected number of import summaries. Expected 1 found {}", eventTrip.getId(), eventPersistResponse.getResponse().getImportSummaries().size());
                this.eventTripService.transformStatus(this.eventTrip, EventTripStatus.REJECTED_BY_DOWNSTREAM, "Unexpected number of import summaries : " + eventPersistResponse.getResponse().getImportSummaries().size());
            } else {
                ImportSummary importSummary = eventPersistResponse.getResponse().getImportSummaries().get(0);
                if (importSummary.getReference() == null) {
                    logger.debug("Event trip {} returned with a null reference");
                    this.eventTripService.transformStatus(this.eventTrip, EventTripStatus.REJECTED_BY_DOWNSTREAM, "Null reference for the persisted event. Import status : " + importSummary.getStatus());
                } else {
                    logger.debug("Event Trip {} persisted event in destination with ID {}", eventTrip.getId(), importSummary.getReference());
                    this.eventTripService.transformStatus(this.eventTrip, EventTripStatus.COMPLETED, "Event persisted with reference : " + importSummary.getReference());
                    this.eventTrip.setDestinationEventId(importSummary.getReference());
                }
            }
            this.eventTripService.save(this.eventTrip);
        } else {
            //todo handle
        }
    }

    @Override
    public EventTrip call() throws Exception {
        logger.debug("Starting to process event trip {}", eventTrip.getId());
        boolean trackedEntityResolved = this.resolveTrackedEntityInstance();
        if (trackedEntityResolved) {
            //resolving data elements
            boolean dataElementsResolved = this.resolveEventAttributes();


            //resolve program and program stage
            this.sendingEvent.setProgram(this.eventTrip.getEventRoute().getDestination().getDhis2InstanceProgram().getId());
            this.sendingEvent.setProgramStage(this.eventTrip.getEventRoute().getDestination().getId());

            this.transferEvent();
        }
        this.eventTripService.save(this.eventTrip);
        return this.eventTrip;
    }
}
