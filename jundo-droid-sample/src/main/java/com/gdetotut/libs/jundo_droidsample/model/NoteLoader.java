package com.gdetotut.libs.jundo_droidsample.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by valerius on 30.06.17.
 *
 * Loads temp notes for demonstrating 'sticky list'
 *
 * @author valerius
 */
public class NoteLoader {

    private static final String TAG = NoteLoader.class.getSimpleName();

    private List<BriefNote> mTestNotes = new ArrayList<>();
    private ArrayList<OnLoadCallback> mOnLoadCallbacks = new ArrayList<>();
    private boolean mLoading;

    public interface OnLoadCallback {
        void onRandomUsersDidLoad(List<BriefNote> testNotes);
        void onRandomUserLoadFailure(Throwable t);
    }

    public List<BriefNote> load() {

        Log.d(TAG, "load()");

        if(!mTestNotes.isEmpty()) {
            return mTestNotes;
        }

        if(mLoading) {
            return mTestNotes;
        }

        mLoading = true;
        final int count = 36;
        final int twoHours = 1000 * 60 * 60 * 2; // 2 hours
        final int twentyFourHours = twoHours * 12; // 24 hours
        final long startTime = System.currentTimeMillis() - twentyFourHours * 3;
        for(int i=0; i<count; ++i) {
            final long time = startTime + twoHours * i;
            final BriefNote briefNote = new BriefNote(time, "note: " + i);
            mTestNotes.add(briefNote);
        }
        return mTestNotes;
    }

    public void load(final OnLoadCallback onLoadCallback) {

        Log.d(TAG, "OnLoadCallback load()");

        if(!mTestNotes.isEmpty()) {
            onLoadCallback.onRandomUsersDidLoad(mTestNotes);
            return;
        }

        mOnLoadCallbacks.add(onLoadCallback);
        if(mLoading) {
            return;
        }

        mLoading = true;
        final int count = 36;
        final int twoHours = 1000 * 60 * 60 * 2; // 2 hours
        final int twentyFourHours = twoHours * 12; // 24 hours
        final long startTime = System.currentTimeMillis() - twentyFourHours * 3;
        for(int i=0; i<count; ++i) {
            final long time = startTime + twoHours * i;
            final BriefNote briefNote = new BriefNote(time, "note: " + i);
            mTestNotes.add(briefNote);
        }
        for (OnLoadCallback c : mOnLoadCallbacks) {
            c.onRandomUsersDidLoad(mTestNotes);
        }

    }

}
