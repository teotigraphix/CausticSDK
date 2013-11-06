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

package com.teotigraphix.caustk.controller;

import java.io.File;

import com.teotigraphix.caustk.controller.command.ICommandManager;
import com.teotigraphix.caustk.controller.core.CaustkController;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.rack.ISoundGenerator;
import com.teotigraphix.caustk.service.ISerializeService;
import com.teotigraphix.caustk.workstation.CaustkFactory;
import com.teotigraphix.caustk.workstation.ICaustkFactory;

/**
 * @author Michael Schmalle
 */
public interface ICaustkConfiguration {

    //--------------------------------------------------------------------------
    // Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // soundGenerator
    //----------------------------------

    /**
     * Returns the core {@link ICausticEngine} implementation for the desktop or
     * android device.
     * 
     * @return The single instance of the {@link ISoundGenerator}.
     */
    ISoundGenerator getSoundGenerator();

    /**
     * Sets the core {@link ICausticEngine} implementation for the desktop or
     * android device.
     * 
     * @param soundGenerator The main sound generator.
     */
    void setSoundGenerator(ISoundGenerator soundGenerator);

    //----------------------------------
    // application
    //----------------------------------

    ICaustkApplication getApplication();

    void setApplication(ICaustkApplication value);

    //----------------------------------
    // applicationId
    //----------------------------------

    /**
     * Returns the application id used for the root param of OSC messages.
     * <p>
     * All lowercase, no spaces, alpha numeric.
     */
    String getApplicationId();

    //----------------------------------
    // applicationTitle
    //----------------------------------

    /**
     * Returns the human readable application name with spaces.
     */
    String getApplicationTitle();

    /**
     * Sets the application's human readable name.
     * 
     * @param value The app name.
     */
    void setApplicationTitle(String value);

    //----------------------------------
    // applicationRoot
    //----------------------------------

    /**
     * Returns the native root of the application's directory.
     */
    File getApplicationRoot();

    /**
     * Set the application directory root.
     * <p>
     * This root uses the application name.
     * 
     * @param value A directory that will be created for the root of the
     *            application.
     */
    void setApplicationRoot(File value);

    //----------------------------------
    // causticStorage
    //----------------------------------

    /**
     * Returns the storage directory that holds the <code>caustic</code>
     * directory.
     */
    File getCausticStorage();

    /**
     * Sets the caustic storage root.
     * 
     * @param value A directory holding the <code>caustic</code> folder.
     */
    void setCausticStorage(File value);

    //--------------------------------------------------------------------------
    // Factory Methods
    //--------------------------------------------------------------------------

    ICaustkLogger createLogger();

    /**
     * The main {@link CaustkController} instance that instrumentates the whole
     * application sequencing from patterns, parts, presets, memory and all
     * other things needing controlling.
     */
    ICaustkController createController();

    /**
     * Creates the single {@link ISerializeService} for the application's
     * controller.
     * 
     * @return An instance of the {@link ISerializeService}
     */
    ISerializeService createSerializeService();

    /**
     * Creates the single {@link ICommandManager} for the application's
     * controller.
     * 
     * @return An instance of the {@link ICommandManager}
     */
    ICommandManager createCommandManager();

    /**
     * Creates the single {@link IProjectManager} for the application's
     * controller.
     * 
     * @return An instance of the {@link IProjectManager}
     */
    IProjectManager createProjectManager();

    /**
     * Creates the single {@link CaustkFactory} used to create all Caustk
     * components for the application.
     * 
     * @param application The {@link ICaustkApplication}
     */
    ICaustkFactory createFactory(ICaustkApplication application);

}
