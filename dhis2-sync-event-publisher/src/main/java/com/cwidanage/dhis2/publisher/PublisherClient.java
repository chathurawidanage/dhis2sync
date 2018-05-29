package com.cwidanage.dhis2.publisher;

import com.cwidanage.dhis2.common.models.Event;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@ComponentScan(basePackages = {"com.cwidanage"})
@EnableJpaRepositories(basePackages = "com.cwidanage")
@EntityScan(basePackages = "com.cwidanage")
@SpringBootApplication
@EnableJms
public class PublisherClient {

    private final static Logger logger = LogManager.getLogger(PublisherClient.class);

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
        try {
            URL resource = PublisherClient.class.getClassLoader().getResource(configFile);
            return Configuration.buildByFile(new File(resource.getFile()));
        } catch (IllegalArgumentException | NullPointerException ilex) {
            return Configuration.buildByFile(new File(configFile));
        }
    }

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return createRestTemplate();
    }

    private RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptorList = new ArrayList<>();
        interceptorList.add(new BasicAuthorizationInterceptor(dhis2Username, dhis2Password));
        restTemplate.setInterceptors(interceptorList);
        return restTemplate;
    }

    @Bean("eventPostRestTemplate")
    public RestTemplate eventPostRestTemplate(@Autowired @Qualifier("eventPostResponseMapper") ObjectMapper objectMapper) {
        RestTemplate restTemplate = this.createRestTemplate();

        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setPrettyPrint(false);
        restTemplate.getMessageConverters().removeIf(m -> m.getClass().getName().equals(MappingJackson2HttpMessageConverter.class.getName()));
        messageConverter.setObjectMapper(objectMapper);

        restTemplate.getMessageConverters().add(messageConverter);
        return restTemplate;
    }

    @Bean("eventPostResponseMapper")
    public ObjectMapper eventPostResponseMapper() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(df);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
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

    public MessageConverter jacksonEventConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
