package com.firhan.leafnote.dagger.modules;

import com.firhan.leafnote.dagger.modules.fragments.NoteFragmentsBuilderModule;
import com.firhan.leafnote.ui.activities.NoteActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {
    @ContributesAndroidInjector(
            modules = {
                    NoteFragmentsBuilderModule.class
            }
    )
    abstract NoteActivity contributeNoteActivity();
}
