
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ToggleButton extends Button {

    public ToggleButton(String text, Skin skin) {
        super(text, skin);
        isToggle = true;
    }

    public ToggleButton(String text, TextButtonStyle style) {
        super(text, style);
        isToggle = true;
    }

    public ToggleButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
        isToggle = true;
    }

}
