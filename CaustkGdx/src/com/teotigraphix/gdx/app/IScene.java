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

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teotigraphix.gdx.app.internal.SceneManager;
import com.teotigraphix.gdx.skin.SkinLibrary;

/**
 * The {@link IScene} API allows an application to display states as UI
 * screens.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public interface IScene {

    /**
     * Returns the owning {@link IApplication}.
     */
    IApplication getApplication();

    /**
     * Returns the screen's {@link Stage}.
     * <p>
     * There is a one to one relationship with a {@link Scene} and
     * {@link Stage}, the screen owns the stage.
     */
    Stage getStage();

    /**
     * Returns the themed skin used with this screen.
     * 
     * @return
     */
    Skin getSkin();

    /**
     * Returns whether this screen has been initialized.
     * <p>
     * The screen will only be created once during the call to
     * <code>setScreen()</code>. This template creation process is as follows
     * (only when uninitialized);
     * <ul>
     * <li>{@link #initialize(IApplication)}</li>
     * <li>{@link #create()}</li>
     * </ul>
     * After initialized;
     * <ul>
     * <li>{@link #show()}</li>
     * <li>{@link #resize(int, int)}</li>
     * </ul>
     * 
     * @see #initialize(IApplication)
     * @see SceneManager#setScene(IScene)
     */
    boolean isInitialized();

    /**
     * Initializes the {@link IScene}.
     * <p>
     * Applications must subclass {@link SkinLibrary} with their {@link Skin}
     * part additions.
     * 
     * @param gdxApplication The owning {@link IApplication}.
     */
    void initialize(IApplication gdxApplication);

    /**
     * Creates the screen and it's contents.
     */
    void create();

    /**
     * Called when the screen should render itself.
     * 
     * @param delta The time in seconds since the last render.
     */
    void render(float delta);

    /**
     * @see ApplicationListener#resize(int, int)
     */
    void resize(int width, int height);

    /**
     * Called when this screen becomes the current screen for a
     * {@link SceneManager}.
     */
    void show();

    /**
     * Called when this screen is no longer the current screen for a
     * {@link SceneManager}.
     */
    void hide();

    /**
     * @see ApplicationListener#pause()
     */
    void pause();

    /**
     * @see ApplicationListener#resume()
     */
    void resume();

    /**
     * Called when this screen should release all resources.
     */
    void dispose();
}
