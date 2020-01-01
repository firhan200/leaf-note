package com.firhan.leafnote.rooms.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.firhan.leafnote.rooms.daos.NoteDao;
import com.firhan.leafnote.rooms.daos.SettingDao;
import com.firhan.leafnote.rooms.entities.Note;
import com.firhan.leafnote.rooms.entities.Setting;

@Database(entities =
        { Note.class, Setting.class}, version = 3, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
    public abstract SettingDao settingDao();
}
