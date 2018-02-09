package com.gdetotut.libs.jundo_droidsample.di;

import android.content.Context;

import com.gdetotut.libs.jundo_droidsample.model.BriefNoteManager;
import com.gdetotut.libs.jundo_droidsample.mvp.presenters.EditModePresenter;
import com.gdetotut.libs.jundo_droidsample.mvp.presenters.MainPresenter;
import com.gdetotut.libs.jundo_droidsample.ui.activity.EditModeActivity;
import com.gdetotut.libs.jundo_droidsample.ui.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ContextModule.class, ApiModule.class})
public interface AppComponent {
    Context getContext();

    void inject(MainActivity activity);
    void inject(EditModeActivity activity);

    void inject(MainPresenter presenter);
    void inject(EditModePresenter presenter);
    void inject(BriefNoteManager manager);
}
