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

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.gdx.groove.ui.components.ModePaneListener.ModePaneEvent;
import com.teotigraphix.caustk.gdx.groove.ui.components.ModePaneListener.ModePaneEventKind;
import com.teotigraphix.caustk.gdx.scene2d.ui.ButtonBar;
import com.teotigraphix.caustk.gdx.scene2d.ui.ButtonBar.ButtonBarItem;
import com.teotigraphix.caustk.gdx.scene2d.ui.ButtonBarListener;

public class ModePane extends UITable {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private Array<ButtonBarItem> items;

    private ButtonBar buttonBar;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ModePane(Skin skin, Array<ButtonBarItem> items) {
        super(skin);
        this.items = items;
        setStyleClass(ModePaneStyle.class);
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    public void select(int index) {
        buttonBar.select(index, true);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createChildren() {
        ModePaneStyle style = getStyle();

        setBackground(style.backgorund);

        buttonBar = new ButtonBar(getSkin(), items, true, style.buttonStyle);
        buttonBar.setGap(style.buttonGap);
        buttonBar.addListener(new ButtonBarListener() {
            @Override
            public void selectedIndexChange(int selectedIndex) {
                ModePaneEvent event = new ModePaneEvent(ModePaneEventKind.selectedIndexChange,
                        selectedIndex);
                fire(event);
            }
        });

        buttonBar.create("default");

        add(buttonBar).expand().fill();
    }

    //--------------------------------------------------------------------------
    // Style
    //--------------------------------------------------------------------------

    public static class ModePaneStyle {

        public float buttonGap;

        public TextButtonStyle buttonStyle;

        /**
         * Optional.
         */
        public Drawable backgorund;

        public ModePaneStyle(TextButtonStyle buttonStyle, float buttonGap) {
            this.buttonStyle = buttonStyle;
            this.buttonGap = buttonGap;
        }
    }

}
