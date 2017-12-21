package com.gdetotut.libs.jundo_droidsample.di;

import com.gdetotut.libs.jundo_droidsample.model.NoteLoader;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by valerius on 30.06.17.
 *
 * @author valerius
 */
@Module
public class ApiModule {
    @Provides
    @Singleton
    public NoteLoader getNoteLoader() { return new NoteLoader();}
}
