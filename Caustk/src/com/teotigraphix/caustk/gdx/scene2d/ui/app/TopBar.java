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

package com.teotigraphix.caustk.gdx.scene2d.ui.app;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.teotigraphix.caustk.gdx.scene2d.ui.ButtonBar;

public class TopBar extends UITable {

    private Table left;

    private Table center;

    private Table right;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // left
    //----------------------------------

    public Table getLeftChild() {
        return left;
    }

    //----------------------------------
    // center
    //----------------------------------

    public Table getCenterChild() {
        return center;
    }

    //----------------------------------
    // right
    //----------------------------------

    public Table getRightChild() {
        return right;
    }

    //----------------------------------
    // buttonBar
    //----------------------------------

    public ButtonBar getButtonBar() {
        return (ButtonBar)center.getChildren().get(0);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public TopBar(Skin skin) {
        super(skin);
        setStyleClass(TopBarStyle.class);
    }

    @Override
    protected void createChildren() {

        TopBarStyle style = getStyle();
        setBackground(style.background);

        left = new Table();
        center = new Table();
        right = new Table();

        add(left).expandY().fillY().width(200f);
        add(center).expandY().fillY().width(400f);
        add(right).expandY().fillY().width(200f);
    }

    //--------------------------------------------------------------------------
    // Style
    //--------------------------------------------------------------------------

    public static class TopBarStyle {

        /**
         * The 9 slice background.
         */
        public Drawable background;

        public TopBarStyle(Drawable background) {
            this.background = background;
        }
    }

}
