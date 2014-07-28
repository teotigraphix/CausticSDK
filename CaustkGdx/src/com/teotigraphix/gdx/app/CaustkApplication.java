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

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.ICaustkLogger;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.core.ICaustkRuntime;
import com.teotigraphix.caustk.core.ISoundGenerator;
import com.teotigraphix.gdx.core.StartupExecutor;

/**
 * The base implementation of the {@link ICaustkApplication} API.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public abstract class CaustkApplication extends Application implements ICaustkApplication,
        ApplicationListener {

    private static final String TAG = "CaustkApplication";

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private ICaustkRuntime runtime;

    private StartupExecutor startupExecutor;

    //--------------------------------------------------------------------------
    // IGdxApplication API :: Properties
    //--------------------------------------------------------------------------

    @Override
    public ICaustkLogger getLogger() {
        return runtime.getLogger();
    }

    @Override
    public ICaustkRack getRack() {
        return runtime.getRack();
    }

    @Override
    public ICaustkScene getScene() {
        return (ICaustkScene)super.getScene();
    }

    //--------------------------------------------------------------------------
    // Protected :: Properties
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new {@link CaustkApplication} specific to the platform
     * {@link ISoundGenerator}.
     * 
     * @param applicationName The name of the application.
     * @param soundGenerator The platform specific {@link ISoundGenerator}.
     */
    public CaustkApplication(String applicationName, ISoundGenerator soundGenerator) {
        super(applicationName);
        startupExecutor = new StartupExecutor(soundGenerator);
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
            getSceneManager().create();
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
        getSceneManager().preRender();
        if (runtime.getRack().isLoaded()) {
            runtime.getRack().frameChanged(Gdx.graphics.getDeltaTime());
            int measure = runtime.getRack().getSequencer().getCurrentMeasure();
            float beat = runtime.getRack().getSequencer().getCurrentFloatBeat();
            int sixteenth = runtime.getRack().getSequencer().getCurrentSixteenthStep();
            int thirtysecond = runtime.getRack().getSequencer().getCurrentThritySecondStep();
            if (measure == -1)
                measure = (int)(beat / 4);
            if (runtime.getRack().getSequencer().isBeatChanged()) {
                getScene().onBeatChange(measure, beat, sixteenth, thirtysecond);
            }
            if (runtime.getRack().getSequencer().isSixteenthChanged()) {
                getScene().onSixteenthChange(measure, beat, sixteenth, thirtysecond);
            }
            if (runtime.getRack().getSequencer().isThirtysecondChanged()) {
                getScene().onThirtysecondChange(measure, beat, sixteenth, thirtysecond);
            }
        }
        getSceneManager().postRender();
    }

    @Override
    public void resize(int width, int height) {
        getLogger().log(TAG, "resize(" + width + ", " + height + ")");
        if (getSceneManager() != null)
            getSceneManager().resize(width, height);
    }

    @Override
    public void pause() {
        runtime.getRack().onPause();
        getLogger().log(TAG, "pause()");
        getSceneManager().pause();
    }

    @Override
    public void resume() {
        runtime.getRack().onResume();
        getLogger().log(TAG, "resume()");
        getSceneManager().resume();
    }

    @Override
    public void dispose() {
        getLogger().log(TAG, "dispose()");
        getSceneManager().dispose();
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
    @Override
    protected abstract void onRegisterModels();

    /**
     * Add {@link ICaustkScene}s to the application.
     * 
     * @see #onRegisterModels()
     * @see SceneManager#addScene(int, Class)
     */
    @Override
    protected abstract void onRegisterScenes();

    /**
     * Set the initial {@link Scene} that starts the application, and perform
     * any other various setup tasks before the main user interface is shown.
     * 
     * @see #onRegisterScenes()
     */
    @Override
    protected abstract void onCreate();

    /**
     * Called by the {@link SceneManager} during a scene change.
     * 
     * @param scene The active scene.
     */
    @Override
    public void onSceneChange(ICaustkScene scene) {
    }
}
