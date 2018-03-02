package com.cwidanage.dhis2.publisher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class PublisherClient {
    public static final String ACCEPTOR_SERVICE_URL
            = "http://ACCEPTOR-SERVICE";

    @Value("${dhis2.username}")
    private String dhis2Username;

    @Value("${dhis2.password}")
    private String dhis2Password;


    public static void main(String[] args) {
        System.setProperty("spring.config.name", "publisher-service");
        SpringApplication.run(PublisherClient.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptorList = new ArrayList<>();
        interceptorList.add(new BasicAuthorizationInterceptor(dhis2Username, dhis2Password));
        restTemplate.setInterceptors(interceptorList);
        return restTemplate;
    }
}
