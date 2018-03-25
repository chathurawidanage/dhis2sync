package com.cwidanage.dhis2.common.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author Chathura Widanage
 */
@Entity
public class Event {

    @Id
    private String event;
    private String program;
    private String programStage;
    private String orgUnit;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Coordinate coordinate;
    private Date eventDate;
    private Date lastUpdated;

    private String trackedEntityInstance;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DataValue> dataValues;

    public String getEvent() {
        return event;
    }

    public void setEvent(String id) {
        this.event = id;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getProgramStage() {
        return programStage;
    }

    public void setProgramStage(String programStage) {
        this.programStage = programStage;
    }

    public String getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getTrackedEntityInstance() {
        return trackedEntityInstance;
    }

    public void setTrackedEntityInstance(String trackedEntityInstance) {
        this.trackedEntityInstance = trackedEntityInstance;
    }

    public List<DataValue> getDataValues() {
        return dataValues;
    }

    public void setDataValues(List<DataValue> dataValues) {
        this.dataValues = dataValues;
    }
}
