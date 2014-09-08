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
import com.teotigraphix.gdx.app.Application;
import com.teotigraphix.gdx.app.IApplication;
import com.teotigraphix.gdx.app.IScene;

/**
 * The {@link SceneManager} manages {@link IScene}s in an {@link IApplication}.
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
     * Sets the active {@link IScene} int id.
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
     * Returns the currently active {@link IScene} instance.
     */
    public IScene getScene() {
        return scene;
    }

    /**
     * Sets the current scene. {@link IScene#hide()} is called on any old scene,
     * and {@link IScene#show()} is called on the new scene, if any.
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
     * Creates a new {@link SceneManager} for the {@link IApplication}.
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
     * Adds an {@link IScene} id and type to the available types in the scene
     * manager.
     * 
     * @param id the int id of the {@link IScene} class type, must be unique.
     * @param type The class type.
     */
    public void addScene(int id, Class<? extends IScene> type) {
        sceneTypes.put(id, type);
    }

    /**
     * Removes a {@link IScene} id and type.
     * 
     * @param id the int id of the {@link IScene} class type.
     */
    public boolean removeScene(int id) {
        // XXX removeIndex() check
        sceneTypes.removeIndex(id);
        return true;
    }

    /**
     * {@link IApplication#create()}.
     */
    public void create() {
    }

    /**
     * {@link IApplication#render()}, called <strong>before</strong>
     * {@link Rack#frameChanged(float)}.
     */
    public void preRender() {
        if (pendingScene != null) {
            setScene(pendingScene);
            pendingScene = null;
        }
    }

    /**
     * {@link IApplication#render()}, called <strong>after</strong>
     * {@link Rack#frameChanged(float)}.
     */
    public void postRender() {
        if (scene != null)
            scene.render(Gdx.graphics.getDeltaTime());
    }

    /**
     * {@link IApplication#resize(int, int)}, resizes the {@link #getScene()} .
     * 
     * @param width Application width.
     * @param height Application height.
     */
    public void resize(int width, int height) {
        if (scene != null)
            scene.resize(width, height);
    }

    /**
     * {@link IApplication#pause()}, pauses the {@link #getScene()}.
     */
    public void pause() {
        if (scene != null)
            scene.pause();
    }

    /**
     * {@link IApplication#resume()}, resumes the {@link #getScene()}.
     */
    public void resume() {
        if (scene != null)
            scene.resume();
    }

    /**
     * {@link IApplication#dispose()}, disposes all instantiated {@link IScene}s
     * that this scene manager contains.
     */
    public void dispose() {
        for (Object scene : scenes.values) {
            if (scene != null)
                ((IScene)scene).dispose();
        }
    }
}
