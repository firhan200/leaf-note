package com.firhan.leafnote.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firhan.leafnote.rooms.daos.SettingDao;
import com.firhan.leafnote.rooms.entities.Setting;

import javax.inject.Inject;

public class SettingRepository {
    private SettingDao settingDao;
    private MutableLiveData<Setting> settings;

    @Inject
    public SettingRepository(SettingDao settingDao) {
        this.settingDao = settingDao;

        //init setting live data
        settings = new MutableLiveData<>();

        //init setting
        initSetting();
    }

    private void initSetting(){
        Setting setting = settingDao.getSetting();
        if(setting == null){
            //create default setting
            settingDao.insertSetting(new Setting(
                    1,
                    null,
                    false,
                    false,
                    false
            ));

            setting = settingDao.getSetting();
        }

        settings.setValue(setting);
    }

    public void updateSetting(Setting setting){
        //update database
        settingDao.updateSetting(setting);

        //update live data
        settings.setValue(setting);
    }

    //get live data
    public LiveData<Setting> getSetting(){
        return settings;
    }
}
