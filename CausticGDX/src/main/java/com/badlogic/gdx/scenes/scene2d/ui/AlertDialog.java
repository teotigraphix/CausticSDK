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

package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.tablelayout.Cell;
import com.teotigraphix.libgdx.ui.OldSelectButton;

public class AlertDialog extends Dialog {

    private Skin skin;

    public Skin getSkin() {
        return skin;
    }

    private OldSelectButton okButton;

    private OldSelectButton cancelButton;

    private OnAlertDialogListener listener;

    public AlertDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
        this.skin = skin;
        createChildren();
    }

    public AlertDialog(String title, Skin skin) {
        super(title, skin);
        this.skin = skin;
        createChildren();
    }

    public AlertDialog(String title, WindowStyle windowStyle) {
        super(title, windowStyle);
        createChildren();
    }

    protected void createChildren() {
        // create buttons
        okButton = new OldSelectButton("OK", skin);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onOk();
                hide();
            }
        });
        cancelButton = new OldSelectButton("Cancel", skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onCancel();
                hide();
            }
        });

        button(okButton, true);
        button(cancelButton, false);
        buttonTable.getCell(okButton).width(100f);
        buttonTable.getCell(cancelButton).width(100f);
    }

    @Override
    protected void result(Object object) {
        boolean result = (Boolean)object;
        if (result) {
            listener.onOk();
        } else {
            listener.onCancel();
        }
        hide();
    }

    public void setOnAlertDialogListener(OnAlertDialogListener l) {
        listener = l;
    }

    public interface OnAlertDialogListener {
        void onCancel();

        void onOk();
    }

    @SuppressWarnings("rawtypes")
    public Cell setContent(Actor actor) {
        return contentTable.add(actor);
    }

    /*
       public void changed (ChangeEvent event, Actor actor) {
    new Dialog("Some Dialog", skin, "dialog") {
    protected void result (Object object) {
    System.out.println("Chosen: " + object);
    }
    }.text("Are you enjoying this demo?").button("Yes", true).button("No", false).key(Keys.ENTER, true)
    .key(Keys.ESCAPE, false).show(stage);
    } 
       
    */

}
