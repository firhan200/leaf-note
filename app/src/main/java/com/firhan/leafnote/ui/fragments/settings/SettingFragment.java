package com.firhan.leafnote.ui.fragments.settings;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.firhan.leafnote.R;
import com.firhan.leafnote.interfaces.INoteNavigation;
import com.firhan.leafnote.rooms.entities.Setting;
import com.firhan.leafnote.ui.activities.NoteActivity;
import com.firhan.leafnote.viewmodels.SettingViewModel;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends DaggerFragment {
    //vars
    private Switch lockApp, usePin, useFingerprint;
    private Boolean isFirstPopulate;
    private LinearLayout setupPinLayout;

    private INoteNavigation noteNavigation;

    @Inject
    SettingViewModel settingViewModel;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init interface
        noteNavigation = (NoteActivity) getContext();

        //set first populate to true
        isFirstPopulate = true;

        //init ids
        initIds(view);

        //init listeners
        initListeners();

        //observe
        settingViewModel.getSettings().observe(this, new Observer<Setting>() {
            @Override
            public void onChanged(Setting setting) {
                //populate setting
                populateSetting(setting);
            }
        });
    }

    private void initListeners(){
        //lock app listener
        lockApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isFirstPopulate) {
                    settingViewModel.updateLockApp(isChecked);
                    showMessage(getResources().getText(R.string.lock_app).toString() + " updated");
                }
            }
        });

        //use pin listener
        usePin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isFirstPopulate) {
                    settingViewModel.updateUsePin(isChecked);
                    showMessage(getResources().getText(R.string.use_pin).toString() + " updated");
                }
            }
        });

        //use fingerprint listener
        useFingerprint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isFirstPopulate){
                    settingViewModel.updateUseFingerprint(isChecked);
                    showMessage(getResources().getText(R.string.use_fingerprint).toString() + " updated");
                }
            }
        });

        //init setup pin listener
        setupPinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show pin fragment
                Bundle bundle = new Bundle();
                bundle.putBoolean("isSetupPin", true);
                noteNavigation.navigateFragment(R.id.action_settingFragment_to_pinFragment, bundle);
            }
        });
    }

    private void showMessage(String message){
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void initIds(View view){
        lockApp = view.findViewById(R.id.setting_lock_app);
        usePin = view.findViewById(R.id.setting_use_pin);
        useFingerprint = view.findViewById(R.id.setting_use_fingerprint);
        setupPinLayout = view.findViewById(R.id.setting_setup_pin_layout);
    }

    private void populateSetting(Setting setting){
        //lock app
        lockApp.setChecked(setting.getAppLock());

        //use pin
        usePin.setChecked(setting.getUsePin());

        //use fingerprint
        useFingerprint.setChecked(setting.getUseFingerPrint());

        //set to false
        isFirstPopulate = false;
    }
}
