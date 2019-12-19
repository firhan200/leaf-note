package com.firhan.leafnote.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firhan.leafnote.repositories.NoteRepository;
import com.firhan.leafnote.rooms.entities.Note;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NotesViewModel extends ViewModel {
    //repo
    private NoteRepository noteRepository;

    //vars
    private MutableLiveData<List<Note>> notes;

    @Inject
    public NotesViewModel(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;

        if(notes == null){
            notes = new MutableLiveData<>();
            notes.setValue(new ArrayList<Note>());
        }
    }

    public long addNote(Note note){
        long lastInsertedId = noteRepository.createNote(note);

        if(lastInsertedId > 0){
            //success, set new val
            notes.getValue().add(note);
            notes.setValue(notes.getValue());
        }

        return lastInsertedId;
    }

    public LiveData<List<Note>> getNotes(){
        return notes;
    }
}
