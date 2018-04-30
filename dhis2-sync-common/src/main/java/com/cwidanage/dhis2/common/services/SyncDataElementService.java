package com.cwidanage.dhis2.common.services;

import com.cwidanage.dhis2.common.exceptions.ValidationException;
import com.cwidanage.dhis2.common.models.sync.SyncDataElement;
import com.cwidanage.dhis2.common.repositories.SyncDataElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author Chathura Widanage
 */
@Component
public class SyncDataElementService {

    @Autowired
    private SyncDataElementRepository repository;

    public SyncDataElement save(SyncDataElement syncDataElement) {
        if (StringUtils.isEmpty(syncDataElement.getDisplayName())) {
            throw new ValidationException("Display name can't be empty");
        } else if (StringUtils.isEmpty(syncDataElement.getCode())) {
            throw new ValidationException("Code can't be empty");
        }
        return this.repository.save(syncDataElement);
    }

    public Iterable<SyncDataElement> getAll() {
        return repository.findAll();
    }

    public SyncDataElement getById(String id) {
        return this.repository.findOne(id);
    }
}

