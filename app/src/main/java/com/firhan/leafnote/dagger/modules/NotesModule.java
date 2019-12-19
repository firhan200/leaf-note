package com.firhan.leafnote.dagger.modules;

import android.app.Application;

import androidx.room.Room;

import com.firhan.leafnote.repositories.NoteRepository;
import com.firhan.leafnote.rooms.daos.NoteDao;
import com.firhan.leafnote.rooms.databases.NoteDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NotesModule {
    @Provides
    @Singleton
    public NoteDatabase provideNoteDatabase(Application application){
        return Room.databaseBuilder(application, NoteDatabase.class, "notes").allowMainThreadQueries().build();
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
}
