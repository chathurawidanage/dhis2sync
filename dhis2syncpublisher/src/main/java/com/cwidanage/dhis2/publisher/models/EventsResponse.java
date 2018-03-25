package com.cwidanage.dhis2.publisher.models;

import com.cwidanage.dhis2.common.models.Event;

import java.util.List;

public class EventsResponse {

    private Pager pager;
    private List<Event> events;

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
