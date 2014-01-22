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

package com.teotigraphix.gdx;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.ISoundGenerator;
import com.teotigraphix.caustk.rack.CaustkRuntime;
import com.teotigraphix.gdx.app.ScreenManager;
import com.teotigraphix.gdx.app.StartupExecutor;

/**
 * The base implementation of the {@link IGdxApplication} API.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class GdxApplication implements IGdxApplication {

    // TODO Temp metrics
    static float WIDTH = 800f;

    static float HEIGHT = 480f;

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private CaustkRuntime runtime;

    private StartupExecutor startupExecutor;

    private ScreenManager screenManager;

    private String applicationName;

    //--------------------------------------------------------------------------
    // IGdxApplication API :: Properties
    //--------------------------------------------------------------------------

    @Override
    public float getWidth() {
        return WIDTH;
    }

    @Override
    public float getHeight() {
        return HEIGHT;
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new {@link GdxApplication} specific to the platform
     * {@link ISoundGenerator}.
     * 
     * @param applicationName The name of the application.
     * @param soundGenerator The platform specific {@link ISoundGenerator}.
     */
    public GdxApplication(String applicationName, ISoundGenerator soundGenerator) {
        this.applicationName = applicationName;
        startupExecutor = new StartupExecutor(soundGenerator);
        screenManager = new ScreenManager(this);
    }

    //--------------------------------------------------------------------------
    // IGdxApplication API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void create() {
        Gdx.app.log("GdxApplication", "create()");
        try {
            runtime = startupExecutor.create(this);
            runtime.getRack().initialize();
            runtime.getRack().onStart();
            runtime.getRack().onResume();
            screenManager.create();
        } catch (CausticException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        //Gdx.app.log("GdxApplication", "render()");
        screenManager.preRender();

        // getController().frameChanged(Gdx.graphics.getDeltaTime());
        runtime.getRack().frameChanged(Gdx.graphics.getDeltaTime());

        screenManager.postRender();
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("GdxApplication", "resize(" + width + ", " + height + ")");
        if (screenManager != null)
            screenManager.resize(width, height);
    }

    @Override
    public void pause() {
        runtime.getRack().onPause();
        Gdx.app.log("GdxApplication", "pause()");
        screenManager.pause();
    }

    @Override
    public void resume() {
        runtime.getRack().onResume();
        Gdx.app.log("GdxApplication", "resume()");
        screenManager.resume();
    }

    @Override
    public void dispose() {
        Gdx.app.log("GdxApplication", "dispose()");
        screenManager.dispose();
        runtime.getRack().onDestroy();
    }
}
