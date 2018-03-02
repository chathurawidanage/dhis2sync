package com.cwidanage.dhis2.acceptor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(EventAcceptorService.class)
public class EventAcceptorServer {
    public static void main(String[] args) {
        System.setProperty("spring.config.name", "acceptor-server");
        SpringApplication.run(EventAcceptorServer.class, args);
    }
}
