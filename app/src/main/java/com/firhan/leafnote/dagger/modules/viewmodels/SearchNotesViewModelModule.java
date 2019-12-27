package com.firhan.leafnote.dagger.modules.viewmodels;

import androidx.lifecycle.ViewModel;

import com.firhan.leafnote.dagger.keys.ViewModelKey;
import com.firhan.leafnote.viewmodels.SearchNotesViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class SearchNotesViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchNotesViewModel.class)
    abstract ViewModel bindSearchNotesViewModel(SearchNotesViewModel searchNotesViewModel);
}
