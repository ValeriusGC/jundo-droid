package com.gdetotut.libs.jundo_droidsample.ui.activity;

import android.util.Log;

import com.gdetotut.libs.jundo_droid_common.UndoCommand;
import com.gdetotut.libs.jundo_droid_common.UndoStack;
import com.gdetotut.libs.jundo_droidsample.model.BriefNote;
import com.gdetotut.libs.jundo_droidsample.model.TypeOf;
import com.gdetotut.libs.jundo_droidsample.mvp.presenters.MainPresenter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by valerius on 25.01.18.
 */

public class MainUndoCtrl implements Serializable {

    public static final String LC_PRES = "presenter";

    public static class RemoveItemUndo extends UndoCommand {

//        List<BriefNote> notes = new ArrayList<>();
        List<String> notes = new ArrayList<>();

        public RemoveItemUndo(UndoStack owner, String caption, List<BriefNote> notes) {
            super(owner, caption, null);
            for (BriefNote n :  notes) {
                this.notes.add(n.getOid().getValue());
            }
            Log.d(MainActivity.TAG, "RemoveItemUndo");
        }

        @Override
        protected void doRedo() {
            Log.d(MainActivity.TAG, "RemoveItemUndo.doRedo");
            try{
                MainPresenter mp = (MainPresenter) owner.getLocalContexts().get(LC_PRES);
                Log.d(MainActivity.TAG, "RemoveItemUndo.doRedo: " + mp);
                List<TypeOf.Oid> oids = new ArrayList<>();
                for (String s : notes) {
                    oids.add(new TypeOf.Oid(s));
                }
                Log.d(MainActivity.TAG, "RemoveItemUndo.doRedo: " + oids);
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
                List<TypeOf.Oid> oids = new ArrayList<>();
                for (String s : notes) {
                    oids.add(new TypeOf.Oid(s));
                }
                Log.d(MainActivity.TAG, "RemoveItemUndo.doUndo: " + oids);
                mp.addByOids(oids);
            }catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
            }
        }
    }

}
