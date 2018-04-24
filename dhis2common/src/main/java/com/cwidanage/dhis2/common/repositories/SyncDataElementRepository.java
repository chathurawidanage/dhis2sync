package com.cwidanage.dhis2.common.repositories;

import com.cwidanage.dhis2.common.models.SyncDataElement;
import org.springframework.data.repository.CrudRepository;

public interface SyncDataElementRepository extends CrudRepository<SyncDataElement, String> {
}
