package com.firhan.leafnote.rooms.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.firhan.leafnote.rooms.daos.NoteDao;
import com.firhan.leafnote.rooms.entities.Note;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
}
