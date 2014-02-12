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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class ScrollList extends ScrollPane {

    private Skin skin;

    private Array<?> items;

    private AdvancedList<LabelRow> list;

    //----------------------------------
    // items
    //----------------------------------

    public Array<?> getItems() {
        return items;
    }

    public void setItems(Array<?> items) {
        this.items = items;
        if (list == null) {
            list = new AdvancedList<LabelRow>(items.toArray(), LabelRow.class, skin);
            list.createChildren(skin);
            setWidget(list);
        } else {
            list.setItems(items.toArray());
        }
    }

    public Object getItem(int index) {
        return items.get(index);
    }

    //----------------------------------
    // selectedIndex
    //----------------------------------

    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    public void setSelectedIndex(int value) {
        list.setSelectedIndex(value);
    }

    //----------------------------------
    // selectedItem
    //----------------------------------

    public Object getSelectedItem() {
        return getItem(getSelectedIndex());
    }

    public void setSelectable(boolean selectable) {
        list.setSelectable(selectable);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

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

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void refresh() {
        list.refresh();
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    private void initialize() {
        setFadeScrollBars(false);
        setFlickScroll(false);
        list = new AdvancedList<LabelRow>(new Object[] {}, LabelRow.class, skin);
        list.createChildren(skin);
        setWidget(list);
    }

    public static class LabelRow extends ListRowRenderer {

        public LabelRow(Skin skin, String styleName) {
            super(skin, styleName);
        }

        public LabelRow(Skin skin) {
            super(skin);
        }
    }
}
