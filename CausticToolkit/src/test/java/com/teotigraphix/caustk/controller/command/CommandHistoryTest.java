
package com.teotigraphix.caustk.controller.command;

import java.util.ArrayList;
import java.util.List;

import org.androidtransfuse.event.EventObserver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.command.ICommandHistory.OnNextComplete;

public class CommandHistoryTest {

    public CommandHistory history;

    private MockUndoableCommand2 testCommand;

    private List<Integer> intList;

    private List<String> stringList;

    private ICaustkApplication application;

    private ICaustkController controller;

    private CommandContext context;

    @Before
    public void setUp() throws Exception {
        application = CaustkApplicationUtils.createAndRun();

        controller = application.getController();
        context = new CommandContext(controller, null);

        intList = new ArrayList<Integer>();
        stringList = new ArrayList<String>();

        history = new CommandHistory(controller.getDispatcher());
        testCommand = new MockUndoableCommand2();
        testCommand.setContext(context);
        testCommand.list = intList;
    }

    @After
    public void tearDown() throws Exception {
        testCommand = null;
    }

    //--------------------------------------------------------------------------
    // 
    //  IUndoableCommand Base :: Tests
    // 
    //--------------------------------------------------------------------------

    @Test
    public void test_execute() {
        testCommand.execute();
        Assert.assertEquals(1, intList.size());
    }

    @Test
    public void test_undo() throws Exception {
        testCommand.execute();
        Assert.assertEquals(1, intList.size());
        testCommand.undo();
        Assert.assertEquals(0, intList.size());
    }

    @Test
    public void test_executeMultiple() throws Exception {
        testCommand.execute();
        testCommand.execute();
        testCommand.execute();
        Assert.assertEquals(1, intList.size());
    }

    @Test
    public void test_undoMultiple() throws Exception {
        testCommand.execute();
        testCommand.undo();
        testCommand.undo();
        testCommand.undo();
        Assert.assertEquals(0, intList.size());
    }

    @Test
    public void test_undoNothing() throws Exception {
        testCommand.undo();
        Assert.assertEquals(0, intList.size());
    }

    @Test
    public void test_defaultUndoableCommand() throws Exception {
        Assert.assertNotNull(history);
        MockUndoableCommand command = new MockUndoableCommand();
        command.setContext(context);
        command.list = stringList;
        command.execute();
        Assert.assertEquals(1, stringList.size());
        command.undo();
        Assert.assertEquals(0, stringList.size());
    }

    static class MockUndoableCommand2 extends UndoCommand {

        public List<Integer> list;

        int count = 0;

        public String name;

        @Override
        protected void doExecute() {
            if (!isCanceled()) {
                list.add(Integer.valueOf(count++));
            }
        }

        @Override
        protected void undoExecute() {
            if (!isCanceled()) {
                list.remove(Integer.valueOf(--count));
            }
        }

        @Override
        public String toString() {
            return "[" + name + "]";
        }
    }

    static class MockUndoableCommand extends UndoCommandBase {

        public List<String> list = new ArrayList<String>();

        @Override
        protected void doExecute() {
            list.add("Foo");
        }

        @Override
        protected void undoExecute() {
            list.remove("Foo");
        }
    }

    //--------------------------------------------------------------------------
    // 
    //  IHistoryManager :: Tests
    // 
    //--------------------------------------------------------------------------

    // Test forward/back/position settings while
    // moving backwards & forwards
    public void test_getCurrentCommand() throws Exception {
        IUndoCommand foo = createCommand("foo");
        IUndoCommand bar = createCommand("bar");
        IUndoCommand baz = createCommand("baz");

        history.execute(foo);
        history.execute(bar);
        history.execute(baz);

        Assert.assertSame(baz, history.getCurrent());
        history.rewind(1);
        Assert.assertSame(bar, history.getCurrent());
        history.forward(1);
        Assert.assertSame(baz, history.getCurrent());
        history.rewind(1);
        history.rewind(1);
        Assert.assertSame(foo, history.getCurrent());
    }

    public void test_addToHistory() throws Exception {
        IUndoCommand foo = createCommand("foo");
        Assert.assertNull(history.getCurrent());

        history.execute(foo);
        Assert.assertSame(foo, history.getCurrent());
        Assert.assertEquals(1, intList.size());
    }

    public void test_commandUndo() throws Exception {
        IUndoCommand foo = createCommand("foo");
        Assert.assertNull(history.getCurrent());

        history.execute(foo);

        Assert.assertSame(foo, history.getCurrent());
        Assert.assertEquals(1, intList.size());

        history.rewind(1);
        Assert.assertNull(history.getCurrent());
        Assert.assertEquals(0, intList.size());
    }

    public void test_historyUndo() throws Exception {
        IUndoCommand foo = createCommand("foo");
        Assert.assertNull(history.getCurrent());

        history.execute(foo);
        Assert.assertSame(foo, history.getCurrent());
        Assert.assertEquals(1, intList.size());
        Assert.assertEquals(1, history.getCursor());

        history.rewind(1);
        Assert.assertEquals(0, history.getCursor());
        Assert.assertNull(history.getCurrent());
        Assert.assertEquals(0, intList.size());

        history.forward(1);
        Assert.assertSame(foo, history.getCurrent());
        Assert.assertEquals(1, intList.size());
        Assert.assertEquals(1, history.getCursor());
    }

    public void test_cancel() throws Exception {
        IUndoCommand foo = createCommand("foo");
        Assert.assertNull(history.getCurrent());
        foo.cancel();
        foo.execute();
        Assert.assertEquals(0, history.getCursor());
        Assert.assertNull(history.getCurrent());
        Assert.assertEquals(0, intList.size());
    }

    private IUndoCommand createCommand(String name) {
        MockUndoableCommand2 instance = new MockUndoableCommand2();
        instance.setContext(context);
        instance.name = name;
        instance.list = intList;
        return instance;
    }

    //--------------------------------------------------------------------------
    // 
    //  IHistoryManager Event :: Tests
    // 
    //--------------------------------------------------------------------------

    private IUndoCommand foo1;

    private IUndoCommand foo2;

    private IUndoCommand foo3;

    private IUndoCommand currentCommand;

    // OnStepForwardComplete
    @Test
    public void test_OnStepForwardComplete() throws Exception {
        foo1 = createCommand("Add pattern A1");
        foo2 = createCommand("Add pattern A2");
        foo3 = createCommand("Remove pattern A1");

        controller.getDispatcher().register(OnNextComplete.class,
                new EventObserver<OnNextComplete>() {
                    @Override
                    public void trigger(OnNextComplete object) {
                        if (currentCommand == null)
                            return;

                        Assert.assertSame(currentCommand, history.getCurrent());
                        Assert.assertSame(currentCommand, object.getCommand());
                    }
                });

        currentCommand = foo1;
        history.execute(foo1);
        currentCommand = foo2;
        history.execute(foo2);
        currentCommand = foo3;
        history.execute(foo3);

        currentCommand = null;
        history.rewind();
        history.forward();

        // this is the undo a user would use
        history.rewind(1);
        history.rewind(1);
        history.rewind(1);
    }

    // OnStepBackwardComplete

    // OnRewindComplete

    // OnFastForwardComplete

}
