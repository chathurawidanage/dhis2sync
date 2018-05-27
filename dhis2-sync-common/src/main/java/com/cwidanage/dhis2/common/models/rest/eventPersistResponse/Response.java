package com.cwidanage.dhis2.common.models.rest.eventPersistResponse;

import java.util.List;

/**
 * @author Chathura Widanage
 */
public class Response {

    private List<ImportSummary> importSummaries;

    public List<ImportSummary> getImportSummaries() {
        return importSummaries;
    }

    public void setImportSummaries(List<ImportSummary> importSummaries) {
        this.importSummaries = importSummaries;
    }

    @Override
    public String toString() {
        return "Response{" +
                "importSummaries=" + importSummaries +
                '}';
    }
}
