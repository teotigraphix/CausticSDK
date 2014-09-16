
package com.teotigraphix.gdx.groove.ui.factory;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.teotigraphix.gdx.groove.ui.components.TopBar;
import com.teotigraphix.gdx.groove.ui.components.TopBar.TopBarStyle;
import com.teotigraphix.gdx.groove.ui.components.TopBarListener.TopBarEvent;
import com.teotigraphix.gdx.groove.ui.components.TopBarListener.TopBarEventKind;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;
import com.teotigraphix.gdx.scene2d.ui.ButtonBarListener;

public class TopBarFactory {

    @Inject
    private UIFactoryModel factoryModel;

    public static final String Font_TextButton = "TopBar.TextButton.Font";

    private UIFactory factory;

    public TopBarFactory(UIFactory factory) {
        this.factory = factory;
    }

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

    public Table createTopBar_Center(Skin skin) {

        TopBarFactoryInfo info = factory.getFactoryModel().getTopBarInfo();

        TextButtonStyle buttonStyle = skin.get("TopBar.TextButton", TextButtonStyle.class);
        final ButtonBar instance = new ButtonBar(skin, info.getCenterItems(), false, buttonStyle);
        instance.addListener(new ButtonBarListener() {
            @Override
            public void selectedIndexChange(int selectedIndex) {
                TopBarEvent event = new TopBarEvent(TopBarEventKind.ViewIndexChange, selectedIndex);
                instance.fire(event);
            }
        });
        instance.setMaxButtonSize(80f);

        return instance;
    }

    public static class TopBarFactoryInfo {
        private Array<ButtonBarItem> centerItems;

        public Array<ButtonBarItem> getCenterItems() {
            return centerItems;
        }

        public void setCenterItems(Array<ButtonBarItem> centerItems) {
            this.centerItems = centerItems;
        }
    }

}
