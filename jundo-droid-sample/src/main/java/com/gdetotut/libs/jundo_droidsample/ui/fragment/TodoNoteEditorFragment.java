package com.gdetotut.libs.jundo_droidsample.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.gdetotut.libs.jundo_droidsample.R;
import com.gdetotut.libs.jundo_droidsample.mvp.presenters.TodoNoteEditorPresenter;
import com.gdetotut.libs.jundo_droidsample.mvp.views.TodoNoteEditorView;

import butterknife.BindView;

public class TodoNoteEditorFragment extends MvpAppCompatFragment implements TodoNoteEditorView {
    public static final String TAG = "TodoNoteEditorFragment";

    @InjectPresenter
    TodoNoteEditorPresenter mTodoNoteEditorPresenter;

    @BindView(R.id.todo_edit_text)
    EditText editText;


    public static TodoNoteEditorFragment newInstance() {
        TodoNoteEditorFragment fragment = new TodoNoteEditorFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_note_editor, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getArguments();

    }
}
