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
import com.firhan.leafnote.rooms.entities.Note;
import com.firhan.leafnote.viewmodels.NotesViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditNoteFragment extends DaggerFragment implements View.OnClickListener {
    @Inject
    NotesViewModel notesViewModel;

    //vars
    private EditText title, body;
    private Note note;

    public EditNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //init ids
        initIds(view);

        //populate data
        populateData(getArguments().getInt("noteId"));

        //show keyboard
        KeyboardHelper.showSoftKeyboard(getActivity());

        //observe
    }

    private void initIds(View view){
        //inputs
        title = view.findViewById(R.id.edit_note_title_input);
        body = view.findViewById(R.id.edit_note_body_input);

        //submit btn
        Button editNoteSubmitBtn = view.findViewById(R.id.edit_note_submit_btn);
        editNoteSubmitBtn.setOnClickListener(this);
    }

    private void populateData(int noteId){
        //get note by id
        note = notesViewModel.getNote(noteId);
        if(note != null){
            //render
            title.setText(note.getTitle());
            body.setText(note.getBody());
        }
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
            note.setTitle(titleValue);
            note.setBody(bodyValue);
            notesViewModel.updateNote(note);

            //success
            //hide soft keyboard
            KeyboardHelper.hideSoftKeyboard(getActivity());
            //show snack bar
            Snackbar.make(getView(), getResources().getText(R.string.edit_success_label), Snackbar.LENGTH_LONG).show();
            //back to list
            getActivity().onBackPressed();
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
