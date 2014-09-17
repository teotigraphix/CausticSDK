
package com.teotigraphix.gdx.groove.ui.factory;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.teotigraphix.gdx.groove.ui.components.TopBar;
import com.teotigraphix.gdx.groove.ui.components.TopBar.TopBarStyle;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar;

public abstract class TopBarFactory extends UIFactoryChild {

    public static final String Font_TextButton = "TopBar.TextButton.Font";

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public TopBarFactory() {
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    public TopBar createTopBar(Skin skin) {

        // TopBarStyle
        TopBarStyle topBarStyle = new TopBarStyle(skin.getDrawable("TopBar_background"));
        skin.add("default", topBarStyle);

        //TopBarStyle.TextButton
        TextButtonStyle textButtonStyle = new TextButtonStyle(
                skin.getDrawable("TopBar_TextButton_up"),
                skin.getDrawable("TopBar_TextButton_over"),
                skin.getDrawable("TopBar_TextButton_checked"), skin.getFont(Font_TextButton));
        skin.add("TopBar.TextButton", textButtonStyle);

        // Create TopBar
        TopBar instance = new TopBar(skin);
        instance.create("default");

        ButtonBar buttonBar = (ButtonBar)createTopBar_Center(skin);
        instance.getCenterChild().add(buttonBar).expand().fill();

        return instance;
    }

    public abstract Table createTopBar_Center(Skin skin);

}
