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

public class ListNoteEditorFragment extends MvpAppCompatFragment
        implements ListNoteEditorView {

    public interface UndoWatcher {

        default void indexChanged(int idx) {
        }

        default void cleanChanged(boolean clean) {
        }

        /**
         * This event fires once when we have no undo anymore (false); we again have them (true).
         *
         * @param canUndo true when stack has command to undo; false otherwise.
         */
        default void canUndoChanged(boolean canUndo) {
        }

        /**
         * This event fires once when we have no redo anymore (false); we again have them (true).
         *
         * @param canRedo true when stack has command to redo; false otherwise.
         */
        default void canRedoChanged(boolean canRedo) {
        }

        /**
         * This event fires after every undo/redo. shows next undoCaption.
         *
         * @param undoCaption caption for next undo command.
         */
        default void undoTextChanged(String undoCaption) {
        }

        /**
         * This event fires after every undo/redo. shows next redoCaption.
         *
         * @param redoCaption caption for next redo command.
         */
        default void redoTextChanged(String redoCaption) {
        }

        /**
         * This event fires once when macro creation starts and stops.
         *
         * @param on true if starts; otherwise false.
         */
        default void macroChanged(boolean on) {
        }

    }

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
