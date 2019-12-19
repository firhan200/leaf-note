package com.firhan.leafnote.dagger.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.firhan.leafnote.dagger.factories.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelProviderModule {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelProviderFactory(ViewModelProviderFactory viewModelProviderFactory);
}
