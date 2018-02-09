package com.gdetotut.libs.jundo_droidsample.mvp.presenters;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.gdetotut.libs.jundo_droid_common.CreatorException;
import com.gdetotut.libs.jundo_droid_common.UndoPacket;
import com.gdetotut.libs.jundo_droid_common.UndoStack;
import com.gdetotut.libs.jundo_droidsample.App;
import com.gdetotut.libs.jundo_droidsample.model.BriefNote;
import com.gdetotut.libs.jundo_droidsample.model.BriefNoteManager;
import com.gdetotut.libs.jundo_droidsample.model.NoteLoader;
import com.gdetotut.libs.jundo_droidsample.model.TypeOf;
import com.gdetotut.libs.jundo_droidsample.mvp.views.MainView;
import com.gdetotut.libs.jundo_droidsample.ui.activity.EditModeActivity;
import com.gdetotut.libs.jundo_droidsample.ui.activity.MainActivity;
import com.gdetotut.libs.jundo_droidsample.ui.activity.MainUndoCtrl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    static final String TAG = "MainPresenter";
    static final String UNDO_KEY = "undo";

    @Inject
    NoteLoader mNoteLoader;

    @Inject
    BriefNoteManager manager;

    @Inject
    Context ctx;

    /**
     * This is a delicate moment. Storing DB state can be impossible...
     */
    public UndoStack undoStack;// = new UndoStack("", null);

    private boolean mIsInLoading;
    private List<BriefNote> notes = new ArrayList<>();

//    private Map<String, BriefNote> mTestNotes = new TreeMap<>();
    private boolean mLoading;

    private CompositeDisposable disposable = new CompositeDisposable();

    public MainPresenter() {
        App.getAppComponent().inject(this);

        SharedPreferences sp = ctx.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        try {
            undoStack = UndoPacket
                    .peek(sp.getString(UNDO_KEY, null), subjInfo -> subjInfo.id.equals(TAG))
                    .restore(null, () -> new UndoStack(""))
                    .prepare((stack, subjInfo, result) -> {
                        stack.getLocalContexts().put(MainUndoCtrl.LC_PRES, this);
                    });
        } catch (CreatorException e) {
            System.err.println(e.getLocalizedMessage());
        }

    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        manager.load();
        show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.d(TAG, "onDestroy");

        try {
            String stack = UndoPacket.make(undoStack, TAG, 1)
                    .zipped(true)
                    .store();
            SharedPreferences.Editor ed = ctx.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
            ed.putString(UNDO_KEY, stack).apply();

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }

        manager.save();
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
        List<BriefNote> notes = manager.getAll();
        Collections.sort(notes, (n1, n2) -> n1.getTime().compareTo(n2.getTime()));
        getViewState().loadData(notes);
        mIsInLoading = false;
    }

    private void onLoadingSuccess() {
//        getViewState().loadData(mTestNotes);
    }

    public List<BriefNote> load() {
        Log.d(TAG, "load()");

        if(!notes.isEmpty() || mLoading) {
            return notes;
        }

        notes = manager.getAll();
        Collections.sort(notes, (n1, n2) -> n1.getTime().compareTo(n2.getTime()));
        return notes;
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

    public void showDelDlg(List<TypeOf.Oid> oids) {
        List<BriefNote> notes = getByIod(oids);
        getViewState().showDelDlg(notes);
    }

    public void closeDlg() {
        getViewState().closeDlg();
    }

    private List<BriefNote> getByIod(List<TypeOf.Oid> oids) {
        List<BriefNote> notes = manager.getBy(oids);
        Collections.sort(notes, (n1, n2) -> n1.getTime().compareTo(n2.getTime()));
        return notes;
    }

    public void del(List<BriefNote> notes) {
        Log.d(MainActivity.TAG, "MainPresenter.del");
        manager.del(notes);
        loadData();
    }

    public void delByOids(List<TypeOf.Oid> oids) {
        Log.d(MainActivity.TAG, "MainPresenter.delByOids");
        manager.del(getByIod(oids));
        loadData();
    }


    public void add(List<BriefNote> notes) {
        manager.add(notes);
        loadData();
    }

    public void resume() {
        loadData();
    }
}
