package com.cwidanage.dhis2.acceptor.models;

public class EventRouteCreateRequest {

    private String name;
    private String sourceProgramStage;
    private String destinationProgramStage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceProgramStage() {
        return sourceProgramStage;
    }

    public void setSourceProgramStage(String sourceProgramStage) {
        this.sourceProgramStage = sourceProgramStage;
    }

    public String getDestinationProgramStage() {
        return destinationProgramStage;
    }

    public void setDestinationProgramStage(String destinationProgramStage) {
        this.destinationProgramStage = destinationProgramStage;
    }
}
