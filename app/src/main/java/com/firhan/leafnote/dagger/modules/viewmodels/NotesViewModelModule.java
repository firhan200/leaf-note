package com.firhan.leafnote.dagger.modules.viewmodels;

import androidx.lifecycle.ViewModel;

import com.firhan.leafnote.dagger.keys.ViewModelKey;
import com.firhan.leafnote.viewmodels.NotesViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class NotesViewModelModule{
    @Binds
    @IntoMap
    @ViewModelKey(NotesViewModel.class)
    abstract ViewModel bindNotesViewModel(NotesViewModel notesViewModel);
}
