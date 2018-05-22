package com.cwidanage.dhis2.common.services.dhis2;

import com.cwidanage.dhis2.common.models.dhis2.Program;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.Syncability;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceProgram;
import com.cwidanage.dhis2.common.repositories.dhis2.DHIS2InstanceProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class DHIS2InstanceProgramService {

    @Autowired
    private DHIS2InstanceProgramRepository repository;

    public Iterable<DHIS2InstanceProgram> save(Set<DHIS2InstanceProgram> programs) {
        return this.repository.save(programs);
    }

    public Map<String, DHIS2InstanceProgram> getExistingProgramIdentifiers(DHIS2Instance dhis2Instance) {
        HashMap<String, DHIS2InstanceProgram> idsSet = new HashMap<>();
        this.repository.findAllByDhis2Instance(dhis2Instance).forEach(dhis2InstanceProgram -> {
            idsSet.put(dhis2InstanceProgram.getIdentifier(), dhis2InstanceProgram);
        });
        return idsSet;
    }

    public static DHIS2InstanceProgram createDHIS2InstanceProgram(DHIS2Instance dhis2Instance, Program program) {
        DHIS2InstanceProgram dhis2InstanceProgram = new DHIS2InstanceProgram();
        dhis2InstanceProgram.setDhis2Instance(dhis2Instance);
        dhis2InstanceProgram.setSyncability(new Syncability());
        dhis2InstanceProgram.setId(program.getId());
        dhis2InstanceProgram.setDisplayName(program.getDisplayName());
        dhis2InstanceProgram.setIdentifier(generateIdentifier(dhis2Instance, program));
        return dhis2InstanceProgram;
    }

    public static String generateIdentifier(DHIS2Instance dhis2Instance, Program program) {
        return String.format("%s_%s", dhis2Instance.getId(), program.getId());
    }
}
