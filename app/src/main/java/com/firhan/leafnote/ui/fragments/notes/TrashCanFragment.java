package com.firhan.leafnote.ui.fragments.notes;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firhan.leafnote.R;
import com.firhan.leafnote.interfaces.INoteListClickListener;
import com.firhan.leafnote.interfaces.INoteNavigation;
import com.firhan.leafnote.rooms.entities.Note;
import com.firhan.leafnote.ui.activities.NoteActivity;
import com.firhan.leafnote.ui.adapters.NotesRecyclerViewAdapter;
import com.firhan.leafnote.viewmodels.NotesViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrashCanFragment extends DaggerFragment implements INoteListClickListener {
    private static final String TAG = "TrashCanFragment";

    //vars
    private RecyclerView notesRecyclerView;
    private NotesRecyclerViewAdapter adapter;
    private LinearLayout trashCanNoteHint;

    //nav controller
    private INoteNavigation noteNavigation;

    //inject view model
    @Inject
    NotesViewModel notesViewModel;

    public TrashCanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trash_can, container, false);
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

        //set observer to notes
        notesViewModel.getTrashNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.notifyDataSetChanged();

                //check notes
                if(notes.size() < 1){
                    //no notes exist, show hint
                    trashCanNoteHint.setVisibility(View.VISIBLE);
                }else{
                    //hide hint
                    trashCanNoteHint.setVisibility(View.GONE);
                }
            }
        });

        //set observer to selected notes
        notesViewModel.getSelectedTrashNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                Log.e(TAG, "onChanged: selected notes "+ notes.size());
                checkActionMenuBarVisibility(notes);
            }
        });
    }

    private void initIds(View view){
        notesRecyclerView = view.findViewById(R.id.trash_notes_recycler_view);
        trashCanNoteHint = view.findViewById(R.id.trash_can_note_hint);
    }

    private void initRecyclerView(){
        //set adapter
        adapter = new NotesRecyclerViewAdapter(getContext() ,notesViewModel.getTrashNotes().getValue(), this);
        notesRecyclerView.setAdapter(adapter);

        //set layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        notesRecyclerView.setLayoutManager(layoutManager);

        //set decoration
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        notesRecyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onNoteClick(int position) {
        Note selectedNote = notesViewModel.getTrashNotes().getValue().get(position);

        //set selected note view model
        notesViewModel.setSelectedNote(selectedNote);

        //get note id
        int noteId = selectedNote.getId();

        //set bundle
        Bundle bundle = new Bundle();
        bundle.putInt("noteId", noteId);
        //go to note detail
        noteNavigation.navigateFragment(R.id.action_trashCanFragment_to_noteDetailFragment, bundle);
    }

    @Override
    public void onNoteLongPress(int position) {
        //get note and update selected
        Note note = notesViewModel.getTrashNotes().getValue().get(position);

        //add or remove from selected notes live data
        notesViewModel.selectTrashNote(note);

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
            pageTitleText = getResources().getString(R.string.menu_trash_can) + " (" + notesViewModel.getTrashNotes().getValue().size() + ")";
            noteNavigation.setPageTitle(pageTitleText);
        }

        //show action menu bar
        noteNavigation.showDeleteMenuIcon(isAnySelected, true);
    }
}
