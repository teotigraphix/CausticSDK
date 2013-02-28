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

import com.google.inject.Inject;
import com.teotigraphix.caustic.command.ICommandHistory;
import com.teotigraphix.caustic.controller.IOSCAware;
import com.teotigraphix.caustic.controller.OSCMessage;

/**
 * This command handles adding itself to the provided/injected CommandHistory.
 * UndoableCommands are pushed to the CommandHistory only once the Command has
 * been executed, and are removed once the Command has been undone. Undoable
 * commands can be cancelled by calling cancel() from within their doExecute()
 * function, and they will not be added to the history. All functions assume the
 * CommandHistory dependency has been provided as the public property 'history'.
 */
public abstract class UndoCommand extends UndoCommandBase implements IOSCAware {

    @Inject
    ICommandHistory history;

    protected boolean isCancelled = false;

    private boolean hasRegisteredWithHistory;

    protected boolean hasSteppedBack = false;

    //--------------------------------------------------------------------------
    // 
    //  IOSCAware API :: Methods
    // 
    //--------------------------------------------------------------------------

    private OSCMessage mMessage;

    @Override
    public OSCMessage getMessage() {
        return mMessage;
    }

    @Override
    public void setMessage(OSCMessage value) {
        mMessage = value;
        commitState();
    }

    /**
     * All injected members are guaranteed to be set when this commit is called.
     * <p>
     * Setup all persistent state that is needed for undo here.
     * </p>
     */
    protected void commitState() {
    }

    public UndoCommand() {
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    @Override
    protected abstract void doExecute();

    @Override
    protected abstract void undoExecute();

    /**
     * @private Checks if this command has added itself to the command history.
     *          Ensures we don't let this Command push to the CommandHistory
     *          more than once.
     */
    @SuppressWarnings("unused")
    private void registerIfRequired() {
        if (!hasRegisteredWithHistory) {
            hasRegisteredWithHistory = true;
            history.execute(this);
        }
    }

    public final int getInteger(int index) {
        return Integer.parseInt(mMessage.getParameters().get(index));
    }

    public final float getFloat(int index) {
        return Float.parseFloat(mMessage.getParameters().get(index));
    }

    public final String getString(int index) {
        return mMessage.getParameters().get(index);
    }
}
