package com.cwidanage.dhis2.acceptor.services;

import com.cwidanage.dhis2.common.constants.EventTripStatus;
import com.cwidanage.dhis2.common.models.sync.EventTrip;
import com.cwidanage.dhis2.common.services.EventTripService;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2EventService;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2InstanceDataElementService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Stream;

/**
 * @author Chathura Widanage
 */
@Service
@EnableScheduling
public class EventDistributorService {

    private final Logger logger = LogManager.getLogger(EventDistributorService.class);

    private final int CONCURRENCY = 1000;

    @Autowired
    private EventTripService eventTripService;

    @Autowired
    private TrackedEntityInstanceService trackedEntityInstanceService;

    @Autowired
    private DHIS2EventService dhis2EventService;

    @Autowired
    private DHIS2InstanceDataElementService dhis2InstanceDataElementService;

    private ExecutorService executor = Executors.newCachedThreadPool();

    @Scheduled(fixedDelay = 1000)
    public void distributeNewEvent() {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
        if (threadPoolExecutor.getActiveCount() > CONCURRENCY) {
            logger.debug("Too many concurrent tasks sleeping distributor for 10 seconds");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                logger.error("Error in waiting till tasks are finished");
            }
            return;
        }

        Iterable<EventTrip> newEventTrips = this.eventTripService.getNewEventTrips();
        Map<EventTrip, Future<EventTrip>> eventTripFutures = new HashMap<>();
        newEventTrips.forEach(eventTrip -> eventTripFutures.put(
                eventTrip,
                executor.submit(new AsyncEventTripHandler(
                        eventTrip,
                        trackedEntityInstanceService,
                        eventTripService,
                        dhis2EventService,
                        dhis2InstanceDataElementService
                ))));
    }
}
