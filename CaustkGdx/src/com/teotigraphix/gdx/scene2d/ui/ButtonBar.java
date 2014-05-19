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

package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.SnapshotArray;
import com.esotericsoftware.tablelayout.Cell;
import com.teotigraphix.gdx.controller.IHelpManager;
import com.teotigraphix.gdx.scene2d.ControlTable;

public class ButtonBar extends ControlTable {

    private int selectedIndex;

    @Override
    public String getHelpText() {
        return "Foo";
    }

    public static class ButtonBarItem {

        private String label;

        private String icon;

        private String helpText;

        public String getLabel() {
            return label;
        }

        public String getIcon() {
            return icon;
        }

        public String getHelpText() {
            return helpText;
        }

        public ButtonBarItem(String label, String icon, String helpText) {
            this.label = label;
            this.icon = icon;
            this.helpText = helpText;
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        //if (this.selectedIndex == selectedIndex)
        //    return;
        this.selectedIndex = selectedIndex;
        invalidate();
    }

    private ButtonGroup group;

    public ButtonGroup getGroup() {
        return group;
    }

    private IHelpManager helpManager;

    private float gap = 0f;

    private float padding = 2f;

    public void setHelpManager(IHelpManager helpManager) {
        this.helpManager = helpManager;
    }

    public void setGap(float gap) {
        this.gap = gap;
    }

    public void setPadding(float padding) {
        this.padding = padding;
    }

    //----------------------------------
    // items
    //----------------------------------

    private Array<ButtonBarItem> items;

    private boolean itemsChanged;

    public Array<ButtonBarItem> getItems() {
        return items;
    }

    public void setItems(Array<ButtonBarItem> value) {
        items = value;
        itemsChanged = true;
        invalidateHierarchy();
    }

    //----------------------------------
    // maxButtonSize
    //----------------------------------

    private Float maxButtonSize;

    public void setMaxButtonSize(Float value) {
        maxButtonSize = value;
    }

    public Float getMaxButtonSize() {
        return maxButtonSize;
    }

    private boolean isVertical;

    private TextButtonStyle buttonStyle;

    private int pendingTipIndex;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ButtonBar(Skin skin, Array<ButtonBarItem> items, boolean isVertical,
            TextButtonStyle buttonStyle) {
        super(skin);
        setItems(items);
        this.isVertical = isVertical;
        this.buttonStyle = buttonStyle;
    }

    @Override
    public void setSkin(Skin skin) {
        super.setSkin(skin);

        if (itemsChanged) {
            refreshButtons();
            itemsChanged = false;
        }
    }

    @SuppressWarnings("rawtypes")
    private void refreshButtons() {
        if (helpManager != null) {
            for (Actor actor : getChildren()) {
                helpManager.unregister(actor);
            }
        }
        clearChildren();

        group = new ButtonGroup();

        for (int i = 0; i < items.size; i++) {
            final TextButton button = createButton(i, buttonStyle);
            button.padLeft(padding).padRight(padding);
            configureButton(button);
            if (isVertical) {
                Cell cell = add(button).uniform().align(Align.top);
                if (maxButtonSize != null) {
                    cell.fillX().expandX().maxHeight(maxButtonSize).prefHeight(maxButtonSize);
                } else {
                    cell.fill().expand().align(Align.top);
                }
                row();
            } else {
                Cell cell = add(button).space(gap);//.uniform().fill().expand().align(Align.left).space(4f);
                if (maxButtonSize != null)
                    cell.maxWidth(maxButtonSize).prefWidth(maxButtonSize);
                else
                    cell.uniform().fill().expand();
            }
        }
    }

    protected void configureButton(TextButton button) {
    }

    @Override
    public void layout() {
        super.layout();

        if (itemsChanged) {
            refreshButtons();
            itemsChanged = false;
        }

        if (selectedIndex != -1) {
            TextButton button = (TextButton)group.getButtons().get(selectedIndex);
            button.setChecked(true);
        } else {
            group.uncheckAll();
        }
    }

    protected TextButton createButton(int index, TextButtonStyle style) {
        final TextButton button = new TextButton(items.get(index).getLabel(), style);
        button.setUserObject(items.get(index));
        if (helpManager != null) {
            helpManager.register(button, items.get(index).getHelpText());
        }
        group.add(button);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (button.isChecked()) {
                    int oldIndex = selectedIndex;
                    setSelectedIndex(group.getButtons().indexOf(button, true));
                    ButtonBarChangeEvent e = Pools.obtain(ButtonBarChangeEvent.class);
                    e.setSelectedIndex(selectedIndex);
                    if (fire(e)) {
                        selectedIndex = oldIndex;
                    } else {
                        onChange(selectedIndex, oldIndex);
                    }
                    Pools.free(e);
                }
            }
        });
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                pendingTipIndex = group.getButtons()
                        .indexOf((Button)event.getListenerActor(), true);
                float cx = Gdx.input.getX();
                float cy = Gdx.input.getY();
                startTooltip(cx, cy);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                stopTooltip();
            }
        });
        return button;
    }

    protected void startTooltip(final float cx, final float cy) {
        addAction(Actions.sequence(Actions.delay(1f), Actions.run(new Runnable() {
            @Override
            public void run() {
                showTooltip(cx, cy);
            }
        })));
    }

    protected void showTooltip(float cx, float cy) {
        if (toolTip != null)
            return;
        String helpText = items.get(pendingTipIndex).getHelpText();
        if (helpText != null) {
            toolTip = Tooltip.show(getStage(), getSkin(), helpText);
            toolTip.setPosition(cx, getStage().getHeight() - cy - toolTip.getHeight());
        }
    }

    protected void stopTooltip() {
        clearActions();
        if (toolTip == null)
            return;
        toolTip.hide();
        toolTip = null;
    }

    private Tooltip toolTip;

    protected void onChange(int selectedIndex, int oldIndex) {
    }

    //--------------------------------------------------------------------------
    // Event
    //--------------------------------------------------------------------------

    public static abstract class ButtonBarListener implements EventListener {

        @Override
        public boolean handle(Event event) {
            if (event instanceof ButtonBarChangeEvent) {
                ButtonBarChangeEvent e = (ButtonBarChangeEvent)event;
                selectedIndexChange(e.getSelectedIndex());
                return false;
            }
            return false;
        }

        public abstract void selectedIndexChange(int selectedIndex);
    }

    public static class ButtonBarChangeEvent extends ChangeEvent {

        private int selectedIndex;

        public int getSelectedIndex() {
            return selectedIndex;
        }

        void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
        }

        public ButtonBarChangeEvent() {
        }

        @Override
        public void reset() {
            super.reset();
            selectedIndex = -1;
        }
    }

    //--------------------------------------------------------------------------
    // Public API Methods
    //--------------------------------------------------------------------------

    //    /**
    //     * Sets the progress bar percent and visible on the button index.
    //     * <p>
    //     * All other progress overlays are invisible.
    //     * 
    //     * @param index The button index.
    //     * @param percent The progress percent (0-100).
    //     */
    //    public void setProgressAt(int index, float percent) {
    //        Array<Button> buttons = group.getButtons();
    //        for (int i = 0; i < buttons.size; i++) {
    //            TextButton button = (TextButton)buttons.get(i);
    //            if (i == index) {
    //                button.setIsProgress(true);
    //                button.setProgress(percent);
    //            } else {
    //                button.setIsProgress(false);
    //            }
    //        }
    //        invalidateHierarchy();
    //    }

    public void select(int index, boolean selected) {
        TextButton button = (TextButton)group.getButtons().get(index);
        button.setChecked(selected);
    }

    public void updateSelection(int index, boolean selected) {
        TextButton button = (TextButton)group.getButtons().get(index);
        button.setChecked(selected);
    }

    public void disableFrom(int length) {
        SnapshotArray<Actor> children = getChildren();
        for (int i = 0; i < children.size; i++) {
            TextButton button = (TextButton)children.get(i);
            if (i < length) {
                button.setDisabled(false);
                button.invalidate();
            } else {
                button.setDisabled(true);
            }
        }
    }

}
