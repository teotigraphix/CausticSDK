
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.AdvancedList.AdvancedListChangeEvent;
import com.badlogic.gdx.scenes.scene2d.ui.AdvancedList.AdvancedListDoubleTapEvent;
import com.badlogic.gdx.scenes.scene2d.ui.AdvancedList.AdvancedListListener;
import com.badlogic.gdx.scenes.scene2d.ui.AdvancedList.AdvancedListLongPressEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class ScrollListPane extends Pane {

    private ScrollList scrollList;

    public int getSelectedIndex() {
        return scrollList.getSelectedIndex();
    }

    public void setSelectedIndex(int value) {
        scrollList.setSelectedIndex(value);
    }

    public Object getSelectedItem() {
        return scrollList.getSelectedItem();
    }

    public void setItems(Array<?> items) {
        scrollList.setItems(items);
    }

    public Array<?> getItems() {
        return scrollList.getItems();
    }

    public ScrollListPane(Skin skin, String label) {
        super(skin, label);
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        scrollList = new ScrollList(getSkin());
        scrollList.setOverscroll(false, true);
        scrollList.setFadeScrollBars(false);

        scrollList.addListener(new AdvancedListListener() {
            @Override
            public void changed(AdvancedListChangeEvent event, Actor actor) {
                listener.onListChange(getSelectedIndex());
            }

            @Override
            public void longPress(AdvancedListLongPressEvent event, Actor actor) {
                listener.onListLongPress(getSelectedIndex());
            }

            @Override
            public void doubleTap(AdvancedListDoubleTapEvent event, Actor actor) {
                listener.onListDoubleTap(getSelectedIndex());
            }

        });

        add(scrollList).fill().expand();
    }

    public void refresh() {
        scrollList.refresh();
    }

    private OnScrollListPaneListener listener;

    public void setOnScrollListPaneListener(OnScrollListPaneListener l) {
        listener = l;
    }

    public interface OnScrollListPaneListener {
        void onListChange(int index);

        void onListLongPress(int index);

        void onListDoubleTap(int index);
    }

}
