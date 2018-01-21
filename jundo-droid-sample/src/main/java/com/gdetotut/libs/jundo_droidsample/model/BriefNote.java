package com.gdetotut.libs.jundo_droidsample.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Represents brief view of Note
 */
public class BriefNote {

    private Long mTime;

    private String mTitle;

    public BriefNote(Long mTime, String mTitle) {
        this.mTime = mTime;
        this.mTitle = mTitle;
    }

    public Long getTime() {
        return mTime;
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String strDate = sdfDate.format(new Date(getTime()));
        return "BriefNote{" +
                "mTime=" + strDate +
                ", mTitle='" + mTitle + '\'' +
                '}';
    }
}
