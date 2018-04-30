package com.cwidanage.dhis2.common.models.sync;

import com.cwidanage.dhis2.common.models.TransmittableEvent;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class EventTrip {

    @Id
    private String id;

    @ManyToOne(optional = false)
    private EventRoute eventRoute;

    @ManyToOne
    private TransmittableEvent transmittableEvent;

    @OneToOne
    private EventTripStatusTransformation latestTransformation;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<EventTripStatusTransformation> eventTripStatusTransformations;

    private Date lastUpdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EventRoute getEventRoute() {
        return eventRoute;
    }

    public void setEventRoute(EventRoute eventRoute) {
        this.eventRoute = eventRoute;
    }

    public TransmittableEvent getTransmittableEvent() {
        return transmittableEvent;
    }

    public void setTransmittableEvent(TransmittableEvent transmittableEvent) {
        this.transmittableEvent = transmittableEvent;
    }

    public EventTripStatusTransformation getLatestTransformation() {
        return latestTransformation;
    }

    public void setLatestTransformation(EventTripStatusTransformation latestTransformation) {
        this.latestTransformation = latestTransformation;
    }

    public List<EventTripStatusTransformation> getEventTripStatusTransformations() {
        return eventTripStatusTransformations;
    }

    public void setEventTripStatusTransformations(List<EventTripStatusTransformation> eventTripStatusTransformations) {
        this.eventTripStatusTransformations = eventTripStatusTransformations;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @PreUpdate
    public void updateLastUpdate() {
        this.lastUpdate = new Date();
    }
}
