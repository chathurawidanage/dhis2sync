package com.cwidanage.dhis2.publisher.exception;

import com.cwidanage.dhis2.common.exceptions.JobTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class EventPersistTimeoutException extends JobTimeoutException {

    public EventPersistTimeoutException(String msg) {
        super(msg);
    }
}
