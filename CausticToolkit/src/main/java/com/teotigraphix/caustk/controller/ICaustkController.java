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

import java.io.IOException;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.application.ICaustkConfiguration;
import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.controller.command.ICommandManager;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.library.ILibraryManager;
import com.teotigraphix.caustk.pattern.IPatternManager;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.sequencer.IQueueSequencer;
import com.teotigraphix.caustk.sequencer.ISongSequencer;
import com.teotigraphix.caustk.sequencer.ISystemSequencer;
import com.teotigraphix.caustk.sequencer.ITrackSequencer;
import com.teotigraphix.caustk.service.ISerializeService;
import com.teotigraphix.caustk.sound.ISoundGenerator;
import com.teotigraphix.caustk.sound.ISoundMixer;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.system.IMemoryManager;
import com.teotigraphix.caustk.system.ISystemState;

/**
 * @author Michael Schmalle
 */
public interface ICaustkController extends ICausticEngine {

    /**
     * Adds a controller component API.
     * 
     * @param clazz The class type API key.
     * @param instance The implementing instance of the class type.
     */
    void addComponent(Class<? extends IControllerComponent> clazz, IControllerComponent instance);

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
    <T extends IControllerComponent> T getComponent(Class<T> clazz);

    ICaustkApplication getApplication();

    ICaustkConfiguration getConfiguration();

    IDispatcher getDispatcher();

    ISerializeService getSerializeService();

    IProjectManager getProjectManager();

    ISoundGenerator getSoundGenerator();

    ISoundSource getSoundSource();

    ISoundMixer getSoundMixer();

    ILibraryManager getLibraryManager();

    ISystemSequencer getSystemSequencer();

    ITrackSequencer getTrackSequencer();

    ISongSequencer getSongSequencer();

    IQueueSequencer getQueueSequencer();

    ISystemState getSystemState();

    IPatternManager getPatternManager();

    IMemoryManager getMemoryManager();

    ICommandManager getCommandManager();

    void execute(String message, Object... args);

    <T> void register(Class<T> type, final EventObserver<T> observer);

    void undo();

    void redo();

    /**
     * Called to create all sub components of the {@link ICaustkController}.
     * 
     * @see ICaustkApplication#initialize()
     */
    void initialize();

    /**
     * Called to start all sub components of the {@link ICaustkController}.
     * 
     * @see ICaustkApplication#start()
     */
    void start();

    /**
     * Saves an sub components.
     * 
     * @see ICaustkApplication#save()
     * @throws IOException
     */
    void save() throws IOException;

    void close();
}
