package com.gdetotut.libs.jundo_droidsample.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.gdetotut.libs.jundo_droidsample.App;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

/**
 * Created by valerius on 25.01.18.
 */

public class BriefNoteManager {

    private static String NAME = "NAME";
    private static String LIST = "list";

    @Inject
    Context ctx;

    private Map<String, BriefNote> notes = new TreeMap<>();

    public BriefNoteManager() {
        App.getAppComponent().inject(this);
    }

    public void load() {

        notes.clear();

        SharedPreferences sp = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String json = sp.getString(LIST, null);
        boolean loaded = false;
        if(null != json) {
            try {
                notes = new Gson().fromJson(json, new TypeToken<Map<String, BriefNote>>(){}.getType());
                loaded = true;
            }catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
            }
        }

        if(!loaded) {
            final int count = 36;
            final int twoHours = 1000 * 60 * 60 * 2; // 2 hours
            final int twentyFourHours = twoHours * 12; // 24 hours
            final long startTime = System.currentTimeMillis() - twentyFourHours*3;
            for(int i=0; i<count; ++i) {
                final long time = startTime + twoHours * i;
                final TypeOf.Oid oid = TypeOf.Oid.generate();
                final BriefNote briefNote = new BriefNote(oid, time, "note: " + i);
                notes.put(oid.getValue(), briefNote);
            }
        }

    }

    public void save() {
        String json = new Gson().toJson(notes);
        SharedPreferences.Editor ed = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        ed.putString(LIST, json).apply();
    }

    public List<BriefNote> getAll() {
        return new ArrayList<>(notes.values());
    }

    public List<BriefNote> getBy(List<TypeOf.Oid> oids) {
        List<BriefNote> res = new ArrayList<>();
        for (TypeOf.Oid to: oids) {
            BriefNote note = notes.get(to.getValue());
            if(note != null) {
                res.add(note);
            }
        }
        return res;
    }

}
