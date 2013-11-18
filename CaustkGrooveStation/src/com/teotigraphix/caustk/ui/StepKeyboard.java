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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.gs.controller.IFunctionController.FunctionGroup;
import com.teotigraphix.caustk.gs.controller.IFunctionController.FunctionGroupItem;
import com.teotigraphix.caustk.workstation.GrooveBox.KeyboardMode;
import com.teotigraphix.libgdx.ui.ControlTable;
import com.teotigraphix.libgdx.ui.caustk.StepButton;
import com.teotigraphix.libgdx.ui.caustk.StepButton.OnStepButtonListener;
import com.teotigraphix.libgdx.ui.caustk.StepButton.StepButtonItem;

public class StepKeyboard extends ControlTable {

    private StepButton lastSelectedStepButton;

    private StepButton lastCurrentStepButton;

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

        row();

        Table functionGroup = new Table(getSkin());
        functionGroup.padLeft(5f);
        functionGroup.padRight(5f);
        for (FunctionGroup group : functionGroups) {
            createGroup(group, functionGroup);
        }
        add(functionGroup).expandX().fillX();
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
                public boolean onChange(int index, boolean selected) {
                    return selectionChange(index, selected);
                }

                @Override
                public void onTouchUp(int index) {
                }

                @Override
                public boolean onTouchDown(int index) {
                    onStepKeyboardListener.onFunctionDown(index);
                    return false;
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
            stepButton.setOnStepButtonListener(new OnStepButtonListener() {
                @Override
                public boolean onChange(int index, boolean selected) {
                    return selectionChange(index, selected);
                }

                @Override
                public void onTouchUp(int index) {
                }

                @Override
                public boolean onTouchDown(int index) {
                    return onStepKeyboardListener.onStepDown(index);
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
                public boolean onChange(int index, boolean selected) {
                    return false;
                }

                @Override
                public void onTouchUp(int index) {
                    onStepKeyboardListener.onKeyUp(index);
                }

                @Override
                public boolean onTouchDown(int index) {
                    onStepKeyboardListener.onKeyDown(index);
                    return false;
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

    private void createGroup(FunctionGroup functionGroup, Table parent) {
        //parent.debug();
        parent.padTop(0f);
        Table fullGroup = new Table(getSkin());

        Table topGroup = new Table(getSkin());

        topGroup.setBackground(getSkin().getDrawable("keyboard/black_background"));
        topGroup.add(functionGroup.getName()).expandX().padTop(-2f).uniformX();

        fullGroup.add(topGroup).expandX().fillX().height(10f).uniformX();
        fullGroup.row();

        Table elements = new Table(getSkin());
        for (FunctionGroupItem item : functionGroup.getFunctions()) {
            Label label = new Label(item.getName(), new LabelStyle(getSkin()
                    .getFont("default-font"), Color.WHITE));
            label.setAlignment(Align.center);
            elements.add(label).expandX().fillX().center().uniformX().minSize(10f);
        }

        fullGroup.add(elements).fillX().expandX().uniformX();
        parent.add(fullGroup).expandX().fillX().top().space(5f).uniformX();

    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    public void setSelectedStep(int localIndex) {
        if (lastSelectedStepButton != null)
            lastSelectedStepButton.selectActive(false);
        if (localIndex == -1)
            return;
        StepButton button = getStepButton(localIndex);
        button.selectActive(true);
        lastSelectedStepButton = button;
    }

    /**
     * Sets the index that uses the currentOverlay of the {@link StepButton}.
     * <p>
     * This is the current step indicator as the pattern is playing.
     * 
     * @param localIndex 0-15
     */
    public void setCurrentStepIndex(int localIndex) {
        if (localIndex == -1) {
            if (lastCurrentStepButton != null)
                lastCurrentStepButton.selectCurrent(false);
            lastCurrentStepButton = null;
        } else {
            StepButton button = getStepButton(localIndex);
            if (lastCurrentStepButton != null)
                lastCurrentStepButton.selectCurrent(false);
            button.selectCurrent(true);
            lastCurrentStepButton = button;
        }
    }

    protected boolean selectionChange(int index, boolean selected) {
        //                     System.out.println("index:" + index + " selected:" + selected);
        //setCurrentStepIndex(index);
        // callback to listeners that a stepButton changed
        return onStepKeyboardListener.onStepChange(index, selected);
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
        boolean onStepChange(int index, boolean selected);

        boolean onStepDown(int index);

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
}
