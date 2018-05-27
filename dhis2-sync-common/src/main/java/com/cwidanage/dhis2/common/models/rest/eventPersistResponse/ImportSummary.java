package com.cwidanage.dhis2.common.models.rest.eventPersistResponse;

/**
 * @author Chathura Widanage
 */
public class ImportSummary {

    private String description;
    private String status;
    private String reference;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ImportSummary{" +
                "description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }
}
