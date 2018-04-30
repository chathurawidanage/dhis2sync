package com.cwidanage.dhis2.publisher.services;

import com.cwidanage.dhis2.common.constants.TransmittableEventStatus;
import com.cwidanage.dhis2.common.models.TransmittableEvent;
import com.cwidanage.dhis2.common.services.TransmittableEventService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Chathura Widanage
 */
@Service
public class EventPublisherService {

    private static final Logger logger = LogManager.getLogger(EventPublisherService.class);


    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private TransmittableEventService transmittableEventService;

    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 2)
    public void publishEvents() {
        Iterable<TransmittableEvent> fetchedEvents = transmittableEventService.getEventsWithStatus(TransmittableEventStatus.FETCHED_FROM_SOURCE);
        fetchedEvents.forEach(transmittableEvent -> {
            try {
                logger.debug("Transmitting event {}", transmittableEvent);
                jmsTemplate.convertAndSend("incoming_events", transmittableEvent);
                transmittableEventService.transformStatus(transmittableEvent, TransmittableEventStatus.ACCEPTED_BY_UPSTREAM, null);
            } catch (JmsException jmsException) {
                transmittableEventService.transformStatus(transmittableEvent, TransmittableEventStatus.ERROR_SENDING_TO_UPSTREAM, jmsException.getMessage());
                logger.error("Error in sending event to server : {}", transmittableEvent, jmsException);
            } finally {
                transmittableEventService.save(transmittableEvent);
            }
        });

        //todo retry events which didn't receive a delivery report
    }
}
