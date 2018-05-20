package com.cwidanage.dhis2.common.models;

import com.cwidanage.dhis2.common.constants.TransmittableEventStatus;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.EventTrip;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class TransmittableEvent {

    @Id
    private String id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Event event;

    //source instance
    private String instanceId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<EventTrip> eventTrips = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private EventStatusTransformation latestTransformation;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<EventStatusTransformation> statusTransformations = new HashSet<>();

    public TransmittableEvent() {
    }

    public TransmittableEvent(Event event, String instanceId) {
        this.id = String.format("%s_%s", instanceId, event.getEvent());
        this.event = event;
        this.instanceId = instanceId;
        this.latestTransformation = new EventStatusTransformation();
        this.latestTransformation.setCurrentStatus(TransmittableEventStatus.FETCHED_FROM_SOURCE);
        this.statusTransformations.add(this.latestTransformation);
    }

    public Set<EventTrip> getEventTrips() {
        return eventTrips;
    }

    public void setEventTrips(Set<EventTrip> eventTrips) {
        this.eventTrips = eventTrips;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public EventStatusTransformation getLatestTransformation() {
        return latestTransformation;
    }

    public void setLatestTransformation(EventStatusTransformation latestTransformation) {
        this.latestTransformation = latestTransformation;
    }

    public Set<EventStatusTransformation> getStatusTransformations() {
        return statusTransformations;
    }

    public void setStatusTransformations(Set<EventStatusTransformation> statusTransformations) {
        this.statusTransformations = statusTransformations;
    }

    @Override
    public String toString() {
        return "TransmittableEvent{" +
                "id='" + id + '\'' +
                ", event=" + event +
                ", instanceId='" + instanceId + '\'' +
                ", latestTransformation=" + latestTransformation +
                ", statusTransformations=" + statusTransformations +
                '}';
    }
}
