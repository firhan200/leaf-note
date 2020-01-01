package com.firhan.leafnote.ui.fragments.settings;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
public class PinFragment extends DaggerFragment {
    private static final int MAX_CHARACTERS = 4;
    //vars
    private TextView pinBtn1, pinBtn2, pinBtn3,
            pinBtn4, pinBtn5, pinBtn6, pinBtn7,
            pinBtn8, pinBtn9, pinBtn0, pinBtnDel,
            pinStarLabel;

    private String pin;

    private Boolean isSetupPin;

    private INoteNavigation noteNavigation;

    //di
    @Inject
    SettingViewModel settingViewModel;

    public PinFragment() {
        // Required empty public constructor
        pin = "";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //interface
        noteNavigation = (NoteActivity) getContext();

        //check if setup or authorized pin
        //get from bundle
        if(getArguments() != null){
            isSetupPin = getArguments().getBoolean("isSetupPin", false);
        }else{
            isSetupPin = false;
        }

        if(!isSetupPin){
            checkIfAppIsLock();
        }

        //init ids
        initIds(view);

        //init listeners
        initListeners();
    }

    private void checkIfAppIsLock(){
        Setting setting = settingViewModel.getSettings().getValue();
        if(!setting.getAppLock()){
            //go to note list
            noteNavigation.navigateFragment(R.id.action_pinFragment_to_noteListFragment, null);
        }
    }

    private void initIds(View view){
        pinBtn0 = view.findViewById(R.id.pin_btn_0);
        pinBtn1 = view.findViewById(R.id.pin_btn_1);
        pinBtn2 = view.findViewById(R.id.pin_btn_2);
        pinBtn3 = view.findViewById(R.id.pin_btn_3);
        pinBtn4 = view.findViewById(R.id.pin_btn_4);
        pinBtn5 = view.findViewById(R.id.pin_btn_5);
        pinBtn6 = view.findViewById(R.id.pin_btn_6);
        pinBtn7 = view.findViewById(R.id.pin_btn_7);
        pinBtn8 = view.findViewById(R.id.pin_btn_8);
        pinBtn9 = view.findViewById(R.id.pin_btn_9);
        pinBtnDel = view.findViewById(R.id.pin_btn_del);
        pinStarLabel = view.findViewById(R.id.pin_star_label);
    }

    private void initListeners(){
        pinBtn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumToPin("0");
            }
        });
        pinBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumToPin("1");
            }
        });
        pinBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumToPin("2");
            }
        });
        pinBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumToPin("3");
            }
        });
        pinBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumToPin("4");
            }
        });
        pinBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumToPin("5");
            }
        });
        pinBtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumToPin("6");
            }
        });
        pinBtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumToPin("7");
            }
        });
        pinBtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumToPin("8");
            }
        });
        pinBtn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumToPin("9");
            }
        });

        pinBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNumFromPin();
            }
        });
    }

    private void deleteNumFromPin(){
        if(pin.length() > 0){
            //delete last characters
            pin = pin.substring(0, pin.length() - 1);

            //update star label
            updateStarLabel();
        }
    }

    private void addNumToPin(String num){
        //check if already maximun character
        if(pin.length() < MAX_CHARACTERS){
            pin += num;

            //update star label
            updateStarLabel();

            if(pin.length() == MAX_CHARACTERS){
                //check if setup or authorize
                if(isSetupPin){
                    //save pin
                    savePin();
                }else{
                    //check pin
                    checkPin();
                }
            }
        }
    }

    private void checkPin(){
        Setting setting = settingViewModel.getSettings().getValue();
        if(setting != null){
            if(setting.getPin().equals(pin)){
                //authorized
                noteNavigation.navigateFragment(R.id.action_pinFragment_to_noteListFragment, null);
            }else{
                //pin not match
                Snackbar.make(getView(), "Incorrect Pin", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void savePin(){
        settingViewModel.updatePin(pin);

        //back to setting
        getActivity().onBackPressed();

        Snackbar.make(getView(), "Pin Setted", Snackbar.LENGTH_SHORT).show();
    }

    private void updateStarLabel(){
        //get total
        StringBuilder starLabel = new StringBuilder();
        for(int counter = 0; counter < pin.length(); counter++){
            starLabel.append("*");
        }

        pinStarLabel.setText(starLabel.toString());
    }
}
