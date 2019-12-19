package com.firhan.leafnote.ui.fragments.notes;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firhan.leafnote.R;
import com.firhan.leafnote.rooms.entities.Note;
import com.firhan.leafnote.ui.adapters.NotesRecyclerViewAdapter;
import com.firhan.leafnote.viewmodels.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends DaggerFragment {
    private static final String TAG = "NoteListFragment";

    //vars
    RecyclerView notesRecyclerView;
    NotesRecyclerViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton floatingActionButton;

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

        //init ids
        initIds(view);

        //init recycler view
        initRecyclerView();

        //set add note btn listener
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notesViewModel.addNote(new Note("bank account number", "secret password here"));
            }
        });

        //set observer
        notesViewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                Log.e(TAG, "onChanged: notes");
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initIds(View view){
        notesRecyclerView = view.findViewById(R.id.notes_recycler_view);
        floatingActionButton = view.findViewById(R.id.add_note_btn);
    }

    private void initRecyclerView(){
        //set adapter
        adapter = new NotesRecyclerViewAdapter(notesViewModel.getNotes().getValue());
        notesRecyclerView.setAdapter(adapter);

        //set layout
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        notesRecyclerView.setLayoutManager(layoutManager);
    }
}
