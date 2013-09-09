
package com.teotigraphix.libgdx.ui.composite;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.SnapshotArray;
import com.teotigraphix.libgdx.ui.ControlTable;
import com.teotigraphix.libgdx.ui.Led;
import com.teotigraphix.libgdx.ui.Led.LedItem;
import com.teotigraphix.libgdx.ui.Led.LedPlacement;
import com.teotigraphix.libgdx.ui.SelectButton;

public class SelectLedGroup extends ControlTable {

    private SelectButton selectButton;

    private int selectedIndex = 0;

    private String text;

    private String buttonStyleName;

    private String ledStyleName;

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int value) {
        if (value == selectedIndex)
            return;
        selectedIndex = value;
        if (onSelectLedGroupListener != null) {
            onSelectLedGroupListener.onChange(selectedIndex, items[selectedIndex]);
        }
        invalidate();
    }

    public SelectLedGroup(String text, String buttonStyleName, LedItem[] items,
            String ledStyleName, Skin skin) {
        super(skin);
        this.text = text;
        this.buttonStyleName = buttonStyleName;
        this.items = items;
        this.ledStyleName = ledStyleName;
    }

    // buttonText, items, buttonStyleName, 

    LedItem[] items;

    private Table column;

    private OnSelectLedGroupListener onSelectLedGroupListener;

    @Override
    protected void createChildren() {
        selectButton = new SelectButton(text, buttonStyleName, getSkin());
        selectButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                int index = selectedIndex + 1;
                if (index > items.length - 1)
                    index = 0;
                setSelectedIndex(index);
            }
        });
        add(selectButton).top().padRight(10f);

        column = new Table();
        add(column);

        for (int i = 0; i < items.length; i++) {
            LedItem item = items[i];
            Led led = new Led(item, LedPlacement.LEFT, getSkin());
            led.setStyleName(ledStyleName);
            column.add(led).left().padBottom(5f);
            column.row();
        }
    }

    @Override
    public void layout() {
        super.layout();

        // set the selected Led
        SnapshotArray<Actor> children = column.getChildren();
        for (int i = 0; i < children.size; i++) {
            Led item = (Led)children.get(i);
            item.turnOff();
        }

        Led item = (Led)children.get(selectedIndex);
        item.turnOn();
    }

    public void setOnSelectLedGroupListener(OnSelectLedGroupListener l) {
        onSelectLedGroupListener = l;
    }

    public interface OnSelectLedGroupListener {
        void onChange(int index, LedItem item);
    }
}
