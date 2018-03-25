package com.cwidanage.dhis2.common.services;

import com.cwidanage.dhis2.common.models.Setting;
import com.cwidanage.dhis2.common.repositories.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    public Setting setValue(String key, String value) {
        return settingRepository.save(new Setting(key, value));
    }

    public String getValue(String key) {
        Setting setting = settingRepository.findOne(key);
        return setting.getValue();
    }
}
