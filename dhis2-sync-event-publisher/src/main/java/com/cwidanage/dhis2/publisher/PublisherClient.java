package com.cwidanage.dhis2.publisher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.web.client.RestTemplate;

import javax.jms.ConnectionFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ComponentScan(basePackages = {"com.cwidanage"})
@EnableJpaRepositories(basePackages = "com.cwidanage")
@EntityScan(basePackages = "com.cwidanage")
@SpringBootApplication
@EnableJms
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

    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all boot's default to this factory, including the message converter
        configurer.configure(factory, connectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
