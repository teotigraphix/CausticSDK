
package com.teotigraphix.gdx.groove.ui.factory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.gdx.groove.ui.components.ViewStack;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;

public class ViewStackFactory {

    @SuppressWarnings("unused")
    private UIFactory factory;

    public ViewStackFactory(UIFactory factory) {
        this.factory = factory;
    }

    public ViewStack createViewStack(Skin skin) {
        LabelStyle labelStyle = new LabelStyle(skin.getFont("default-font"), Color.WHITE);
        skin.add("default", labelStyle);

        ViewStack instance = new ViewStack(skin);

        instance.addView(new Label("1", skin));
        instance.addView(new Label("2", skin));
        instance.addView(new Label("3", skin));

        instance.create("default");

        return instance;
    }

    public static class ViewStackFactoryInfo {

        private Array<ButtonBarItem> items;

        /**
         * Returns the linear list of all items held within the view stack.
         */
        public Array<ButtonBarItem> getItems() {
            return items;
        }

        public void setItems(Array<ButtonBarItem> items) {
            this.items = items;
        }

        public ViewStackFactoryInfo() {
        }

    }

}
