package com.firhan.leafnote.dagger.modules.viewmodels;

import androidx.lifecycle.ViewModel;

import com.firhan.leafnote.dagger.keys.ViewModelKey;
import com.firhan.leafnote.viewmodels.SettingViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class SettingViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel.class)
    abstract ViewModel bindSettingViewModel(SettingViewModel settingViewModel);
}
