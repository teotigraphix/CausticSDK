
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

    Map<String, Object> properties = new HashMap<String, Object>();

    public final Map<String, Object> getProperties() {
        return properties;
    }

    boolean isToggle = false;

    private ButtonStyle style;

    public boolean isToggle() {
        return isToggle;
    }

    public void setToggle(boolean value) {
        isToggle = value;
    }

    @Override
    public void setChecked(boolean value) {
        if (isToggle) {
            super.setChecked(value);
        }
    }

    private boolean isCurrent = false;

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
        invalidate();
    }

    @Override
    public ButtonStyle getStyle() {
        return style;
    }

    public GDXButton(String text, Skin skin) {
        super(text, skin);
        // calls setStyle()
        style = skin.get(ButtonStyle.class);
    }

    public GDXButton(String text, ButtonStyle style) {
        super(text, style);
        // special constructor
        this.style = style;
    }

    public GDXButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
        // calls setStyle()
        style = skin.get(styleName, ButtonStyle.class);
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        // TODO Auto-generated method stub
        super.draw(batch, parentAlpha);

        Drawable overlay1 = style.currentOverlay;
        if (isCurrent) {
            overlay1.draw(batch, getX(), getY(), getWidth(), getHeight());
        }
    }

    public static class ButtonStyle extends TextButtonStyle {

        public Drawable currentOverlay;

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
