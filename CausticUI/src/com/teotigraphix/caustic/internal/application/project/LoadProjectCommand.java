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

package com.teotigraphix.caustic.internal.application.project;

import java.io.File;

import com.google.inject.Inject;
import com.teotigraphix.caustic.controller.IApplicationController;
import com.teotigraphix.caustic.controller.OSCMessage;
import com.teotigraphix.caustic.internal.command.OSCCommandBase;
import com.teotigraphix.caustic.song.IWorkspace;

/**
 * Loads a project from the workspace.
 * <p>
 * <strong>Param0:</strong> absolute_path.
 * 
 * @see IApplicationController#COMMAND_LOAD_PROJECT
 * @see RestoreProjectCommand
 */
public class LoadProjectCommand extends OSCCommandBase {

    @Inject
    IWorkspace workspace;

    @Override
    public void execute(OSCMessage message) {
        // will NOT load the initial memento state of the project XML,
        // only loads the project into the workspace
        String absolutePath = message.getParameters().get(0);
        workspace.loadProject(new File(absolutePath));
    }
}
