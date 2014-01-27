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
import com.teotigraphix.gdx.IGdxScene;

/**
 * The {@link SceneManager} manages {@link IGdxScene}s in an
 * {@link IGdxApplication}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class SceneManager {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private IGdxScene scene;

    private IGdxScene pendingScene;

    private ArrayMap<Integer, IGdxScene> scenes = new ArrayMap<Integer, IGdxScene>();

    private ArrayMap<Integer, Class<? extends IGdxScene>> sceneTypes = new ArrayMap<Integer, Class<? extends IGdxScene>>();

    private IGdxApplication application;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // scene
    //----------------------------------

    /**
     * Sets the active {@link IGdxScene} int id.
     * 
     * @param id The next active scene id, must have already been registered
     *            with the scene manager.
     */
    public void setScene(int id) {
        IGdxScene scene = scenes.get(id);
        pendingScene = scene;
        if (scene == null) {
            Class<? extends IGdxScene> type = sceneTypes.get(id);
            try {
                pendingScene = type.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            scenes.put(id, pendingScene);
        }
    }

    /**
     * Returns the currently active {@link IGdxScene} instance.
     */
    public IGdxScene getScene() {
        return scene;
    }

    /**
     * Sets the current scene. {@link IGdxScene#hide()} is called on any old
     * scene, and {@link IGdxScene#show()} is called on the new scene, if any.
     * 
     * @param scene may be {@code null}
     */
    public void setScene(IGdxScene scene) {
        IGdxScene oldScene = this.scene;
        if (oldScene != null)
            oldScene.hide();

        this.scene = scene;

        if (scene != null) {
            // XXX sceneProvider.setScene(scene);
            if (!scene.isInitialized()) {
                // XXX injector.injectMembers(scene);
                scene.initialize(application);
                scene.create();
            }
            scene.show();
            scene.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new {@link SceneManager} for the {@link IGdxApplication}.
     * 
     * @param application The owning application.
     */
    public SceneManager(IGdxApplication application) {
        this.application = application;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Adds an {@link IGdxScene} id and type to the available types in the scene
     * manager.
     * 
     * @param id the int id of the {@link IGdxScene} class type, must be unique.
     * @param type The class type.
     */
    public void addScene(int id, Class<? extends IGdxScene> type) {
        sceneTypes.put(id, type);
    }

    /**
     * Removes a {@link IGdxScene} id and type.
     * 
     * @param id the int id of the {@link IGdxScene} class type.
     */
    public boolean removeScene(int id) {
        // XXX removeIndex() check
        sceneTypes.removeIndex(id);
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
        if (pendingScene != null) {
            setScene(pendingScene);
            pendingScene = null;
        }
    }

    /**
     * {@link IGdxApplication#render()}, called <strong>after</strong>
     * {@link Rack#frameChanged(float)}.
     */
    public void postRender() {
        if (scene != null)
            scene.render(Gdx.graphics.getDeltaTime());
    }

    /**
     * {@link IGdxApplication#resize(int, int)}, resizes the {@link #getScene()}
     * .
     * 
     * @param width Application width.
     * @param height Application height.
     */
    public void resize(int width, int height) {
        if (scene != null)
            scene.resize(width, height);
    }

    /**
     * {@link IGdxApplication#pause()}, pauses the {@link #getScene()}.
     */
    public void pause() {
        if (scene != null)
            scene.pause();
    }

    /**
     * {@link IGdxApplication#resume()}, resumes the {@link #getScene()}.
     */
    public void resume() {
        if (scene != null)
            scene.resume();
    }

    /**
     * {@link IGdxApplication#dispose()}, disposes all instantiated
     * {@link IGdxScene}s that this scene manager contains.
     */
    public void dispose() {
        for (Object scene : scenes.values) {
            if (scene != null)
                ((IGdxScene)scene).dispose();
        }
    }
}
