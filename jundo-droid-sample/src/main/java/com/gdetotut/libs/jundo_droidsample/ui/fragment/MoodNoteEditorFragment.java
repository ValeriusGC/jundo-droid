package com.gdetotut.libs.jundo_droidsample.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.gdetotut.libs.jundo_droidsample.R;
import com.gdetotut.libs.jundo_droidsample.mvp.presenters.MoodNoteEditorPresenter;
import com.gdetotut.libs.jundo_droidsample.mvp.views.MoodNoteEditorView;

public class MoodNoteEditorFragment extends MvpAppCompatFragment implements MoodNoteEditorView {
    public static final String TAG = "MoodNoteEditorFragment";
    @InjectPresenter
    MoodNoteEditorPresenter mMoodNoteEditorPresenter;

    public static MoodNoteEditorFragment newInstance() {
        MoodNoteEditorFragment fragment = new MoodNoteEditorFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mood_note_editor, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
