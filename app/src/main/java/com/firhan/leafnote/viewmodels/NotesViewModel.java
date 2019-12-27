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
    private MutableLiveData<List<Note>> trashNotes;
    private MutableLiveData<List<Note>> selectedNotes;
    private MutableLiveData<List<Note>> selectedTrashNotes;
    private MutableLiveData<Note> selectedNote;

    @Inject
    public NotesViewModel(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;

        if(notes == null){
            //notes
            notes = new MutableLiveData<>();
            notes.setValue(noteRepository.getAll());

            //trash notes
            trashNotes = new MutableLiveData<>();
            trashNotes.setValue(noteRepository.getAllTrashCan());

            //selected note
            selectedNote = new MutableLiveData<>();
            selectedNote.setValue(null);

            //selected notes list
            selectedNotes = new MutableLiveData<>();
            selectedNotes.setValue(new ArrayList<Note>());

            //selected trash notes list
            selectedTrashNotes = new MutableLiveData<>();
            selectedTrashNotes.setValue(new ArrayList<Note>());
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
            removeNoteFromSelectedNotes(note, selectedNotes);
        }else{
            note.setSelected(true);

            //add to selected notes live data
            addNoteToSelectedNotes(note, selectedNotes);
        }
    }

    //set select note, after long press on note list
    public void selectTrashNote(Note note){
        //check if already selected
        if(note.getSelected()){
            note.setSelected(false);

            //remove from selected notes live data
            removeNoteFromSelectedNotes(note, selectedTrashNotes);
        }else{
            note.setSelected(true);

            //add to selected notes live data
            addNoteToSelectedNotes(note, selectedTrashNotes);
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

    //get all notes in trash
    public LiveData<List<Note>> getTrashNotes(){
        return trashNotes;
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
    public void addNoteToSelectedNotes(Note note, MutableLiveData<List<Note>> currentNoteList){
        List<Note> newSelectedNotes = getSelectedNotes().getValue();
        newSelectedNotes.add(note);
        currentNoteList.setValue(newSelectedNotes);
    }

    //remove note from selected notes
    public void removeNoteFromSelectedNotes(Note note, MutableLiveData<List<Note>> currentNoteList){
        List<Note> newSelectedNotes = getSelectedNotes().getValue();
        newSelectedNotes.remove(note);
        currentNoteList.setValue(newSelectedNotes);
    }

    public void deleteSelectedNotes(boolean isPermanent){
        //init new notes list
        List<Note> newNotes = getNotes().getValue();
        List<Note> newTrashNotes = getTrashNotes().getValue();

        //deleting
        if(getSelectedNotes().getValue().size() > 0){
            for(Note note: getSelectedNotes().getValue()){
                //remove from database
                if(!isPermanent){
                    //only soft delete
                    noteRepository.softDeleteNote(note);
                }else{
                    //permanent delete
                    noteRepository.deleteNote(note);
                }

                if(!isPermanent){
                    //remove from new note list
                    newNotes.remove(note);

                    //add removed note to trash can
                    //remove selected attr
                    note.setSelected(false);
                    newTrashNotes.add(note);
                }else{
                    //remove from trash can
                    newTrashNotes.remove(note);
                }
            }

            //update live data
            notes.setValue(newNotes);

            //update trash can
            trashNotes.setValue(newTrashNotes);

            //clear selected notes live data
            selectedNotes.setValue(new ArrayList<Note>());
            selectedTrashNotes.setValue(new ArrayList<Note>());
        }
    }

    public void deleteSelectedNote(){
        //remove from database
        noteRepository.softDeleteNote(getSelectedNote().getValue());

        //update live data
        notes.setValue(noteRepository.getAll());

        //update trash can
        trashNotes.setValue(noteRepository.getAllTrashCan());
    }

    //get selected notes
    public LiveData<List<Note>> getSelectedNotes(){
        return selectedNotes;
    }
    public LiveData<List<Note>> getSelectedTrashNotes(){
        return selectedTrashNotes;
    }
}
