package com.gdetotut.libs.jundo_droidsample.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.gdetotut.libs.jundo_droidsample.R;
import com.gdetotut.libs.jundo_droidsample.commons.Fab;
import com.gdetotut.libs.jundo_droidsample.commons.ItemClickSupport;
import com.gdetotut.libs.jundo_droidsample.model.BriefNote;
import com.gdetotut.libs.jundo_droidsample.mvp.presenters.MainPresenter;
import com.gdetotut.libs.jundo_droidsample.mvp.views.MainView;
import com.gdetotut.libs.jundo_droidsample.ui.adapters.MainActivityAdapter;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gdetotut.libs.jundo_droidsample.ui.activity.EditModeActivity.EDITOR_LIST;
import static com.gdetotut.libs.jundo_droidsample.ui.activity.EditModeActivity.EDITOR_MOOD;
import static com.gdetotut.libs.jundo_droidsample.ui.activity.EditModeActivity.EDITOR_SIMPLE;
import static com.gdetotut.libs.jundo_droidsample.ui.activity.EditModeActivity.EDITOR_TODO;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    // Use as symbol in Log.
    public static final String TAG = MainActivity.class.getSimpleName();

    // Use for retain List position after screen rotation.
    private static final String STATE_SCROLL_POSITION = "MainActivityAdapter.STATE_SCROLL_POSITION";

    private static final int REQUEST_CODE_EDIT_MODE = 1001;

    @InjectPresenter
    MainPresenter mPresenter;

    @BindView(R.id.appBar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress)
    ProgressBar mProgressBar;

    @BindView(R.id.fab)
    Fab mFabView;

    // Common class for FAB
    private MaterialSheetFab mFab;

    // Saves previous bar color
    private int mStatusBarColor;

    // Adapter for RecyclerView
    private MainActivityAdapter mAdapter = new MainActivityAdapter();

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayout());
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        mRecyclerView.setLayoutManager(new StickyHeaderLayoutManager());
        mRecyclerView.setAdapter(mAdapter);

        // Bind methods to events
        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(this::onListItemClicked);
        ItemClickSupport.addTo(mRecyclerView).setOnItemLongClickListener(this::onListItemLongClicked);

        setupFab();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        RecyclerView.LayoutManager lm = mRecyclerView.getLayoutManager();
        Parcelable scrollState = lm.onSaveInstanceState();
        outState.putParcelable(STATE_SCROLL_POSITION, scrollState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState
                    .getParcelable(STATE_SCROLL_POSITION));
        }
    }


    protected void onListItemClicked(RecyclerView recyclerView, int position, View v) {
        Toast.makeText(this, "MainActivity.onListItemClicked", Toast.LENGTH_SHORT).show();
    }

    protected boolean onListItemLongClicked(RecyclerView recyclerView, int position, View v) {
        Toast.makeText(this, "MainActivity.onListItemLongClicked: ", Toast.LENGTH_SHORT).show();
        return false;
    }

    @LayoutRes
    protected int getContentViewLayout(){
        return R.layout.activity_main;
    }

    @Override
    public void showListProgress() {

    }

    @Override
    public void hideListProgress() {

    }

    @Override
    public void loadData(@NonNull List<BriefNote> list) {
        Log.d(TAG, "loadData");
        mAdapter.setNotes(list);
    }

    @Override
    public void onEditorShow(@EditModeActivity.EditorType int type) {
        Intent intent = EditModeActivity.getIntent(this);
        intent.putExtra(EditModeActivity.PARAM_TYPE, type);
        startActivityForResult(intent, REQUEST_CODE_EDIT_MODE);
    }

    @Override
    public void updateView() {
        Log.d(TAG, "updateView");
    }

    /**
     * Sets up the Floating action button.
     */
    private void setupFab() {
        View sheetView = ButterKnife.findById(this, R.id.fab_sheet);
        View overlay = ButterKnife.findById(this, R.id.overlay);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.theme_accent);
        // Create material sheet FAB
        mFab = new MaterialSheetFab<>(mFabView, sheetView, overlay, sheetColor, fabColor);
        // Create material sheet FAB
        mFab = new MaterialSheetFab<>(mFabView, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet event listener
        mFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Save current status bar color
                mStatusBarColor = getStatusBarColor();
                // Set darker status bar color to match the dim overlay
                setStatusBarColor(getResources().getColor(R.color.theme_primary_dark2));
            }

            @Override
            public void onHideSheet() {
                // Restore status bar color
                setStatusBarColor(mStatusBarColor);
            }
        });

        // Set material sheet item click listeners
        ButterKnife.findById(this, R.id.fab_sheet_item_simple)
                .setOnClickListener(v -> clickFab(EDITOR_SIMPLE));
        ButterKnife.findById(this, R.id.fab_sheet_item_list)
                .setOnClickListener(v -> clickFab(EDITOR_LIST));
        ButterKnife.findById(this, R.id.fab_sheet_item_todo)
                .setOnClickListener(v -> clickFab(EDITOR_TODO));
        ButterKnife.findById(this, R.id.fab_sheet_item_mood)
                .setOnClickListener(v -> clickFab(EDITOR_MOOD));
    }

    @Override
    public void onBackPressed() {
        if (mFab.isSheetVisible()) {
            mFab.hideSheet();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this, "MainActivity.requestCode: " + requestCode, Toast.LENGTH_SHORT).show();
        mPresenter.updateView();
    }

    /**
     * Processes click on FAB.
     *
     * @param type
     */
    private void clickFab(@EditModeActivity.EditorType int type) {
        mFab.hideSheet();
        mPresenter.showEditor(type);
    }

    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getWindow().getStatusBarColor();
        }
        return 0;
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

}