
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GDXToggleButton extends GDXButton {

    public GDXToggleButton(String text, Skin skin) {
        super(text, skin);
        isToggle = true;
    }

    public GDXToggleButton(String text, ButtonStyle style) {
        super(text, style);
        isToggle = true;
    }

    public GDXToggleButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
        isToggle = true;
    }

}
