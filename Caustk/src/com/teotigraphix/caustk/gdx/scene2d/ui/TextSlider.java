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

package com.teotigraphix.caustk.gdx.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class TextSlider extends ControlTable {

    private String text;

    private float minimum;

    private float maximum;

    private float stepSize;

    private boolean vertical;

    //----------------------------------
    // slider
    //----------------------------------

    private Slider slider;

    public Slider getSlider() {
        return slider;
    }

    //----------------------------------
    // label
    //----------------------------------

    private Label label;

    public Label getLabel() {
        return label;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public TextSlider(String text, float minimum, float maximum, float stepSize, boolean vertical,
            Skin skin) {
        super(skin);
        this.text = text;
        this.minimum = minimum;
        this.maximum = maximum;
        this.stepSize = stepSize;
        this.vertical = vertical;
        styleClass = TextSliderStyle.class;
        String name = (vertical) ? "vertical" : "horizontal";
        setStyleName("default-" + name);
        createChildren();
    }

    //--------------------------------------------------------------------------
    // Overridden :: Methods
    //--------------------------------------------------------------------------

    protected void createChildren() {
        TextSliderStyle style = getStyle();

        slider = new Slider(minimum, maximum, stepSize, vertical, style);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (onTextSliderListener != null)
                    onTextSliderListener.onChange(slider.getValue());
            }
        });
        add(slider).fill().expand().pad(4f);
        row();
        label = new Label(text, new LabelStyle(style.font, style.fontColor));
        add(label);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        slider.setColor(getColor());
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Returns the value of the
     * {@link com.badlogic.gdx.scenes.scene2d.ui.Slider} composite.
     */
    @Override
    public float getValue() {
        return slider.getValue();
    }

    @Override
    public boolean setValue(float value) {
        slider.setValue(value);
        return true;
    }

    public void setRange(float minimum, float maximum) {
        slider.setRange(minimum, maximum);
    }

    //--------------------------------------------------------------------------
    // Style
    //--------------------------------------------------------------------------

    public static class TextSliderStyle extends SliderStyle {
        public BitmapFont font;

        public Color fontColor;

        public TextSliderStyle() {
        }

        public TextSliderStyle(Drawable background, Drawable knob, BitmapFont font, Color fontColor) {
            super(background, knob);
            this.font = font;
            this.fontColor = fontColor;
        }
    }

    //--------------------------------------------------------------------------
    // Listeners
    //--------------------------------------------------------------------------

    private OnTextSliderListener onTextSliderListener;

    public void setOnTextSliderListener(OnTextSliderListener l) {
        onTextSliderListener = l;
    }

    public interface OnTextSliderListener {
        void onChange(float value);
    }

    @Override
    public String getHelpText() {
        // TODO Auto-generated method stub
        return null;
    }

}
