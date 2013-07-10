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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustic.core.Dispatcher;
import com.teotigraphix.caustic.core.IDispatcher;
import com.teotigraphix.caustic.device.IDeviceFactory;
import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.application.ICaustkConfiguration;
import com.teotigraphix.caustk.controller.command.CommandManager;
import com.teotigraphix.caustk.controller.command.ICommand;
import com.teotigraphix.caustk.controller.command.ICommandManager;
import com.teotigraphix.caustk.controller.command.OSCMessage;
import com.teotigraphix.caustk.library.ILibraryManager;
import com.teotigraphix.caustk.library.LibraryManager;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.project.ISongManager;
import com.teotigraphix.caustk.project.ProjectManager;
import com.teotigraphix.caustk.project.SongManager;
import com.teotigraphix.caustk.sequencer.SystemSequencer;
import com.teotigraphix.caustk.service.ISerializeService;
import com.teotigraphix.caustk.service.SerializeService;
import com.teotigraphix.caustk.sound.CaustkSoundSource;
import com.teotigraphix.caustk.sound.ICaustkSoundGenerator;
import com.teotigraphix.caustk.sound.ICaustkSoundMixer;
import com.teotigraphix.caustk.sound.ICaustkSoundSource;
import com.teotigraphix.caustk.sound.SoundMixer;

/**
 * @author Michael Schmalle
 */
public class CaustkController implements ICaustkController {

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    private Map<Class<? extends IControllerAPI>, IControllerAPI> api = new HashMap<Class<? extends IControllerAPI>, IControllerAPI>();

    @Override
    public void registerAPI(Class<? extends IControllerAPI> clazz, IControllerAPI instance) {
        api.put(clazz, instance);
    }

    @Override
    public <T extends IControllerAPI> T api(Class<T> clazz) {
        return clazz.cast(api.get(clazz));
    }

    //----------------------------------
    // application
    //----------------------------------

    private ICaustkApplication application;

    @Override
    public ICaustkApplication getApplication() {
        return application;
    }

    //----------------------------------
    // applicationConfiguration
    //----------------------------------

    @Override
    public ICaustkConfiguration getConfiguration() {
        return application.getConfiguration();
    }

    //----------------------------------
    // dispatcher
    //----------------------------------

    private IDispatcher dispatcher;

    @Override
    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    //----------------------------------
    // factory
    //----------------------------------

    private IDeviceFactory factory;

    @Override
    public IDeviceFactory getFactory() {
        return factory;
    }

    //----------------------------------
    // projectManager
    //----------------------------------

    private IProjectManager projectManager;

    @Override
    public IProjectManager getProjectManager() {
        return projectManager;
    }

    //----------------------------------
    // serializeService
    //----------------------------------

    private ISerializeService serializeService;

    @Override
    public ISerializeService getSerializeService() {
        return serializeService;
    }

    //----------------------------------
    // songManager
    //----------------------------------

    private ISongManager songManager;

    @Override
    public ISongManager getSongManager() {
        return songManager;
    }

    //----------------------------------
    // libraryManager
    //----------------------------------

    private ILibraryManager libraryManager;

    @Override
    public ILibraryManager getLibraryManager() {
        return libraryManager;
    }

    //----------------------------------
    // commandManager
    //----------------------------------

    private ICommandManager commandManager;

    @Override
    public ICommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Executes an {@link ICommand} against a registered message.
     * 
     * @param message The message without the controller/applicationId.
     * @param args Arguments to pass to the created {@link OSCMessage} that will
     *            be created.
     * @see #sendOSCCommand(OSCMessage)
     */
    @Override
    public void execute(String message, Object... args) {
        commandManager.execute(message, args);
    }

    @Override
    public void undo() {
        commandManager.undo();
    }

    @Override
    public void redo() {
        commandManager.redo();
    }

    //----------------------------------
    // soundGenerator
    //----------------------------------

    private ICaustkSoundGenerator soundGenerator;

    @Override
    public ICaustkSoundGenerator getSoundGenerator() {
        return soundGenerator;
    }

    //----------------------------------
    // soundSource
    //----------------------------------

    private ICaustkSoundSource soundSource;

    @Override
    public ICaustkSoundSource getSoundSource() {
        return soundSource;
    }

    //----------------------------------
    // soundSource
    //----------------------------------

    private ICaustkSoundMixer soundMixer;

    @Override
    public ICaustkSoundMixer getSoundMixer() {
        return soundMixer;
    }

    //----------------------------------
    // systemSequencer
    //----------------------------------

    private SystemSequencer systemSequencer;

    @Override
    public SystemSequencer getSystemSequencer() {
        return systemSequencer;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Constructor, creates the {@link IDeviceFactory}, {@link IDispatcher}.
     * <p>
     * {@link #initialize()} creates all sub components of the
     * {@link ICaustkController}.
     * 
     * @param application The main application.
     */
    public CaustkController(ICaustkApplication application) {
        this.application = application;

        factory = getConfiguration().getDeviceFactory(this);
        dispatcher = new Dispatcher();
    }

    //--------------------------------------------------------------------------
    // ICausticEngine API
    //--------------------------------------------------------------------------

    // we proxy the actual OSC impl so we can stop, or reroute
    @Override
    public float sendMessage(String message) {
        return soundGenerator.sendMessage(message);
    }

    @Override
    public String queryMessage(String message) {
        return soundGenerator.queryMessage(message);
    }

    //--------------------------------------------------------------------------
    // ISystemController API
    //--------------------------------------------------------------------------

    @Override
    public void initialize() {
        File applicationRoot = getConfiguration().getApplicationRoot();
        if (!applicationRoot.exists())
            applicationRoot.mkdirs();

        // sub composites will add their ICommands in their constructors
        serializeService = new SerializeService(this);
        projectManager = new ProjectManager(this, applicationRoot);
        songManager = new SongManager(this, applicationRoot);
        libraryManager = new LibraryManager(this);
        commandManager = new CommandManager(this);

        soundGenerator = getConfiguration().createSoundGenerator(this);
        soundSource = new CaustkSoundSource(this);
        soundMixer = new SoundMixer(this);
        systemSequencer = new SystemSequencer(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void save() throws IOException {
        projectManager.save();
    }

    @Override
    public void close() {
        getDispatcher().trigger(new OnControllerSave());
        soundGenerator.close();
    }

}
