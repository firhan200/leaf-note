package com.firhan.leafnote.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firhan.leafnote.R;
import com.firhan.leafnote.helpers.KeyboardHelper;
import com.firhan.leafnote.interfaces.INoteNavigation;
import com.firhan.leafnote.rooms.entities.Note;
import com.firhan.leafnote.viewmodels.NotesViewModel;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class NoteActivity extends DaggerAppCompatActivity implements INoteNavigation, NavController.OnDestinationChangedListener {
    private static final String TAG = "NoteActivity";

    //vars
    public NavController navController;
    private TextView pageTitle;
    private ImageView deleteSelectedNotesIcon, deleteSelectedNoteIcon, editSelectedNoteIcon;

    @Inject
    NotesViewModel notesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        //init ids
        initIds();

        //init listeners
        initListeners();

        setupNavigation();
    }

    private void initIds(){
        pageTitle = findViewById(R.id.page_title);
        deleteSelectedNotesIcon = findViewById(R.id.delete_selected_notes_icon);
        deleteSelectedNoteIcon = findViewById(R.id.delete_selected_note_icon);
        editSelectedNoteIcon = findViewById(R.id.edit_selected_note_icon);
    }

    private void initListeners(){
        deleteSelectedNotesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesViewModel.deleteSelectedNotes();
            }
        });

        deleteSelectedNoteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesViewModel.deleteSelectedNote();

                //show snack bar
                Snackbar.make(findViewById(android.R.id.content), getResources().getText(R.string.delete_success_label), Snackbar.LENGTH_LONG).show();

                //go back
                onBackPressed();
            }
        });
    }

    private void setupNavigation(){
        //set navigation graph
        navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //disable default toolbar
        getSupportActionBar().hide();

        //setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationUI.setupWithNavController(toolbar, navController);

        //set on destination change
        navController.addOnDestinationChangedListener(this);

        editSelectedNoteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("noteId", notesViewModel.getSelectedNote().getValue().getId());
                navController.navigate(R.id.action_noteDetailFragment_to_editNoteFragment, bundle);
            }
        });
    }

    @Override
    public void navigateFragment(int destinationId, Bundle bundle) {
        navController.navigate(destinationId, bundle);
    }

    @Override
    public void showDeleteMenuIcon(boolean isShow) {
        if(isShow){
            deleteSelectedNotesIcon.setVisibility(View.VISIBLE);
        }else{
            deleteSelectedNotesIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEditMenuIcon(boolean isShow) {
        if(isShow){
            editSelectedNoteIcon.setVisibility(View.VISIBLE);
            deleteSelectedNoteIcon.setVisibility(View.VISIBLE);
        }else{
            editSelectedNoteIcon.setVisibility(View.GONE);
            deleteSelectedNoteIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public void setPageTitle(String title) {
        pageTitle.setText(title);
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();

        return super.onSupportNavigateUp();
    }


    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        String pageTitleText = destination.getLabel().toString();

        //check if on list
        if(
                destination.getId() == R.id.noteListFragment
        ){
            pageTitleText = pageTitleText + "(" + notesViewModel.getNotes().getValue().size()  + ")";
        }

        pageTitle.setText(pageTitleText);

        //hide clear icon
        showDeleteMenuIcon(false);

        if(destination.getId() == R.id.noteDetailFragment){
            //show edit btn
            showEditMenuIcon(true);
        }else{
            showEditMenuIcon(false);
        }
    }
}
