
package com.teotigraphix.caustk.gs.view.keyboardControls;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.tablelayout.Cell;
import com.teotigraphix.caustk.gs.view.GrooveBoxMediatorChild;
import com.teotigraphix.caustk.workstation.GrooveBox.KeyboardMode;
import com.teotigraphix.libgdx.screen.IScreen;

public class KeyboardButtonBarMediator extends GrooveBoxMediatorChild {

    private TextButton shiftToggle;

    private TextButton keyBoardToggle;

    public KeyboardButtonBarMediator() {
    }

    @Override
    public void onCreate(IScreen screen, Cell<Actor> parent) {
        parent.pad(5f);

        Table container = new Table();

        shiftToggle = new TextButton("SHIFT", screen.getSkin());
        shiftToggle.setDisabled(true);
        shiftToggle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Button button = (Button)actor;
                if (button.isChecked()) {
                    fireModeChange(KeyboardMode.Shift);
                } else {
                    if (keyBoardToggle.isChecked()) {
                        fireModeChange(KeyboardMode.Key);
                    } else {
                        fireModeChange(KeyboardMode.Step);
                    }
                }
            }
        });
        container.add(shiftToggle);

        keyBoardToggle = new TextButton("KEYBOARD", screen.getSkin());
        keyBoardToggle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (shiftToggle.isChecked()) {
                    event.cancel();
                    return;
                }
                Button button = (Button)actor;
                if (button.isChecked()) {
                    fireModeChange(KeyboardMode.Key);
                } else {
                    if (shiftToggle.isChecked()) {
                        fireModeChange(KeyboardMode.Shift);
                    } else {
                        fireModeChange(KeyboardMode.Step);
                    }
                }
            }
        });

        container.add(keyBoardToggle).pad(5f);

        parent.setWidget(container);
    }

    private void fireModeChange(KeyboardMode mode) {
        getGrooveBox().setKeyboardMode(mode);
    }
}
