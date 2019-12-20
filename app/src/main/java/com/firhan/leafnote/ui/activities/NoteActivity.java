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

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class NoteActivity extends DaggerAppCompatActivity implements INoteNavigation, NavController.OnDestinationChangedListener {
    private static final String TAG = "NoteActivity";

    //vars
    public NavController navController;
    private TextView pageTitle;
    private ImageView deleteSelectedNoteIcon, editSelectedNoteIcon;

    @Inject
    NotesViewModel notesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initIds();

        setupNavigation();
    }

    private void initIds(){
        pageTitle = findViewById(R.id.page_title);
        deleteSelectedNoteIcon = findViewById(R.id.delete_selected_note_icon);
        editSelectedNoteIcon = findViewById(R.id.edit_selected_note_icon);
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

        notesViewModel.getSelectedNote().observe(this, new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                Log.e(TAG, "onChanged: " + "selected note");
            }
        });

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
            deleteSelectedNoteIcon.setVisibility(View.VISIBLE);
        }else{
            deleteSelectedNoteIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEditMenuIcon(boolean isShow) {
        if(isShow){
            editSelectedNoteIcon.setVisibility(View.VISIBLE);
        }else{
            editSelectedNoteIcon.setVisibility(View.GONE);
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
        pageTitle.setText(destination.getLabel());

        //hide clear icon
        deleteSelectedNoteIcon.setVisibility(View.GONE);

        if(destination.getId() == R.id.noteDetailFragment){
            //show edit btn
            showEditMenuIcon(true);
        }else{
            showEditMenuIcon(false);
        }
    }
}
