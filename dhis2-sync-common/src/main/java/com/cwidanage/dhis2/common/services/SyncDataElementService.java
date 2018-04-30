package com.cwidanage.dhis2.common.services;

import com.cwidanage.dhis2.common.models.sync.SyncDataElement;
import com.cwidanage.dhis2.common.repositories.SyncDataElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Chathura Widanage
 */
@Component
public class SyncDataElementService {

    @Autowired
    private SyncDataElementRepository repository;

    public SyncDataElement save(SyncDataElement syncDataElement) {
        return this.repository.save(syncDataElement);
    }

    public Iterable<SyncDataElement> getAll() {
        return repository.findAll();
    }

    public SyncDataElement getById(String id) {
        return this.repository.findOne(id);
    }
}

