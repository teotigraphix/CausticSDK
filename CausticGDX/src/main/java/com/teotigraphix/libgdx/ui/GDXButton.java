
package com.teotigraphix.libgdx.ui;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * The {@link GDXButton} disables the checked property when
 * {@link #setToggle(boolean)} is set.
 * <p>
 * The {@link GDXButton} is not a toggle button.
 */
public class GDXButton extends TextButton {

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

    private ButtonStyle style;

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
    public ButtonStyle getStyle() {
        return style;
    }

    protected Class<? extends ButtonStyle> getStyleType() {
        return ButtonStyle.class;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public GDXButton(String text, Skin skin) {
        super(text, skin);
        // calls setStyle()
        style = skin.get(getStyleType());
        init();
    }

    public GDXButton(String text, ButtonStyle style) {
        super(text, style);
        // special constructor
        this.style = style;
        init();
    }

    public GDXButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
        // calls setStyle()
        style = skin.get(styleName, getStyleType());
        init();
    }

    protected void init() {
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

    public static class ButtonStyle extends TextButtonStyle {

        public Drawable progressOverlay;

        public ButtonStyle() {
        }

        public ButtonStyle(Drawable up, Drawable down, Drawable checked, BitmapFont font) {
            super(up, down, checked, font);
        }

        public ButtonStyle(TextButtonStyle style) {
            super(style);
        }

    }
}
