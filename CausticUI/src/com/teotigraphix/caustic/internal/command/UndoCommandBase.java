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

package com.teotigraphix.caustic.internal.command;

import roboguice.event.EventManager;

import com.google.inject.Inject;
import com.teotigraphix.caustic.command.CommandExecutionException;
import com.teotigraphix.caustic.command.IUndoCommand;

public abstract class UndoCommandBase implements IUndoCommand {

    /**
     * Keeps track of whether this command has been executed, to prevent undoing
     * commands that have not been yet been executed.
     */
    protected boolean mHasExectued = false;

    @Inject
    EventManager eventManager;

    public UndoCommandBase() {
    }

    @Override
    public void execute() {
        if (!mHasExectued) {
            try {
                doExecute();
            } catch (Exception e) {
                throw new CommandExecutionException("Problem with execute()", e);
            }
            mHasExectued = true;
            eventManager.fire(new OnExecuteComplete(this));
        }
    }

    /**
     * Subclasses must override this function.
     * <p>
     * This function should undo whatever the doExecute command did.
     * </p>
     */
    abstract protected void doExecute();

    @Override
    public void undo() {
        if (mHasExectued) {
            try {
                undoExecute();
            } catch (Exception e) {
                throw new CommandExecutionException("Problem with undo()", e);
            }
            mHasExectued = false;
            eventManager.fire(new OnUndoExecuteComplete(this));
        }
    }

    @Override
    public void cancel() {
    }

    abstract protected void undoExecute();

}
