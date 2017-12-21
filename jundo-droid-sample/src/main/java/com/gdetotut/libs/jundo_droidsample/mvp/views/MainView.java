package com.gdetotut.libs.jundo_droidsample.mvp.views;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.gdetotut.libs.jundo_droidsample.model.BriefNote;
import com.gdetotut.libs.jundo_droidsample.ui.activity.EditModeActivity;

import java.util.List;

/**
 * 2017-06-30 12:48:27
 *
 * @author valerius
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface MainView extends MvpView {

    void showListProgress();

    void hideListProgress();

    void loadData(@NonNull List<BriefNote> list);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void onEditorShow(@EditModeActivity.EditorType int type);

    void updateView();
}
