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
public class SearchNotesViewModel extends ViewModel {
    private NoteRepository noteRepository;
    private MutableLiveData<List<Note>> searchResults;
    private MutableLiveData<String> keyword;

    @Inject
    public SearchNotesViewModel(NoteRepository noteRepository){
        this.noteRepository = noteRepository;

        //init default
        searchResults = new MutableLiveData<>();
        searchResults.setValue(new ArrayList<Note>());

        //init default keyword
        keyword = new MutableLiveData<>();
        keyword.setValue(null);
    }

    public void searchNotes(){
        List<Note> results = noteRepository.searchNotes(keyword.getValue());

        //dummy sleep
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        searchResults.postValue(results);
    }

    public void clearResults(){
        searchResults.setValue(new ArrayList<Note>());
    }

    public LiveData<List<Note>> getSearchResult(){
        return searchResults;
    }

    public LiveData<String> getKeyword(){
        return keyword;
    }

    public void setKeyword(String keywordValue){
        keyword.setValue(keywordValue);
    }
}
