package com.cwidanage.dhis2.publisher.services;

import com.cwidanage.dhis2.common.constants.TransmittableEventStatus;
import com.cwidanage.dhis2.common.models.EventDeliverReport;
import com.cwidanage.dhis2.common.models.TransmittableEvent;
import com.cwidanage.dhis2.common.services.TransmittableEventService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class EventDeliveryReportListener {

    private final static Logger logger = LogManager.getLogger(EventDeliveryReportListener.class);

    @Autowired
    private TransmittableEventService transmittableEventService;

    @JmsListener(destination = "${dhis2.deliveryReportQueue}", containerFactory = "myFactory")
    public void onDeliverReport(EventDeliverReport eventDeliverReport) {
        logger.debug("Received delivery report for {}", eventDeliverReport.getTransmittableEventId());
        TransmittableEvent transmittableEvent = transmittableEventService.getById(eventDeliverReport.getTransmittableEventId());
        TransmittableEvent acceptedEvent = transmittableEventService.transformStatus(transmittableEvent, TransmittableEventStatus.ACCEPTED_BY_UPSTREAM);
        transmittableEventService.save(acceptedEvent);
    }
}
