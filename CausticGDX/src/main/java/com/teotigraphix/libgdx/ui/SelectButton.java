
package com.teotigraphix.libgdx.ui;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * The {@link SelectButton} disables the checked property when
 * {@link #setToggle(boolean)} is set.
 * <p>
 * The {@link SelectButton} is not a toggle button.
 */
public class SelectButton extends TextButton {

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

    public SelectButton(String text, Skin skin) {
        super(text, skin);
        // calls setStyle()
        style = skin.get(getStyleType());
        init();
    }

    public SelectButton(String text, SelectButtonStyle style) {
        super(text, style);
        // special constructor
        this.style = style;
        init();
    }

    public SelectButton(String text, Skin skin, String styleName) {
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
