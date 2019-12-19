package com.firhan.leafnote.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.firhan.leafnote.R;

import dagger.android.support.DaggerAppCompatActivity;

public class NoteActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
    }
}
