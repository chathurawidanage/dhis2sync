package com.cwidanage.dhis2.publisher.services;

import com.cwidanage.dhis2.common.models.dhis2.TrackedEntityAttribute;
import com.cwidanage.dhis2.common.models.dhis2.TrackedEntityInstance;
import com.cwidanage.dhis2.common.models.rest.TrackedEntityInstanceResponse;
import com.cwidanage.dhis2.publisher.Configuration;
import com.cwidanage.dhis2.publisher.exception.TrackedEntityInstanceIDNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
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

    public TrackedEntityInstanceResponse fetchById(String teiId) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(dhis2ApiEndpoint);
        uriComponentsBuilder.pathSegment("trackedEntityInstances", teiId + ".json");

        ResponseEntity<TrackedEntityInstance> entity = restTemplate.getForEntity(uriComponentsBuilder.toUriString(),
                TrackedEntityInstance.class);
        TrackedEntityInstance trackedEntityInstance = entity.getBody();

        Map<String, String> attributesMap = trackedEntityInstance.getAttributes()
                .stream()
                .collect(Collectors.toMap(TrackedEntityAttribute::getAttribute, TrackedEntityAttribute::getValue));
        if (attributesMap.containsKey(this.configuration.getTrackedEntityIdentifier())) {
            TrackedEntityInstanceResponse trackedEntityInstanceResponse = new TrackedEntityInstanceResponse();
            trackedEntityInstanceResponse.setIdentifier(attributesMap.get(this.configuration.getTrackedEntityIdentifier()));
            trackedEntityInstance.getAttributes().clear();//no need to send attributes
            trackedEntityInstanceResponse.setTrackedEntityInstance(trackedEntityInstance);
            return trackedEntityInstanceResponse;
        } else {
            throw new TrackedEntityInstanceIDNotFoundException(teiId);
        }
    }
}
