package com.cwidanage.dhis2.acceptor.services;

import com.cwidanage.dhis2.common.models.rest.TrackedEntityInstanceQueryResponse;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.TrackedEntityInstanceIdentifier;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceTrackedEntityInstance;
import com.cwidanage.dhis2.common.repositories.dhis2.DHIS2TrackedEntityInstanceRepository;
import com.cwidanage.dhis2.common.services.TrackedEntityInstanceIdentifierService;
import com.cwidanage.dhis2.common.services.dhis2.DHIS2TrackedEntityInstanceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * @author Chathura Widanage
 */
@Service
public class TrackedEntityInstanceService extends DHIS2TrackedEntityInstanceService {

    private final Logger logger = LogManager.getLogger(TrackedEntityInstanceService.class);

    @Autowired
    private TrackedEntityInstanceIdentifierService teiIdentifierService;

    public DHIS2InstanceTrackedEntityInstance findByTEIId(DHIS2Instance dhis2Instance, String trackedEntityInstanceId) {
        logger.debug("Finding TEI {} on instance {}", trackedEntityInstanceId, dhis2Instance.getId());
        DHIS2InstanceTrackedEntityInstance instance = super.findByTEIId(dhis2Instance, trackedEntityInstanceId);
        if (instance == null) {
            logger.debug("TEI {} of {} was not found in local cache(DB). Calling remote instance...",
                    trackedEntityInstanceId, dhis2Instance.getId());
            //do network call
            TrackedEntityInstanceQueryResponse uniqueByTeiId = this.getUniqueByTeiId(dhis2Instance, trackedEntityInstanceId);
            if (uniqueByTeiId == null) {
                logger.debug("TEI {} of {} not found in remote instance", trackedEntityInstanceId, dhis2Instance.getId());
                return null;
            } else {
                logger.debug("Creating TEI Identifier for {} of {}", trackedEntityInstanceId, dhis2Instance.getId());
                TrackedEntityInstanceIdentifier teiIdentifier = teiIdentifierService.getOrCreateIfNotExist(
                        uniqueByTeiId.getIdentifier()
                );

                return super.createAndSave(
                        dhis2Instance,
                        uniqueByTeiId.getTrackedEntityInstance(),
                        teiIdentifier
                );
            }
        } else {
            logger.debug("Found TEI {} of {} in local cache(DB)", trackedEntityInstanceId, dhis2Instance.getId());
            return instance;
        }
    }

    public DHIS2InstanceTrackedEntityInstance findByAttributeValue(DHIS2Instance dhis2Instance, String attributeValue) {
        logger.debug("Finding TEI with attribute value {} on instance {}", attributeValue, dhis2Instance.getId());
        DHIS2InstanceTrackedEntityInstance instance = super.findByAttributeValue(dhis2Instance, attributeValue);
        if (instance == null) {
            logger.debug("TEI with attribute value {} of {} was not found in local cache(DB). Calling remote instance...",
                    attributeValue, dhis2Instance.getId());
            //do network call
            TrackedEntityInstanceQueryResponse uniqueByAttributeValue = this.getUniqueByAttributeValue(dhis2Instance, attributeValue);
            if (uniqueByAttributeValue == null) {
                logger.debug("TEI with attribute value {} of {} not found in remote instance", attributeValue, dhis2Instance.getId());
                return null;
            } else {
                logger.debug("Creating TEIIdentifier for TEI with attribute value {} of {}", attributeValue, dhis2Instance.getId());
                TrackedEntityInstanceIdentifier teiIdentifier = teiIdentifierService.getOrCreateIfNotExist(
                        uniqueByAttributeValue.getIdentifier()
                );

                return super.createAndSave(
                        dhis2Instance,
                        uniqueByAttributeValue.getTrackedEntityInstance(),
                        teiIdentifier
                );
            }
        } else {
            logger.debug("Found TEI with attribute value {} of {} in local cache(DB)", attributeValue, dhis2Instance.getId());
            return instance;
        }
    }

    private TrackedEntityInstanceQueryResponse getUniqueByAttributeValue(DHIS2Instance dhis2Instance, String attributeValue) {
        return this.retrieveTEI(dhis2Instance, null, attributeValue);
    }

    private TrackedEntityInstanceQueryResponse getUniqueByTeiId(DHIS2Instance dhis2Instance, String trackedEntityInstanceId) {
        return this.retrieveTEI(dhis2Instance, trackedEntityInstanceId, null);
    }

    private TrackedEntityInstanceQueryResponse retrieveTEI(DHIS2Instance dhis2Instance, String trackedEntityInstanceId, String attributeValue) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(dhis2Instance.getUrl());
        uriComponentsBuilder.pathSegment("trackedEntityInstances", "query");
        if (trackedEntityInstanceId != null) {
            uriComponentsBuilder.queryParam("tei", trackedEntityInstanceId);
        } else if (attributeValue != null) {
            uriComponentsBuilder.queryParam("attributeValue", attributeValue);
        } else {
            return null;
        }

        try {
            ResponseEntity<TrackedEntityInstanceQueryResponse> responseResponseEntity = restTemplate.getForEntity(
                    uriComponentsBuilder.toUriString(),
                    TrackedEntityInstanceQueryResponse.class
            );
            return responseResponseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Failed TEI query request for TEI {}:{}", trackedEntityInstanceId, attributeValue, ex);
            return null;
        }
    }
}
