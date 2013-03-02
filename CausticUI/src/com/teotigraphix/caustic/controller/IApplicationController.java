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

package com.teotigraphix.caustic.controller;

import android.content.SharedPreferences;

import com.google.inject.ImplementedBy;
import com.teotigraphix.android.components.support.MainLayout;
import com.teotigraphix.android.service.ITouchService;
import com.teotigraphix.caustic.activity.IApplicationRuntime;
import com.teotigraphix.caustic.internal.controller.application.ApplicationController;
import com.teotigraphix.caustic.router.IRouterClient;
import com.teotigraphix.caustic.song.IProject;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.common.IMemento;

@ImplementedBy(ApplicationController.class)
public interface IApplicationController extends IRouterClient {

    public static final String DEVICE_ID = "application";

    /**
     * Registers the {@link MainLayout} with the instance of the
     * {@link ITouchService}.
     */
    public static final String REGISTER_MAIN_LAYOUT = "register_main_layout";

    /**
     * Starts the workspace.
     * <p>
     * All {@link IApplicationRuntime} calls are dependent on the applications
     * implementation of it's own runtime.
     * <p>
     * It's up to the calling client to dispatch any events that signify the
     * {@link IWorkspace} is in a state of running.
     * <ul>
     * <li>Installs the application if not installed.</li>
     * <li>Loads the properties file <code>config.properties</code></li>
     * <li>Sets application root.</li>
     * <li>Installs the runtime.</li>
     * <li>Boots the runtime.</li>
     * <li>Runs the runtime.</li>
     * </ul>
     */
    public static final String START_WORKSPACE = "start_workspace";

    /**
     * Loads a project from the workspace.
     * <p>
     * <ul>
     * <li><strong>Message:</strong> /Controller/app/load_project
     * [load_project].</li>
     * <li><strong>Param0:</strong> absolute_path to save image.</li>
     * </ul>
     */
    public static final String LOAD_PROJECT = "load_project";

    /**
     * Restores the project's memento state after {@link #LOAD_PROJECT} has been
     * run.
     */
    public static final String RESTORE_PROJECT = "restore_project";

    /**
     * Saves a project's {@link IMemento} state to disk.
     * <p>
     * This will use the current {@link IProject#getFile()} for the location of
     * the save.
     */
    public static final String SAVE_PROJECT = "save_project";

    /**
     * Saves the current file as a new file on disk, while reloading the saved
     * file.
     * <ul>
     * <li><strong>param[0]</strong> the relative path from the project/
     * directory.</li>
     * </ul>
     */
    public static final String SAVE_PROJECT_AS = "save_project_as";

    /**
     * Calls a save on the workspace, allows the {@link IApplicationPreferences}
     * to save itself and it's clients in the {@link SharedPreferences} editor.
     */
    public static final String WORKSPACE_SAVE_QUICK = "workspace_save_quick";

    /**
     * Shuts the workspace down and saves ALL workspace, preference and project
     * state.
     */
    public static final String WORKSPACE_SHUTDOWN = "workspace_shutdown";
}
