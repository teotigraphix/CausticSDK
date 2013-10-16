////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.teotigraphix.libgdx.ui.OverlayButton.OverlayButtonStyle;

public class ButtonBar extends ControlTable {

    private ButtonGroup group;

    private String[] items;

    private boolean itemsChanged;

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] value) {
        items = value;
        itemsChanged = true;
        invalidateHierarchy();
    }

    private boolean isVertical;

    private String buttonStyleName = "default";

    //----------------------------------
    // currentIndex
    //----------------------------------

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ButtonBar(Skin skin, String[] items, boolean isVertical, String buttonStyleName) {
        super(skin);
        setItems(items);
        this.isVertical = isVertical;
        this.buttonStyleName = buttonStyleName;
    }

    @Override
    protected void createChildren() {
        if (itemsChanged) {
            refreshButtons();
            itemsChanged = false;
        }
    }

    private void refreshButtons() {
        clearChildren();

        group = new ButtonGroup();

        final OverlayButtonStyle style = getSkin().get(buttonStyleName, OverlayButtonStyle.class);

        for (int i = 0; i < items.length; i++) {
            final OverlayButton button = createButton(i, style);
            if (isVertical) {
                add(button).uniform().fill().expand();
                row();
            } else {
                add(button).uniform().fill().expand();
            }
        }
    }

    @Override
    public void layout() {
        super.layout();

        if (itemsChanged) {
            refreshButtons();
            itemsChanged = false;
        }
    }

    protected OverlayButton createButton(int index, OverlayButtonStyle style) {
        final OverlayButton button = new OverlayButton(items[index], style);
        button.setToggle(true);
        group.add(button);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (button.isChecked()) {
                    if (listener != null) {
                        listener.onChange(group.getButtons().indexOf(button, true));
                    }
                }
            }
        });
        return button;
    }

    //--------------------------------------------------------------------------
    // Public API Methods
    //--------------------------------------------------------------------------

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
            OverlayButton button = (OverlayButton)buttons.get(i);
            if (i == index) {
                button.setIsProgress(true);
                button.setProgress(percent);
            } else {
                button.setIsProgress(false);
            }
        }
        invalidateHierarchy();
    }

    public void select(int index, boolean selected) {
        OverlayButton button = (OverlayButton)group.getButtons().get(index);
        button.setChecked(selected);
    }

    public void updateSelection(int index, boolean selected) {
        OverlayButton button = (OverlayButton)group.getButtons().get(index);
        button.check(selected);
    }

    public void disableFrom(int length) {
        SnapshotArray<Actor> children = getChildren();
        for (int i = 0; i < children.size; i++) {
            OverlayButton button = (OverlayButton)children.get(i);
            if (i < length) {
                button.setDisabled(false);
                button.invalidate();
            } else {
                button.setDisabled(true);
            }
        }
    }

    //--------------------------------------------------------------------------
    // Event
    //--------------------------------------------------------------------------

    private OnButtonBarListener listener;

    public void setOnButtonBarListener(OnButtonBarListener l) {
        listener = l;
    }

    public interface OnButtonBarListener {
        void onChange(int index);
    }
}
