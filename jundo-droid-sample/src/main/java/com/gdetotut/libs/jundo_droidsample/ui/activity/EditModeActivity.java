package com.gdetotut.libs.jundo_droidsample.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.gdetotut.libs.jundo_droidsample.R;
import com.gdetotut.libs.jundo_droidsample.mvp.presenters.EditModePresenter;
import com.gdetotut.libs.jundo_droidsample.mvp.views.EditModeView;
import com.gdetotut.libs.jundo_droidsample.ui.fragment.ListNoteEditorFragment;
import com.gdetotut.libs.jundo_droidsample.ui.fragment.MoodNoteEditorFragment;
import com.gdetotut.libs.jundo_droidsample.ui.fragment.SimpleNoteEditorFragment;
import com.gdetotut.libs.jundo_droidsample.ui.fragment.TodoNoteEditorFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 2017-07-04 10:07:37
 *
 * Activity to maintain base editor mode.
 * Incapsulates creating 'editors' for *Notes.
 *
 * @author valerius
 */
public class EditModeActivity extends MvpAppCompatActivity implements EditModeView {

    public static final String TAG = "EditModeActivity";

    @InjectPresenter
    EditModePresenter mEditModePresenter;

    // Define the list of accepted constants and declare the EditorType annotation
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({EDITOR_SIMPLE, EDITOR_LIST, EDITOR_TODO, EDITOR_MOOD})
    public @interface EditorType {}
    // Constant for Simple Editor
    public static final int EDITOR_SIMPLE = 0;
    // Constant for List Editor
    public static final int EDITOR_LIST = 1;
    // Constant for Todo Editor
    public static final int EDITOR_TODO = 2;
    // Constant for Mood Editor
    public static final int EDITOR_MOOD = 3;
    // ~Define the list of accepted constants and declare the EditorType annotation

    public static final String PARAM_TYPE = TAG + ".type";

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, EditModeActivity.class);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mode);

        @EditorType int type = getIntent().getExtras().getInt(PARAM_TYPE, EDITOR_SIMPLE);
        Toast.makeText(this, "EditModeActivity.type: " + type, Toast.LENGTH_SHORT).show();

        Fragment f = null;
        switch (type) {
            case EDITOR_SIMPLE:
                f = SimpleNoteEditorFragment.newInstance();
                break;
            case EDITOR_LIST:
                f = ListNoteEditorFragment.newInstance();
                break;
            case EDITOR_TODO:
                f = TodoNoteEditorFragment.newInstance();
                break;
            case EDITOR_MOOD:
                f = MoodNoteEditorFragment.newInstance();
                break;
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.edit_mode_fragment, f);
        transaction.commit();

    }
}
