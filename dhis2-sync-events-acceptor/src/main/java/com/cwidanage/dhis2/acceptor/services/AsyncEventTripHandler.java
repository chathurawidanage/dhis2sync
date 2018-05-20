package com.cwidanage.dhis2.acceptor.services;

import com.cwidanage.dhis2.common.constants.EventTripStatus;
import com.cwidanage.dhis2.common.models.DataValue;
import com.cwidanage.dhis2.common.models.Event;
import com.cwidanage.dhis2.common.models.TransmittableEvent;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.EventTrip;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceDataElement;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceTrackedEntityInstance;
import com.cwidanage.dhis2.common.services.EventTripService;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2EventService;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2InstanceDataElementService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.client.RestTemplate;

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
            logger.debug("Couldn't findByTEIId unique TEI attribute in source for {}", trackedEntityInstanceId);
            this.eventTripService.transformStatus(this.eventTrip, EventTripStatus.WAITING_FOR_TEI_DATA,
                    "Couldn't findByTEIId unique TEI attribute in source " + sourceInstance.getId());
            return false;
        }


        //resolve destination
        DHIS2Instance destinationInstance = this.eventTrip.getEventRoute().getDestination().getDhis2Instance();
        DHIS2InstanceTrackedEntityInstance destinationTEI = sourceInstanceTEI.getTrackedEntityInstanceIdentifier()
                .getInstancesMap()
                .getOrDefault(destinationInstance, teiService.findByAttributeValue(destinationInstance,
                        sourceInstanceTEI.getTrackedEntityInstanceIdentifier().getId()));
        if (destinationTEI == null) {
            logger.debug("Couldn't findByTEIId unique TEI attribute in destination for {}", trackedEntityInstanceId);

            //failed
            this.eventTripService.transformStatus(this.eventTrip, EventTripStatus.WAITING_FOR_TEI_DATA,
                    "Couldn't findByTEIId unique TEI attribute in destination " + destinationInstance.getId());
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
        Event event = this.dhis2EventService.loadEventAttributes(transmittableEvent.getEvent().getId());

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

    @Override
    public EventTrip call() throws Exception {
        logger.debug("Starting to process event trip {}", eventTrip.getId());
        boolean trackedEntityResolved = this.resolveTrackedEntityInstance();
        if (trackedEntityResolved) {
            //resolving data elements
            boolean dataElementsResolved = this.resolveEventAttributes();


            //resolve program and program stage
            this.sendingEvent.setProgram(this.eventTrip.getEventRoute().getDestination().getProgramId());
            this.sendingEvent.setProgramStage(this.eventTrip.getEventRoute().getDestination().getId());

            System.out.println(this.sendingEvent);
        }
        return this.eventTrip;
    }
}
