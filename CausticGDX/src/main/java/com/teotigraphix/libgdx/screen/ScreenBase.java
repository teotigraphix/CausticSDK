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

package com.teotigraphix.libgdx.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.libgdx.application.IGame;
import com.teotigraphix.libgdx.controller.ScreenMediator;

public class ScreenBase implements IScreen {
    public static final String LOG = ScreenBase.class.getSimpleName();

    // the fixed viewport dimensions (ratio: 1.6)
    public static final int GAME_VIEWPORT_WIDTH = 800, GAME_VIEWPORT_HEIGHT = 240;

    public static final int MENU_VIEWPORT_WIDTH = 1200, MENU_VIEWPORT_HEIGHT = 480;

    private static final boolean DEV_MODE = false;

    protected Color backgroundColor = new Color();

    protected IGame game;

    protected final Stage stage;

    private BitmapFont font;

    private SpriteBatch batch;

    private static Skin skin;

    private boolean initialized;

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public IGame getGame() {
        return game;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    private TextureAtlas atlas;

    private Table table;

    List<ScreenMediator> mediators = new ArrayList<ScreenMediator>();

    protected void addMediator(ScreenMediator mediator) {
        mediators.add(mediator);
    }

    public ScreenBase() {
        //int width = (isGameScreen() ? GAME_VIEWPORT_WIDTH : MENU_VIEWPORT_WIDTH);
        //int height = (isGameScreen() ? GAME_VIEWPORT_HEIGHT : MENU_VIEWPORT_HEIGHT);
        //stage = new Stage(width, height, true);
        stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        // set the stage as the input processor
        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas(Gdx.files.internal("game.atlas"));
        skin = new Skin(atlas);
    }

    protected String getName() {
        return getClass().getSimpleName();
    }

    protected boolean isGameScreen() {
        return false;
    }

    // Lazily loaded collaborators

    public BitmapFont getFont() {
        if (font == null) {
            font = new BitmapFont();
        }
        return font;
    }

    public SpriteBatch getBatch() {
        if (batch == null) {
            batch = new SpriteBatch();
        }
        return batch;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public Skin getSkin() {
        return skin;
    }

    protected Table getTable() {
        if (table == null) {
            table = new Table(getSkin());
            table.setFillParent(true);
            if (DEV_MODE) {
                table.debug();
            }
            stage.addActor(table);
        }
        return table;
    }

    // Screen implementation

    @Override
    public void create() {
        Gdx.app.log(LOG, "Creating screen: " + getName());

        for (ScreenMediator mediator : mediators) {
            mediator.onInitialize(this);
            mediator.onAttach(this);
            mediator.onCreate(this);
        }
    }

    @Override
    public void show() {
        Gdx.app.log(LOG, "Showing screen: " + getName());

        for (ScreenMediator mediator : mediators) {
            mediator.onShow(this);
        }
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height);
    }

    @Override
    public void render(float delta) {

        // clear the screen with the given RGB color
        //        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.b, backgroundColor.g,
        //                backgroundColor.a);
        //        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        stage.act(delta);

        stage.draw();

        // draw the table debug lines
        Table.drawDebug(stage);
    }

    @Override
    public void hide() {
        Gdx.app.log(LOG, "Hiding screen: " + getName());

        for (ScreenMediator mediator : mediators) {
            mediator.onHide(this);
        }

        // dispose the screen when leaving the screen;
        // note that the dipose() method is not called automatically by the
        // framework, so we must figure out when it's appropriate to call it
        //dispose();
    }

    @Override
    public void pause() {
        Gdx.app.log(LOG, "Pausing screen: " + getName());

        for (ScreenMediator mediator : mediators) {
            mediator.onPause(this);
        }
    }

    @Override
    public void resume() {
        Gdx.app.log(LOG, "Resuming screen: " + getName());

        for (ScreenMediator mediator : mediators) {
            mediator.onResume(this);
        }
    }

    @Override
    public void dispose() {
        Gdx.app.log(LOG, "Disposing screen: " + getName());

        for (ScreenMediator mediator : mediators) {
            mediator.onDetach(this);
            mediator.onDispose(this);
        }

        mediators.clear();

        // the following call disposes the screen's stage, but on my computer it
        // crashes the game so I commented it out; more info can be found at:
        // http://www.badlogicgames.com/forum/viewtopic.php?f=11&t=3624
        // stage.dispose();

        // as the collaborators are lazily loaded, they may be null
        if (font != null)
            font.dispose();
        if (batch != null)
            batch.dispose();
        if (skin != null)
            skin.dispose();
        if (atlas != null)
            atlas.dispose();
    }

    @Override
    public void initialize(IGame game) {
        initialized = true;
        this.game = game;
    }
}
