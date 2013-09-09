
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.graphics.Color;
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
    }

    //--------------------------------------------------------------------------
    // Overridden :: Methods
    //--------------------------------------------------------------------------

    @Override
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
        add(slider);
        row();
        label = new Label(text, new LabelStyle(style.font, style.fontColor));
        add(label);
    }

    //--------------------------------------------------------------------------
    // Style
    //--------------------------------------------------------------------------

    public static class TextSliderStyle extends SliderStyle {
        public BitmapFont font;

        public Color fontColor;

        public TextSliderStyle() {
        }

        public TextSliderStyle(Drawable background, Drawable knob) {
            super(background, knob);
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
}
