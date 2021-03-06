package com.firhan.leafnote.dagger.modules.fragments;

import com.firhan.leafnote.ui.fragments.notes.AddNoteFragment;
import com.firhan.leafnote.ui.fragments.notes.EditNoteFragment;
import com.firhan.leafnote.ui.fragments.notes.NoteDetailFragment;
import com.firhan.leafnote.ui.fragments.notes.NoteListFragment;
import com.firhan.leafnote.ui.fragments.notes.SearchFragment;
import com.firhan.leafnote.ui.fragments.notes.TrashCanFragment;
import com.firhan.leafnote.ui.fragments.settings.PinFragment;
import com.firhan.leafnote.ui.fragments.settings.SettingFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class NoteFragmentsBuilderModule {
    @ContributesAndroidInjector
    abstract NoteListFragment contributeNoteListFragment();

    @ContributesAndroidInjector
    abstract AddNoteFragment contributeAddNoteFragment();

    @ContributesAndroidInjector
    abstract EditNoteFragment contributeEditNoteFragment();

    @ContributesAndroidInjector
    abstract NoteDetailFragment contributeNoteDetailFragment();

    @ContributesAndroidInjector
    abstract TrashCanFragment contributeTrashCanFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector
    abstract SettingFragment contributeSettingFragment();

    @ContributesAndroidInjector
    abstract PinFragment contributePinFragment();
}
