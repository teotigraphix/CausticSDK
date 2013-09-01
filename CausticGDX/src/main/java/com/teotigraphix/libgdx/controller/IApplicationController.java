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

package com.teotigraphix.libgdx.controller;

import java.io.File;
import java.io.IOException;

import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkConfiguration;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.libgdx.model.IApplicationModel;
import com.teotigraphix.libgdx.model.ICaustkModel;

public interface IApplicationController {

    /**
     * Returns the single instance of the {@link ICaustkController}.
     */
    ICaustkController getController();

    /**
     * Loads {@link ICaustkModel} state after the {@link Project} has been
     * deserialized.
     */
    void load();

    /**
     * Starts the {@link ICaustkApplication}.
     * <p>
     * Sets the caustic and application root directory.
     * <p>
     * Initializes and starts the {@link ICaustkApplication}.
     * <p>
     * Starts the {@link IApplicationModel#start()}.
     * <p>
     * Creates or loads the lastProject.
     * 
     * @see ICaustkConfiguration#setApplicationRoot(File)
     * @see ICaustkConfiguration#setCausticStorage(File)
     * @see ICaustkApplication#initialize()
     * @see IProjectManager#initialize(File)
     * @see ICaustkApplication#start()
     * @see IProjectManager#load(File)
     * @throws IOException
     */
    void start() throws IOException;

    void show();

    /**
     * Registers a model against the application controller.
     * <p>
     * Clients do not call this method, all registration happens automatically.
     * 
     * @param model The model to register.
     */
    void registerModel(ICaustkModel model);

    /**
     * Calls {@link ICaustkModel#onRegister()} on all registered models.
     * <p>
     * Main application calls this method in the startup sequence.
     */
    void registerModels();

    /**
     * Registers a mediator against the application controller.
     * <p>
     * Clients do not call this method, all registration happens automatically.
     * 
     * @param mediator The mediator to register.
     */
    void registerMeditor(ICaustkMediator mediator);

    /**
     * Calls {@link ICaustkMediator#onRegisterObservers()} on all registered
     * mediators.
     * <p>
     * Main application calls this method in the startup sequence.
     */
    void registerMediatorObservers();

    /**
     * Calls {@link ICaustkMediator#onRegister()} on all registered mediators.
     * <p>
     * Main application calls this method in the startup sequence.
     */
    void registerMeditors();

}
