package com.firhan.leafnote.rooms.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.firhan.leafnote.rooms.entities.Setting;

@Dao
public interface SettingDao {
    @Query("SELECT * FROM settings LIMIT 0,1")
    Setting getSetting();

    @Update
    void updateSetting(Setting setting);

    @Insert
    long insertSetting(Setting setting);
}
