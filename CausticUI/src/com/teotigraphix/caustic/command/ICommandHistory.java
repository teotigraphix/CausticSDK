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

package com.teotigraphix.caustic.command;

import com.teotigraphix.caustic.internal.command.CommandHistory;

/**
 * A command history that manages undoable commands, this API is not
 * asynchronous.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ICommandHistory {

    /**
     * Returns the current {@link ICommand} in the stack.
     * <p>
     * This is not necessarily the head of the stack since rewind() and
     * fastforward() can be used.
     * </p>
     */
    IUndoCommand getCurrent();

    /**
     * Whether the stack contains the actual {@link ICommand} instance.
     * 
     * @param command The command instance to test.
     */
    boolean contains(IUndoCommand command);

    /**
     * Clears the entire stack of commands.
     * 
     * @see OnClearComplete
     */
    void clear();

    /**
     * Pushes a {@link ICommand} instance onto the head of the stack and calls
     * {@link ICommand#execute()}.
     * <p>
     * If the cursor is not at the head command in the history list, all
     * commands from the cursor to the head will be sliced off and the command
     * executing will become the new current command.
     * </p>
     * 
     * @param command The command instance.
     */
    int execute(IUndoCommand command);

    /**
     * Fast forwards all commands back to the <code>cursor</code> calling
     * execute() on them.
     * 
     * @throws Exception
     */
    int forward(int cursor);

    /**
     * Fast forwards all commands back to the head calling execute() on them.
     * 
     * @throws Exception
     */
    int forward();

    /**
     * Rewinds all commands back to <code>cursor</code> calling undo() on them.
     * 
     * @throws Exception
     */
    int rewind(int cursor) throws Exception;

    /**
     * Rewinds all commands back to 0 calling undo() on them.
     * 
     * @throws Exception
     */
    int rewind() throws Exception;

    /**
     * The number of commands existing in the history stack.
     */
    int size();

    public static class HistoryEvent {

        private IUndoCommand command;

        public IUndoCommand getCommand() {
            return command;
        }

        public HistoryEvent(IUndoCommand command) {
            this.command = command;
        }
    }

    /**
     * @see ICommandHistory#clear()
     */
    public static class OnClearComplete extends HistoryEvent {
        public OnClearComplete() {
            super(null);
        }
    }

    /**
     * @see ICommandHistory#next()
     */
    public static class OnNextComplete extends HistoryEvent {
        public OnNextComplete(IUndoCommand command) {
            super(command);
        }
    }

    /**
     * @see ICommandHistory#previous()
     */
    public static class OnPreviousComplete extends HistoryEvent {
        public OnPreviousComplete(IUndoCommand command) {
            super(command);
        }
    }

    /**
     * @see CommandHistory#rewind()
     */
    public static class OnRewindComplete extends HistoryEvent {
        public OnRewindComplete(IUndoCommand command) {
            super(command);
        }
    }

    /**
     * @see CommandHistory#forward()
     */
    public static class OnFastForwardComplete extends HistoryEvent {
        public OnFastForwardComplete(IUndoCommand command) {
            super(command);
        }
    }
}
