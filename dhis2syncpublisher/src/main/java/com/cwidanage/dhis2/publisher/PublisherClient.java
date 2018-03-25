package com.cwidanage.dhis2.publisher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EnableJpaRepositories(basePackages = "com.cwidanage")
@EntityScan(basePackages = "com.cwidanage")
@SpringBootApplication
@EnableDiscoveryClient
public class PublisherClient {
    public static final String ACCEPTOR_SERVICE_URL
            = "http://ACCEPTOR-SERVICE";

    @Value("${dhis2.username}")
    private String dhis2Username;

    @Value("${dhis2.password}")
    private String dhis2Password;

    @Value("${dhis2.config}")
    private String configFile;

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "publisher-service");
        SpringApplication.run(PublisherClient.class, args);
    }

    @Bean
    public Configuration configuration() throws IOException {
        return Configuration.buildByFile(new File(PublisherClient.class.getClassLoader().getResource(configFile).getFile()));
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
