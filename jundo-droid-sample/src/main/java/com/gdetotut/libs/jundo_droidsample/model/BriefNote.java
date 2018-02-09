package com.gdetotut.libs.jundo_droidsample.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Represents brief view of Note
 */
public class BriefNote implements Serializable {

    private final TypeOf.Oid oid;
    private Long mTime;
    private String mTitle;
    private String text;

    public BriefNote(TypeOf.Oid oid, Long mTime, String mTitle, String text) {
        this.oid = oid;
        this.mTime = mTime;
        this.mTitle = mTitle;
        this.text = text;
    }

    public TypeOf.Oid getOid() {
        return oid;
    }

    public Long getTime() {
        return mTime;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
//        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        String strDate = sdfDate.format(new Date(getTime()));
        return "BriefNote{" +
                "oid=" + oid +
                ", mTitle='" + mTitle + '\'' +
                '}';
    }
}
