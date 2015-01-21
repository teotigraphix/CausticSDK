////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.gdx.app.controller.command;

public interface IUndoCommand extends ICommand {

    String getLabel();

    /**
     * Reverse the performed action
     * 
     * @throws CommandExecutionException An exception occurred during undo.
     */
    void undo() throws CommandExecutionException;

    boolean isCanceled();

    void cancel();

    public static class CommandEvent {

        public IUndoCommand command;

        public CommandEvent(IUndoCommand command) {
            this.command = command;
        }
    }

    public static class OnExecuteComplete extends CommandEvent {
        public OnExecuteComplete(IUndoCommand command) {
            super(command);
        }
    }

    public static class OnUndoExecuteComplete extends CommandEvent {
        public OnUndoExecuteComplete(IUndoCommand command) {
            super(command);
        }
    }

    public static class OnExecuteCanceled extends CommandEvent {
        public OnExecuteCanceled(IUndoCommand command) {
            super(command);
        }
    }

}
