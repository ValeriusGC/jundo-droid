package com.gdetotut.libs.jundo_droidsample.mvp.presenters;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.gdetotut.libs.jundo_droidsample.App;
import com.gdetotut.libs.jundo_droidsample.model.BriefNote;
import com.gdetotut.libs.jundo_droidsample.model.NoteLoader;
import com.gdetotut.libs.jundo_droidsample.mvp.views.MainView;
import com.gdetotut.libs.jundo_droidsample.ui.activity.EditModeActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    static final String TAG = "MainPresenter";

    @Inject
    NoteLoader mNoteLoader;

    @Inject
    Context ctx;

    private boolean mIsInLoading;
    private List<BriefNote> mTestNotes = new ArrayList<>();
    private boolean mLoading;

    private CompositeDisposable disposable = new CompositeDisposable();

    private static String NAME = "NAME";
    private static String LIST = "list";

    public MainPresenter() {
        App.getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        String json = new Gson().toJson(mTestNotes);
        SharedPreferences.Editor ed = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        ed.putString(LIST, json).apply();

        disposable.clear();
    }

    public void show() {
        loadData();
    }

    private void loadData() {


        if(mIsInLoading){
            return;
        }
        mIsInLoading = true;

        showProgress();

        final Observable<List<BriefNote>> observable = Observable.just(load())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        disposable.add(observable
                .subscribe(mNoteLoader -> {
                    onLoadingFinish();
                    onLoadingSuccess();
                }, error -> {
                    onLoadingFinish();
                }));

//        Observable.just(load()).subscribe(mNoteLoader -> {
//                    onLoadingFinish();
//                    onLoadingSuccess();
//                }, error -> {
//                    onLoadingFinish();
//                });

//        final io.reactivex.Observable<BriefNote> observable =
//                io.reactivex.Observable.fromIterable(load())
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread());
//        disposable.add(observable.subscribe());

//        disposable.add(observable
//                .subscribe(mNoteLoader -> {
//                    onLoadingFinish();
//                    onLoadingSuccess();
//                }, error -> {
//                    onLoadingFinish();
//                }));
    }

    private void showProgress() {
        getViewState().showListProgress();
    }

    private void onLoadingFinish() {
        getViewState().loadData(mTestNotes);
    }

    private void onLoadingSuccess() {
//        getViewState().loadData(mTestNotes);
    }

    public List<BriefNote> load() {

        Log.d(TAG, "load()");

        if(!mTestNotes.isEmpty()) {
            return mTestNotes;
        }

        if(mLoading) {
            return mTestNotes;
        }

        SharedPreferences sp = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String json = sp.getString(LIST, null);
        if(null != json) {
            Log.d(TAG, "load: " + json);
            mTestNotes = new Gson().fromJson(json, new TypeToken<List<BriefNote>>(){}.getType());
            return mTestNotes;
        }


        mLoading = true;
        final int count = 36;
        final int twoHours = 1000 * 60 * 60 * 2; // 2 hours
        final int twentyFourHours = twoHours * 12; // 24 hours
        final long startTime = System.currentTimeMillis() - twentyFourHours*3;
        for(int i=0; i<count; ++i) {
            final long time = startTime + twoHours * i;
            final BriefNote briefNote = new BriefNote(time, "note: " + i);
            mTestNotes.add(briefNote);
        }
        return mTestNotes;
    }

    /**
     *  Shows editor of some type.
     *
     * @param type One of editor's type
     */
    public void showEditor(@EditModeActivity.EditorType int type) {
        Log.d(TAG, "showEditor");
        Toast.makeText(App.getAppComponent().getContext(), "MainPresenter.showEditor", Toast.LENGTH_SHORT).show();
        getViewState().onEditorShow(type);
    }

    public void updateView() {
        getViewState().updateView();
    }
}
