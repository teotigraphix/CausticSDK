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

package com.teotigraphix.caustk.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.gs.controller.IFunctionController.FunctionGroup;
import com.teotigraphix.caustk.gs.controller.IFunctionController.FunctionGroupItem;
import com.teotigraphix.caustk.workstation.GrooveBox.KeyboardMode;
import com.teotigraphix.libgdx.ui.ControlTable;
import com.teotigraphix.libgdx.ui.caustk.StepButton;
import com.teotigraphix.libgdx.ui.caustk.StepButton.OnStepButtonListener;
import com.teotigraphix.libgdx.ui.caustk.StepButton.StepButtonItem;

public class StepKeyboard extends ControlTable {

    @SuppressWarnings("unused")
    private boolean triggerEnabled;

    private StepButton lastStepButton;

    private Stack stack;

    private Table stepGroup;

    private Table keyGroup;

    private Table shiftGroup;

    private Array<FunctionGroup> functionGroups;

    int[] sharps = new int[] {
            1, 4, 6, 9, 11, 13
    };

    private OnStepKeyboardListener onStepKeyboardListener;

    //----------------------------------
    // mode
    //----------------------------------

    private KeyboardMode mode;

    public void setMode(KeyboardMode value) {
        mode = value;
        invalidate();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public StepKeyboard(Array<FunctionGroup> groups, Skin skin) {
        super(skin);
        this.functionGroups = groups;
    }

    //--------------------------------------------------------------------------
    // Overridden :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createChildren() {
        stack = new Stack();

        shiftGroup = createShiftGroup();
        stepGroup = createStepGroup();
        keyGroup = createKeyGroup();

        stack.add(shiftGroup);
        stack.add(stepGroup);
        stack.add(keyGroup);

        add(stack).fill().expand();

        //row();

        //        Table functionGroup = new Table(getSkin());
        //        for (FunctionGroup group : functionGroups) {
        //            createGroup(group, functionGroup);
        //        }
        //        add(functionGroup).expandX().fillX();
    }

    @Override
    public void layout() {
        super.layout();

        if (mode == KeyboardMode.Key) {
            shiftGroup.setVisible(false);
            stepGroup.setVisible(false);
            keyGroup.setVisible(true);
        } else if (mode == KeyboardMode.Step) {
            shiftGroup.setVisible(false);
            stepGroup.setVisible(true);
            keyGroup.setVisible(false);
        } else if (mode == KeyboardMode.Shift) {
            shiftGroup.setVisible(true);
            stepGroup.setVisible(false);
            keyGroup.setVisible(false);
        }
    }

    private Table createShiftGroup() {
        ButtonGroup buttonGroup = new ButtonGroup();
        Table table = new Table();
        for (int i = 0; i < 16; i++) {
            FunctionGroupItem item = getItem(i);
            StepButton stepButton = createButton(i, !item.isAutoExecute());
            if (!item.isAutoExecute())
                stepButton.setButtonGroup(buttonGroup);
            stepButton.setOnStepButtonListener(new OnStepButtonListener() {
                @Override
                public void onChange(int index, boolean selected) {
                    //selectionChange(index, selected);
                }

                @Override
                public void onTouchUp(int index) {
                }

                @Override
                public void onTouchDown(int index) {
                    onStepKeyboardListener.onFunctionDown(index);
                }

            });
            table.add(stepButton).space(5f).fill().expand();
        }

        return table;
    }

    public FunctionGroupItem getItem(int index) {
        int bank = (int)Math.floor((index) / 4);
        int itemIndex = index % 4;
        FunctionGroup group = functionGroups.get(bank);
        FunctionGroupItem item = group.getFunctions().get(itemIndex);
        return item;
    }

    private Table createStepGroup() {
        Table table = new Table();
        for (int i = 0; i < 16; i++) {
            StepButton stepButton = createButton(i, true);
            stepButton.addCaptureListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //if (!triggerEnabled)
                    //    event.cancel();
                }
            });
            stepButton.setOnStepButtonListener(new OnStepButtonListener() {
                @Override
                public void onChange(int index, boolean selected) {
                    selectionChange(index, selected);
                }

                @Override
                public void onTouchUp(int index) {
                }

                @Override
                public void onTouchDown(int index) {
                }

            });
            table.add(stepButton).space(5f).fill().expand();
        }
        return table;
    }

    private Table createKeyGroup() {
        Table table = new Table();
        for (int i = 0; i < 16; i++) {
            StepButton stepButton = createButton(i, false);
            stepButton.setOnStepButtonListener(new OnStepButtonListener() {
                @Override
                public void onChange(int index, boolean selected) {
                }

                @Override
                public void onTouchUp(int index) {
                    onStepKeyboardListener.onKeyUp(index);
                }

                @Override
                public void onTouchDown(int index) {
                    onStepKeyboardListener.onKeyDown(index);
                }

            });
            table.add(stepButton).space(5f).fill().expand();
        }

        return table;
    }

    private StepButton createButton(int index, boolean isToggle) {
        String text = "" + (index + 1);
        String styleName = "step-button";
        if (isSharp(index))
            styleName = "step-button-sharp";

        final StepButtonItem item = new StepButtonItem(index, text, isToggle, styleName);
        final StepButton stepButton = new StepButton(item, getSkin());

        return stepButton;
    }

    @SuppressWarnings("unused")
    private void createGroup(FunctionGroup functionGroup, Table parent) {
        //parent.debug();
        parent.padTop(0f);
        Table fullGroup = new Table(getSkin());

        Table topGroup = new Table(getSkin());

        topGroup.setBackground(getSkin().getDrawable("keyboard/black_background"));
        topGroup.add(functionGroup.getName()).expandX().padTop(-2f);

        fullGroup.add(topGroup).expandX().fillX().height(10f);
        //        fullGroup.row();
        //
        //        Table elements = new Table(getSkin());
        //        elements.debug();
        //        for (FunctionGroupItem item : functionGroup.getFunctions()) {
        //            Label label = new Label(item.getName(), getSkin());
        //            label.setAlignment(Align.center);
        //            elements.add(label).expandX().center().uniformX();
        //        }
        //
        //        fullGroup.add(elements).fillX().expandX();
        parent.add(fullGroup).expandX().fillX().top().space(10f).uniformX();

    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    protected void selectionChange(int index, boolean selected) {
        //                     System.out.println("index:" + index + " selected:" + selected);
        StepButton button = getStepButton(index);
        if (lastStepButton != null)
            lastStepButton.selectActive(false);
        button.selectActive(true);
        lastStepButton = button;

        // callback to listeners that a stepButton changed
        onStepKeyboardListener.onStepChange(index, selected);
    }

    private StepButton lastCurrent;

    /**
     * Sets the index that uses the currentOverlay of the {@link StepButton}.
     * 
     * @param index 0-15
     */
    public void setCurrentIndex(int index) {
        if (index == -1) {
            if (lastCurrent != null)
                lastCurrent.selectCurrent(false);
            lastCurrent = null;
        } else {
            StepButton button = getStepButton(index);
            if (lastCurrent != null)
                lastCurrent.selectCurrent(false);
            button.selectCurrent(true);
            lastCurrent = button;
        }
    }

    protected final StepButton getStepButton(int index) {
        if (mode == KeyboardMode.Key)
            return (StepButton)keyGroup.getChildren().get(index);
        else if (mode == KeyboardMode.Step)
            return (StepButton)stepGroup.getChildren().get(index);
        return null;
    }

    public void select(int step, boolean selected) {
        // using this method only updates the view, not events are fired
        final StepButton button = (StepButton)stepGroup.getChildren().get(step);
        button.updateSelected(selected);
    }

    //--------------------------------------------------------------------------
    // Listener
    //--------------------------------------------------------------------------

    public void setOnStepKeyboardListener(OnStepKeyboardListener l) {
        onStepKeyboardListener = l;
    }

    public interface OnStepKeyboardListener {
        void onStepChange(int index, boolean selected);

        void onModeStateChange(KeyboardMode mode);

        void onKeyDown(int index);

        void onKeyUp(int index);

        void onFunctionDown(int index);
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    private boolean isSharp(int index) {
        for (int i = 0; i < sharps.length; i++) {
            if (sharps[i] == index)
                return true;
        }
        return false;
    }

    public void setTriggerEnabled(boolean enabled) {
        triggerEnabled = enabled;
    }
}
