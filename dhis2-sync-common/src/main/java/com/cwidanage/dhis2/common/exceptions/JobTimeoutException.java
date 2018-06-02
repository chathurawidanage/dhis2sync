package com.cwidanage.dhis2.common.exceptions;

public class JobTimeoutException extends RuntimeException {

    public JobTimeoutException(String msg) {
        super(msg);
    }
}
