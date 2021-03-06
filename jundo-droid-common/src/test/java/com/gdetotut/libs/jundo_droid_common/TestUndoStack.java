package com.gdetotut.libs.jundo_droid_common;

import com.gdetotut.libs.jundo_droid_common.aux.NonTrivialClass;
import com.gdetotut.libs.jundo_droid_common.aux.Point;
import com.gdetotut.libs.jundo_droid_common.aux.SimpleClass;
import com.gdetotut.libs.jundo_droid_common.aux.SimpleUndoWatcher;

import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;

import static com.gdetotut.libs.jundo_droid_common.aux.NonTrivialClass.Item.Type.CIRCLE;
import static com.gdetotut.libs.jundo_droid_common.aux.NonTrivialClass.Item.Type.RECT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestUndoStack extends BaseTest implements Serializable {

    UndoStack stack;
    Object[] arr;
    Serializable subj;

    @SuppressWarnings("unchecked")
    public <V extends Serializable> void initSimple(Class<V> type, V[] array) throws Exception {
        arr = array;
        subj = new SimpleClass<V>(type);
        stack = new UndoStack(subj, null);
        stack.setWatcher(new SimpleUndoWatcher());
        for (V i : array) {
            stack.push(new FunctionalCommand<>("", ((SimpleClass<V>) subj)::getValue,
                    ((SimpleClass<V>) subj)::setValue, i, null));
        }
    }

    @SuppressWarnings("unchecked")
    public <V extends Serializable> void testSimple() throws IOException, ClassNotFoundException {

        UndoManager manager = new UndoManager(null, 333, stack);
        UndoManager managerBack = UndoManager.deserialize(UndoManager.serialize(manager, false));
        UndoStack stackBack = managerBack.getStack();
        // Here we can not compare stacks themselves 'cause of stack's comparison principle
        assertEquals(stack.getSubject(), stackBack.getSubject());
        SimpleClass<V> objBack = (SimpleClass<V>) stackBack.getSubject();
        assertEquals(subj, objBack);

        // Walk here and there
        for (int i = arr.length - 1; i > 0; i--) {
            stackBack.undo();
//            System.out.println(objBack.getValue());
            assertEquals((arr[i - 1]), objBack.getValue());
        }
        for (int i = 1; i < arr.length; i++) {
            stackBack.redo();
            assertEquals(arr[i], objBack.getValue());
        }
    }


    /**
     * Simply shows how elegant {@link FunctionalCommand} works
     */
    @Test
    public void testIntegerClass() throws Exception {

        Point pt = new Point(-30, -40);
        UndoStack stack = new UndoStack(pt, null);
        UndoCommand undoCommand = new UndoCommand("Move point", null);
        new FunctionalCommand<>("Change x", pt::getX, pt::setX, 10, undoCommand);
        new FunctionalCommand<>("Change y", pt::getY, pt::setY, 20, undoCommand);
        stack.push(undoCommand);
        assertEquals(1, stack.count());
        assertEquals(10, pt.getX());
        assertEquals(20, pt.getY());
        stack.undo();
        assertEquals(-30, pt.getX());
        assertEquals(-40, pt.getY());
        assertEquals(0, stack.getIdx());

        UndoManager manager = new UndoManager(null, 4, stack);
        manager = UndoManager.deserialize(UndoManager.serialize(manager, true));

        UndoStack stackBack = manager.getStack();
        Point ptBack = (Point) stackBack.getSubject();
        assertEquals(pt, ptBack);
        assertEquals(-30, ptBack.getX());
        assertEquals(-40, ptBack.getY());
        assertEquals(1, stackBack.count());
        assertEquals(0, stackBack.getIdx());

        stackBack.redo();
        // ))
        stackBack.redo();
        stackBack.redo();
        assertEquals(10, ptBack.getX());
        assertEquals(20, ptBack.getY());


    }

    /**
     * Create {@link UndoStack} with or without groups.
     */
    @Test
    public void creation() {

        {
            // Create without group
            Serializable subj = new SimpleClass<>(Integer.class);
            UndoStack stack = new UndoStack(subj, null);
            stack.setWatcher(new SimpleUndoWatcher());
            assertEquals(true, stack.isClean());
            assertEquals(false, stack.canRedo());
            assertEquals(false, stack.canUndo());
            assertEquals(0, stack.count());
            assertEquals(true, stack.isActive());
            assertEquals(subj, stack.getSubject());
            assertEquals("", stack.redoText());
            assertEquals("", stack.undoText());
            assertEquals(0, stack.getCleanIdx());
            assertEquals(0, stack.getIdx());
            assertEquals(0, stack.getUndoLimit());
        }

        {
            // Create with group
            // Checks:
            //  - setActive()
            //  - active()
            Serializable subjA = new SimpleClass<>(Integer.class);
            Serializable subjB = new SimpleClass<>(String.class);
            assertNotEquals(subjA, subjB);
            UndoGroup group = new UndoGroup();
            assertEquals(0, group.getStacks().size());

            UndoStack stackA = new UndoStack(subjA, group);
            stackA.setWatcher(new SimpleUndoWatcher());
            assertEquals(1, group.getStacks().size());
            assertEquals(null, group.getActive());
            assertEquals(false, stackA.isActive());

            // Set active thru UndoStack
            stackA.setActive(true);
            assertEquals(stackA, group.getActive());
            assertEquals(true, stackA.isActive());
            //
            stackA.setActive(false);
            assertEquals(null, group.getActive());
            assertEquals(false, stackA.isActive());

            // Set active thru UndoGroup
            group.setActive(stackA);
            assertEquals(stackA, group.getActive());
            assertEquals(true, stackA.isActive());
            //
            group.setActive(null);
            assertEquals(null, group.getActive());
            assertEquals(false, stackA.isActive());

            // Second stack. Do the same
            UndoStack stackB = new UndoStack(subjB, group);
            stackB.setWatcher(new SimpleUndoWatcher());
            assertEquals(2, group.getStacks().size());
            assertEquals(null, group.getActive());
            assertEquals(false, stackA.isActive());
            assertEquals(false, stackB.isActive());

            group.setActive(stackB);
            assertEquals(stackB, group.getActive());
            assertEquals(false, stackA.isActive());
            assertEquals(true, stackB.isActive());

            group.setActive(stackA);
            assertEquals(stackA, group.getActive());
            assertEquals(true, stackA.isActive());
            assertEquals(false, stackB.isActive());
        }

    }

    /**
     * Adding and clearing
     */
    @Test
    public void addAndClear() throws Exception {

        NonTrivialClass scene = new NonTrivialClass();
        UndoGroup group = new UndoGroup();
        UndoStack stack = new UndoStack(scene, group);
        stack.setWatcher(new SimpleUndoWatcher());
        group.setActive(stack);

        stack.push(new NonTrivialClass.AddCommand(CIRCLE, scene, null));
        assertEquals(1, stack.count());
        assertEquals(1, stack.getIdx());
        stack.push(new NonTrivialClass.AddCommand(CIRCLE, scene, null));
        assertEquals(2, stack.count());
        assertEquals(2, stack.getIdx());
        stack.clear();
        assertEquals(0, stack.count());
        assertEquals(0, stack.getIdx());

    }

    /**
     * Set and check limits:
     * - undoLimit
     * - setIndex
     */
    @Test
    public void limits() throws Exception {

        SimpleClass<Integer> subj = new SimpleClass<>(Integer.class);
        UndoGroup group = new UndoGroup();
        UndoStack stack = new UndoStack(subj, group);
        stack.setWatcher(new SimpleUndoWatcher());
        stack.setUndoLimit(5);
        for (int i = 0; i < 10; ++i) {
            stack.push(new FunctionalCommand<>(String.valueOf(i), subj::getValue, subj::setValue, i, null));
        }
        assertEquals(5, stack.count());
        stack.setIndex(0);
        assertEquals(0, stack.getIdx());
        assertEquals((Integer) 4, subj.getValue());
        stack.setIndex(stack.count());
        assertEquals(stack.count(), stack.getIdx());
        assertEquals((Integer) 9, subj.getValue());
    }

    /**
     * Set and check clean:
     * - setClean
     * - isClean
     * - getCleanIdx
     */
    @Test
    public void clean() throws Exception {

        SimpleClass<Integer> subj = new SimpleClass<>(Integer.class);
        UndoGroup group = new UndoGroup();
        UndoStack stack = new UndoStack(subj, group);
        stack.setWatcher(new SimpleUndoWatcher());
        for (int i = 0; i < 10; ++i) {
            stack.push(new FunctionalCommand<>(String.valueOf(i), subj::getValue, subj::setValue, i, null));
        }
        assertEquals(10, stack.count());
        stack.setIndex(5);
        assertEquals((Integer) 4, subj.getValue());
        stack.setClean();
        assertEquals(5, stack.getCleanIdx());
        assertEquals(true, stack.isClean());
        stack.undo();
        assertEquals(false, stack.isClean());
        stack.redo();
        assertEquals(true, stack.isClean());
        stack.redo();
        assertEquals(false, stack.isClean());
        stack.clear();
        assertEquals(0, stack.getCleanIdx());

        // Now set limit, set clean, and go out of it
        stack.setUndoLimit(5);
        for (int i = 0; i < 5; ++i) {
            stack.push(new FunctionalCommand<>(String.valueOf(i), subj::getValue, subj::setValue, i, null));
        }
        assertEquals(5, stack.count());
        stack.setIndex(2);
        stack.setClean();
        assertEquals(2, stack.getCleanIdx());
        stack.setIndex(0);
        assertEquals(2, stack.getCleanIdx());
        stack.push(new FunctionalCommand<>(String.valueOf(10), subj::getValue, subj::setValue, 10, null));
        assertEquals(-1, stack.getCleanIdx());
        assertEquals(false, stack.isClean());
    }

    /**
     * - canUndo
     * - canRedo
     * - undoText
     * - redoText
     */
    @Test
    public void auxProps() throws Exception {
        SimpleClass<Integer> subj = new SimpleClass<>(Integer.class);
        UndoGroup group = new UndoGroup();
        UndoStack stack = new UndoStack(subj, group);
        stack.setWatcher(new SimpleUndoWatcher());
        group.setActive(stack);
        assertEquals(false, stack.canUndo());
        assertEquals(false, stack.canRedo());
        assertEquals("", stack.undoText());
        assertEquals("", stack.redoText());
        assertEquals("", group.undoText());
        assertEquals("", group.redoText());
        assertEquals(false, group.canUndo());
        assertEquals(false, group.canRedo());

        for (int i = 0; i < 3; ++i) {
            stack.push(new FunctionalCommand<>(String.valueOf(i), subj::getValue, subj::setValue, i, null));
        }
        assertEquals(true, stack.canUndo());
        assertEquals(false, stack.canRedo());
        assertEquals("2", stack.undoText());
        assertEquals("", stack.redoText());
        assertEquals("2", group.undoText());
        assertEquals("", group.redoText());
        assertEquals(true, group.canUndo());
        assertEquals(false, group.canRedo());

        group.undo();
        assertEquals(true, stack.canUndo());
        assertEquals(true, stack.canRedo());
        assertEquals("1", stack.undoText());
        assertEquals("2", stack.redoText());
        assertEquals("1", group.undoText());
        assertEquals("2", group.redoText());
        assertEquals(true, group.canUndo());
        assertEquals(true, group.canRedo());

        group.getActive().setIndex(0);
        assertEquals(false, stack.canUndo());
        assertEquals(true, stack.canRedo());
        assertEquals("", stack.undoText());
        assertEquals("0", stack.redoText());
        assertEquals("", group.undoText());
        assertEquals("0", group.redoText());
        assertEquals(false, group.canUndo());
        assertEquals(true, group.canRedo());
    }

    /**
     * Undo props like {@link Integer}, {@link String}, etc
     */
    @Test
    public void testSimpleUndo() throws Exception {

        initSimple(String.class, new String[]{"one", null, "two"});
        testSimple();

        initSimple(Integer.class, new Integer[]{1, 2, 3, null, 8});
        testSimple();

        initSimple(Long.class, new Long[]{11L, 12L, 13L, 14L, null});
        testSimple();

        initSimple(Double.class, new Double[]{1.1, 2.2, 3.222});
        testSimple();

        initSimple(Boolean.class, new Boolean[]{true, false, true, null});
        testSimple();

    }

    @Test
    public void testNonTrivial() throws Exception {
        NonTrivialClass ntc = new NonTrivialClass();
        UndoStack stack = new UndoStack(ntc, null);
        stack.setWatcher(new SimpleUndoWatcher());
        assertEquals(0, ntc.items.size());

        {
            stack.push(new NonTrivialClass.AddCommand(CIRCLE, ntc, null));
            assertEquals(1, stack.count());
            assertEquals(1, stack.getIdx());
            assertEquals(1, ntc.items.size());
//            System.out.println(ntc);

            stack.push(new NonTrivialClass.AddCommand(RECT, ntc, null));
            assertEquals(2, stack.count());
            assertEquals(2, stack.getIdx());
            assertEquals(2, ntc.items.size());
//            System.out.println(ntc);

            stack.undo();
            assertEquals(2, stack.count());
            assertEquals(1, stack.getIdx());
            assertEquals(1, ntc.items.size());
//            System.out.println(ntc);

            stack.undo();
            assertEquals(2, stack.count());
            assertEquals(0, stack.getIdx());
            assertEquals(0, ntc.items.size());
//            System.out.println(ntc);

            UndoManager manager = new UndoManager(null, 333, stack);
            UndoManager managerBack = UndoManager.deserialize(UndoManager.serialize(manager, false));
            UndoStack stackBack = managerBack.getStack();
//            assertEquals(stack, stackBack);
            NonTrivialClass objBack = (NonTrivialClass) stackBack.getSubject();
//            assertEquals(subj, objBack);

//            System.out.println("-------serializ -");

            assertEquals(2, stackBack.count());
            assertEquals(0, stackBack.getIdx());
            assertEquals(0, objBack.items.size());
//            System.out.println(objBack);

            stackBack.redo();
            assertEquals(1, objBack.items.size());
//            System.out.println(objBack);

            stackBack.redo();
            assertEquals(2, objBack.items.size());
//            System.out.println(objBack);
        }


        {
//            System.out.println("--- Add/Del ---");
            stack.push(new NonTrivialClass.AddCommand(CIRCLE, ntc, null));
            assertEquals(1, stack.count());
            assertEquals(1, stack.getIdx());
            assertEquals(1, ntc.items.size());
//            System.out.println(ntc);
            stack.push(new NonTrivialClass.DeleteCommand(ntc, null));
            assertEquals(2, stack.count());
            assertEquals(2, stack.getIdx());
            assertEquals(0, ntc.items.size());
//            System.out.println(ntc);

            stack.undo();
            assertEquals(2, stack.count());
            assertEquals(1, stack.getIdx());
            assertEquals(1, ntc.items.size());
//            System.out.println(ntc);

            stack.undo();
            assertEquals(2, stack.count());
            assertEquals(0, stack.getIdx());
            assertEquals(0, ntc.items.size());
//            System.out.println(ntc);

            stack.undo();
            assertEquals(2, stack.count());
            assertEquals(0, stack.getIdx());
            assertEquals(0, ntc.items.size());
//            System.out.println(ntc);

        }

        {
//            System.out.println("--- Add/Del/Move ---");
            stack.redo();
            assertEquals(2, stack.count());
            assertEquals(1, stack.getIdx());
            assertEquals(1, ntc.items.size());

            NonTrivialClass.Item item = ((NonTrivialClass) stack.getSubject()).items.get(0);
            int newPos = 100;
            int oldPos = item.x;
            item.x = newPos; // Moved
            stack.push(new NonTrivialClass.MovedCommand(item, oldPos, null));
            assertEquals(2, stack.count());
            assertEquals(2, stack.getIdx());
            assertEquals(1, ntc.items.size());

            assertEquals(newPos, item.x);
            stack.undo();
            assertEquals(oldPos, item.x);
            stack.redo();
            assertEquals(newPos, item.x);

            // Merge
            newPos = 200;
            item.x = newPos; // Moved again
            stack.push(new NonTrivialClass.MovedCommand(item, item.x, null));
            assertEquals(2, stack.count());
            assertEquals(2, stack.getIdx());
            assertEquals(1, ntc.items.size());
//            System.out.println("4: " + stack);


            // Back
            stack.undo();
            assertEquals(oldPos, item.x);
            assertEquals(2, stack.count());
            assertEquals(1, stack.getIdx());
            assertEquals(1, ntc.items.size());

            // Serialize
            UndoManager manager = new UndoManager(null, 333, stack);
            UndoManager managerBack = UndoManager.deserialize(UndoManager.serialize(manager, false));
            UndoStack stackBack = managerBack.getStack();
            NonTrivialClass objBack = (NonTrivialClass) stackBack.getSubject();

//            System.out.println("-------serializ -");

            assertEquals(2, stackBack.count());
            assertEquals(1, stackBack.getIdx());
            assertEquals(1, objBack.items.size());
//            System.out.println(objBack);

            stackBack.redo();
            assertEquals(1, objBack.items.size());
//            System.out.println(objBack);

        }

    }

    /**
     * Test for macrocommands.
     */
    @Test
    public void macro() throws Exception {

        NonTrivialClass subj = new NonTrivialClass();
        UndoStack stack = new UndoStack(subj, null);

        stack.push(new NonTrivialClass.AddCommand(CIRCLE, subj, null));
        stack.push(new NonTrivialClass.AddCommand(RECT, subj, null));
        assertEquals(2, stack.count());
        assertEquals(0, stack.getCleanIdx());
        assertEquals(true, stack.canUndo());
        assertEquals(false, stack.canRedo());
        stack.undo();
        assertEquals(true, stack.canRedo());
        stack.redo();

        // Adding macrocommand not affects count of simple commands exclude moment of beginning
        stack.beginMacro("Moving");
        assertEquals(3, stack.count());
        assertEquals(false, stack.canUndo());
        stack.push(new NonTrivialClass.MovedCommand(subj.items.get(0), 20, null));
        stack.push(new NonTrivialClass.AddCommand(RECT, subj, null));
        stack.push(new NonTrivialClass.AddCommand(RECT, subj, null));
        stack.push(new NonTrivialClass.AddCommand(RECT, subj, null));
        stack.push(new NonTrivialClass.AddCommand(RECT, subj, null));
        stack.push(new NonTrivialClass.AddCommand(RECT, subj, null));
        stack.push(new NonTrivialClass.AddCommand(RECT, subj, null));
        // Adding macrocommand not affects count
        assertEquals(3, stack.count());

        // Should has no effect inside macro process
        stack.setClean();
        assertEquals(0, stack.getCleanIdx());
        assertEquals(3, stack.count());
        assertEquals(2, stack.getIdx());
        stack.undo();
        assertEquals(2, stack.getIdx());
        stack.redo();
        assertEquals(2, stack.getIdx());
        stack.setIndex(0);
        assertEquals(2, stack.getIdx());

        stack.endMacro();
        assertEquals(3, stack.getIdx());
        // 2 simple and 1 macro
        assertEquals(3, stack.count());
        assertEquals(8, subj.items.size());

        // Undo macro
        stack.undo();
        assertEquals(2, stack.getIdx());
        assertEquals(2, subj.items.size());

        // Undo macro
        stack.redo();
        assertEquals(3, stack.getIdx());
        assertEquals(8, subj.items.size());

        UndoManager manager = new UndoManager(null, 2, stack);
        String z_data = UndoManager.serialize(manager, true);
//        System.out.println("zipped length : " + z_data.length());
        UndoManager managerBack = UndoManager.deserialize(z_data);
        assertEquals(manager.VERSION, managerBack.VERSION);
        assertEquals(manager.getExtras(), managerBack.getExtras());
        assertEquals(manager.getStack().getSubject(), managerBack.getStack().getSubject());
        assertEquals(NonTrivialClass.class, manager.getStack().getSubject().getClass());
        assertEquals(3, manager.getStack().getIdx());
        assertEquals(8, ((NonTrivialClass) manager.getStack().getSubject()).items.size());

        // After deserialization
        // Undo macro
        manager.getStack().undo();
        assertEquals(2, manager.getStack().getIdx());
        assertEquals(2, ((NonTrivialClass) manager.getStack().getSubject()).items.size());

        // Undo macro
        manager.getStack().redo();
        assertEquals(3, manager.getStack().getIdx());
        assertEquals(8, ((NonTrivialClass) manager.getStack().getSubject()).items.size());

    }

    /**
     * Test for command's chain
     * <p>Makes command chain without using macrocommands.
     */
    @Test
    public void chain() throws Exception {

        // TODO Make the same in KUndo

        {
            // Independently
            final int x = 10;
            final int y = 20;
            Point subj = new Point(x, y);
            UndoCommand parentCmd = new UndoCommand("parent", null);
            new FunctionalCommand<>("move 1", subj::getY, subj::setY, 50, parentCmd);
            new FunctionalCommand<>("move 2", subj::getX, subj::setX, 35, parentCmd);
            new FunctionalCommand<>("move 3", subj::getY, subj::setY, 55, parentCmd);
            new FunctionalCommand<>("move 4", subj::getX, subj::setX, 39, parentCmd);
            parentCmd.redo();
            assertEquals(39, subj.getX());
            assertEquals(55, subj.getY());

            parentCmd.undo();
            assertEquals(x, subj.getX());
            assertEquals(y, subj.getY());
        }

        {
            // In stack
            final int x = 10;
            final int y = 20;
            Point subj = new Point(x, y);
            UndoStack stack = new UndoStack(subj, null);
            UndoCommand parentCmd = new UndoCommand("parent", null);
            new FunctionalCommand<>("move 1", subj::getY, subj::setY, 50, parentCmd);
            new FunctionalCommand<>("move 2", subj::getX, subj::setX, 35, parentCmd);
            new FunctionalCommand<>("move 3", subj::getY, subj::setY, 55, parentCmd);
            new FunctionalCommand<>("move 4", subj::getX, subj::setX, 39, parentCmd);
            stack.push(parentCmd);
            assertEquals(39, subj.getX());
            assertEquals(55, subj.getY());

            stack.undo();
            assertEquals(x, subj.getX());
            assertEquals(y, subj.getY());
            stack.undo();
            assertEquals(x, subj.getX());
            assertEquals(y, subj.getY());


            stack.redo();
            assertEquals(39, subj.getX());
            assertEquals(55, subj.getY());
            stack.redo();
            assertEquals(39, subj.getX());
            assertEquals(55, subj.getY());
        }
    }


}
