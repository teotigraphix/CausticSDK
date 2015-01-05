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

package com.teotigraphix.caustk.gdx.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ArrayMap;

/**
 * The {@link SceneManager} manages
 * {@link com.teotigraphix.caustk.gdx.app.IScene}s in an
 * {@link com.teotigraphix.caustk.gdx.app.IApplication}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class SceneManager {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private IScene scene;

    private IScene pendingScene;

    private ArrayMap<Integer, IScene> scenes = new ArrayMap<Integer, IScene>();

    private ArrayMap<Integer, Class<? extends IScene>> sceneTypes = new ArrayMap<Integer, Class<? extends IScene>>();

    private IApplication application;

    private int currentSceneId = -1;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // scene
    //----------------------------------

    /**
     * Sets the active {@link com.teotigraphix.caustk.gdx.app.IScene} int id.
     * 
     * @param sceneId The next active scene id, must have already been
     *            registered with the scene manager.
     */
    public void setScene(int sceneId) {
        currentSceneId = sceneId;
        IScene scene = scenes.get(currentSceneId);
        pendingScene = scene;
        if (scene == null) {
            Class<? extends IScene> type = sceneTypes.get(currentSceneId);
            try {
                pendingScene = type.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            scenes.put(currentSceneId, pendingScene);
        }
    }

    /**
     * Returns the currently active
     * {@link com.teotigraphix.caustk.gdx.app.IScene} instance.
     */
    public IScene getScene() {
        return scene;
    }

    /**
     * Sets the current scene.
     * {@link com.teotigraphix.caustk.gdx.app.IScene#hide()} is called on any
     * old scene, and {@link com.teotigraphix.caustk.gdx.app.IScene#show()} is
     * called on the new scene, if any.
     * 
     * @param scene may be {@code null}
     */
    public void setScene(IScene scene) {
        IScene oldScene = this.scene;
        if (oldScene != null)
            oldScene.hide();

        this.scene = scene;

        if (scene != null) {
            if (!scene.isInitialized()) {
                ((Application)application).onSceneChange(scene);
                scene.initialize(application);
                scene.create();
                scene.start();
            }
            scene.show();
            scene.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    public boolean isCurrentScene(int sceneId) {
        return currentSceneId == sceneId;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new {@link SceneManager} for the
     * {@link com.teotigraphix.caustk.gdx.app.IApplication}.
     * 
     * @param application The owning application.
     */
    public SceneManager(IApplication application) {
        this.application = application;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Adds an {@link com.teotigraphix.caustk.gdx.app.IScene} id and type to the
     * available types in the scene manager.
     * 
     * @param id the int id of the
     *            {@link com.teotigraphix.caustk.gdx.app.IScene} class type,
     *            must be unique.
     * @param type The class type.
     */
    public void addScene(int id, Class<? extends IScene> type) {
        sceneTypes.put(id, type);
    }

    /**
     * Removes a {@link com.teotigraphix.caustk.gdx.app.IScene} id and type.
     * 
     * @param id the int id of the
     *            {@link com.teotigraphix.caustk.gdx.app.IScene} class type.
     */
    public boolean removeScene(int id) {
        // XXX removeIndex() check
        sceneTypes.removeIndex(id);
        return true;
    }

    /**
     * {@link com.teotigraphix.caustk.gdx.app.IApplication#create()}.
     */
    public void create() {
    }

    /**
     * {@link com.teotigraphix.caustk.gdx.app.IApplication#render()}, called
     * <strong>before</strong> {@link Rack#frameChanged(float)}.
     */
    public void preRender() {
        if (pendingScene != null) {
            setScene(pendingScene);
            pendingScene = null;
        }
    }

    /**
     * {@link com.teotigraphix.caustk.gdx.app.IApplication#render()}, called
     * <strong>after</strong> {@link Rack#frameChanged(float)}.
     */
    public void postRender() {
        if (scene != null)
            scene.render(Gdx.graphics.getDeltaTime());
    }

    /**
     * {@link com.teotigraphix.caustk.gdx.app.IApplication#resize(int, int)},
     * resizes the {@link #getScene()} .
     * 
     * @param width Application width.
     * @param height Application height.
     */
    public void resize(int width, int height) {
        if (scene != null)
            scene.resize(width, height);
    }

    /**
     * {@link com.teotigraphix.caustk.gdx.app.IApplication#pause()}, pauses the
     * {@link #getScene()}.
     */
    public void pause() {
        if (scene != null)
            scene.pause();
    }

    /**
     * {@link com.teotigraphix.caustk.gdx.app.IApplication#resume()}, resumes
     * the {@link #getScene()}.
     */
    public void resume() {
        if (scene != null)
            scene.resume();
    }

    /**
     * {@link com.teotigraphix.caustk.gdx.app.IApplication#dispose()}, disposes
     * all instantiated {@link com.teotigraphix.caustk.gdx.app.IScene}s that
     * this scene manager contains.
     */
    public void dispose() {
        for (Object scene : scenes.values) {
            if (scene != null)
                ((IScene)scene).dispose();
        }
    }

    /**
     * Flushes all scene instances so when the next scene is set, its a brand
     * new instance with no state.
     */
    public void reset() {
        for (Object scene : scenes.values) {
            if (scene != null)
                ((IScene)scene).dispose();
        }
        currentSceneId = -1;
        pendingScene = null;
        scene = null;
        scenes = new ArrayMap<Integer, IScene>();
    }
}
