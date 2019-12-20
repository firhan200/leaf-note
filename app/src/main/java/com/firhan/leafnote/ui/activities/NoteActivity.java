package com.firhan.leafnote.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firhan.leafnote.R;
import com.firhan.leafnote.helpers.KeyboardHelper;
import com.firhan.leafnote.interfaces.INoteNavigation;

import dagger.android.support.DaggerAppCompatActivity;

public class NoteActivity extends DaggerAppCompatActivity implements INoteNavigation, NavController.OnDestinationChangedListener {
    public NavController navController;
    private TextView pageTitle;
    private ImageView deleteSelectedNoteIcon;

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
    }

    @Override
    public void navigateFragment(int destinationId, Bundle bundle) {
        navController.navigate(destinationId, bundle);
    }

    @Override
    public void showActionMenuIcon(boolean isShow) {
        if(isShow){
            deleteSelectedNoteIcon.setVisibility(View.VISIBLE);
        }else{
            deleteSelectedNoteIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();

        KeyboardHelper.hideSoftKeyboard(this);

        return super.onSupportNavigateUp();
    }


    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        pageTitle.setText(destination.getLabel());

        //hide clear icon
        deleteSelectedNoteIcon.setVisibility(View.GONE);
    }
}
