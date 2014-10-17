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

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ToggleButtonInternal;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

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
public class ToggleButton extends ToggleButtonInternal {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private boolean isToggle = true;

    @SuppressWarnings("unused")
    private Drawable imageDrawable;

    private Image image;

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

    //----------------------------------
    // imageDrawable
    //----------------------------------

    public void setImageDrawable(Drawable imageDrawable) {
        this.imageDrawable = imageDrawable;
        image = new Image(imageDrawable);
        image.setScaling(Scaling.none);
        addActor(image);
        invalidate();
    }

    @Override
    public void setChecked(boolean isChecked) {
        if (!isToggle)
            return;
        super.setChecked(isChecked);
    }

    @Override
    public void setChecked(boolean isChecked, boolean fireEvent) {
        if (!isToggle)
            return;
        super.setChecked(isChecked, fireEvent);
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

    @Override
    public void layout() {
        super.layout();
        if (image != null)
            image.setBounds(0f, 0f, getWidth(), getHeight());
    }

}
