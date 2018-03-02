package com.cwidanage.dhis2.publisher.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@EnableScheduling
public class PublisherService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${dhis2.apiEndPoint}")
    String dhis2ApiEndpoint;


    @Async
    @Scheduled(fixedRate = 1000)
    public void fetch() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(dhis2ApiEndpoint);

        String response = restTemplate.getForEntity(dhis2ApiEndpoint + "/events.json", String.class);
        System.out.println(response);
    }
}
