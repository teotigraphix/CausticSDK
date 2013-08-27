
package com.teotigraphix.libgdx.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.google.inject.Singleton;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.Button;

@Singleton
public class DialogManager implements IDialogManager {

    IScreen screen;

    public IScreen getScreen() {
        return screen;
    }

    public void setScreen(IScreen screen) {
        this.screen = screen;
    }

    public DialogManager() {
    }

    public void create() {
        final Window window = new Window("Main Menu", screen.getSkin());
        window.setMovable(false);
        //window.setSize(400, 240);
        window.setX(Gdx.graphics.getWidth() / 4);
        window.setY(Gdx.graphics.getHeight() / 3);
        screen.getStage().addActor(window);
        window.getColor().a = 0f;
        window.addAction(Actions.fadeIn(0.25f));

        Button okButton = new Button("OK", screen.getSkin());
        Button cancelButton = new Button("Cancel", screen.getSkin());
        okButton.setSize(100f, 40f);
        cancelButton.setSize(100f, 40f);
        window.defaults().spaceBottom(10);
        window.row().fill().expandX();
        window.add(okButton).fill(0f, 0f).width(100f).height(30f);
        window.add(cancelButton).fill(0f, 0f).width(100f);
        window.row();
        window.setSize(window.getPrefWidth(), window.getPrefHeight());
    }
}
