package com.firhan.leafnote.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.firhan.leafnote.repositories.SettingRepository;
import com.firhan.leafnote.rooms.entities.Setting;

import javax.inject.Inject;

public class SettingViewModel extends ViewModel {
    private SettingRepository settingRepository;
    private LiveData<Setting> settings;

    @Inject
    public SettingViewModel(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;

        settings = this.settingRepository.getSetting();
    }

    public void updateLockApp(Boolean isLock){
        //set new setting
        Setting setting = getSettings().getValue();
        setting.setAppLock(isLock);

        //call repo
        settingRepository.updateSetting(setting);
    }

    public void updateUsePin(Boolean isUsePin){
        //set new setting
        Setting setting = getSettings().getValue();
        setting.setUsePin(isUsePin);

        //call repo
        settingRepository.updateSetting(setting);
    }

    public void updatePin(String pin){
        //set new setting
        Setting setting = getSettings().getValue();
        setting.setPin(pin);

        //call repo
        settingRepository.updateSetting(setting);
    }

    public void updateUseFingerprint(Boolean isUseFingerprint){
        //set new setting
        Setting setting = getSettings().getValue();
        setting.setUseFingerPrint(isUseFingerprint);

        //call repo
        settingRepository.updateSetting(setting);
    }

    public LiveData<Setting> getSettings(){
        return settings;
    }
}
