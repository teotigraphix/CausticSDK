
package com.teotigraphix.libgdx.dialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.AlertDialog;
import com.badlogic.gdx.scenes.scene2d.ui.ListDialog;
import com.teotigraphix.libgdx.screen.IScreen;

public interface IDialogManager {

    AlertDialog createAlert(IScreen screen, String title, Actor actor);

    ListDialog createListDialog(IScreen screen, String title, Object[] items);

}
