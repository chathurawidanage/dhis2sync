package com.cwidanage.dhis2.common.models;

import com.cwidanage.dhis2.common.constants.TransmittableEventStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TransmittableEvent {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @OneToOne(cascade = CascadeType.ALL)
    private Event event;

    //source instance
    private String instanceId;

    @OneToOne(cascade = CascadeType.ALL)
    private EventStatusTransformation latestTransformation;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EventStatusTransformation> statusTransformations = new ArrayList<>();

    public TransmittableEvent() {
    }

    public TransmittableEvent(Event event, String instanceId) {
        this.event = event;
        this.instanceId = instanceId;
        this.latestTransformation = new EventStatusTransformation();
        this.latestTransformation.setCurrentStatus(TransmittableEventStatus.FETCHED_FROM_SOURCE);
        this.statusTransformations.add(this.latestTransformation);
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

    public List<EventStatusTransformation> getStatusTransformations() {
        return statusTransformations;
    }

    public void setStatusTransformations(List<EventStatusTransformation> statusTransformations) {
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
