package com.cwidanage.dhis2.publisher.services;

import com.cwidanage.dhis2.common.models.dhis2.DataElement;
import com.cwidanage.dhis2.publisher.Configuration;
import com.cwidanage.dhis2.publisher.models.DataElementResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataElementService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${dhis2.apiEndPoint}")
    String dhis2ApiEndpoint;

    @Autowired
    private Configuration configuration;

    public List<DataElement> getAllDataElements() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(dhis2ApiEndpoint);
        uriComponentsBuilder.path("dataElements.json");
        uriComponentsBuilder.queryParam("page", "1");

        List<DataElement> dataElements = new ArrayList<>();

        int totalPages;
        int pageToFetch = 1;
        do {
            uriComponentsBuilder.replaceQueryParam("page", pageToFetch);
            ResponseEntity<DataElementResponse> responseEntity = restTemplate.getForEntity(uriComponentsBuilder.toUriString(), DataElementResponse.class);
            pageToFetch = responseEntity.getBody().getPager().getPage() + 1;
            totalPages = responseEntity.getBody().getPager().getPageCount();
            dataElements.addAll(responseEntity.getBody().getDataElements());
        } while (totalPages >= pageToFetch);

        return dataElements;
    }
}
