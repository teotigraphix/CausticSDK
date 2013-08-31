
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.teotigraphix.libgdx.ui.GDXButton.ButtonStyle;

public class ButtonBar extends Table {

    private ButtonGroup group;

    private Skin skin;

    private OnButtonBarListener listener;

    private String[] items;

    private boolean isVertical;

    private String buttonStyleName = "default";

    //----------------------------------
    // currentIndex
    //----------------------------------

    private int currentIndex;

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int value) {
        currentIndex = value;
        Array<Button> buttons = group.getButtons();
        for (int i = 0; i < buttons.size; i++) {
            GDXToggleButton button = (GDXToggleButton)buttons.get(i);
            if (i == currentIndex) {
                button.setCurrent(true);
            } else {
                button.setCurrent(false);
            }
        }
        invalidateHierarchy();
    }

    public ButtonBar() {
    }

    public ButtonBar(Skin skin, String[] items, boolean isViertical) {
        super(skin);
        this.skin = skin;
        this.items = items;
        isVertical = isViertical;
        group = new ButtonGroup();
        createChildren();
    }

    public ButtonBar(Skin skin, String[] items, boolean isViertical, String buttonStyleName) {
        this(skin, items, isViertical);
        this.buttonStyleName = buttonStyleName;
    }

    private void createChildren() {
        for (int i = 0; i < items.length; i++) {
            final int index = i;
            final GDXToggleButton button = new GDXToggleButton(items[i], skin.get(buttonStyleName,
                    ButtonStyle.class));
            if (isVertical) {
                add(button).fill().expand().minHeight(0).prefHeight(999);
                row();
            } else {
                add(button).fill().expand().minWidth(0).prefWidth(999);
            }

            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (button.isChecked()) {
                        if (listener != null)
                            listener.onChange(index);
                    }
                }
            });

            group.add(button);
        }
    }

    public void select(int index) {
        // XXX Need a way to not dispatch the change listener when programatically selecting
        group.getButtons().get(index).setChecked(true);
    }

    public void setOnButtonBarListener(OnButtonBarListener l) {
        listener = l;
    }

    public interface OnButtonBarListener {
        void onChange(int index);
    }

    public void disableFrom(int length) {
        SnapshotArray<Actor> children = getChildren();
        for (int i = 0; i < children.size; i++) {
            GDXToggleButton button = (GDXToggleButton)children.get(i);
            if (i < length) {
                button.setDisabled(false);
                button.invalidate();
            } else {
                button.setDisabled(true);
            }
        }

    }
}
