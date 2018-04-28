package com.cwidanage.dhis2.common.repositories;

import com.cwidanage.dhis2.common.models.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event,String>{
}
