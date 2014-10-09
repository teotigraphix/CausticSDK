
package com.teotigraphix.gdx.groove.ui.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.gdx.groove.ui.factory.StylesDefault;

public class ViewStackPane extends UITable {

    private ViewStack viewStack;

    private Array<Actor> views;

    public void select(int index) {
        viewStack.setSelectedIndex(index);
    }

    public ViewStackPane(Skin skin, Array<Actor> views) {
        super(skin);
        this.views = views;
    }

    @Override
    protected void createChildren() {
        viewStack = new ViewStack(getSkin());
        for (Actor actor : views) {
            viewStack.addView(actor); // Mute
        }

        viewStack.create(StylesDefault.ViewStack);
        add(viewStack).expand().fill();
    }

}
