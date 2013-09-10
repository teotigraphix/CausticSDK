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

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * The {@link OldSelectButton} disables the checked property when
 * {@link #setToggle(boolean)} is set.
 * <p>
 * The {@link OldSelectButton} is not a toggle button.
 */
public class OldSelectButton extends TextButton {

    //----------------------------------
    // properties
    //----------------------------------

    Map<String, Object> properties = new HashMap<String, Object>();

    public final Map<String, Object> getProperties() {
        return properties;
    }

    //----------------------------------
    // isToggle
    //----------------------------------

    boolean isToggle = false;

    private SelectButtonStyle style;

    public boolean isToggle() {
        return isToggle;
    }

    public void setToggle(boolean value) {
        isToggle = value;
    }

    //----------------------------------
    // checked
    //----------------------------------

    @Override
    public void setChecked(boolean value) {
        if (isToggle) {
            super.setChecked(value);
        }
    }

    //----------------------------------
    // progress
    //----------------------------------

    private float progress = 100f;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float value) {
        progress = value;
        invalidate();
    }

    //----------------------------------
    // isProgress
    //----------------------------------

    private boolean isProgress = false;

    public boolean isProgress() {
        return isProgress;
    }

    public void setIsProgress(boolean value) {
        isProgress = value;
        invalidate();
    }

    @Override
    public SelectButtonStyle getStyle() {
        return style;
    }

    protected Class<? extends SelectButtonStyle> getStyleType() {
        return SelectButtonStyle.class;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public OldSelectButton(String text, Skin skin) {
        super(text, skin);
        // calls setStyle()
        style = skin.get(getStyleType());
        init();
    }

    public OldSelectButton(String text, SelectButtonStyle style) {
        super(text, style);
        // special constructor
        this.style = style;
        init();
    }

    public OldSelectButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
        // calls setStyle()
        style = skin.get(styleName, getStyleType());
        init();
    }

    protected void init() {
        isToggle = true;
    }

    //--------------------------------------------------------------------------
    // Overridden Public :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Drawable progressOverlay = style.progressOverlay;
        if (isProgress) {
            float width = (progress / 100f) * getWidth();
            progressOverlay.draw(batch, getX(), getY(), width, getHeight());
        }
    }

    //--------------------------------------------------------------------------
    // Style
    //--------------------------------------------------------------------------

    public static class SelectButtonStyle extends TextButtonStyle {

        public Drawable progressOverlay;

        public SelectButtonStyle() {
        }

        public SelectButtonStyle(Drawable up, Drawable down, Drawable checked, BitmapFont font) {
            super(up, down, checked, font);
        }

        public SelectButtonStyle(TextButtonStyle style) {
            super(style);
        }

    }
}
