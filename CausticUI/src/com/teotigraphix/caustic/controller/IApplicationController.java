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

import com.google.inject.ImplementedBy;
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

    public static final String REGISTER_MAIN_LAYOUT = "register_main_layout";

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

    public static final String RESTORE_PROJECT = "restore_project";

    /**
     * Saves a project's {@link IMemento} state to disk.
     * <p>
     * This will use the current {@link IProject#getFile()} for the location of
     * the save.
     */
    public static final String SAVE_PROJECT = "save_project";

}
