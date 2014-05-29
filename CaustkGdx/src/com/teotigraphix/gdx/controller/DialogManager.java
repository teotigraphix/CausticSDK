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

package com.teotigraphix.gdx.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.app.IScene;
import com.teotigraphix.gdx.scene2d.ui.PopUp;

@Singleton
public class DialogManager implements IDialogManager {

    public DialogManager() {
    }

    public void center(Dialog dialog) {
        dialog.setX(Gdx.graphics.getWidth() / 4);
        dialog.setY(Gdx.graphics.getHeight() / 3);
    }

    //    @Override
    //    public ListDialog createListDialog(String title, Object[] items, float width, float height) {
    //        final ListDialog dialog = new ListDialog(title, screenProvider.getScreen().getSkin());
    //        dialog.setSize(width, height);
    //        dialog.setItems(items);
    //        dialog.setMovable(false);
    //        return dialog;
    //    }

    //    @Override
    //    public ListDialog createListDialog(IScreen screen, String title, Object[] items, float width,
    //            float height) {
    //        final ListDialog dialog = new ListDialog(title, screen.getSkin());
    //        dialog.setSize(width, height);
    //        dialog.setItems(items);
    //        dialog.setMovable(false);
    //        return dialog;
    //    }

    //    @Override
    //    public AlertDialog createAlert(IScreen screen, String title, Actor actor) {
    //        final AlertDialog dialog = new AlertDialog(title, screen.getSkin());
    //        dialog.setContent(actor);
    //        dialog.setMovable(false);
    //        return dialog;
    //    }

    //    @Override
    //    public ContextMenu createContextMenu(Object[] items) {
    //        ContextMenu menu = new ContextMenu(screenProvider.getScreen().getSkin(), "default");
    //        menu.setItems(items);
    //        return menu;
    //    }

    @Override
    public PopUp createPopUp(IScene scene, String title, Actor actor) {
        final PopUp dialog = new PopUp(title, scene.getSkin());
        dialog.setModal(false);
        dialog.setMovable(true);
        return dialog;
    }

    @Override
    public void createToast(IScene scene, String message, float duration) {
        final PopUp popUp = createPopUp(scene, "", null);
        WindowStyle style = scene.getSkin().get("toast", WindowStyle.class);
        popUp.setStyle(style);
        popUp.setMovable(false);
        popUp.clearChildren();
        popUp.add(new Label(message, new LabelStyle(style.titleFont, style.titleFontColor)));
        popUp.show(scene.getStage());
        popUp.addAction(Actions.delay(duration, new Action() {
            @Override
            public boolean act(float delta) {
                popUp.hide();
                return true;
            }
        }));
    }

    @Override
    public void show(IScene scene, final Dialog dialog, Vector2 point) {
        dialog.show(scene.getStage());
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
    public void show(IScene scene, Dialog dialog) {
        dialog.show(scene.getStage());
    }
}
