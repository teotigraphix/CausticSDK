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

package com.teotigraphix.libgdx.application;

import java.io.IOException;

import org.androidtransfuse.event.EventObserver;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.utils.ArrayMap;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.rack.ISoundGenerator;
import com.teotigraphix.libgdx.dialog.IDialogManager;
import com.teotigraphix.libgdx.model.IApplicationModel;
import com.teotigraphix.libgdx.model.IApplicationModel.OnApplicationModelNewProjectComplete;
import com.teotigraphix.libgdx.scene2d.IScreenProvider;
import com.teotigraphix.libgdx.screen.IScreen;

/**
 * An {@link ApplicationListener} that delegates to a {@link Screen}. This
 * allows an application to easily have multiple screens. </p>
 * <p>
 * Screens are not disposed automatically. You must handle whether you want to
 * keep screens around or dispose of them when another screen is set.
 */
public abstract class GdxApplication implements IGdxApplication {

    @Inject
    Injector injector;

    @Inject
    IDialogManager dialogManager;

    @Inject
    IScreenProvider screenProvider;

    @Inject
    IApplicationModel applicationModel;

    private ArrayMap<Integer, IScreen> screens = new ArrayMap<Integer, IScreen>();

    private ArrayMap<Integer, Class<? extends IScreen>> screenTypes = new ArrayMap<Integer, Class<? extends IScreen>>();

    private StartupExecutor executor;

    private Module module;

    private IScreen screen;

    private IScreen pendingScreen;

    private boolean printFps;

    private FPSLogger fpsLogger;

    public static final boolean DEV_MODE = true;

    //----------------------------------
    // dialogManager
    //----------------------------------

    @Override
    public IDialogManager getDialogManager() {
        return dialogManager;
    }

    //----------------------------------
    // controller
    //----------------------------------

    private ISoundGenerator soundGenerator;

    @Override
    public ISoundGenerator getSoundGenerator() {
        return soundGenerator;
    }

    //----------------------------------
    // controller
    //----------------------------------

    private ICaustkController controller;

    @Override
    public ICaustkController getController() {
        return controller;
    }

    protected void setController(ICaustkController value) {
        controller = value;
    }

    //----------------------------------
    // appName
    //----------------------------------

    private String appName;

    @Override
    public String getAppName() {
        return appName;
    }

    //--------------------------------------------------------------------------
    // appName
    //--------------------------------------------------------------------------

    public GdxApplication(String appName, ISoundGenerator soundGenerator, Module module) {
        this.appName = appName;
        this.soundGenerator = soundGenerator;
        this.module = module;

        executor = new StartupExecutor();
        fpsLogger = new FPSLogger();
    }

    @Override
    public void create() {
        Gdx.app.log("GDXGame", "create()");
        executor.addModule(module);
        try {
            executor.create(this);

            executor.run();

            setController(executor.getController());
            // calls onStart() of the rack and sound engine
            controller.onStart();
        } catch (CausticException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        applicationModel.register(OnApplicationModelNewProjectComplete.class,
                new EventObserver<OnApplicationModelNewProjectComplete>() {
                    @Override
                    public void trigger(OnApplicationModelNewProjectComplete object) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                getController().getRack().restore();
                                screen.show();
                                screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                            }
                        });
                    }
                });
    }

    @Override
    public void dispose() {
        for (Object screen : screens.values) {
            if (screen != null)
                ((IScreen)screen).dispose();
        }

        try {
            getController().getApplication().save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        soundGenerator.onDestroy();
        controller.onDestroy();
    }

    @Override
    public void pause() {
        controller.onPause();
        if (screen != null)
            screen.pause();
    }

    @Override
    public void resume() {
        controller.onResume();
        if (screen != null)
            screen.resume();
    }

    @Override
    public void render() {
        if (pendingScreen != null) {
            setScreen(pendingScreen);
            pendingScreen = null;
        }

        if (printFps)
            fpsLogger.log();

        if (getController() != null) {
            getController().frameChanged(Gdx.graphics.getDeltaTime());
        }

        if (screen != null)
            screen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("GDXGame", "resize()");
        if (screen != null)
            screen.resize(width, height);
    }

    protected void addScreen(int id, Class<? extends IScreen> type) {
        screenTypes.put(id, type);
    }

    @Override
    public void setScreen(int id) {
        IScreen screen = screens.get(id);
        pendingScreen = screen;
        if (screen == null) {
            Class<? extends IScreen> type = screenTypes.get(id);
            try {
                pendingScreen = type.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            screens.put(id, pendingScreen);
        }
    }

    /**
     * Sets the current screen. {@link IScreen#hide()} is called on any old
     * screen, and {@link IScreen#show()} is called on the new screen, if any.
     * 
     * @param value may be {@code null}
     */
    @Override
    public void setScreen(IScreen value) {
        if (screen != null)
            screen.hide();

        screen = value;

        if (screen != null) {
            screenProvider.setScreen(screen);
            if (!screen.isInitialized()) {
                injector.injectMembers(screen);
                screen.initialize(this);
                screen.create();
            }
            screen.show();
            screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    /**
     * @return the currently active {@link IScreen}.
     */
    @Override
    public IScreen getScreen() {
        return screen;
    }

}
