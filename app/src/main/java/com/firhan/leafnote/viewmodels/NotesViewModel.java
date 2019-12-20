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
    private MutableLiveData<List<Note>> selectedNotes;
    private MutableLiveData<Note> selectedNote;

    @Inject
    public NotesViewModel(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;

        if(notes == null){
            notes = new MutableLiveData<>();
            notes.setValue(noteRepository.getAll());

            //selected note
            selectedNote = new MutableLiveData<>();
            selectedNote.setValue(null);

            //selected notes list
            selectedNotes = new MutableLiveData<>();
            selectedNotes.setValue(new ArrayList<Note>());
        }
    }

    public long addNote(Note note){
        long lastInsertedId = noteRepository.createNote(note);

        if(lastInsertedId > 0){
            //success, set new val
            notes.postValue(noteRepository.getAll());
        }

        return lastInsertedId;
    }

    //set select note, after long press on note list
    public void selectNote(Note note){
        //check if already selected
        if(note.getSelected()){
            note.setSelected(false);

            //remove from selected notes live data
            removeNoteFromSelectedNotes(note);
        }else{
            note.setSelected(true);

            //add to selected notes live data
            addNoteToSelectedNotes(note);
        }
    }

    //update note data
    public void updateNote(Note note){
        noteRepository.editNote(note);

        //update live data
        notes.postValue(noteRepository.getAll());
    }

    //get note by id
    public Note getNote(long id){
        return noteRepository.getNoteById(id);
    }

    //get all notes
    public LiveData<List<Note>> getNotes(){
        return notes;
    }

    //get only selected note
    public LiveData<Note> getSelectedNote(){
        return selectedNote;
    }

    //set selected note after click on list
    public void setSelectedNote(Note note){
        selectedNote.setValue(note);
    }

    //add note to selected notes
    public void addNoteToSelectedNotes(Note note){
        List<Note> newSelectedNotes = getSelectedNotes().getValue();
        newSelectedNotes.add(note);
        selectedNotes.setValue(newSelectedNotes);
    }

    public void removeNoteFromSelectedNotes(Note note){
        List<Note> newSelectedNotes = getSelectedNotes().getValue();
        newSelectedNotes.remove(note);
        selectedNotes.setValue(newSelectedNotes);
    }

    //get selected notes
    public LiveData<List<Note>> getSelectedNotes(){
        return selectedNotes;
    }
}
