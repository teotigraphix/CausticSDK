
package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ListDialog extends AlertDialog {

    List list;

    Object[] items;

    public Object[] getItems() {
        return items;
    }

    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    public void setItems(Object[] items) {
        debug();
        this.items = items;
        list = new List(items, getSkin());
        list.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });

        ScrollPane pane = new ScrollPane(list, getSkin());
        pane.setOverscroll(false, false);
        setContent(pane).size(420f, 400f).expandX();
    }

    public ListDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    public ListDialog(String title, Skin skin) {
        super(title, skin);
    }

    public ListDialog(String title, WindowStyle windowStyle) {
        super(title, windowStyle);
    }

    @Override
    protected void createChildren() {
        super.createChildren();
    }

}
