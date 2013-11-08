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

package com.teotigraphix.libgdx.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.AlertDialog;
import com.badlogic.gdx.scenes.scene2d.ui.ContextMenu;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ListDialog;
import com.badlogic.gdx.scenes.scene2d.ui.PopUp;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.libgdx.scene2d.IScreenProvider;
import com.teotigraphix.libgdx.screen.IScreen;

@Singleton
public class DialogManager implements IDialogManager {

    @Inject
    IScreenProvider screenProvider;

    public DialogManager() {
    }

    public void center(Dialog dialog) {
        dialog.setX(Gdx.graphics.getWidth() / 4);
        dialog.setY(Gdx.graphics.getHeight() / 3);
    }

    @Override
    public ListDialog createListDialog(String title, Object[] items, float width, float height) {
        final ListDialog dialog = new ListDialog(title, screenProvider.getScreen().getSkin());
        dialog.setSize(width, height);
        dialog.setItems(items);
        dialog.setMovable(false);
        return dialog;
    }

    @Override
    public ListDialog createListDialog(IScreen screen, String title, Object[] items, float width,
            float height) {
        final ListDialog dialog = new ListDialog(title, screen.getSkin());
        dialog.setSize(width, height);
        dialog.setItems(items);
        dialog.setMovable(false);
        return dialog;
    }

    @Override
    public void createToast(String message, float duration) {
        final IScreen screen = screenProvider.getScreen();
        final PopUp popUp = createPopUp(screen, "", null);
        popUp.add(new Label(message, screen.getSkin()));

        popUp.show(screen.getStage());
        popUp.pad(0f);
        popUp.addAction(Actions.delay(duration, new Action() {
            @Override
            public boolean act(float delta) {
                popUp.hide();
                return true;
            }
        }));
    }

    @Override
    public PopUp createPopUp(IScreen screen, String title, Actor actor) {
        final PopUp dialog = new PopUp(title, screen.getSkin());
        dialog.setModal(false);
        //dialog.setContent(actor);
        dialog.setMovable(true);
        return dialog;
    }

    @Override
    public AlertDialog createAlert(IScreen screen, String title, Actor actor) {
        final AlertDialog dialog = new AlertDialog(title, screen.getSkin());
        dialog.setContent(actor);
        dialog.setMovable(false);
        return dialog;
    }

    @Override
    public ContextMenu createContextMenu(Object[] items) {
        ContextMenu menu = new ContextMenu(screenProvider.getScreen().getSkin(), "default");
        menu.setItems(items);
        return menu;
    }

    @Override
    public void show(final Dialog dialog, Vector2 point) {
        dialog.show(screenProvider.getScreen().getStage());
        dialog.setWidth(150f);
        dialog.setPosition(point.x, point.y - dialog.getHeight());
        dialog.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dialog.hide();
                return true;
            }
        });
    }

    @Override
    public void show(Dialog dialog) {
        dialog.show(screenProvider.getScreen().getStage());
    }
}
