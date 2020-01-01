package com.firhan.leafnote.rooms.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "settings")
public class Setting {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String pin;

    private Boolean isUsePin;

    private Boolean isUseFingerPrint;

    private Boolean isAppLock;

    public Setting(int id, String pin, Boolean isUsePin, Boolean isUseFingerPrint, Boolean isAppLock) {
        this.id = id;
        this.pin = pin;
        this.isUsePin = isUsePin;
        this.isUseFingerPrint = isUseFingerPrint;
        this.isAppLock = isAppLock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Boolean getUsePin() {
        return isUsePin;
    }

    public void setUsePin(Boolean usePin) {
        isUsePin = usePin;
    }

    public Boolean getUseFingerPrint() {
        return isUseFingerPrint;
    }

    public void setUseFingerPrint(Boolean useFingerPrint) {
        isUseFingerPrint = useFingerPrint;
    }

    public Boolean getAppLock() {
        return isAppLock;
    }

    public void setAppLock(Boolean appLock) {
        isAppLock = appLock;
    }
}
