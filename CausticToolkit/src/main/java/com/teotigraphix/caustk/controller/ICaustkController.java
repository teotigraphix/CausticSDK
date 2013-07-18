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

import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.application.ICaustkConfiguration;
import com.teotigraphix.caustk.application.IDeviceFactory;
import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.service.ISerializeService;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.sound.ISoundGenerator;

/**
 * @author Michael Schmalle
 */
public interface ICaustkController extends ICausticEngine {

    /**
     * @param clazz
     * @param instance
     */
    void addComponent(Class<? extends IControllerComponent> clazz, IControllerComponent instance);

    /**
     * <pre>
     * ISoundSourceAPI api = context.api().get(ISoundSourceAPI.class);
     * api.setMasterVolume(0.5f);
     * </pre>
     * 
     * @param clazz
     */
    <T extends IControllerComponent> T getComponent(Class<T> clazz);

    ICaustkApplication getApplication();

    ICaustkConfiguration getConfiguration();

    IDispatcher getDispatcher();

    IDeviceFactory getDeviceFactory();

    ISerializeService getSerializeService();

    ISoundGenerator getSoundGenerator();

    ISoundSource getSoundSource();

    IProjectManager getProjectManager();

    //    ISongManager getSongManager();

    //    ILibraryManager getLibraryManager();

    //    ICaustkSoundMixer getSoundMixer();

    //    SystemSequencer getSystemSequencer();

    //    ICommandManager getCommandManager();

    //    void execute(String message, Object... args);
    //
    //    void undo();
    //
    //    void redo();

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

    //    public class OnControllerSave {
    //
    //    }
    //
    //    public class OnApplicationClose {
    //
    //    }

}
