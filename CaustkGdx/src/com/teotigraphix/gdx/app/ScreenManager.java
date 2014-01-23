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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ArrayMap;
import com.teotigraphix.gdx.IGdxApplication;
import com.teotigraphix.gdx.IGdxScreen;

/**
 * The {@link ScreenManager} manages {@link IGdxScreen}s in an
 * {@link IGdxApplication}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class ScreenManager {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private IGdxScreen screen;

    private IGdxScreen pendingScreen;

    private ArrayMap<Integer, IGdxScreen> screens = new ArrayMap<Integer, IGdxScreen>();

    private ArrayMap<Integer, Class<? extends IGdxScreen>> screenTypes = new ArrayMap<Integer, Class<? extends IGdxScreen>>();

    private IGdxApplication gdxApplication;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // screen
    //----------------------------------

    /**
     * Sets the active {@link IGdxScreen} int id.
     * 
     * @param id The next active screen id, must have already been registered
     *            with the screen manager.
     */
    public void setScreen(int id) {
        IGdxScreen screen = screens.get(id);
        pendingScreen = screen;
        if (screen == null) {
            Class<? extends IGdxScreen> type = screenTypes.get(id);
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
     * Returns the currently active {@link IGdxScreen} instance.
     */
    public IGdxScreen getScreen() {
        return screen;
    }

    /**
     * Sets the current screen. {@link IScreen#hide()} is called on any old
     * screen, and {@link IScreen#show()} is called on the new screen, if any.
     * 
     * @param value may be {@code null}
     */
    public void setScreen(IGdxScreen value) {
        if (screen != null)
            screen.hide();

        screen = value;

        if (screen != null) {
            // XXX screenProvider.setScreen(screen);
            if (!screen.isInitialized()) {
                // XXX injector.injectMembers(screen);
                screen.initialize(gdxApplication);
                screen.create();
            }
            screen.show();
            screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new {@link ScreenManager} for the {@link IGdxApplication}.
     * 
     * @param gdxApplication The owning application.
     */
    public ScreenManager(IGdxApplication gdxApplication) {
        this.gdxApplication = gdxApplication;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Adds an {@link IGdxScreen} id and type to the available types in the
     * screen manager.
     * 
     * @param id the int id of the {@link IGdxScreen} class type, must be
     *            unique.
     * @param type The class type.
     */
    public void addScreen(int id, Class<? extends IGdxScreen> type) {
        screenTypes.put(id, type);
    }

    /**
     * Removes a {@link IGdxScreen} id and type.
     * 
     * @param id the int id of the {@link IGdxScreen} class type.
     */
    public boolean removeScreen(int id) {
        // XXX removeIndex() check
        screenTypes.removeIndex(id);
        return true;
    }

    /**
     * {@link IGdxApplication#create()}.
     */
    public void create() {
    }

    /**
     * {@link IGdxApplication#render()}, called <strong>before</strong>
     * {@link Rack#frameChanged(float)}.
     */
    public void preRender() {
        if (pendingScreen != null) {
            setScreen(pendingScreen);
            pendingScreen = null;
        }
    }

    /**
     * {@link IGdxApplication#render()}, called <strong>after</strong>
     * {@link Rack#frameChanged(float)}.
     */
    public void postRender() {
        if (screen != null)
            screen.render(Gdx.graphics.getDeltaTime());
    }

    /**
     * {@link IGdxApplication#resize(int, int)}, resizes the
     * {@link #getScreen()}.
     * 
     * @param width Application width.
     * @param height Application height.
     */
    public void resize(int width, int height) {
        if (screen != null)
            screen.resize(width, height);
    }

    /**
     * {@link IGdxApplication#pause()}, pauses the {@link #getScreen()}.
     */
    public void pause() {
        if (screen != null)
            screen.pause();
    }

    /**
     * {@link IGdxApplication#resume()}, resumes the {@link #getScreen()}.
     */
    public void resume() {
        if (screen != null)
            screen.resume();
    }

    /**
     * {@link IGdxApplication#dispose()}, disposes all instantiated
     * {@link IGdxScreen}s that this screen manager contains.
     */
    public void dispose() {
        for (Object screen : screens.values) {
            if (screen != null)
                ((IGdxScreen)screen).dispose();
        }
    }
}
