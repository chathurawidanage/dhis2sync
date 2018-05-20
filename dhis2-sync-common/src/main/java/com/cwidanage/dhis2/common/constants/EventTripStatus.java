package com.cwidanage.dhis2.common.constants;

public enum EventTripStatus {
    INITIALIZED,
    WAITING_FOR_TEI_DATA,
    WAITING_FOR_EVENT_TRANSFORMATION_DATA,
    UPSTREAM_OFFLINE,
    DOWNSTREAM_OFFLINE,
    REJECTED_BY_DOWNSTREAM,
    COMPLETED
}