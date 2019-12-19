package com.firhan.leafnote.repositories;

import com.firhan.leafnote.rooms.daos.NoteDao;
import com.firhan.leafnote.rooms.entities.Note;

import java.util.List;

import javax.inject.Inject;

public class NoteRepository {
    private NoteDao noteDao;

    @Inject
    public NoteRepository(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    public List<Note> getAll(){
        return noteDao.getAll();
    }

    public long createNote(Note note){
        return noteDao.insert(note);
    }

    public void deleteNote(Note note){
        noteDao.delete(note);
    }

    public void editNote(Note note){
        noteDao.update(note);
    }
}
