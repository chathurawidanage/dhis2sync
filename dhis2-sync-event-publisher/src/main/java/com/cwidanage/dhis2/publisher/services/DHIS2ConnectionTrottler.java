package com.cwidanage.dhis2.publisher.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;

@Component
public class DHIS2ConnectionTrottler extends Semaphore {

    public DHIS2ConnectionTrottler(@Value("${config.max-dhis2-connections}") int permits) {
        super(permits);
    }
}
