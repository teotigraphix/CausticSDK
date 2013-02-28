////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustic.internal.command;

import java.util.ArrayList;
import java.util.List;

import roboguice.event.EventManager;
import roboguice.inject.ContextSingleton;

import com.google.inject.Inject;
import com.teotigraphix.caustic.command.ICommandHistory;
import com.teotigraphix.caustic.command.IUndoCommand;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
@ContextSingleton
public class CommandHistory implements ICommandHistory {

    @Inject
    EventManager eventManager;

    private int mCursor;

    int getCurosr() {
        return mCursor;
    }

    private List<IUndoCommand> mCommands;

    /**
     * Total number of items in history, irrespective of their undone/redone
     * state.
     * 
     * @return total number of items in history
     */
    @Override
    public int size() {
        return mCommands.size();
    }

    /**
     * Gets the command at the top of the history stack. This command will have
     * already been executed.
     * 
     * @return command at the current position in the history stack, or null if
     *         we're at position 0, in this case, there may be no commands on
     *         the stack at all.
     * @see currentPosition
     * @see numberOfHistoryItems
     */
    @Override
    public IUndoCommand getCurrent() {
        if (mCommands.size() == 0 || mCursor == 0) {
            return null;
        }
        return mCommands.get(mCursor - 1);
    }

    public CommandHistory() {
        mCommands = new ArrayList<IUndoCommand>();
        mCursor = 0;
    }

    @Override
    public void clear() {
        mCommands.clear();
        mCursor = 0;
        eventManager.fire(new OnClearComplete());
    }

    /**
     * True if there's a command to redo.
     * 
     * @return true if there's a command to redo
     */
    private boolean hasNext() {
        return (mCursor < size());
    }

    /**
     * True if there's a command to undo.
     * 
     * @return true if there's a command to undo
     */
    private boolean hasPrevious() {
        return (mCursor > 0);
    }

    /**
     * Redo/execute the next command on the history stack.
     * 
     * @return position in history stack after this operation
     */
    private int next() {
        if (!hasNext())
            return mCursor;

        mCommands.get(mCursor).execute();
        mCursor++;

        eventManager.fire(new OnNextComplete(getCurrent()));

        return mCursor;
    }

    private int previous() throws Exception {
        if (!hasPrevious())
            return 0;

        mCommands.get(mCursor - 1).undo();
        mCursor--;

        // If there's no undone command, 
        // dispatch null as the historyevent command
        IUndoCommand undoneCommand;
        if (mCommands.size() > 0 && mCursor != 0) {
            //the undone command
            undoneCommand = mCommands.get(mCursor);
        } else {
            undoneCommand = null;
        }

        eventManager.fire(new OnPreviousComplete(undoneCommand));

        return mCursor;
    }

    @Override
    public int rewind() throws Exception {
        return rewind(0);
    }

    @Override
    public int rewind(int cursor) throws Exception {
        int newCurosr;

        if (cursor == 0) {
            newCurosr = 0;
        } else {
            newCurosr = mCursor - cursor;
        }

        // Move backward while possible
        while (hasPrevious() && mCursor != newCurosr) {
            previous();
        }

        eventManager.fire(new OnRewindComplete(getCurrent()));

        return mCursor;
    }

    @Override
    public int forward() {
        return forward(0);
    }

    @Override
    public int forward(int cursor) {
        int newCursor;

        if (cursor == 0) {
            newCursor = size();
        } else {
            newCursor = mCursor + cursor;
        }

        // Move forward
        while (hasNext() && mCursor != newCursor) {
            next();
        }

        eventManager.fire(new OnFastForwardComplete(getCurrent()));
        return mCursor;
    }

    @Override
    public boolean contains(IUndoCommand command) {
        return mCommands.contains(command);
    }

    @Override
    public int execute(IUndoCommand command) {
        // this will happen when there is a rewind half way through
        // and then a new command is pushed which wipes the head
        // of the stack out down to the currentPosition
        if (mCursor != size()) {
            mCommands = mCommands.subList(0, mCursor);
        }

        mCommands.add(command);

        // Execute the command & move pointer forward
        // if there is an Exception throw, the cursor has not been
        // touched yet so bailing right now is safe
        next();

        return mCursor;
    }

    @Override
    public String toString() {
        String output = "";
        int count = 0;
        for (IUndoCommand command : mCommands) {
            output += count + "" + command + "\n";
            count++;
        }
        return output;
    }
}
