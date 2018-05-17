package com.cwidanage.dhis2.publisher;

import com.cwidanage.dhis2.common.models.dhis2.Program;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Chathura Widanage
 */
public class Configuration {

    private List<Program> programs;

    private String instanceId;

    private String trackedEntityIdentifier;

    private Configuration() {
    }

    public String getTrackedEntityIdentifier() {
        return trackedEntityIdentifier;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public static Configuration buildByFile(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file, Configuration.class);
    }

    public List<Program> getPrograms() {
        return programs;
    }
}
