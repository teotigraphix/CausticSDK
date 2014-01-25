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

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.gdx.app.GdxBehavior;
import com.teotigraphix.gdx.app.GdxComponent;
import com.teotigraphix.gdx.app.IGdxBehavior;
import com.teotigraphix.gdx.skin.SkinLibrary;

/**
 * The {@link GdxScene} is the base implementation of the {@link IGdxScene} API.
 * <p>
 * An {@link IGdxScene} holds {@link GdxBehavior}s that assemble a view with
 * user interface components. The behavior is responsible for creating user
 * interface components and mediating the component's events.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public abstract class GdxScene implements IGdxScene {

    public static final String LOG = GdxScene.class.getSimpleName();

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private IGdxApplication application;

    private Stage stage;

    private Skin skin;

    private TextureAtlas atlas;

    private SkinLibrary skinLibrary;

    private boolean initialized;

    private Color backgroundColor = new Color();

    private List<IGdxBehavior> components = new ArrayList<IGdxBehavior>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // application
    //----------------------------------

    @Override
    public IGdxApplication getApplication() {
        return application;
    }

    //----------------------------------
    // stage
    //----------------------------------

    @Override
    public Stage getStage() {
        return stage;
    }

    //----------------------------------
    // skin
    //----------------------------------

    @Override
    public Skin getSkin() {
        return skin;
    }

    //----------------------------------
    // initialized
    //----------------------------------

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    //--------------------------------------------------------------------------
    // Protected :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // skinLibrary
    //----------------------------------

    /**
     * Returns the screen's {@link SkinLibrary} implementation.
     */
    protected SkinLibrary getSkinLibrary() {
        return skinLibrary;
    }

    /**
     * Assigns the screen's {@link SkinLibrary} implementation.
     * 
     * @param skinLibrary The implemented skin library.
     */
    protected void setSkinLibrary(SkinLibrary skinLibrary) {
        this.skinLibrary = skinLibrary;
    }

    /**
     * Returns the simple name of the class.
     * 
     * @return
     */
    protected final String getName() {
        return getClass().getSimpleName();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new {@link GdxScene}.
     */
    public GdxScene() {
        stage = new Stage();
        atlas = new TextureAtlas(Gdx.files.internal("skin.atlas"));
        skin = new Skin(atlas);
    }

    //--------------------------------------------------------------------------
    // IGdxScreen API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void initialize(IGdxApplication application) {
        this.application = application;
        initialized = true;
        skinLibrary.initialize(skin);
    }

    @Override
    public void create() {
        Gdx.app.log(LOG, "Creating screen: " + getName());

        // all behaviors create their user interface components
        for (IGdxBehavior behavior : components) {
            behavior.onStart();
        }
    }

    @Override
    public void render(float delta) {
        // clear the screen with the given RGB color
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.b, backgroundColor.g,
                backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (IGdxBehavior behavior : components) {
            behavior.onUpdate();
        }

        stage.act(delta);

        stage.draw();

        // draw the table debug lines
        Table.drawDebug(stage);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height);
        stage.setViewport(application.getWidth(), application.getHeight(), true);
        stage.getCamera().translate(-stage.getGutterWidth(), -stage.getGutterHeight(), 0);
    }

    @Override
    public void show() {
        Gdx.app.log(LOG, "Showing screen: " + getName());
        // set the stage as the input processor
        Gdx.input.setInputProcessor(stage);

        for (IGdxBehavior behavior : components) {
            behavior.onShow();
        }
    }

    @Override
    public void hide() {
        Gdx.app.log(LOG, "Hiding screen: " + getName());

        for (IGdxBehavior behavior : components) {
            behavior.onHide();
        }
    }

    @Override
    public void pause() {
        Gdx.app.log(LOG, "Pausing screen: " + getName());

        for (IGdxBehavior behavior : components) {
            behavior.onDisable();
        }
    }

    @Override
    public void resume() {
        Gdx.app.log(LOG, "Resuming screen: " + getName());

        for (IGdxBehavior behavior : components) {
            behavior.onEnable();
        }
    }

    @Override
    public void dispose() {
        Gdx.app.log(LOG, "Disposing screen: " + getName());

        // disable all behaviors
        for (IGdxBehavior behavior : components) {
            behavior.onDisable();
        }

        // dispose all behaviors
        for (IGdxBehavior behavior : components) {
            behavior.onDestroy();
        }

        components.clear();

        if (skin != null)
            skin.dispose();
        if (atlas != null)
            atlas.dispose();
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Adds a behavior to the screen.
     * <p>
     * Call during {@link #initialize(IGdxApplication)} override.
     * <p>
     * The behavior's {@link GdxComponent#setScene(IGdxScene)} will be called
     * during the add logic.
     * 
     * @param component The {@link IGdxBehavior}.
     * @see IGdxBehavior#onAwake()
     */
    protected final void addComponent(IGdxBehavior component) {
        ((GdxComponent)component).setScene(this);
        components.add(component);
        // all behaviors attach their application events
        component.onAwake();
    }
}
