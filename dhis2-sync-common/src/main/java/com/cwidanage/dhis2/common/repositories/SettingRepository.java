package com.cwidanage.dhis2.common.repositories;

import com.cwidanage.dhis2.common.models.Setting;
import org.springframework.data.repository.CrudRepository;

public interface SettingRepository extends CrudRepository<Setting, String> {
}
