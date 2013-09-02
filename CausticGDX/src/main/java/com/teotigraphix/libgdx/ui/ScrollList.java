
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class ScrollList extends ScrollPane {

    private Skin skin;

    private Array<?> items;

    public Array<?> getItems() {
        return items;
    }

    public void setItems(Array<?> scenes) {
        items = scenes;
        if (list == null) {
            list = new List(scenes.toArray(), skin);
            setWidget(list);
        } else {
            list.setItems(scenes.toArray());
        }
    }

    private List list;

    public ScrollList(Skin skin) {
        super(null, skin);
        this.skin = skin;
        initialize();
    }

    public ScrollList(Skin skin, Array<?> items) {
        super(null, skin);
        this.skin = skin;
        this.items = items;
        initialize();
    }

    public ScrollList(Actor widget, Skin skin, String styleName) {
        super(widget, skin, styleName);
        this.skin = skin;
        initialize();
    }

    private void initialize() {
        if (items != null) {
            list = new List(items.toArray(), skin);
            setWidget(list);
        }
    }

    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    public Object getItem(int index) {
        return items.get(index);
    }
}
