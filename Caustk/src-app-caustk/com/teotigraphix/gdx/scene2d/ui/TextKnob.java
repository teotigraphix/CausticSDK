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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class TextKnob extends Knob {

    private Label label;

    //--------------------------------------------------------------------------
    // Public Property :: API
    //--------------------------------------------------------------------------

    private boolean textIsValue;

    public boolean getTextIsValue() {
        return textIsValue;
    }

    public void setTextIsValue(boolean value) {
        textIsValue = value;
        invalidate();
    }

    //----------------------------------
    // text
    //----------------------------------

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String value) {
        text = value;
        invalidate();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public TextKnob(float min, float max, float stepSize, String text, Skin skin, String styleName) {
        super(min, max, stepSize, skin, styleName);
        setText(text);
    }

    //--------------------------------------------------------------------------
    // Overridden :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void initialize() {
        super.initialize();

        styleClass = TextKnobStyle.class;

        row();

        TextKnobStyle style = getStyle();
        label = new Label(text, new LabelStyle(style.font, style.fontColor));
        label.setAlignment(Align.center);
        add(label);
    }

    @Override
    public void layout() {
        super.layout();

        if (!textIsValue) {
            label.setText(text);
        } else {
            // will need some type of rounding
            label.setText(Float.toString(getValue()));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextKnobStyle style = getStyle();
        label.setColor(getColor() != null ? getColor() : style.fontColor);
        super.draw(batch, parentAlpha);
    }

    public static class TextKnobStyle extends KnobStyle {

        /**
         * 
         */
        public BitmapFont font;

        public Color fontColor;

        public TextKnobStyle() {
        }
    }
}
