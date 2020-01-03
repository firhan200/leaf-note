package com.firhan.leafnote.ui.fragments.notes;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firhan.leafnote.R;
import com.firhan.leafnote.helpers.KeyboardHelper;
import com.firhan.leafnote.interfaces.INoteListClickListener;
import com.firhan.leafnote.interfaces.INoteNavigation;
import com.firhan.leafnote.rooms.entities.Note;
import com.firhan.leafnote.ui.activities.NoteActivity;
import com.firhan.leafnote.ui.adapters.NotesRecyclerViewAdapter;
import com.firhan.leafnote.viewmodels.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends DaggerFragment implements INoteListClickListener {
    private static final String TAG = "NoteListFragment";

    //vars
    private RecyclerView notesRecyclerView;
    private NotesRecyclerViewAdapter adapter;
    private LinearLayout createFirstNoteHint;

    //nav controller
    private INoteNavigation noteNavigation;

    //inject view model
    @Inject
    NotesViewModel notesViewModel;

    public NoteListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init navigation interface
        noteNavigation = (NoteActivity) getContext();

        //init ids
        initIds(view);

        //init recycler view
        initRecyclerView();

        //init listeners
        initListeners();

        //set observer to notes
        notesViewModel.getNotes().observe(getActivity(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
            adapter.submitList(notes);

            //check notes
            if(notes.size() < 1){
                //no notes exist, show hint
                createFirstNoteHint.setVisibility(View.VISIBLE);
            }else{
                //hide hint
                createFirstNoteHint.setVisibility(View.GONE);
            }
            }
        });

        //set observer to selected notes
        notesViewModel.getSelectedNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                Log.e(TAG, "onChanged: selected notes "+ notes.size());
                checkActionMenuBarVisibility(notes);
            }
        });
    }

    private void initListeners(){
        //set click listener to create first note
        createFirstNoteHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to add note fragment
                noteNavigation.navigateFragment(R.id.action_noteListFragment_to_addNoteFragment, null);
            }
        });
    }

    private void initIds(View view){
        notesRecyclerView = view.findViewById(R.id.notes_recycler_view);
        createFirstNoteHint = view.findViewById(R.id.create_first_note_hint);
    }

    private void initRecyclerView(){
        //set adapter
        adapter = new NotesRecyclerViewAdapter(getContext() , this);
        notesRecyclerView.setAdapter(adapter);

        //set layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        notesRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onNoteClick(int position) {
        Note selectedNote = notesViewModel.getNotes().getValue().get(position);

        //set selected note view model
        notesViewModel.setSelectedNote(selectedNote);

        //get note id
        int noteId = selectedNote.getId();

        //set bundle
        Bundle bundle = new Bundle();
        bundle.putInt("noteId", noteId);
        //go to note detail
        noteNavigation.navigateFragment(R.id.action_noteListFragment_to_noteDetailFragment, bundle);
    }

    @Override
    public void onNoteLongPress(int position) {
        //get note and update selected
        Note note = notesViewModel.getNotes().getValue().get(position);

        //add or remove from selected notes live data
        notesViewModel.selectNote(note);

        //toggle selected value
        note.setSelected(note.getSelected());

        //update adapter
        adapter.notifyItemChanged(position, note);
    }

    private void checkActionMenuBarVisibility(List<Note> notes){
        //if any selected
        boolean isAnySelected = false;

        //loop through notes
        for(Note note : notes){
            if(note.getSelected()){
                isAnySelected = true;
                break;
            }
        }

        //check if any selected
        if(isAnySelected){
            //show total selected
            String totalSelected = String.valueOf(notesViewModel.getSelectedNotes().getValue().size());
            totalSelected = totalSelected + " items";
            noteNavigation.setPageTitle(totalSelected);
        }else{
            //set back to app name
            String pageTitleText = getResources().getString(R.string.app_name);
            pageTitleText = getResources().getString(R.string.app_name);
            noteNavigation.setPageTitle(pageTitleText);
        }

        //show action menu bar
        noteNavigation.showDeleteMenuIcon(isAnySelected, false);
    }
}
