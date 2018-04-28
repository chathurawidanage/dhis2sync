package com.cwidanage.dhis2.acceptor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;


@ComponentScan(basePackages = {"com.cwidanage"})
@EnableJpaRepositories(basePackages = "com.cwidanage")
@EntityScan(basePackages = "com.cwidanage")
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableJms
@Import(EventAcceptorService.class)
public class EventAcceptorServer {
    public static void main(String[] args) {
        System.setProperty("spring.config.name", "acceptor-server");
        SpringApplication.run(EventAcceptorServer.class, args);
    }
}
