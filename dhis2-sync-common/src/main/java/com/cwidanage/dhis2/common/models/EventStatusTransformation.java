package com.cwidanage.dhis2.common.models;

import com.cwidanage.dhis2.common.constants.TransmittableEventStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
public class EventStatusTransformation {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Enumerated(EnumType.STRING)
    private TransmittableEventStatus previousStatus;

    @Enumerated(EnumType.STRING)
    private TransmittableEventStatus currentStatus;

    private Date timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TransmittableEventStatus getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(TransmittableEventStatus previousStatus) {
        this.previousStatus = previousStatus;
    }

    public TransmittableEventStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(TransmittableEventStatus currentStatus) {
        this.currentStatus = currentStatus;
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
