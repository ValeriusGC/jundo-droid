package com.gdetotut.libs.jundo_droidsample.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.gdetotut.libs.jundo_droidsample.R;
import com.gdetotut.libs.jundo_droidsample.mvp.presenters.SimpleNoteEditorPresenter;
import com.gdetotut.libs.jundo_droidsample.mvp.views.SimpleNoteEditorView;

public class SimpleNoteEditorFragment extends MvpAppCompatFragment implements SimpleNoteEditorView {

    public static final String TAG = "SimpleNoteEditorFragment";

    @InjectPresenter
    SimpleNoteEditorPresenter mSimpleNoteEditorPresenter;

    public static SimpleNoteEditorFragment newInstance() {
        SimpleNoteEditorFragment fragment = new SimpleNoteEditorFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_note, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
