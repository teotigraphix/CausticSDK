
package com.teotigraphix.libgdx.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teotigraphix.libgdx.application.IGame;

public interface IScreen extends Screen {

    boolean isInitialized();

    void initialize(IGame gdxGame);

    Skin getSkin();

    Stage getStage();

    IGame getGame();

    void create();

}
