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
import com.teotigraphix.caustk.controller.core.Rack;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.library.ILibraryManager;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.sequencer.IQueueSequencer;
import com.teotigraphix.caustk.service.ISerializeService;

/**
 * @author Michael Schmalle
 */
public interface ICaustkController extends ICausticEngine, IDispatcher {

    /**
     * The controller's internal dispatcher.
     */
    IDispatcher getDispatcher();

    /**
     * Adds a controller component API.
     * 
     * @param clazz The class type API key.
     * @param instance The implementing instance of the class type.
     */
    void addComponent(Class<?> clazz, Object instance);

    /**
     * Returns a registered API controller component.
     * 
     * <pre>
     * ISoundSourceAPI api = context.getComponent(ISoundSourceAPI.class);
     * api.setMasterVolume(0.5f);
     * </pre>
     * 
     * @param clazz The class type API key.
     */
    <T> T getComponent(Class<T> clazz);

    /**
     * Returns the top level application created at startup.
     */
    ICaustkApplication getApplication();

    File getApplicationRoot();

    String getApplicationId();

    ICausticLogger getLogger();

    ISerializeService getSerializeService();

    IProjectManager getProjectManager();

    Rack getRack();

    void setRack(Rack value);

    ILibraryManager getLibraryManager();

    IQueueSequencer getQueueSequencer();

    ICommandManager getCommandManager();

    void undo();

    void redo();

    void execute(String message, Object... args);

    void update();

}