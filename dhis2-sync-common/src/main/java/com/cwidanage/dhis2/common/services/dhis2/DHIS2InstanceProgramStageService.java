package com.cwidanage.dhis2.common.services.dhis2;

import com.cwidanage.dhis2.common.models.dhis2.ProgramStage;
import com.cwidanage.dhis2.common.models.sync.DHIS2Instance;
import com.cwidanage.dhis2.common.models.sync.Syncability;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceProgram;
import com.cwidanage.dhis2.common.models.sync.dhis2.DHIS2InstanceProgramStage;
import com.cwidanage.dhis2.common.repositories.dhis2.DHIS2InstanceProgramStageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DHIS2InstanceProgramStageService {

    @Autowired
    private DHIS2InstanceProgramStageRepository repository;

    public Map<String, DHIS2InstanceProgramStage> getProgramStagesMap(DHIS2Instance dhis2Instance) {
        Iterable<DHIS2InstanceProgramStage> programStageIterable = this.repository.findByDhis2Instance(dhis2Instance);
        Map<String, DHIS2InstanceProgramStage> programStageMap = new HashMap<>();
        programStageIterable.forEach(ps -> programStageMap.put(ps.getIdentifier(), ps));
        return programStageMap;
    }

    public Iterable<DHIS2InstanceProgramStage> save(Set<DHIS2InstanceProgramStage> dhis2InstanceProgramStage) {
        return this.repository.save(dhis2InstanceProgramStage);
    }

    public static String generateIdentifier(DHIS2Instance dhis2Instance, ProgramStage programStage) {
        return String.format("%s_%s", dhis2Instance.getId(), programStage.getId());
    }

    public DHIS2InstanceProgramStage getByIdentifier(String identifier) {
        return this.repository.findOne(identifier);
    }

    public static DHIS2InstanceProgramStage createDHIS2InstanceProgramStage(DHIS2Instance dhis2Instance,
                                                                            ProgramStage programStage, DHIS2InstanceProgram dhis2InstanceProgram) {
        DHIS2InstanceProgramStage d2iProgramStage = new DHIS2InstanceProgramStage();
        d2iProgramStage.setDhis2Instance(dhis2Instance);
        d2iProgramStage.setIdentifier(generateIdentifier(dhis2Instance, programStage));
        d2iProgramStage.setId(programStage.getId());
        d2iProgramStage.setSyncability(new Syncability());
        d2iProgramStage.setDisplayName(programStage.getDisplayName());
        d2iProgramStage.setDhis2InstanceProgram(dhis2InstanceProgram);
        return d2iProgramStage;
    }
}
