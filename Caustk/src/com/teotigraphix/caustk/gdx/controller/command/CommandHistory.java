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

package com.teotigraphix.caustk.gdx.controller.command;

import com.google.common.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class CommandHistory implements ICommandHistory {

    private static final int NO_CURSOR = -1;

    private EventBus eventBus;

    private int currentCursor;

    private List<IUndoCommand> commands;

    @Override
    public int getCursor() {
        return currentCursor;
    }

    public List<IUndoCommand> getCommands() {
        return commands;
    }

    /**
     * Total number of items in history, irrespective of their undone/redone
     * state.
     * 
     * @return total number of items in history
     */
    @Override
    public int size() {
        return commands.size();
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
        if (commands.size() == 0 || currentCursor == 0) {
            return null;
        }
        return commands.get(currentCursor - 1);
    }

    public CommandHistory(EventBus eventBus) {
        this.eventBus = eventBus;
        commands = new ArrayList<IUndoCommand>();
        currentCursor = 0;
    }

    @Override
    public void clear() {
        commands.clear();
        currentCursor = 0;
        eventBus.post(new OnClearComplete());
    }

    /**
     * True if there's a command to redo.
     * 
     * @return true if there's a command to redo
     */
    private boolean hasNext() {
        return (currentCursor < size());
    }

    /**
     * True if there's a command to undo.
     * 
     * @return true if there's a command to undo
     */
    private boolean hasPrevious() {
        return (currentCursor > 0);
    }

    /**
     * Redo/execute the next command on the history stack.
     * 
     * @return position in history stack after this operation
     * @throws CommandExecutionException
     */
    private int next() throws CommandExecutionException {
        if (!hasNext())
            return currentCursor;

        commands.get(currentCursor).execute();
        if (commands.get(currentCursor).isCanceled())
            return currentCursor;

        currentCursor++;

        eventBus.post(new OnNextComplete(getCurrent()));

        return currentCursor;
    }

    private int previous() throws CommandExecutionException {
        if (!hasPrevious())
            return 0;

        commands.get(currentCursor - 1).undo();

        // If there's no undone command, 
        // dispatch null as the historyevent command
        IUndoCommand undoneCommand;
        if (commands.size() > 0 && currentCursor != 0) {
            //the undone command
            undoneCommand = commands.get(currentCursor - 1);
        } else {
            undoneCommand = null;
        }

        currentCursor--;

        eventBus.post(new OnPreviousComplete(undoneCommand));

        return currentCursor;
    }

    @Override
    public int rewind() throws CommandExecutionException {
        return rewind(0);
    }

    @Override
    public int rewind(int cursor) throws CommandExecutionException {
        int newCursor;

        if (cursor == 0) {
            newCursor = 0;
        } else {
            newCursor = currentCursor - cursor;
        }

        if (newCursor != NO_CURSOR) {
            // Move backward while possible
            while (hasPrevious() && currentCursor != newCursor) {
                previous();
            }

            eventBus.post(new OnRewindComplete(getCurrent()));
        } else {
            return NO_CURSOR;
        }

        return currentCursor;
    }

    @Override
    public int forward() throws CommandExecutionException {
        return forward(0);
    }

    @Override
    public int forward(int cursor) throws CommandExecutionException {
        int newCursor;

        if (cursor == 0) {
            newCursor = size();
        } else {
            newCursor = currentCursor + cursor;
        }

        // Move forward
        while (hasNext() && currentCursor != newCursor) {
            next();
        }

        eventBus.post(new OnFastForwardComplete(getCurrent()));
        return currentCursor;
    }

    @Override
    public boolean contains(IUndoCommand command) {
        return commands.contains(command);
    }

    @Override
    public int execute(IUndoCommand command) throws CommandExecutionException {
        // this will happen when there is a rewind half way through
        // and then a new command is pushed which wipes the head
        // of the stack out down to the currentPosition
        ArrayList<IUndoCommand> copy = new ArrayList<IUndoCommand>(commands);
        if (currentCursor != size()) {
            commands = commands.subList(0, currentCursor);
        }

        commands.add(command);

        // Execute the command & move pointer forward
        // if there is an Exception throw, the cursor has not been
        // touched yet so bailing right now is safe
        next();

        if (command.isCanceled()) {
            copy.remove(command);
            commands = copy;
        }

        return currentCursor;
    }

    @Override
    public String toString() {
        String output = "";
        int count = 0;
        for (IUndoCommand command : commands) {
            output += count + "" + command + "\n";
            count++;
        }
        return output;
    }
}
