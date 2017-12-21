package com.gdetotut.libs.jundo_droidsample.mvp.presenters;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.gdetotut.libs.jundo_droidsample.App;
import com.gdetotut.libs.jundo_droidsample.mvp.views.EditModeView;

@InjectViewState
public class EditModePresenter extends MvpPresenter<EditModeView> {

    public EditModePresenter() {
        App.getAppComponent().inject(this);
    }


}
