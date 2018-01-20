package com.gdetotut.libs.jundo_droidsample;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void testJundo() throws Exception {

        Point pt = new Point(10,20);
        assertEquals(10, pt.getX());
        assertEquals(20, pt.getY());

        UndoStack stack = new UndoStack(pt, null);
        stack.push(new RefCmd<>(stack, "Change x", pt::getX, pt::setX, 33, null));
        assertEquals(33, pt.getX());

        String store = UndoPacket
                .make(stack, "com.gdetotut.libs.jundo_droidsample.Point", 1)
                .store();

        UndoStack stack1 = UndoPacket
                .peek(store, subjInfo -> "com.gdetotut.libs.jundo_droidsample.Point".equals(subjInfo.id))
                .restore(null)
                .stack(null);

        Point pt1 = (Point) stack1.getSubj();
        stack1.undo();
        assertEquals(10, pt1.getX());

    }
}