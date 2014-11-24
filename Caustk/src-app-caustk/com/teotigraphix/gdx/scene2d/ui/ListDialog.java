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

package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ListDialog extends AlertDialog {

    List list;

    Object[] items;

    public Object[] getItems() {
        return items;
    }

    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    public void setItems(Object[] items) {
        //debug();
        this.items = items;
        list = new List(getSkin());
        list.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });
        list.setItems(items);

        ScrollPane pane = new ScrollPane(list, getSkin());
        pane.setOverscroll(false, false);
        setContent(pane);
    }

    public ListDialog(String title, Skin skin) {
        super(title, skin);
    }

    public ListDialog(String title, WindowStyle windowStyle) {
        super(title, windowStyle);
    }

    @Override
    protected void createChildren() {
        super.createChildren();
    }

}
