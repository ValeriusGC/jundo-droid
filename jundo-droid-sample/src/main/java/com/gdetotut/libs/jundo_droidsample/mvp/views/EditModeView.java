package com.gdetotut.libs.jundo_droidsample.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.gdetotut.libs.jundo_droidsample.model.BriefNote;

public interface EditModeView extends MvpView {

    void onShow(BriefNote briefNote);

}
