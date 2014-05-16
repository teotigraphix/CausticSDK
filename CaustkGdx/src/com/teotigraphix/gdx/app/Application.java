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

package com.teotigraphix.gdx.app;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.google.common.eventbus.EventBus;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CaustkRack;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.core.ICaustkLogger;
import com.teotigraphix.caustk.core.ISoundGenerator;
import com.teotigraphix.gdx.app.internal.SceneManager;
import com.teotigraphix.gdx.core.StartupExecutor;

/**
 * The base implementation of the {@link IApplication} API.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public abstract class Application implements IApplication {

    private static final String TAG = "Application";

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private String applicationName;

    private int width;

    private int height;

    private EventBus eventBus;

    private CaustkRuntime runtime;

    private StartupExecutor startupExecutor;

    private SceneManager sceneManager;

    //--------------------------------------------------------------------------
    // IGdxApplication API :: Properties
    //--------------------------------------------------------------------------

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public float getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public final EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public ICaustkLogger getLogger() {
        return runtime.getLogger();
    }

    @Override
    public CaustkRack getRack() {
        return runtime.getRack();
    }

    @Override
    public IScene getScene() {
        return getSceneManager().getScene();
    }

    @Override
    public boolean isCurrentScene(int sceneId) {
        return getSceneManager().isCurrentScene(sceneId);
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
     * {@link ISoundGenerator}.
     * 
     * @param applicationName The name of the application.
     * @param soundGenerator The platform specific {@link ISoundGenerator}.
     */
    public Application(String applicationName, ISoundGenerator soundGenerator) {
        this.applicationName = applicationName;
        eventBus = new EventBus("application");
        startupExecutor = new StartupExecutor(soundGenerator);
        sceneManager = new SceneManager(this);
    }

    //--------------------------------------------------------------------------
    // IGdxApplication API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public final void create() {
        Gdx.app.log(TAG, "create()");
        try {
            Gdx.app.log("StartupExecutor", "create()");
            runtime = startupExecutor.create(this);
            getLogger().log("Rack", "initialize()");
            runtime.getRack().initialize();
            getLogger().log("Rack", "onStart()");
            runtime.getRack().onStart();
            getLogger().log("SceneManager", "create()");
            sceneManager.create();
        } catch (CausticException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        onRegisterScenes();
        onRegisterModels();
        onCreate();
    }

    @Override
    public void render() {
        sceneManager.preRender();
        if (runtime.getRack().isLoaded()) {
            runtime.getRack().frameChanged(Gdx.graphics.getDeltaTime());
            int measure = runtime.getRack().getSequencer().getCurrentMeasure();
            float beat = runtime.getRack().getSequencer().getCurrentFloatBeat();
            int sixteenth = runtime.getRack().getSequencer().getCurrentSixteenthStep();
            int thirtysecond = runtime.getRack().getSequencer().getCurrentThritySecondStep();
            if (runtime.getRack().getSequencer().isBeatChanged()) {
                sceneManager.getScene().onBeatChange(measure, beat, sixteenth, thirtysecond);
            }
            if (runtime.getRack().getSequencer().isSixteenthChanged()) {
                sceneManager.getScene().onSixteenthChange(measure, beat, sixteenth, thirtysecond);
            }
            if (runtime.getRack().getSequencer().isThirtysecondChanged()) {
                sceneManager.getScene()
                        .onThirtysecondChange(measure, beat, sixteenth, thirtysecond);
            }
        }
        sceneManager.postRender();
    }

    @Override
    public void resize(int width, int height) {
        getLogger().log(TAG, "resize(" + width + ", " + height + ")");
        if (sceneManager != null)
            sceneManager.resize(width, height);
    }

    @Override
    public void pause() {
        runtime.getRack().onPause();
        getLogger().log(TAG, "pause()");
        sceneManager.pause();
    }

    @Override
    public void resume() {
        runtime.getRack().onResume();
        getLogger().log(TAG, "resume()");
        sceneManager.resume();
    }

    @Override
    public void dispose() {
        getLogger().log(TAG, "dispose()");
        sceneManager.dispose();
        runtime.getRack().onDestroy();
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Register application {@link IModel}s.
     * <p>
     * First of the register methods to be called.
     * 
     * @see ApplicationComponentRegistery#put(Class, IModel)
     */
    protected abstract void onRegisterModels();

    /**
     * Add {@link IScene}s to the application.
     * 
     * @see #onRegisterModels()
     * @see SceneManager#addScene(int, Class)
     */
    protected abstract void onRegisterScenes();

    /**
     * Set the initial {@link Scene} that starts the application, and perform
     * any other various setup tasks before the main user interface is shown.
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
