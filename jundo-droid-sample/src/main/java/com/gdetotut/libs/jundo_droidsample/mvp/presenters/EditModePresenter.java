package com.gdetotut.libs.jundo_droidsample.mvp.presenters;


import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.gdetotut.libs.jundo_droidsample.App;
import com.gdetotut.libs.jundo_droidsample.model.BriefNote;
import com.gdetotut.libs.jundo_droidsample.model.BriefNoteManager;
import com.gdetotut.libs.jundo_droidsample.model.TypeOf;
import com.gdetotut.libs.jundo_droidsample.mvp.views.EditModeView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

@InjectViewState
public class EditModePresenter extends MvpPresenter<EditModeView> {

    @Inject
    BriefNoteManager manager;

    @Inject
    Context ctx;

    public EditModePresenter() {
        App.getAppComponent().inject(this);
    }

    public void showNote(TypeOf.Oid oid) {

        List<BriefNote> notes = manager.getBy(Collections.singletonList(oid));
        if(notes.size() > 0) {
            getViewState().onShow(notes.get(0));
        }

    }

    public void save(BriefNote note) {
        manager.save(Collections.singletonList(note));
    }
}
