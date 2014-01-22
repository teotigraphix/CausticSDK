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
import com.teotigraphix.gdx.app.ScreenMediator;
import com.teotigraphix.gdx.skin.SkinLibrary;

/**
 * The {@link GdxScreen} is the base implementation of the {@link IGdxScreen}
 * API.
 * <p>
 * An {@link IGdxScreen} holds {@link ScreenMediator}s that assemble a view with
 * user interface components. The mediator is responsible for creating user
 * interface components and mediating the component's events.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public abstract class GdxScreen implements IGdxScreen {

    public static final String LOG = GdxScreen.class.getSimpleName();

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private IGdxApplication gdxApplication;

    private Stage stage;

    private Skin skin;

    private TextureAtlas atlas;

    private SkinLibrary skinLibrary;

    private boolean initialized;

    private Color backgroundColor = new Color();

    private List<ScreenMediator> mediators = new ArrayList<ScreenMediator>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // gdxApplication
    //----------------------------------

    @Override
    public IGdxApplication getApplication() {
        return gdxApplication;
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
     * Creates a new {@link GdxScreen}.
     */
    public GdxScreen() {
        stage = new Stage();
        atlas = new TextureAtlas(Gdx.files.internal("skin.atlas"));
        skin = new Skin(atlas);
    }

    //--------------------------------------------------------------------------
    // IGdxScreen API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void initialize(IGdxApplication gdxApplication) {
        this.gdxApplication = gdxApplication;
        initialized = true;
        skinLibrary.initialize(skin);
    }

    @Override
    public void create() {
        Gdx.app.log(LOG, "Creating screen: " + getName());

        // all mediators attach their application events
        for (ScreenMediator mediator : mediators) {
            mediator.onAttach();
        }

        // all mediators create their user interface components
        for (ScreenMediator mediator : mediators) {
            mediator.onCreate();
        }
    }

    @Override
    public void render(float delta) {
        // clear the screen with the given RGB color
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.b, backgroundColor.g,
                backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);

        stage.draw();

        // draw the table debug lines
        Table.drawDebug(stage);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height);
        stage.setViewport(gdxApplication.getWidth(), gdxApplication.getHeight(), true);
        stage.getCamera().translate(-stage.getGutterWidth(), -stage.getGutterHeight(), 0);
    }

    @Override
    public void show() {
        Gdx.app.log(LOG, "Showing screen: " + getName());
        // set the stage as the input processor
        Gdx.input.setInputProcessor(stage);

        for (ScreenMediator mediator : mediators) {
            mediator.onShow();
        }
    }

    @Override
    public void hide() {
        Gdx.app.log(LOG, "Hiding screen: " + getName());

        for (ScreenMediator mediator : mediators) {
            mediator.onHide();
        }
    }

    @Override
    public void pause() {
        Gdx.app.log(LOG, "Pausing screen: " + getName());

        for (ScreenMediator mediator : mediators) {
            mediator.onPause();
        }
    }

    @Override
    public void resume() {
        Gdx.app.log(LOG, "Resuming screen: " + getName());

        for (ScreenMediator mediator : mediators) {
            mediator.onResume();
        }
    }

    @Override
    public void dispose() {
        Gdx.app.log(LOG, "Disposing screen: " + getName());

        // detach all mediators
        for (ScreenMediator mediator : mediators) {
            mediator.onDetach();
        }

        // dispose all mediators
        for (ScreenMediator mediator : mediators) {
            mediator.onDispose();
        }

        mediators.clear();

        if (skin != null)
            skin.dispose();
        if (atlas != null)
            atlas.dispose();
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Adds a mediator to the screen.
     * <p>
     * Call during {@link #initialize(IGdxApplication)} override.
     * <p>
     * The meidator's {@link ScreenMediator#setScreen(IGdxScreen)} will be
     * called during the add logic.
     * 
     * @param mediator The {@link ScreenMediator}.
     */
    protected final void addMediator(ScreenMediator mediator) {
        mediator.setScreen(this);
        mediators.add(mediator);
    }
}
