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

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

/**
 * The {@link ToggleButton} simply bypasses the
 * {@link TextButton#setChecked(boolean)} if not a toggle.
 * <p>
 * The widget also adds a {@link #check(boolean)} method to bypass events and
 * just update ui.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class ToggleButton extends TextButton {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    boolean isToggle = true;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // isToggle
    //----------------------------------

    /**
     * Whether the button is a toggle.
     */
    public boolean isToggle() {
        return isToggle;
    }

    /**
     * Sets whether the button will toggle.
     * 
     * @param value Button toggle.
     */
    public void setToggle(boolean value) {
        isToggle = value;
    }

    @Override
    public void setChecked(boolean isChecked) {
        if (!isToggle)
            return;
        super.setChecked(isChecked);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public ToggleButton(String text, Skin skin) {
        super(text, skin);
    }

    public ToggleButton(String text, TextButtonStyle style) {
        super(text, style);
    }

    public ToggleButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Checks the button without dispatching a {@link ChangeEvent} event.
     * 
     * @param checked Whether the ui shows checked or unchecked.
     */
    public void check(boolean checked) {
        // event is not posted when widget is disabled
        setDisabled(true);
        setChecked(checked);
        setDisabled(false);
    }
}
