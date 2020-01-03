package com.firhan.leafnote.repositories;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firhan.leafnote.rooms.daos.NoteDao;
import com.firhan.leafnote.rooms.entities.Note;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> notes;
    private LiveData<List<Note>> trashNotes;

    @Inject
    public NoteRepository(NoteDao noteDao) {
        this.noteDao = noteDao;

        notes = noteDao.getAll();
        trashNotes = noteDao.getAllTrash();
    }

    public LiveData<List<Note>> getAllNotes(){
        return notes;
    }

    public List<Note> searchNotes(String keyword){
        return noteDao.searchActiveNotes(keyword);
    }

    public LiveData<List<Note>> getAllTrashCan(){
        return trashNotes;
    }

    public Note getNoteById(long id){
        return noteDao.getNoteById(id);
    }

    public long createNote(Note note){
        //insert into db
        new InsertNote(noteDao).execute(note);

        return 1;
    }

    //soft delete single note
    public void softDeleteNote(Note note){
        new SoftDeleteNote(noteDao).execute(note);
    }

    //soft delete list of note
    public void softDeleteNotes(List<Note> notes){
        new SoftDeleteNotes(noteDao).execute(notes);
    }

    //permanent delete list of note
    public void deleteNotes(List<Note> notes){
        noteDao.deleteNotes(notes);
    }

    //edit note
    public void editNote(Note note){
        new UpdateNote(noteDao).execute(note);
    }

    private static class InsertNote extends AsyncTask<Note, Void, Void>{
        private NoteDao noteDao;

        public InsertNote(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);

            return null;
        }
    }

    private static class UpdateNote extends AsyncTask<Note, Void, Void>{
        private NoteDao noteDao;

        public UpdateNote(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);

            return null;
        }
    }

    private static class SoftDeleteNote extends AsyncTask<Note, Void, Void>{
        private NoteDao noteDao;

        public SoftDeleteNote(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            notes[0].setDeleted(true);
            noteDao.update(notes[0]);

            return null;
        }
    }

    private static class SoftDeleteNotes extends AsyncTask<List<Note>, Void, Void>{
        private NoteDao noteDao;

        public SoftDeleteNotes(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Note>... lists) {
            for(Note note: lists[0]){
                note.setDeleted(true);
            }
            noteDao.updateNotes(lists[0]);

            return null;
        }
    }
}
