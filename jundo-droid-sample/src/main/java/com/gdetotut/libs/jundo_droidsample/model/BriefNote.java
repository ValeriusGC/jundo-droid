package com.gdetotut.libs.jundo_droidsample.model;

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

}
