package com.firhan.leafnote.ui.fragments.notes;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firhan.leafnote.R;
import com.firhan.leafnote.interfaces.INoteListClickListener;
import com.firhan.leafnote.interfaces.INoteNavigation;
import com.firhan.leafnote.rooms.entities.Note;
import com.firhan.leafnote.ui.activities.NoteActivity;
import com.firhan.leafnote.ui.adapters.NotesRecyclerViewAdapter;
import com.firhan.leafnote.viewmodels.NotesViewModel;
import com.firhan.leafnote.viewmodels.SearchNotesViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends DaggerFragment implements INoteListClickListener {
    private static final String TAG = "SearchFragment";

    //vars
    private EditText keywordInput;
    private ProgressBar searchLoading;
    private RecyclerView searchNotesRecyclerView;
    private NotesRecyclerViewAdapter adapter;
    private List<Note> results;

    //nav controller
    private INoteNavigation noteNavigation;

    //view models
    @Inject
    SearchNotesViewModel searchNotesViewModel;

    @Inject
    NotesViewModel notesViewModel;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init navigation interface
        noteNavigation = (NoteActivity) getContext();

        //init ids
        initIds(view);

        //request focus
        keywordInput.requestFocus();

        //set listener
        keywordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchNotesViewModel.setKeyword(v.getText().toString());
                }

                return false;
            }
        });

        //observe
        searchNotesViewModel.getKeyword().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "searching w/ keyword: " + s);

                //perform search
                if(s != null && s != ""){
                    new SearchNotesTask().execute(s);
                }
            }
        });

        searchNotesViewModel.getSearchResult().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                Log.e(TAG, "results: " + notes.size());

                //clear results
                results.clear();

                //fill results
                results.addAll(notes);

                adapter.notifyDataSetChanged();
            }
        });

        //init recycler view
        initRecyclerView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //clear keyword
        searchNotesViewModel.setKeyword("");
    }

    private void initIds(View view){
        keywordInput = view.findViewById(R.id.keyword_input);
        searchLoading = view.findViewById(R.id.search_loading);
        searchNotesRecyclerView = view.findViewById(R.id.search_notes_recycler_view);
    }

    private void initRecyclerView(){
        results = new ArrayList<>();

        //set adapter
        adapter = new NotesRecyclerViewAdapter(getContext() , results, this);
        searchNotesRecyclerView.setAdapter(adapter);

        //set layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        searchNotesRecyclerView.setLayoutManager(layoutManager);

        //set decoration
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        searchNotesRecyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onNoteClick(int position) {
        Note selectedNote = searchNotesViewModel.getSearchResult().getValue().get(position);

        //set selected note view model
        notesViewModel.setSelectedNote(selectedNote);

        //get note id
        int noteId = selectedNote.getId();

        //set bundle
        Bundle bundle = new Bundle();
        bundle.putInt("noteId", noteId);
        //go to note detail
        noteNavigation.navigateFragment(R.id.action_searchFragment_to_noteDetailFragment, bundle);
    }

    @Override
    public void onNoteLongPress(int position) {

    }

    private class SearchNotesTask extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... strings) {
            //searching notes
            searchNotesViewModel.searchNotes();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //show loading
            searchNotesRecyclerView.setVisibility(View.INVISIBLE);
            searchLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //hide loading
            searchLoading.setVisibility(View.INVISIBLE);
            searchNotesRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}


