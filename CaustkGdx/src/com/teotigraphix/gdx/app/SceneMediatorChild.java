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

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.teotigraphix.gdx.IGdxScene;

/**
 * The {@link SceneMediatorChild} class creates user interface components
 * within a parent {@link Table}.
 * <p>
 * The parent {@link Table} is managed by a {@link SceneMediator} who is the
 * owner of this child mediator.
 * <p>
 * The parent {@link SceneMediator} is responsible for creating and positioning
 * the {@link Table} instance within the bounds of the {@link #getParent()}'s
 * {@link Stage}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public abstract class SceneMediatorChild extends GdxMediator {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private SceneMediator parent;

    private IGdxScene scene;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // parent
    //----------------------------------

    /**
     * Returns the parent {@link SceneMediator} of this child mediator.
     */
    public SceneMediator getParent() {
        return parent;
    }

    void setParent(SceneMediator parent) {
        this.parent = parent;
        scene = parent.getScene();
        onParentChanged(parent);
    }

    //----------------------------------
    // screen
    //----------------------------------

    /**
     * Returns the owning {@link IGdxScene}.
     */
    protected IGdxScene getScene() {
        return scene;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Creates a {@link SceneMediatorChild}.
     */
    public SceneMediatorChild() {
    }

    //--------------------------------------------------------------------------
    // LifeCycle
    //--------------------------------------------------------------------------

    protected void onParentChanged(SceneMediator parent) {
    }

    @Override
    public void onAttach() {
    }

    /**
     * Create the child mediator's user interface component's within the
     * {@link Table} passed.
     * <p>
     * The parent {@link SceneMediator} creates and positions the {@link Table}.
     * 
     * @param parent The parent {@link SceneMediator}.
     */
    public abstract void onCreate(WidgetGroup parent);

    public void onShow() {
    }

    public void onHide() {
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public void onDetach() {
    }

    public void onDispose() {
    }
}
