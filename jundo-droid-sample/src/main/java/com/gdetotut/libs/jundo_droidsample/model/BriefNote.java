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

    public BriefNote(TypeOf.Oid oid, Long mTime, String mTitle) {
        this.oid = oid;
        this.mTime = mTime;
        this.mTitle = mTitle;
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
