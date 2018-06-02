package com.cwidanage.dhis2.acceptor.services;

import com.cwidanage.dhis2.common.constants.EventTripStatus;
import com.cwidanage.dhis2.common.models.sync.EventTrip;
import com.cwidanage.dhis2.common.services.EventTripService;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2InstanceDataElementService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * @author Chathura Widanage
 */
@Service
@EnableScheduling
public class EventDistributorService {

    private final Logger logger = LogManager.getLogger(EventDistributorService.class);

    @Value("${config.max-event-handlers}")
    private int concurrency;

    @Autowired
    private EventTripService eventTripService;

    @Autowired
    private TrackedEntityInstanceService trackedEntityInstanceService;

    @Autowired
    private DHIS2InstanceDataElementService dhis2InstanceDataElementService;

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    private ScheduledExecutorService timeoutExecutor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    @Scheduled(fixedDelay = 1000)
    public void distributeNewEvent() {
        if (executor.getActiveCount() > concurrency) {
            logger.debug("Too many concurrent tasks sleeping distributor for 10 seconds");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                logger.error("Error in waiting till tasks are finished");
            }
            return;
        }

        Iterable<EventTrip> newEventTrips = this.eventTripService.getNewEventTrips();
        newEventTrips.forEach(eventTrip -> {
            //changing the state to prevent re-picking
            eventTripService.transformStatus(eventTrip, EventTripStatus.SCHEDULED_FOR_PROCESSING, null);
            eventTripService.save(eventTrip);

            Future<EventTrip> eventTripFuture = executor.submit(new AsyncEventTripHandler(
                    eventTrip,
                    trackedEntityInstanceService,
                    eventTripService,
                    dhis2InstanceDataElementService
            ));

            //timeout tasks in 5 minutes if blocking
            timeoutExecutor.schedule(() -> {
                eventTripFuture.cancel(true);
            }, 5, TimeUnit.MINUTES);
        });
    }
}
