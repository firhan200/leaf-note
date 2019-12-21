package com.firhan.leafnote.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //go to note activity
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
        finish();
    }
}
