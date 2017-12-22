package com.gdetotut.libs.jundo_droidsample.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.gdetotut.libs.jundo_droidsample.R;
import com.gdetotut.libs.jundo_droidsample.mvp.presenters.ListNoteEditorPresenter;
import com.gdetotut.libs.jundo_droidsample.mvp.views.ListNoteEditorView;

public class ListNoteEditorFragment extends MvpAppCompatFragment implements ListNoteEditorView {

    public static final String TAG = "ListNoteEditorFragment";

    @InjectPresenter
    ListNoteEditorPresenter mListNoteEditorPresenter;

    public static ListNoteEditorFragment newInstance() {
        ListNoteEditorFragment fragment = new ListNoteEditorFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_note_editor, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}