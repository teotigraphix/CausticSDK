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
import java.util.HashMap;
import java.util.Map;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.ICaustkLogger;
import com.teotigraphix.caustk.controller.IControllerAware;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.controller.command.ICommand;
import com.teotigraphix.caustk.controller.command.ICommandManager;
import com.teotigraphix.caustk.controller.command.OSCMessage;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.service.ISerializeService;
import com.teotigraphix.caustk.workstation.ICaustkFactory;
import com.teotigraphix.caustk.workstation.RackSet;

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

    @Override
    public IDispatcher getDispatcher() {
        return dispatcher;
    }

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
    // logger
    //----------------------------------

    @Override
    public ICaustkLogger getLogger() {
        return application.getLogger();
    }

    //----------------------------------
    // factory
    //----------------------------------

    @Override
    public ICaustkFactory getFactory() {
        return application.getFactory();
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
    // rack
    //----------------------------------

    @Override
    public final IRack getRack() {
        return application.getRack();
    }

    @Override
    public final RackSet getRackSet() {
        return application.getRack().getRackSet();
    }

    //----------------------------------
    // commandManager
    //----------------------------------

    private ICommandManager commandManager;

    public ICommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public void put(String message, Class<? extends ICommand> command) {
        commandManager.put(message, command);
    }

    @Override
    public void remove(String message) {
        commandManager.remove(message);
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
    public void execute(String message, Object... args) throws CausticException {
        commandManager.execute(message, args);
    }

    @Override
    public void undo() throws CausticException {
        commandManager.undo();
    }

    @Override
    public void redo() throws CausticException {
        commandManager.redo();
    }

    @Override
    public void clearHistory() {
        commandManager.clearHistory();
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

    private Map<Class<?>, Object> api = new HashMap<Class<?>, Object>();

    @Override
    public void addComponent(Class<?> clazz, Object component) {
        getLogger().log("CaustkController", clazz + " added to CaustkController");
        api.put(clazz, component);
        if (component instanceof IControllerAware)
            ((IControllerAware)component).onAttach(this);
    }

    @Override
    public Object removeComponent(Class<?> clazz) {
        Object component = api.remove(clazz);
        if (component instanceof IControllerAware)
            ((IControllerAware)component).onDetach();
        return component;
    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
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
        return getRack().sendMessage(message);
    }

    @Override
    public final String queryMessage(String message) {
        return getRack().queryMessage(message);
    }

    //--------------------------------------------------------------------------
    // ISystemController API
    //--------------------------------------------------------------------------

    @Override
    public void initialize() {
        getLogger().log("CaustkController", "Initialize app root dir if not created");
        File applicationRoot = application.getConfiguration().getApplicationRoot();
        if (!applicationRoot.exists())
            applicationRoot.mkdirs();

        getLogger().log("CaustkController", "Create all Sub components");

        // sub composites will add their ICommands in their constructors
        serializeService = application.getConfiguration().createSerializeService();
        commandManager = application.getConfiguration().createCommandManager();
        projectManager = application.getConfiguration().createProjectManager();
    }

    @Override
    public void create() {
        // all controller component's onAttatch() are called
        addComponent(ISerializeService.class, serializeService);
        addComponent(ICommandManager.class, commandManager);
        addComponent(IProjectManager.class, projectManager);
    }

    @Override
    public void run() {
    }

    @Override
    public void frameChanged(float delta) {
        getRack().frameChanged(delta);
    }

    @Override
    public void save() throws IOException {
        getLogger().log("CaustkController", "Save");
        projectManager.save();
    }

    @Override
    public void close() {
        getLogger().log("CaustkController", "Close");
    }

    //--------------------------------------------------------------------------
    // IActivityCycle API
    //--------------------------------------------------------------------------

    @Override
    public void onStart() {
        getRack().onStart();
    }

    @Override
    public void onResume() {
        getRack().onResume();
    }

    @Override
    public void onPause() {
        getRack().onPause();
    }

    @Override
    public void onStop() {
        getRack().onStop();
    }

    @Override
    public void onDestroy() {
        getRack().onDestroy();
    }

    @Override
    public void onRestart() {
        getRack().onRestart();
    }

    @Override
    public void dispose() {
        getRack().dispose();
    }
}
