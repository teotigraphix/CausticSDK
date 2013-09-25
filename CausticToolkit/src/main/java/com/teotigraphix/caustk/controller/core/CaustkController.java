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

package com.teotigraphix.caustk.controller.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IControllerComponent;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.controller.command.CommandManager;
import com.teotigraphix.caustk.controller.command.ICommand;
import com.teotigraphix.caustk.controller.command.ICommandManager;
import com.teotigraphix.caustk.controller.command.OSCMessage;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.library.ILibraryManager;
import com.teotigraphix.caustk.library.core.LibraryManager;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.project.ProjectManager;
import com.teotigraphix.caustk.sequencer.IQueueSequencer;
import com.teotigraphix.caustk.sequencer.ISystemSequencer;
import com.teotigraphix.caustk.sequencer.ITrackSequencer;
import com.teotigraphix.caustk.sequencer.queue.QueueSequencer;
import com.teotigraphix.caustk.sequencer.system.SystemSequencer;
import com.teotigraphix.caustk.sequencer.track.TrackSequencer;
import com.teotigraphix.caustk.service.ISerializeService;
import com.teotigraphix.caustk.service.serialize.SerializeService;
import com.teotigraphix.caustk.sound.ISoundGenerator;
import com.teotigraphix.caustk.sound.ISoundMixer;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.sound.mixer.SoundMixer;
import com.teotigraphix.caustk.sound.source.SoundSource;

/**
 * @author Michael Schmalle
 */
public class CaustkController implements ICaustkController {

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // dispatcher
    //----------------------------------

    private final IDispatcher dispatcher;

    //----------------------------------
    // application
    //----------------------------------

    private ICaustkApplication application;

    @Override
    public final ICaustkApplication getApplication() {
        return application;
    }

    @Override
    public final String getApplicationId() {
        return application.getConfiguration().getApplicationId();
    }

    @Override
    public final File getApplicationRoot() {
        return application.getConfiguration().getApplicationRoot();
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
    // trackSequencer
    //----------------------------------

    private ITrackSequencer trackSequencer;

    @Override
    public ITrackSequencer getTrackSequencer() {
        return trackSequencer;
    }

    //----------------------------------
    // queueSequencer
    //----------------------------------

    private IQueueSequencer queueSequencer;

    @Override
    public IQueueSequencer getQueueSequencer() {
        return queueSequencer;
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
    // soundGenerator
    //----------------------------------

    private ISoundGenerator soundGenerator;

    @Override
    public ISoundGenerator getSoundGenerator() {
        return soundGenerator;
    }

    //----------------------------------
    // soundSource
    //----------------------------------

    private ISoundSource soundSource;

    @Override
    public ISoundSource getSoundSource() {
        return soundSource;
    }

    //----------------------------------
    // soundSource
    //----------------------------------

    private ISoundMixer soundMixer;

    @Override
    public ISoundMixer getSoundMixer() {
        return soundMixer;
    }

    //----------------------------------
    // systemSequencer
    //----------------------------------

    private ISystemSequencer systemSequencer;

    @Override
    public ISystemSequencer getSystemSequencer() {
        return systemSequencer;
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

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Constructor, creates the {@link IDeviceFactory}, {@link IDispatcher}.
     * <p>
     * {@link #create()} creates all sub components of the
     * {@link ICaustkController}.
     * 
     * @param application The main application.
     */
    public CaustkController(ICaustkApplication application) {
        this.application = application;

        dispatcher = new Dispatcher();
    }

    //----------------------------------
    // ControllerComponents
    //----------------------------------

    private Map<Class<? extends IControllerComponent>, IControllerComponent> api = new HashMap<Class<? extends IControllerComponent>, IControllerComponent>();

    private List<IControllerComponent> components = new ArrayList<IControllerComponent>();

    @Override
    public void addComponent(Class<? extends IControllerComponent> clazz,
            IControllerComponent instance) {
        api.put(clazz, instance);
    }

    @Override
    public <T extends IControllerComponent> T getComponent(Class<T> clazz) {
        return clazz.cast(api.get(clazz));
    }

    //--------------------------------------------------------------------------
    // IDispatcher API
    //--------------------------------------------------------------------------

    @Override
    public <T> void register(Class<T> event, EventObserver<T> observer) {
        dispatcher.register(event, observer);
    }

    @Override
    public void unregister(EventObserver<?> observer) {
        dispatcher.unregister(observer);
    }

    @Override
    public void trigger(Object event) {
        dispatcher.trigger(event);
    }

    @Override
    public void clear() {
        dispatcher.clear();
    }

    //--------------------------------------------------------------------------
    // ICausticEngine API
    //--------------------------------------------------------------------------

    // we proxy the actual OSC impl so we can stop, or reroute
    @Override
    public final float sendMessage(String message) {
        return soundGenerator.sendMessage(message);
    }

    @Override
    public final String queryMessage(String message) {
        return soundGenerator.queryMessage(message);
    }

    //--------------------------------------------------------------------------
    // ISystemController API
    //--------------------------------------------------------------------------

    void create() {
        CtkDebug.log("CaustkController: Create app root dir if not created");
        File applicationRoot = application.getConfiguration().getApplicationRoot();
        if (!applicationRoot.exists())
            applicationRoot.mkdirs();

        CtkDebug.log("CaustkController: Create all Sub components");
        soundGenerator = application.getConfiguration().getSoundGenerator();
        soundGenerator.initialize();

        // sub composites will add their ICommands in their constructors

        serializeService = new SerializeService(this);

        commandManager = new CommandManager(this);
        projectManager = new ProjectManager(this);
        libraryManager = new LibraryManager(this);

        soundSource = new SoundSource(this);
        soundMixer = new SoundMixer(this);

        systemSequencer = new SystemSequencer(this);
        trackSequencer = new TrackSequencer(this);
        queueSequencer = new QueueSequencer(this);

        components.add(libraryManager);
        components.add(trackSequencer);
        components.add(soundMixer);
        components.add(systemSequencer);
        components.add(queueSequencer);

        for (IControllerComponent component : components) {
            component.onRegister();
        }

        projectManager.initialize();
    }

    void save() throws IOException {
        CtkDebug.log("CaustkController: Save");
        projectManager.save();
    }

    void close() {
        CtkDebug.log("CaustkController: Close");
        soundGenerator.close();
    }

    //--------------------------------------------------------------------------
    // IActivityCycle API
    //--------------------------------------------------------------------------

    @Override
    public void onStart() {
        soundGenerator.onStart();
    }

    @Override
    public void onResume() {
        soundGenerator.onResume();
    }

    @Override
    public void onPause() {
        soundGenerator.onPause();
    }

    @Override
    public void onStop() {
        soundGenerator.onStop();
    }

    @Override
    public void onDestroy() {
        soundGenerator.onDestroy();
    }

    @Override
    public void onRestart() {
        soundGenerator.onRestart();
    }

    @Override
    public void dispose() {
        soundGenerator.dispose();
    }
}
