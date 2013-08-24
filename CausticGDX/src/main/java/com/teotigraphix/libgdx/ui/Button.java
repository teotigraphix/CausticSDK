
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * The {@link Button} disables the checked property when
 * {@link #setToggle(boolean)} is set.
 * <p>
 * The {@link Button} is not a toggle button.
 */
public class Button extends TextButton {

    boolean isToggle = false;

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

    public Button(String text, Skin skin) {
        super(text, skin);
        // calls setStyle()
    }

    public Button(String text, TextButtonStyle style) {
        super(text, style);
        // special constructor
    }

    public Button(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
        // calls setStyle()
    }

}
