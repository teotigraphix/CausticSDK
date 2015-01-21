////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.gdx.app;

import com.badlogic.gdx.Gdx;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.teotigraphix.caustk.gdx.app.controller.IPreferenceManager;
import com.teotigraphix.caustk.gdx.app.controller.command.ICommandManager;
import com.teotigraphix.caustk.gdx.app.ui.IScene;
import com.teotigraphix.caustk.gdx.app.ui.SceneManager;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public abstract class Application implements IApplication {

    private boolean dirty = false;

    //----------------------------------
    // dirty
    //----------------------------------

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty() {
        setDirty(true);
    }

    protected void setDirty(boolean dirty) {
        if (dirty == this.dirty)
            return;
        this.dirty = dirty;
        getEventBus().post(new ApplicationEvent(this, ApplicationEventKind.IsDirtyChange));
    }

    //===============

    private static final String TAG = "Application";

    public static boolean TEST = false;

    //--------------------------------------------------------------------------
    // Inject :: Variables
    //--------------------------------------------------------------------------

    @Inject
    private ICommandManager commandManager;

    @Inject
    private IPreferenceManager preferenceManager;

    //----------------------------------
    // ICommandManager
    //----------------------------------

    public ICommandManager getCommandManager() {
        return commandManager;
    }

    protected IPreferenceManager getPreferenceManager() {
        return preferenceManager;
    }

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private String applicationId;

    private String applicationName;

    private int width;

    private int height;

    private EventBus eventBus;

    private SceneManager sceneManager;

    private int initialScene;

    //--------------------------------------------------------------------------
    // IApplication API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // applicationName
    //----------------------------------

    @Override
    public String getApplicationId() {
        return applicationId;
    }

    protected void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    //----------------------------------
    // applicationName
    //----------------------------------

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    //----------------------------------
    // width
    //----------------------------------

    @Override
    public float getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    //----------------------------------
    // height
    //----------------------------------

    @Override
    public float getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    //----------------------------------
    // eventBus
    //----------------------------------

    @Override
    public final EventBus getEventBus() {
        return eventBus;
    }

    //----------------------------------
    // scene
    //----------------------------------

    @Override
    public IScene getScene() {
        return getSceneManager().getScene();
    }

    @Override
    public void setScene(int sceneId) {
        getSceneManager().setScene(sceneId);
    }

    @Override
    public boolean isCurrentScene(int sceneId) {
        return getSceneManager().isCurrentScene(sceneId);
    }

    //----------------------------------
    // initialScene
    //----------------------------------

    protected int getInitialScene() {
        return initialScene;
    }

    protected void setInitialScene(int initialScene) {
        this.initialScene = initialScene;
    }

    //--------------------------------------------------------------------------
    // Protected :: Properties
    //--------------------------------------------------------------------------

    protected final SceneManager getSceneManager() {
        return sceneManager;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new {@link Application} specific to the platform
     * {@link com.teotigraphix.caustk.core.ISoundGenerator}.
     * 
     * @param applicationName The name of the application.
     * @param soundGenerator The platform specific
     *            {@link com.teotigraphix.caustk.core.ISoundGenerator}.
     */
    public Application(String applicationName) {
        this.applicationName = applicationName;
        eventBus = new EventBus("application");
        sceneManager = new SceneManager(this);
    }

    //--------------------------------------------------------------------------
    // IApplication API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void create() {
        Gdx.app.log(TAG, "create()");
        //try {
        //Gdx.app.log("StartupExecutor", "create()");
        //runtime = startupExecutor.create(this);
        //getLogger().log("Rack", "initialize()");
        //runtime.getRack().initialize();
        //getLogger().log("Rack", "onStart()");
        //runtime.getRack().onStart();
        //getLogger().log("SceneManager", "create()");
        sceneManager.create();
        //} catch (IOException e) {
        //    // TODO Auto-generated catch block
        //    e.printStackTrace();
        //}
        onRegisterScenes();
        onRegisterModels();
        onCreate();
    }

    @Override
    public void render() {
        sceneManager.preRender();
        sceneManager.postRender();
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(TAG, "resize(" + width + ", " + height + ")");
        if (sceneManager != null)
            sceneManager.resize(width, height);
    }

    @Override
    public void pause() {
        Gdx.app.log(TAG, "pause()");
        sceneManager.pause();
    }

    @Override
    public void resume() {
        Gdx.app.log(TAG, "resume()");
        sceneManager.resume();
    }

    @Override
    public void dispose() {
        Gdx.app.log(TAG, "dispose()");
        sceneManager.dispose();
        //runtime.getRack().onDestroy();
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Register application {@link IModel}s.
     * <p>
     * First of the register methods to be called.
     * 
     * @see ApplicationComponentRegistery#putCommand(Class, IModel)
     */
    protected abstract void onRegisterModels();

    /**
     * Add {@link com.teotigraphix.caustk.gdx.app.ui.IScene}s to the application.
     * 
     * @see #onRegisterModels()
     * @see SceneManager#addScene(int, Class)
     */
    protected abstract void onRegisterScenes();

    /**
     * Set the initial {@link com.teotigraphix.caustk.gdx.app.ui.Scene} that starts
     * the application, and perform any other various setup tasks before the
     * main user interface is shown.
     * 
     * @see #onRegisterScenes()
     */
    protected abstract void onCreate();

    /**
     * Called by the {@link SceneManager} during a scene change.
     * 
     * @param scene The active scene.
     */
    public void onSceneChange(IScene scene) {
    }
}
