package com.cwidanage.dhis2.publisher.services;

import com.cwidanage.dhis2.common.models.dhis2.ProgramStage;
import com.cwidanage.dhis2.publisher.Configuration;
import com.cwidanage.dhis2.publisher.models.ProgramStagesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProgramStageService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${dhis2.apiEndPoint}")
    String dhis2ApiEndpoint;

    @Autowired
    private Configuration configuration;

    public List<ProgramStage> getAllProgramStages() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(dhis2ApiEndpoint);
        uriComponentsBuilder.path("programStages.json");
        uriComponentsBuilder.queryParam("page", "1");

        List<ProgramStage> programStages = new ArrayList<>();

        int totalPages;
        int pageToFetch = 1;
        do {
            uriComponentsBuilder.replaceQueryParam("page", pageToFetch);
            ResponseEntity<ProgramStagesResponse> responseEntity = restTemplate.getForEntity(uriComponentsBuilder.toUriString(), ProgramStagesResponse.class);
            pageToFetch = responseEntity.getBody().getPager().getPage() + 1;
            totalPages = responseEntity.getBody().getPager().getPageCount();
            programStages.addAll(responseEntity.getBody().getProgramStages());
        } while (totalPages >= pageToFetch);

        return programStages;
    }
}
