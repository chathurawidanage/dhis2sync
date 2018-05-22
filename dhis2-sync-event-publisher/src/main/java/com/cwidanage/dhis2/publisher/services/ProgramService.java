package com.cwidanage.dhis2.publisher.services;

import com.cwidanage.dhis2.common.models.dhis2.Program;
import com.cwidanage.dhis2.common.models.dhis2.ProgramStage;
import com.cwidanage.dhis2.publisher.Configuration;
import com.cwidanage.dhis2.publisher.models.ProgramResponse;
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
public class ProgramService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${dhis2.apiEndPoint}")
    String dhis2ApiEndpoint;

    public List<Program> getAllPrograms() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(dhis2ApiEndpoint);
        uriComponentsBuilder.path("programs.json");
        uriComponentsBuilder.queryParam("page", "1");

        List<Program> programs = new ArrayList<>();

        int totalPages;
        int pageToFetch = 1;
        do {
            uriComponentsBuilder.replaceQueryParam("page", pageToFetch);
            ResponseEntity<ProgramResponse> responseEntity = restTemplate.getForEntity(uriComponentsBuilder.build(false).toUriString(), ProgramResponse.class);
            pageToFetch = responseEntity.getBody().getPager().getPage() + 1;
            totalPages = responseEntity.getBody().getPager().getPageCount();
            programs.addAll(responseEntity.getBody().getPrograms());
        } while (totalPages >= pageToFetch);

        return programs;
    }
}
