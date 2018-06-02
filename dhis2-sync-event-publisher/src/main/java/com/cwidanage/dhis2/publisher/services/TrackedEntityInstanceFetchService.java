package com.cwidanage.dhis2.publisher.services;

import com.cwidanage.dhis2.common.models.dhis2.TrackedEntityAttribute;
import com.cwidanage.dhis2.common.models.dhis2.TrackedEntityInstance;
import com.cwidanage.dhis2.common.models.rest.TrackedEntityInstanceQueryResponse;
import com.cwidanage.dhis2.publisher.Configuration;
import com.cwidanage.dhis2.publisher.exception.MultipleTrackedEntityInstancesException;
import com.cwidanage.dhis2.publisher.exception.TrackedEntityInstanceDataFetchTimeoutException;
import com.cwidanage.dhis2.publisher.exception.TrackedEntityInstanceIDNotFoundException;
import com.cwidanage.dhis2.publisher.models.TrackedEntityInstanceResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

/**
 * @author Chathura Widanage
 */
@Service
public class TrackedEntityInstanceFetchService {

    private final static Logger logger = LogManager.getLogger(TrackedEntityInstanceFetchService.class);

    @Value("${dhis2.apiEndPoint}")
    String dhis2ApiEndpoint;

    @Autowired
    private Configuration configuration;

    @Autowired
    private RestTemplate restTemplate;

    //to prevent underlying DHIS2 instance from overloading
    private Semaphore dhis2ConnectionTrottler = new Semaphore(100);

    public TrackedEntityInstanceQueryResponse fetchByTEIId(String teiId) {
        try {
            dhis2ConnectionTrottler.acquire();
            logger.debug("Request received to query TEI with TeiID {}", teiId);
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(dhis2ApiEndpoint);
            uriComponentsBuilder.pathSegment("trackedEntityInstances", teiId + ".json");
            ResponseEntity<TrackedEntityInstance> entity = restTemplate.getForEntity(uriComponentsBuilder.toUriString(),
                    TrackedEntityInstance.class);
            TrackedEntityInstance trackedEntityInstance = entity.getBody();
            return this.createTrackedEntityInstanceResponse(trackedEntityInstance);
        } catch (InterruptedException e) {
            logger.error("Error occurred when waiting for the TEI data in queue", e);
            throw new TrackedEntityInstanceDataFetchTimeoutException("Error occurred when waiting for the TEI data in queue");
        } finally {
            dhis2ConnectionTrottler.release();
        }
    }

    public TrackedEntityInstanceQueryResponse fetchByAttributeValue(String attributeValue) {
        try {
            this.dhis2ConnectionTrottler.acquire();
            logger.debug("Request received to query TEI with attribute {}", attributeValue);
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(dhis2ApiEndpoint);
            uriComponentsBuilder.pathSegment("trackedEntityInstances.json");
            uriComponentsBuilder.queryParam("ouMode", "ACCESSIBLE");
            uriComponentsBuilder.queryParam("filter", String.format("%s:EQ:%s", this.configuration.getTrackedEntityIdentifier(), attributeValue));

            ResponseEntity<TrackedEntityInstanceResponse> responseEntity = restTemplate.getForEntity(uriComponentsBuilder.toUriString(),
                    TrackedEntityInstanceResponse.class);

            List<TrackedEntityInstance> trackedEntityInstances = responseEntity.getBody().getTrackedEntityInstances();
            if (trackedEntityInstances.isEmpty()) {
                logger.debug("TEI not found for attribute value {}", attributeValue);
                throw new TrackedEntityInstanceIDNotFoundException(attributeValue);
            } else if (trackedEntityInstances.size() > 1) {
                List<String> invalidTEIIds = trackedEntityInstances.stream().map(TrackedEntityInstance::getTrackedEntityInstance).collect(Collectors.toList());
                logger.warn("More than one TEI found for unique attribute {} : {}", attributeValue, invalidTEIIds.toString());
                throw new MultipleTrackedEntityInstancesException(String.format("More than one TEI found for unique attribute %s : %s", attributeValue, invalidTEIIds.toString()));
            } else {
                TrackedEntityInstance trackedEntityInstance = trackedEntityInstances.get(0);
                logger.debug("TEI {} found for attribute value {}", trackedEntityInstance.getTrackedEntityInstance(), attributeValue);
                return this.createTrackedEntityInstanceResponse(trackedEntityInstance);
            }
        } catch (InterruptedException e) {
            logger.error("Error occurred when waiting for the TEI data in queue", e);
            throw new TrackedEntityInstanceDataFetchTimeoutException("Error occurred when waiting for the TEI data in queue");
        } finally {
            this.dhis2ConnectionTrottler.release();
        }
    }


    private TrackedEntityInstanceQueryResponse createTrackedEntityInstanceResponse(TrackedEntityInstance trackedEntityInstance) {
        Map<String, String> attributesMap = trackedEntityInstance.getAttributes()
                .stream()
                .collect(Collectors.toMap(TrackedEntityAttribute::getAttribute, TrackedEntityAttribute::getValue));
        if (attributesMap.containsKey(this.configuration.getTrackedEntityIdentifier())) {
            TrackedEntityInstanceQueryResponse trackedEntityInstanceQueryResponse = new TrackedEntityInstanceQueryResponse();
            trackedEntityInstanceQueryResponse.setIdentifier(attributesMap.get(this.configuration.getTrackedEntityIdentifier()));
            trackedEntityInstance.getAttributes().clear();//no need to send attributes
            trackedEntityInstanceQueryResponse.setTrackedEntityInstance(trackedEntityInstance);
            return trackedEntityInstanceQueryResponse;
        } else {
            throw new TrackedEntityInstanceIDNotFoundException(trackedEntityInstance.getTrackedEntityInstance());
        }
    }
}
