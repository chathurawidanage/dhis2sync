package com.cwidanage.dhis2.acceptor.services;

import com.cwidanage.dhis2.common.constants.TransmittableEventStatus;
import com.cwidanage.dhis2.common.models.EventDeliverReport;
import com.cwidanage.dhis2.common.models.TransmittableEvent;
import com.cwidanage.dhis2.common.services.TransmittableEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Chathura Widanage
 */
@Component
public class IncomingEventListener {

    @Autowired
    private TransmittableEventService transmittableEventService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = "incoming_events", containerFactory = "myFactory")
    public void onEvent(TransmittableEvent transmittableEvent) {
        TransmittableEvent statusTransformedEvent = this.transmittableEventService.transformStatus(transmittableEvent, TransmittableEventStatus.ACCEPTED_BY_UPSTREAM);
        transmittableEventService.save(statusTransformedEvent);
        EventDeliverReport eventDeliverReport = new EventDeliverReport();
        eventDeliverReport.setTransmittableEventId(transmittableEvent.getId());
        jmsTemplate.convertAndSend(String.format("%s_event_delivery_reports", transmittableEvent.getInstanceId()), eventDeliverReport);
    }
}
