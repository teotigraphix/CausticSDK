
package com.teotigraphix.libgdx.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.AlertDialog;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ListDialog;
import com.google.inject.Singleton;
import com.teotigraphix.libgdx.screen.IScreen;

@Singleton
public class DialogManager implements IDialogManager {

    public DialogManager() {
    }

    public void center(Dialog dialog) {
        dialog.setX(Gdx.graphics.getWidth() / 4);
        dialog.setY(Gdx.graphics.getHeight() / 3);
    }

    @Override
    public ListDialog createListDialog(IScreen screen, String title, Object[] items) {
        final ListDialog dialog = new ListDialog(title, screen.getSkin());
        dialog.setItems(items);
        dialog.setMovable(false);
        dialog.setSize(500f, 400f);
        center(dialog);
        return dialog;
    }

    @Override
    public AlertDialog createAlert(IScreen screen, String title, Actor actor) {
        final AlertDialog dialog = new AlertDialog(title, screen.getSkin());
        dialog.setContent(actor);
        dialog.setMovable(false);
        dialog.setSize(500f, 400f);
        center(dialog);
        return dialog;
    }
}
