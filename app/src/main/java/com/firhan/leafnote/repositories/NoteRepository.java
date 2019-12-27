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

    public List<Note> searchNotes(String keyword){
        return noteDao.searchActiveNotes(keyword);
    }

    public List<Note> getAllTrashCan(){
        return noteDao.getAllTrash();
    }

    public Note getNoteById(long id){
        return noteDao.getNoteById(id);
    }

    public long createNote(Note note){
        return noteDao.insert(note);
    }

    public void softDeleteNote(Note note){
        note.setDeleted(true);
        noteDao.update(note);
    }

    public void deleteNote(Note note){
        noteDao.delete(note);
    }

    public void editNote(Note note){
        noteDao.update(note);
    }
}
