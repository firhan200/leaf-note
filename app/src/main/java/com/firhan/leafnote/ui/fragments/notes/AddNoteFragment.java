package com.firhan.leafnote.ui.fragments.notes;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.firhan.leafnote.R;
import com.firhan.leafnote.helpers.KeyboardHelper;
import com.firhan.leafnote.interfaces.INoteNavigation;
import com.firhan.leafnote.rooms.entities.Note;
import com.firhan.leafnote.ui.activities.NoteActivity;
import com.firhan.leafnote.viewmodels.NotesViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNoteFragment extends DaggerFragment implements View.OnClickListener {
    //vars
    EditText title, body;
    Button addNoteSubmitBtn;

    //interface
    INoteNavigation noteNavigation;

    @Inject
    NotesViewModel notesViewModel;

    public AddNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        noteNavigation = (NoteActivity) getContext();

        //init ids
        initIds(view);
    }

    private void initIds(View view){
        //inputs
        title = view.findViewById(R.id.add_note_title_input);
        body = view.findViewById(R.id.add_note_body_input);

        //submit btn
        addNoteSubmitBtn = view.findViewById(R.id.add_note_submit_btn);
        addNoteSubmitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //on submit click
        //get inputs
        String titleValue = title.getText().toString();
        String bodyValue = body.getText().toString();

        //validate
        if(validate(titleValue, bodyValue)){
            //save to db
            long insertedId = notesViewModel.addNote(new Note(titleValue, bodyValue));
            if(insertedId > 0){
                //success
                //hide soft keyboard
                KeyboardHelper.hideSoftKeyboard(getActivity());
                //back to list
                noteNavigation.navigateFragment(R.id.noteListFragment, null);
            }else{
                //failed
                showError("Failed to create new note.");
            }
        }else{
            showError("Please fill all field.");
        }
    }

    //add note validation
    private boolean validate(String title, String body){
        //defualt
        ArrayList<Boolean> error = new ArrayList<>();

        if(title.length() < 1){
            error.add(true);
        }

        if(body.length() < 1){
            error.add(true);
        }

        if(error.size() > 0){
            return false;
        }else{
            return true;
        }
    }

    //show snackbar error
    private void showError(String errorMessage){
        Snackbar.make(getView(), errorMessage, Snackbar.LENGTH_LONG).show();
    }
}
