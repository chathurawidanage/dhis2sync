package com.cwidanage.dhis2.common.constants;

public enum TransmittableEventStatus {
    FETCHED_FROM_SOURCE,
    SUBMITTED_TO_UPSTREAM,//waiting for delivery report
    ACCEPTED_BY_UPSTREAM,//delivery report received
    WAITING_FOR_TRANSFORMATION_DATA,
    TRANSFORMED_TO_COMMON_FORMAT,
}
