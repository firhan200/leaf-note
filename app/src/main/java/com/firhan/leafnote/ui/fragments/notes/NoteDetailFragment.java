package com.firhan.leafnote.ui.fragments.notes;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firhan.leafnote.R;
import com.firhan.leafnote.interfaces.INoteNavigation;
import com.firhan.leafnote.rooms.entities.Note;
import com.firhan.leafnote.ui.activities.NoteActivity;
import com.firhan.leafnote.viewmodels.NotesViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteDetailFragment extends DaggerFragment {
    //vars
    TextView title, body;

    @Inject
    NotesViewModel notesViewModel;

    INoteNavigation noteNavigation;

    public NoteDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //init interface
        noteNavigation = (NoteActivity) getContext();

        //init ids
        initIds(view);

        //populate data
        populateData(getArguments().getInt("noteId"));
    }

    @Override
    public void onDetach() {
        super.onDetach();

        //remove selected note
        notesViewModel.clearSelectedNote();
    }

    private void initIds(View view){
        title = view.findViewById(R.id.detail_note_title);
        body = view.findViewById(R.id.detail_note_body);
    }

    private void populateData(int noteId){
        //get note by id
        Note note = notesViewModel.getNote(noteId);
        if(note != null){
            //render
            title.setText(note.getTitle());
            body.setText(note.getBody());

            //check if should show edit menu icon
            if(!note.getDeleted()){
                noteNavigation.showEditMenuIcon(true, note.getDeleted());
            }else{
                noteNavigation.showEditMenuIcon(true, note.getDeleted());
            }
        }
    }
}
