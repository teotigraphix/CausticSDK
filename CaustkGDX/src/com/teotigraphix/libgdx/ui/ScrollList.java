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

package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.AdvancedList;
import com.badlogic.gdx.scenes.scene2d.ui.ListRowRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class ScrollList extends ScrollPane {

    private Skin skin;

    private Array<?> items;

    public Array<?> getItems() {
        return items;
    }

    public void setItems(Array<?> scenes) {
        items = scenes;
        if (list == null) {
            list = new AdvancedList<LabelRow>(scenes.toArray(), LabelRow.class);
            list.createChildren(skin);
            setWidget(list);
        } else {
            //list.setItems(scenes.toArray());
        }
    }

    private AdvancedList<LabelRow> list;

    public static class LabelRow extends ListRowRenderer {

        public LabelRow(Skin skin, String styleName) {
            super(skin, styleName);
        }

        public LabelRow(Skin skin) {
            super(skin);
        }

    }

    public ScrollList(Skin skin) {
        super(null, skin);
        this.skin = skin;
        initialize();
    }

    public ScrollList(Skin skin, Array<?> items) {
        super(null, skin);
        this.skin = skin;
        this.items = items;
        initialize();
    }

    public ScrollList(Actor widget, Skin skin, String styleName) {
        super(widget, skin, styleName);
        this.skin = skin;
        initialize();
    }

    private void initialize() {
        if (items != null) {
            list = new AdvancedList<LabelRow>(items.toArray(), LabelRow.class);
            setWidget(list);
        }
    }

    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    public void setSelectedIndex(int value) {
        //list.setSelectedIndex(value);
    }

    public Object getItem(int index) {
        return items.get(index);
    }

    public Object getSelectedItem() {
        return getItem(getSelectedIndex());
    }
}
