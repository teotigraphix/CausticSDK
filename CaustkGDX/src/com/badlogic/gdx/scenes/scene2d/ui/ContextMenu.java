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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ContextMenu extends Dialog {

    private Skin skin;

    private List list;

    private Object[] items;

    public Object[] getItems() {
        return items;
    }

    public void setItems(Object[] items) {
        this.items = items;
        //debug();
        list = new List(items, skin);
        list.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listener.onSelect(list.getSelectedIndex());
            }
        });
        list.setSelectedIndex(-1);
        contentTable.add(list).fill().expand();
    }

    public ContextMenu(WindowStyle windowStyle) {
        super("", windowStyle);
    }

    public ContextMenu(Skin skin, String windowStyleName) {
        super("", skin, windowStyleName);
        this.skin = skin;
    }

    @Override
    public void hide() {
        super.hide();
        listener.onSelect(-1);
    }

    private OnContextMenuListener listener;

    public void setOnContextMenuListener(OnContextMenuListener l) {
        this.listener = l;
    }

    public interface OnContextMenuListener {
        /**
         * @param selectedIndex The selectedIndex choosen when the context menu
         *            is hidden, -1 for dismissal of the menu with no selection.
         */
        void onSelect(int selectedIndex);
    }
}
