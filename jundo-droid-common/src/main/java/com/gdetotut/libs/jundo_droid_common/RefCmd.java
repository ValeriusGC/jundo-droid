package com.gdetotut.libs.jundo_droid_common;

import java.io.Serializable;

/**
 * Simple command with access via getter/setter references.
 *
 * @param <V> the type of the referenced value.
 */
public final class RefCmd<V extends Serializable> extends UndoCommand {

    private final Setter<V> setter;
    private final V oldValue;
    private final V newValue;

    /**
     * Constructs object.
     *
     * @param caption a short string describing what this command does. Optional.
     * @param getter   a reference to getter-method for this value. Getter shouldn't has parameters
     *                 and should return value of the V type. Required.
     * @param setter   a reference to setter-method for this value. Getter should has parameter
     *                 of the V type and shouldn't return value. Required.
     * @param newValue the value to set to.
     */
    public RefCmd(String caption, Getter<V> getter, Setter<V> setter, V newValue) {
        super(caption);
        if (getter == null) {
            throw new NullPointerException("getter");
        } else if (setter == null) {
            throw new NullPointerException("setter");
        } else {
            this.setter = setter;
            this.oldValue = getter.get();
            this.newValue = newValue;
        }
    }

    @Override
    protected void doUndo() {
        setter.set(oldValue);
    }

    @Override
    protected void doRedo() {
        setter.set(newValue);
    }

}
