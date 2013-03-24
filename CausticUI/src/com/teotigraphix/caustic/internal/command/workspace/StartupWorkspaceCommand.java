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

package com.teotigraphix.caustic.internal.command.workspace;

import android.util.Log;

import com.google.inject.Inject;
import com.teotigraphix.caustic.controller.OSCMessage;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.internal.command.OSCCommandBase;
import com.teotigraphix.caustic.song.IWorkspace;

public class StartupWorkspaceCommand extends OSCCommandBase {

    @Inject
    IWorkspace workspace;

    @Override
    public void execute(OSCMessage message) {
        try {
            workspace.startAndRun();
        } catch (CausticException e) {
            Log.e("StartupWorkspaceCommand", "workspace.startAndRun()", e);
        }
    }
}
