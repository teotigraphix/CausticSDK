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

package com.teotigraphix.caustk.gdx.app.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.common.eventbus.EventBus;
import com.teotigraphix.caustk.gdx.app.IApplication;

/**
 * The {@link SceneComponent} is the base class for all view behaviors.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public abstract class SceneComponent implements ISceneComponent {

    private IApplication application;

    private IScene scene;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // application
    //----------------------------------

    @Override
    public IApplication getApplication() {
        return application;
    }

    /**
     * Sets the {@link com.teotigraphix.caustk.gdx.app.IApplication} owning
     * application.
     * 
     * @param application The mediator's owning application.
     */
    public void setApplication(IApplication application) {
        this.application = application;
    }

    //----------------------------------
    // scene
    //----------------------------------

    @Override
    public IScene getScene() {
        return scene;
    }

    /**
     * Sets the {@link com.teotigraphix.caustk.gdx.app.ui.IScene} owning scene.
     * 
     * @param scene The mediator's owner.
     * @see #onSceneChange(com.teotigraphix.caustk.gdx.app.ui.IScene)
     */
    public void setScene(IScene scene) {
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
    public Skin getSkin() {
        return scene.getSkin();
    }

    //----------------------------------
    // stage
    //----------------------------------

    /**
     * Returns the screen's {@link Stage}.
     */
    public Stage getStage() {
        return scene.getStage();
    }

    //----------------------------------
    // eventBus
    //----------------------------------

    /**
     * Returns the {@link com.teotigraphix.caustk.gdx.app.IApplication}'s
     * {@link com.google.common.eventbus.EventBus}.
     * <p>
     * Behaviors can listen to application events using this eventBus.
     */
    protected EventBus getEventBus() {
        return application.getEventBus();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new mediator.
     */
    public SceneComponent() {
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public abstract void onAwake();

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Called when {@link #setScene(com.teotigraphix.caustk.gdx.app.ui.IScene)}'s
     * value has changed.
     * 
     * @param screen The new {@link com.teotigraphix.caustk.gdx.app.ui.IScene}.
     */
    protected void onSceneChange(IScene screen) {
    }
}
