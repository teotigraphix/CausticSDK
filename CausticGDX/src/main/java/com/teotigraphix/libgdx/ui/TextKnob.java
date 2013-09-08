
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.graphics.Color;
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

    public TextKnob(float min, float max, float stepSize, Skin skin) {
        super(min, max, stepSize, skin);
        styleClass = TextKnobStyle.class;
    }

    public TextKnob(float min, float max, float stepSize, String text, Skin skin) {
        this(min, max, stepSize, skin);
        setText(text);
    }

    //--------------------------------------------------------------------------
    // Overridden :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createChildren() {
        super.createChildren();

        row();

        TextKnobStyle style = getStyle();
        label = new Label(text, new LabelStyle(style.font, style.fontColor));
        label.setAlignment(Align.center);
        add(label);
    }

    @Override
    protected void commitProperties() {
        if (!textIsValue) {
            label.setText(text);
        } else {
            // will need some type of rounding
            label.setText(Float.toString(getValue()));
        }
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
