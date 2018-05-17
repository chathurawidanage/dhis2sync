package com.cwidanage.dhis2.common.models.sync;

import com.cwidanage.dhis2.common.models.TransmittableEvent;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class EventTrip {

    @Id
    private String id;

    @ManyToOne(optional = false)
    private EventRoute eventRoute;

    @ManyToOne(optional = false)
    private TransmittableEvent transmittableEvent;

    @OneToOne
    private EventTripStatusTransformation latestTransformation;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<EventTripStatusTransformation> eventTripStatusTransformations = new HashSet<>();

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

    public Set<EventTripStatusTransformation> getEventTripStatusTransformations() {
        return eventTripStatusTransformations;
    }

    public void setEventTripStatusTransformations(Set<EventTripStatusTransformation> eventTripStatusTransformations) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventTrip eventTrip = (EventTrip) o;

        return id.equals(eventTrip.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
