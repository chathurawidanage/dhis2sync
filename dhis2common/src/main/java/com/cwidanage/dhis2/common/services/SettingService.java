package com.cwidanage.dhis2.common.services;

import com.cwidanage.dhis2.common.models.Setting;
import com.cwidanage.dhis2.common.repositories.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class SettingService {

    private static HashMap<String, Setting> memoryCache = new HashMap<>();

    @Autowired
    private SettingRepository settingRepository;

    public Setting setValue(String key, String value) {
        Setting setting = settingRepository.save(new Setting(key, value));
        memoryCache.put(key, setting);
        return setting;
    }

    public Setting getValue(String key) {
        return memoryCache.computeIfAbsent(key, s -> this.settingRepository.findOne(s));
    }
}
