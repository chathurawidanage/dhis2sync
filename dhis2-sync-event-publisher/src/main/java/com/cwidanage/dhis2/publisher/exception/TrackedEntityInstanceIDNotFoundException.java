package com.cwidanage.dhis2.publisher.exception;

import com.cwidanage.dhis2.common.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TrackedEntityInstanceIDNotFoundException extends NotFoundException {

    public TrackedEntityInstanceIDNotFoundException(String teiId) {
        super("TrackedEntity unique identifier not found for " + teiId);
    }
}
