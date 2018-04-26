package com.cwidanage.dhis2.common.services;

import com.cwidanage.dhis2.common.models.DHIS2Instance;
import com.cwidanage.dhis2.common.repositories.DHIS2InstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DHIS2InstanceService {

    @Autowired
    private DHIS2InstanceRepository dhis2InstanceRepository;

    public DHIS2Instance save(DHIS2Instance dhis2Instance) {
        return this.dhis2InstanceRepository.save(dhis2Instance);
    }

    public Iterable<DHIS2Instance> getAll() {
        return this.dhis2InstanceRepository.findAll();
    }
}
