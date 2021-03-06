package com.firhan.leafnote.dagger.modules;

import android.app.Application;

import androidx.room.Room;

import com.firhan.leafnote.repositories.NoteRepository;
import com.firhan.leafnote.repositories.SettingRepository;
import com.firhan.leafnote.rooms.daos.NoteDao;
import com.firhan.leafnote.rooms.daos.SettingDao;
import com.firhan.leafnote.rooms.databases.NoteDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NotesModule {
    @Provides
    @Singleton
    public NoteDatabase provideNoteDatabase(Application application){
        return Room.databaseBuilder(application, NoteDatabase.class, "notes")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    public NoteDao provideNoteDao(NoteDatabase noteDatabase){
        return noteDatabase.noteDao();
    }

    @Provides
    @Singleton
    public NoteRepository noteRepository(NoteDao noteDao){
        return new NoteRepository(noteDao);
    }

    @Provides
    @Singleton
    public SettingDao provideSettingDao(NoteDatabase noteDatabase){
        return noteDatabase.settingDao();
    }

    @Provides
    @Singleton
    public SettingRepository provideSettingRepository(SettingDao settingDao){
        return new SettingRepository(settingDao);
    }
}
