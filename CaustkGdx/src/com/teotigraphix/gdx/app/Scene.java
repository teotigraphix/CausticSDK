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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.teotigraphix.gdx.skin.SkinLibrary;

/**
 * The {@link Scene} is the base implementation of the {@link IScene} API.
 * <p>
 * An {@link IScene} holds {@link Behavior}s that assemble a view with user
 * interface components. The behavior is responsible for creating user interface
 * components and mediating the component's events.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public abstract class Scene implements IScene {

    public static final String LOG = Scene.class.getSimpleName();

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private IApplication application;

    private Stage stage;

    private Skin skin;

    private Group root;

    private TextureAtlas atlas;

    private SkinLibrary skinLibrary;

    private boolean initialized;

    private Color backgroundColor = new Color();

    private List<ISceneBehavior> behaviors = new ArrayList<ISceneBehavior>();

    protected final List<ISceneBehavior> getBehaviors() {
        return behaviors;
    }

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

    //----------------------------------
    // stage
    //----------------------------------

    @Override
    public Stage getStage() {
        return stage;
    }

    protected void setStage(Stage stage) {
        this.stage = stage;
    }

    //----------------------------------
    // skin
    //----------------------------------

    @Override
    public Skin getSkin() {
        return skin;
    }

    //----------------------------------
    // root
    //----------------------------------

    @Override
    public Group getRoot() {
        return root;
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
     * Creates a new {@link Scene}.
     */
    public Scene() {
        setStage(new Stage(new FitViewport(800f, 480f)));
        atlas = new TextureAtlas(Gdx.files.internal("skin.atlas"));
        skin = new Skin(atlas);
    }

    //--------------------------------------------------------------------------
    // IGdxScreen API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void initialize(IApplication application) {
        this.application = application;
        initialized = true;
        skinLibrary.initialize(skin);
    }

    @Override
    public void create() {
        Gdx.app.log(LOG, ">Creating scene: " + getName());
        root = new WidgetGroup();
        root.setBounds(0f, 0f, application.getWidth(), application.getHeight());
        stage.addActor(root);

        createUI();
        // all behaviors create their user interface components
        for (ISceneBehavior behavior : behaviors) {
            behavior.onStart();
        }
        Gdx.app.log(LOG, "<Behaviors started");
    }

    protected abstract void createUI();

    @Override
    public void start() {
    }

    @Override
    public void render(float delta) {
        // clear the screen with the given RGB color
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.b, backgroundColor.g,
                backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (ISceneBehavior behavior : behaviors) {
            behavior.onUpdate();
        }

        stage.act(delta);

        stage.draw();

        // draw the table debug lines
        Table.drawDebug(stage);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(LOG, "Resizing scene: " + getName() + " to: " + width + " x " + height);
        stage.getViewport().update(width, height);
        // stage.setViewport(application.getWidth(), application.getHeight(), true);
        // stage.getCamera().translate(-stage.getGutterWidth(), -stage.getGutterHeight(), 0);
    }

    @Override
    public void show() {
        Gdx.app.log(LOG, "Showing scene: " + getName());
        // set the stage as the input processor
        Gdx.input.setInputProcessor(stage);

        for (ISceneBehavior behavior : behaviors) {
            behavior.setVisible(true);
        }
    }

    @Override
    public void reset() {
        for (ISceneBehavior behavior : behaviors) {
            behavior.onReset();
        }
    }

    @Override
    public void hide() {
        Gdx.app.log(LOG, "Hiding scene: " + getName());

        for (ISceneBehavior behavior : behaviors) {
            behavior.setVisible(false);
        }
    }

    @Override
    public void pause() {
        Gdx.app.log(LOG, "Pausing scene: " + getName());

        for (ISceneBehavior behavior : behaviors) {
            behavior.onPause();
        }
    }

    @Override
    public void resume() {
        Gdx.app.log(LOG, "Resuming scene: " + getName());

        for (ISceneBehavior behavior : behaviors) {
            behavior.onResume();
        }
    }

    @Override
    public void dispose() {
        Gdx.app.log(LOG, "Disposing scene: " + getName());

        // disable all behaviors
        for (ISceneBehavior behavior : behaviors) {
            behavior.onDisable();
        }

        // dispose all behaviors
        for (ISceneBehavior behavior : behaviors) {
            behavior.onDestroy();
        }

        behaviors.clear();

        if (skin != null)
            skin.dispose();
        if (atlas != null)
            atlas.dispose();
    }

    @Override
    public void onBeatChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior behavior : behaviors) {
            behavior.onBeatChange(measure, beat, sixteenth, thirtysecond);
        }
    }

    @Override
    public void onSixteenthChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior behavior : behaviors) {
            behavior.onSixteenthChange(measure, beat, sixteenth, thirtysecond);
        }
    }

    @Override
    public void onThirtysecondChange(int measure, float beat, int sixteenth, int thirtysecond) {
        for (ISceneBehavior behavior : behaviors) {
            behavior.onThirtysecondChange(measure, beat, sixteenth, thirtysecond);
        }
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Adds a behavior to the screen.
     * <p>
     * Call during {@link #initialize(IApplication)} override.
     * <p>
     * The behavior's {@link SceneComponent#setApplication(IApplication)} and
     * {@link SceneComponent#setScene(IScene)} will be called during the add
     * logic.
     * 
     * @param component The {@link ISceneBehavior}.
     * @see ISceneBehavior#onAwake()
     */
    protected final void addComponent(ISceneBehavior component) {
        ((SceneComponent)component).setApplication(getApplication());
        ((SceneComponent)component).setScene(this);
        behaviors.add(component);
        // all behaviors attach their application events
        component.onAwake();
    }
}
