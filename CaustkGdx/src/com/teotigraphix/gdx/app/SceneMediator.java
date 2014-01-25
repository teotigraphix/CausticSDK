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

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.gdx.GdxScene;
import com.teotigraphix.gdx.IGdxScene;

/**
 * The {@link SceneMediator} is a view mediator that draws and handles UI events
 * from custom components.
 * <p>
 * The mediator is also capable of handling child mediators that mediate
 * specific parts of a complicated view.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class SceneMediator extends GdxMediator {

    protected List<SceneMediatorChild> children = new ArrayList<SceneMediatorChild>();

    private IGdxScene scene;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // scene
    //----------------------------------

    /**
     * Returns the mediator's owning {@link IGdxScene}.
     */
    public IGdxScene getScene() {
        return scene;
    }

    /**
     * Sets the {@link IGdxScene} owning scene.
     * 
     * @param screen The mediator's owner.
     * @see #onSceneChange(IGdxScene)
     */
    public void setScene(IGdxScene scene) {
        this.scene = scene;
        onSceneChange(scene);
    }

    //--------------------------------------------------------------------------
    // Protected :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // skin
    //----------------------------------

    /**
     * Returns the screen's {@link Skin}.
     */
    protected Skin getSkin() {
        return scene.getSkin();
    }

    //----------------------------------
    // stage
    //----------------------------------

    /**
     * Returns the screen's {@link Stage}.
     */
    protected Stage getStage() {
        return scene.getStage();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Creates a {@link SceneMediator}.
     */
    public SceneMediator() {
    }

    //--------------------------------------------------------------------------
    // LifeCycle
    //--------------------------------------------------------------------------

    /**
     * Called once during {@link GdxScene#create()}, before {@link #onCreate()}
     * .
     * <p>
     * Add all global/model event listeners.
     */
    @Override
    public void onAttach() {
        for (SceneMediatorChild child : children) {
            child.onAttach();
        }
    }

    /**
     * Called once during {@link GdxScene#create()}, after {@link #onAttach()}.
     * <p>
     * Create all user interface components that are attached to the
     * {@link #getStage()}.
     * <p>
     * Call {@link #createChildren(Table)} from the {@link #onCreate(IScreen)}
     * method in sub classes if this mediator is using child tables with
     * mediators.
     */
    public void onCreate() {
    }

    /**
     * Called during {@link IGdxScene#show()}.
     */
    public void onShow() {
        for (SceneMediatorChild child : children) {
            child.onShow();
        }
    }

    /**
     * Called during {@link IGdxScene#hide()}.
     */
    public void onHide() {
        for (SceneMediatorChild child : children) {
            child.onHide();
        }
    }

    /**
     * Called during {@link IGdxScene#resume()}.
     */
    public void onResume() {
        for (SceneMediatorChild child : children) {
            child.onResume();
        }
    }

    /**
     * Called during {@link IGdxScene#pause()}.
     */
    public void onPause() {
        for (SceneMediatorChild child : children) {
            child.onPause();
        }
    }

    /**
     * Called during {@link IGdxScene#dispose()}.
     * <p>
     * Called before {@link #onDispose()}.
     */
    public void onDetach() {
        for (SceneMediatorChild child : children) {
            child.onDetach();
        }
    }

    /**
     * Called during {@link IGdxScene#dispose()}.
     * <p>
     * Called after {@link #onDetach()}.
     */
    public void onDispose() {
        for (SceneMediatorChild child : children) {
            child.onDispose();
        }
        scene = null;
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Called when {@link #setScene(IGdxScene)}'s value has changed.
     * 
     * @param screen The new {@link IGdxScene}.
     */
    protected void onSceneChange(IGdxScene screen) {
    }

    /**
     * Adds a child mediator and sets its parent to this mediator.
     * 
     * @param child The {@link SceneMediatorChild}.
     */
    protected void addChild(SceneMediatorChild child) {
        child.setParent(this);
        children.add(child);
    }

    /**
     * Call in a subclass that uses {@link SceneMediatorChild} composites, to
     * pass them their {@link Table} parent during creation.
     * 
     * @param parent The parent {@link Table} instance that will hold the child
     *            mediator's ui components.
     */
    protected void createChildren(Table parent) {
        for (SceneMediatorChild mediator : children) {
            mediator.onCreate(parent);
        }
    }
}
