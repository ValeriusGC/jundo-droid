package com.gdetotut.libs.jundo_droidsample.ui.activity;

import android.util.Log;

import com.gdetotut.libs.jundo_droid_common.UndoCommand;
import com.gdetotut.libs.jundo_droid_common.UndoStack;
import com.gdetotut.libs.jundo_droidsample.model.BriefNote;
import com.gdetotut.libs.jundo_droidsample.model.TypeOf;
import com.gdetotut.libs.jundo_droidsample.mvp.presenters.MainPresenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by valerius on 25.01.18.
 */

public class MainUndoCtrl implements Serializable {

    public static final String LC_PRES = "presenter";

    /**
     * Размышляем над концепцией "Undo для удаления записей".
     * Класс {@link BriefNote} не является сериализуемым, следовательно, хранить его экземпляры в командах недопустимо.
     * Будем хранить, как JSON.
     */
    public static class RemoveItemUndo extends UndoCommand {

        final String json;
        List<TypeOf.Oid> oids = new ArrayList<>();

        public RemoveItemUndo(UndoStack owner, String caption, List<BriefNote> notes) {
            super(owner, caption, null);
            for (BriefNote n : notes) {
                oids.add(n.getOid());
            }
            json = new Gson().toJson(notes);
            Log.d(MainActivity.TAG, "RemoveItemUndo");
        }

        @Override
        protected void doRedo() {
            Log.d(MainActivity.TAG, "RemoveItemUndo.doRedo");
            try{
                MainPresenter mp = (MainPresenter) owner.getLocalContexts().get(LC_PRES);
                Log.d(MainActivity.TAG, "RemoveItemUndo.doRedo: " + mp);
                mp.delByOids(oids);
                Log.d(MainActivity.TAG, "RemoveItemUndo.mp.delByOids(oids)");
            }catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
            }
            Log.d(MainActivity.TAG, "RemoveItemUndo.doRedo: FINISH");
        }

        @Override
        protected void doUndo() {
            Log.d(MainActivity.TAG, "RemoveItemUndo.doUndo");
            try{
                MainPresenter mp = (MainPresenter) owner.getLocalContexts().get(LC_PRES);
                Type listType = new TypeToken<ArrayList<BriefNote>>(){}.getType();
                List<BriefNote> notes = new Gson().fromJson(json, listType);
                mp.add(notes);
            }catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
            }
        }
    }

}
