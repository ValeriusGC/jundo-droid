package com.gdetotut.libs.jundo_droid_common;


import com.gdetotut.libs.jundo_droid_common.aux.SimpleClass;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestUndoGroup  extends BaseTest{

    /**
     * Group should not contain two or more stacks with one object.
     */
    @Test
    public void oneGroupOneObject() {

        SimpleClass<Integer> subj = new SimpleClass<>(Integer.class);
        UndoGroup group = new UndoGroup();
        UndoStack stackA = new UndoStack(subj, group);
        assertEquals(1, group.getStacks().size());
        UndoStack stackB = new UndoStack(subj, group);
        assertEquals(1, group.getStacks().size());

        SimpleClass<Integer> subj2 = new SimpleClass<>(Integer.class);
        UndoStack stackC = new UndoStack(subj2, group);
        group.add(stackC);
        assertEquals(2, group.getStacks().size());


    }

}
