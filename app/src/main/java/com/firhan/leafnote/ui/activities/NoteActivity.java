package com.firhan.leafnote.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class NoteActivity extends DaggerAppCompatActivity implements INoteNavigation, NavController.OnDestinationChangedListener {
    private static final String TAG = "NoteActivity";

    //vars
    public NavController navController;
    BottomNavigationView bottomNavigationView;
    private TextView pageTitle;
    private ImageView deleteSelectedNotesIcon, deleteSelectedNoteIcon, editSelectedNoteIcon, deleteNotePermanentIcon;

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

        //observe
        notesViewModel.getSelectedNote().observe(this, new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                if(note != null){
                    if(note.getDeleted()){
                        //show edit btn
                        showEditMenuIcon(false);
                    }else{
                        //show edit btn
                        showEditMenuIcon(true);
                    }
                }else{
                    //hide detail actions icon
                    showEditMenuIcon(false);
                }
            }
        });
    }

    private void initIds(){
        pageTitle = findViewById(R.id.page_title);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        deleteSelectedNotesIcon = findViewById(R.id.delete_selected_notes_icon);
        deleteSelectedNoteIcon = findViewById(R.id.delete_selected_note_icon);
        editSelectedNoteIcon = findViewById(R.id.edit_selected_note_icon);
        deleteNotePermanentIcon = findViewById(R.id.delete_note_permanent_icon);
    }

    private void initListeners(){
        deleteSelectedNotesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(NoteActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.delete_selected_notes)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete
                                notesViewModel.deleteSelectedNotes(false);

                                //show snack bar
                                Snackbar.make(findViewById(android.R.id.content), getResources().getText(R.string.delete_success_label), Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();

                dialog.show();
            }
        });

        deleteSelectedNoteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show confirmation dialog
                AlertDialog dialog = new AlertDialog.Builder(NoteActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.delete_selected_notes)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notesViewModel.deleteSelectedNote();

                                //show snack bar
                                Snackbar.make(findViewById(android.R.id.content), getResources().getText(R.string.delete_success_label), Snackbar.LENGTH_LONG).show();

                                //go back
                                onBackPressed();
                            }
                        })
                        .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss dialog
                                dialog.dismiss();
                            }
                        })
                        .create();

                //show dialog
                dialog.show();
            }
        });

        deleteNotePermanentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show confirmation dialog
                AlertDialog dialog = new AlertDialog.Builder(NoteActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.permanent_delete_selected_notes)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notesViewModel.deleteSelectedNotes(true);

                                //show snack bar
                                Snackbar.make(findViewById(android.R.id.content), getResources().getText(R.string.delete_success_label), Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss dialog
                                dialog.dismiss();
                            }
                        })
                        .create();

                //show dialog
                dialog.show();
            }
        });
    }

    private void setupNavigation(){
        //set navigation graph
        navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

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
    public void showDeleteMenuIcon(boolean isShow, boolean isTrashCan) {
        if(isTrashCan){
            if(isShow){
                deleteNotePermanentIcon.setVisibility(View.VISIBLE);
            }else{
                deleteNotePermanentIcon.setVisibility(View.GONE);
            }
        }else{
            if(isShow){
                deleteSelectedNotesIcon.setVisibility(View.VISIBLE);
            }else{
                deleteSelectedNotesIcon.setVisibility(View.GONE);
            }
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
        //default page title
        String pageTitleText = destination.getLabel().toString();

        //hide clear icon
        showDeleteMenuIcon(false, true);
        showDeleteMenuIcon(false, false);

        switch (destination.getId()){
            case R.id.noteListFragment:
                //set page title
                pageTitleText = pageTitleText + "(" + notesViewModel.getNotes().getValue().size()  + ")";

                break;
            case R.id.trashCanFragment:
                //set page title
                pageTitleText = pageTitleText + "(" + notesViewModel.getTrashNotes().getValue().size()  + ")";
                break;
            default:
                break;
        }

        //set page title
        pageTitle.setText(pageTitleText);

        //check is show bottom nav
        isShowBottomNav(destination.getId());
    }

    private void isShowBottomNav(int fragmentId){
        if(
                fragmentId == R.id.noteDetailFragment
                || fragmentId == R.id.editNoteFragment
        ){
            //hide bottom nav
            setBottomNavigationVisibility(false);
        }else{
            //show bottom nav
            setBottomNavigationVisibility(true);
        }
    }

    void setBottomNavigationVisibility(boolean isShow){
        if(isShow){
            bottomNavigationView.setVisibility(View.VISIBLE);
        }else{
            bottomNavigationView.setVisibility(View.GONE);
        }
    }
}
