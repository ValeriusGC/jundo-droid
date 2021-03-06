package com.gdetotut.libs.jundo_droid_common;

import org.junit.Assert;
import org.junit.Test;

public class TestUndoCommand extends BaseTest {


    @Test
    public void childCount() {
        final UndoCommand cmd = new UndoCommand("", null);
        Assert.assertEquals(0, cmd.childCount());

        final UndoCommand cmdChild = new UndoCommand("", cmd);
        Assert.assertEquals(0, cmdChild.childCount());
        Assert.assertEquals(1, cmd.childCount());

    }

}
