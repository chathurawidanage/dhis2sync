package com.cwidanage.dhis2.publisher;

import com.cwidanage.dhis2.common.models.Program;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Chathura Widanage
 */
public class Configuration {

    private List<Program> programs;

    private Configuration() {
    }

    public static Configuration buildByFile(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file, Configuration.class);
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }
}
