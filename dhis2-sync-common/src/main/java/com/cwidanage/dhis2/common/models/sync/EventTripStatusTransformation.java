package com.cwidanage.dhis2.common.models.sync;

import com.cwidanage.dhis2.common.constants.EventTripStatus;
import com.cwidanage.dhis2.common.constants.TransmittableEventStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
public class EventTripStatusTransformation {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Enumerated(EnumType.STRING)
    private EventTripStatus previousStatus;

    @Enumerated(EnumType.STRING)
    private EventTripStatus currentStatus;

    private String message;

    private Date timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EventTripStatus getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(EventTripStatus previousStatus) {
        this.previousStatus = previousStatus;
    }

    public EventTripStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(EventTripStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @PrePersist
    public void onPrePersist() {
        this.timestamp = new Date();
    }
}
