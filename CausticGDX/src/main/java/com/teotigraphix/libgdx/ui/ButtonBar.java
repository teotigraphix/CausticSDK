
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.teotigraphix.libgdx.ui.OldSelectButton.SelectButtonStyle;

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

    /**
     * Sets the progress bar percent and visible on the button index.
     * <p>
     * All other progress overlays are invisible.
     * 
     * @param index The button index.
     * @param percent The progress percent (0-100).
     */
    public void setProgressAt(int index, float percent) {
        Array<Button> buttons = group.getButtons();
        for (int i = 0; i < buttons.size; i++) {
            OldSelectButton button = (OldSelectButton)buttons.get(i);
            if (i == index) {
                button.setIsProgress(true);
                button.setProgress(percent);
            } else {
                button.setIsProgress(false);
            }
        }
        invalidateHierarchy();
    }

    public ButtonBar() {
    }

    public ButtonBar(Skin skin, String[] items, boolean isVertical) {
        super(skin);
        this.skin = skin;
        this.items = items;
        this.isVertical = isVertical;
        group = new ButtonGroup();
        createChildren();
    }

    public ButtonBar(Skin skin, String[] items, boolean isViertical, String buttonStyleName) {
        this(skin, items, isViertical);
        this.buttonStyleName = buttonStyleName;
    }

    protected void createChildren() {
        for (int i = 0; i < items.length; i++) {
            final int index = i;
            final OldSelectButton button = new OldSelectButton(items[i], skin.get(buttonStyleName,
                    SelectButtonStyle.class));
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
        if (index == -1)
            return;
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
            OldSelectButton button = (OldSelectButton)children.get(i);
            if (i < length) {
                button.setDisabled(false);
                button.invalidate();
            } else {
                button.setDisabled(true);
            }

        }

    }
}
