////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.gdx.groove.ui.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.teotigraphix.caustk.gdx.groove.ui.components.ViewStackListener.ViewStackEvent;
import com.teotigraphix.caustk.gdx.groove.ui.components.ViewStackListener.ViewStackEventKind;

public class ViewStack extends UITable {

    private Stack stack;

    // private ViewStackData data;

    private int selectedIndex = 0;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // data
    //----------------------------------

    //    public ViewStackData getData() {
    //        return data;
    //    }
    //
    //    public void setData(ViewStackData data) {
    //        this.data = data;
    //    }

    //----------------------------------
    // selectedIndex
    //----------------------------------

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int value) {
        if (value == selectedIndex)
            return;
        // XXX Possibly think about a fade or slide transition
        int old = selectedIndex;
        selectedIndex = value;
        ViewStackEvent event = new ViewStackEvent(ViewStackEventKind.SelectedIndexChange,
                selectedIndex, old);
        if (fire(event)) {
            selectedIndex = old;
        }
        invalidate();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ViewStack(Skin skin) {
        super(skin);
        setSkin(skin);
        setStyleClass(ViewStackStyle.class);

        // needs access
        stack = new Stack();
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    public void addView(Actor actor) {
        stack.add(actor);
        actor.setVisible(false);
    }

    //--------------------------------------------------------------------------
    // Overridden Public :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void layout() {
        SnapshotArray<Actor> children = stack.getChildren();
        if (children.size > 0) {
            for (Actor actor : stack.getChildren()) {
                actor.setVisible(false);
            }
            stack.getChildren().get(selectedIndex).setVisible(true);
        }
        super.layout();
    }

    @Override
    protected void createChildren() {
        ViewStackStyle style = getStyle();
        setBackground(style.background);

        add(stack).expand().fill();
    }

    public static class ViewStackStyle {

        public Drawable background;

        public ViewStackStyle(Drawable background) {
            this.background = background;
        }
    }
}
