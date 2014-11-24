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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.SnapshotArray;
import com.teotigraphix.gdx.controller.IHelpManager;
import com.teotigraphix.gdx.groove.ui.components.UITable;
import com.teotigraphix.gdx.scene2d.ui.ButtonBarListener.ButtonBarChangeEvent;

public class ButtonBar extends UITable {

    private int selectedIndex;

    private boolean disabled;

    public static class ButtonBarItem {

        private String id;

        private String label;

        private String icon;

        private String helpText;

        public String getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        public String getIcon() {
            return icon;
        }

        public String getHelpText() {
            return helpText;
        }

        public ButtonBarItem(String id, String label, String icon, String helpText) {
            this.id = id;
            this.label = label;
            this.icon = icon;
            this.helpText = helpText;
        }
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        for (Button button : group.getButtons()) {
            button.setDisabled(disabled);
            if (((TextButton)button).getText().equals(" "))
                button.setDisabled(true);
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

    public Array<ButtonBarItem> getItems() {
        return items;
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
        this.items = items;
        this.isVertical = isVertical;
        this.buttonStyle = buttonStyle;
    }

    protected void configureButton(TextButton button) {
    }

    @Override
    public void layout() {
        super.layout();

        if (selectedIndex != -1) {
            ToggleButton button = (ToggleButton)group.getButtons().get(selectedIndex);
            button.setChecked(true, false);
        } else {
            for (Button button : group.getButtons()) {
                ((ToggleButton)button).setChecked(false, false);
            }
        }
    }

    @Override
    protected void createChildren() {
        refreshButtons();
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
                Cell cell = add(button);
                if (maxButtonSize != null) {
                    cell.fillX().expandX().maxHeight(maxButtonSize).prefHeight(maxButtonSize);
                } else {
                    cell.fill().expand().space(gap).align(Align.top);
                }
                row();
            } else {
                Cell cell = add(button).space(gap);
                if (maxButtonSize != null)
                    cell.maxWidth(maxButtonSize).prefWidth(maxButtonSize).fillY().expandY();
                else
                    cell.uniform().fill().expand();
            }
        }
    }

    protected TextButton createButton(int index, TextButtonStyle style) {
        String label = items.get(index).getLabel();
        final ToggleButton button = new ToggleButton(label, style);
        if (label.equals(" "))
            button.setDisabled(true);
        button.setUserObject(items.get(index));
        if (helpManager != null) {
            helpManager.register(button, items.get(index).getHelpText());
        }
        group.add(button);
        button.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                event.stop();
                if (disabled || button.isDisabled())
                    return;
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
        if (toolTip == null || toolTip.equals(""))
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
    // Public API Methods
    //--------------------------------------------------------------------------

    /**
     * Selected the button and fires change event.
     * 
     * @param index
     * @param selected
     */
    public void select(int index, boolean selected) {
        ToggleButton button = (ToggleButton)group.getButtons().get(index);
        button.setChecked(selected);
    }

    /**
     * Redraws the selection without firing a change event.
     * 
     * @param index
     * @param selected
     */
    public void redrawSelection(int index, boolean selected) {
        ToggleButton button = (ToggleButton)group.getButtons().get(index);
        button.setChecked(selected, false);
    }

    public void disableFrom(int length) {
        SnapshotArray<Actor> children = getChildren();
        for (int i = 0; i < children.size; i++) {
            ToggleButton button = (ToggleButton)children.get(i);
            if (i < length) {
                button.setDisabled(false);
                button.invalidate();
            } else {
                button.setDisabled(true);
            }
        }
    }

}
