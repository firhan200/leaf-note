package com.firhan.leafnote.dagger.components;

import android.app.Application;

import com.firhan.leafnote.BaseApplication;
import com.firhan.leafnote.dagger.factories.ViewModelProviderFactory;
import com.firhan.leafnote.dagger.modules.ActivityBuilderModule;
import com.firhan.leafnote.dagger.modules.NotesModule;
import com.firhan.leafnote.dagger.modules.ViewModelProviderModule;
import com.firhan.leafnote.rooms.daos.NoteDao;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivityBuilderModule.class,
                ViewModelProviderModule.class,
                NotesModule.class
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication> {
    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
