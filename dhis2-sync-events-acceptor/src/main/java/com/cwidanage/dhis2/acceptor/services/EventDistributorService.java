package com.cwidanage.dhis2.acceptor.services;

import com.cwidanage.dhis2.common.models.sync.EventTrip;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceDataElement;
import com.cwidanage.dhis2.common.services.EventTripService;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2EventService;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2InstanceDataElementService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Chathura Widanage
 */
@Service
@EnableScheduling
public class EventDistributorService {

    private final Logger logger = LogManager.getLogger(EventDistributorService.class);

    @Autowired
    private EventTripService eventTripService;

    @Autowired
    private TrackedEntityInstanceService trackedEntityInstanceService;

    @Autowired
    private DHIS2EventService dhis2EventService;

    @Autowired
    private DHIS2InstanceDataElementService dhis2InstanceDataElementService;

    private ExecutorService newEventProcessor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 2)
    public void distributeNewEvent() {
        Iterable<EventTrip> newEventTrips = this.eventTripService.getNewEventTrips();

        List<Future<EventTrip>> tripProcessingFuture = new ArrayList<>();
        newEventTrips.forEach(newEventTrip -> {
            tripProcessingFuture.add(newEventProcessor.submit(
                    new AsyncEventTripHandler(
                            newEventTrip,
                            trackedEntityInstanceService,
                            eventTripService,
                            dhis2EventService,
                            dhis2InstanceDataElementService
                    )));
        });

        for (Future<EventTrip> future : tripProcessingFuture) {
            try {
                EventTrip eventTrip = future.get();
                System.out.println(eventTrip);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
