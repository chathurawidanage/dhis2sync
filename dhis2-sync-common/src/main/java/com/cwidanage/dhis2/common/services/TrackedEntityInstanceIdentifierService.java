package com.cwidanage.dhis2.common.services;

import com.cwidanage.dhis2.common.models.sync.TrackedEntityInstanceIdentifier;
import com.cwidanage.dhis2.common.repositories.TrackedEntityInstanceIdentifierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Chathura Widanage
 */
@Component
public class TrackedEntityInstanceIdentifierService {

    @Autowired
    private TrackedEntityInstanceIdentifierRepository repository;

    private TrackedEntityInstanceIdentifier getById(String id) {
        return repository.findOne(id);
    }

    private TrackedEntityInstanceIdentifier create(String id) {
        TrackedEntityInstanceIdentifier trackedEntityInstanceIdentifier = new TrackedEntityInstanceIdentifier();
        trackedEntityInstanceIdentifier.setId(id);
        return repository.save(trackedEntityInstanceIdentifier);
    }

    public TrackedEntityInstanceIdentifier getOrCreateIfNotExist(String id) {
        TrackedEntityInstanceIdentifier trackedEntityInstanceIdentifier = this.getById(id);
        if (trackedEntityInstanceIdentifier == null) {
            return this.create(id);
        }
        return trackedEntityInstanceIdentifier;
    }

}
