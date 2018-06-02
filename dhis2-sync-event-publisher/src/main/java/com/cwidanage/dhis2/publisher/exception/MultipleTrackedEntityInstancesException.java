package com.cwidanage.dhis2.publisher.exception;

import com.cwidanage.dhis2.common.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class MultipleTrackedEntityInstancesException extends ValidationException {

    public MultipleTrackedEntityInstancesException(String msg) {
        super(msg);
    }

}
