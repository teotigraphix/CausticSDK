
package com.teotigraphix.libgdx.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.libgdx.application.IGame;
import com.teotigraphix.libgdx.controller.ICaustkMediator;

public class ScreenBase implements IScreen {
    public static final String LOG = ScreenBase.class.getSimpleName();

    // the fixed viewport dimensions (ratio: 1.6)
    public static final int GAME_VIEWPORT_WIDTH = 800, GAME_VIEWPORT_HEIGHT = 240;

    public static final int MENU_VIEWPORT_WIDTH = 1200, MENU_VIEWPORT_HEIGHT = 480;

    private static final boolean DEV_MODE = false;

    protected IGame game;

    protected final Stage stage;

    private BitmapFont font;

    private SpriteBatch batch;

    private Skin skin;

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

    List<ICaustkMediator> mediators = new ArrayList<ICaustkMediator>();

    protected void addMediator(ICaustkMediator mediator) {
        mediators.add(mediator);
    }

    public ScreenBase() {
        int width = (isGameScreen() ? GAME_VIEWPORT_WIDTH : MENU_VIEWPORT_WIDTH);
        int height = (isGameScreen() ? GAME_VIEWPORT_HEIGHT : MENU_VIEWPORT_HEIGHT);
        stage = new Stage(width, height, true);
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
        if (atlas == null) {
            // image-atlases/pages.atlas
            atlas = new TextureAtlas(Gdx.files.internal("game.atlas"));
        }
        return atlas;
    }

    @Override
    public Skin getSkin() {
        if (skin == null) {
            FileHandle skinFile = Gdx.files.internal("skin/uiskin.json");
            skin = new Skin(skinFile, getAtlas());
        }
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

        for (ICaustkMediator mediator : mediators) {
            mediator.create(this);
            // mediator.onRegisterObservers();
            mediator.onRegister(this);
        }
    }

    @Override
    public void show() {
        Gdx.app.log(LOG, "Showing screen: " + getName());

        // set the stage as the input processor
        Gdx.input.setInputProcessor(stage);

        for (ICaustkMediator mediator : mediators) {
            mediator.onShow(this);
        }
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height);
    }

    @Override
    public void render(float delta) {
        // (1) process the game logic

        // update the actors
        stage.act(delta);

        // (2) draw the result

        // clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // draw the actors
        stage.draw();

        // draw the table debug lines
        Table.drawDebug(stage);
    }

    @Override
    public void hide() {
        Gdx.app.log(LOG, "Hiding screen: " + getName());

        // dispose the screen when leaving the screen;
        // note that the dipose() method is not called automatically by the
        // framework, so we must figure out when it's appropriate to call it
        //dispose();
    }

    @Override
    public void pause() {
        Gdx.app.log(LOG, "Pausing screen: " + getName());
    }

    @Override
    public void resume() {
        Gdx.app.log(LOG, "Resuming screen: " + getName());
    }

    @Override
    public void dispose() {
        Gdx.app.log(LOG, "Disposing screen: " + getName());

        for (ICaustkMediator mediator : mediators) {
            mediator.dispose();
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
