package com.firhan.leafnote.ui.fragments.notes;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    RecyclerView notesRecyclerView;
    NotesRecyclerViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton floatingActionButton;

    //nav controller
    INoteNavigation noteNavigation;

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

        //set add note btn listener
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to add note fragment
                noteNavigation.navigateFragment(R.id.action_noteListFragment_to_addNoteFragment, null);
            }
        });

        //set observer to notes
        notesViewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
            adapter.notifyDataSetChanged();

            checkActionMenuBarVisibility(notes);
            }
        });

        //set observer to selected note
        notesViewModel.getSelectedNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {

            }
        });
    }

    private void initIds(View view){
        notesRecyclerView = view.findViewById(R.id.notes_recycler_view);
        floatingActionButton = view.findViewById(R.id.add_note_btn);
    }

    private void initRecyclerView(){
        //set adapter
        adapter = new NotesRecyclerViewAdapter(getContext() ,notesViewModel.getNotes().getValue(), this);
        notesRecyclerView.setAdapter(adapter);

        //set layout
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
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
        //update live data to show style
        notesViewModel.selectNote(position);
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
            noteNavigation.setPageTitle("Leaf Note");
        }

        //show action menu bar
        noteNavigation.showDeleteMenuIcon(isAnySelected);
    }
}
