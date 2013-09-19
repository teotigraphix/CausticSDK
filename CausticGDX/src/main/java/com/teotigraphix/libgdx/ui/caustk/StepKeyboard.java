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

package com.teotigraphix.libgdx.ui.caustk;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.libgdx.ui.ControlTable;
import com.teotigraphix.libgdx.ui.caustk.StepButton.OnStepButtonListener;
import com.teotigraphix.libgdx.ui.caustk.StepButton.StepButtonItem;

public class StepKeyboard extends ControlTable {

    private StepButton lastStepButton;

    private Stack stack;

    private Table stepGroup;

    private Table keyGroup;

    private Table shiftGroup;

    private Button keyBoardToggle;

    private Button shiftToggle;

    private Array<FunctionGroup> groups;

    int[] sharps = new int[] {
            1, 4, 6, 9, 11, 13
    };

    private OnStepKeyboardListener onStepKeyboardListener;

    public enum StepKeyboardMode {

        Step(0),

        Key(1),

        Shift(1);

        private int index;

        public int getIndex() {
            return index;
        }

        StepKeyboardMode(int index) {
            this.index = index;
        }
    }

    //----------------------------------
    // mode
    //----------------------------------

    private StepKeyboardMode mode;

    public StepKeyboardMode getMode() {
        return mode;
    }

    public void setMode(StepKeyboardMode value) {
        if (value == mode)
            return;
        mode = value;
        invalidate();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public StepKeyboard(Array<FunctionGroup> groups, Skin skin) {
        super(skin);
        this.groups = groups;
    }

    //--------------------------------------------------------------------------
    // Overridden :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createChildren() {
        debug();

        keyBoardToggle = new Button(getSkin());
        keyBoardToggle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (shiftToggle.isChecked()) {
                    event.cancel();
                    return;
                }
                Button button = (Button)actor;
                if (button.isChecked()) {
                    setMode(StepKeyboardMode.Key);
                } else {
                    if (shiftToggle.isChecked()) {
                        setMode(StepKeyboardMode.Shift);
                    } else {
                        setMode(StepKeyboardMode.Step);
                    }
                }
            }
        });

        add(keyBoardToggle).size(40f, 60f).top().left().padRight(10f);

        stack = new Stack();

        shiftGroup = createShiftGroup();
        stepGroup = createStepGroup();
        keyGroup = createKeyGroup();

        stack.add(shiftGroup);
        stack.add(stepGroup);
        stack.add(keyGroup);

        add(stack);

        row();

        shiftToggle = new TextButton("SHIFT", getSkin());
        shiftToggle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Button button = (Button)actor;
                if (button.isChecked()) {
                    setMode(StepKeyboardMode.Shift);
                } else {
                    if (keyBoardToggle.isChecked()) {
                        setMode(StepKeyboardMode.Key);
                    } else {
                        setMode(StepKeyboardMode.Step);
                    }
                }
            }
        });

        add(shiftToggle).size(40f, 40f).top().left();
        Table functionGroup = new Table(getSkin());
        for (FunctionGroup group : groups) {
            createGroup(group, functionGroup);
        }
        add(functionGroup).expandX().fillX();
    }

    @Override
    public void layout() {
        super.layout();

        if (mode == StepKeyboardMode.Key) {
            shiftGroup.setVisible(false);
            stepGroup.setVisible(false);
            keyGroup.setVisible(true);
        } else if (mode == StepKeyboardMode.Step) {
            shiftGroup.setVisible(false);
            stepGroup.setVisible(true);
            keyGroup.setVisible(false);
        } else if (mode == StepKeyboardMode.Shift) {
            shiftGroup.setVisible(true);
            stepGroup.setVisible(false);
            keyGroup.setVisible(false);
        }
    }

    private Table createShiftGroup() {
        ButtonGroup buttonGroup = new ButtonGroup();
        Table table = new Table();
        for (int i = 0; i < 16; i++) {
            StepButton stepButton = createButton(i, true);
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
                }

            });
            table.add(stepButton).space(10f).size(50f, 70f);
        }

        return table;
    }

    private Table createStepGroup() {
        Table table = new Table();
        for (int i = 0; i < 16; i++) {
            StepButton stepButton = createButton(i, true);
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
            table.add(stepButton).space(10f).size(50f, 70f);
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
            table.add(stepButton).space(10f).size(50f, 70f);
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
        topGroup.add(functionGroup.getName()).expandX().padTop(-4f);

        fullGroup.add(topGroup).expandX().fillX().height(15f);
        fullGroup.row();

        Table elements = new Table(getSkin());
        //elements.debug();
        for (FunctionGroupItem item : functionGroup.getFunctions()) {
            Label label = new Label(item.getName(), getSkin());
            label.setAlignment(Align.center);
            elements.add(label).expandX().center().uniformX();
        }

        fullGroup.add(elements).fillX().expandX();
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

    protected final StepButton getStepButton(int index) {
        if (mode == StepKeyboardMode.Key)
            return (StepButton)keyGroup.getChildren().get(index);
        else if (mode == StepKeyboardMode.Step)
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

        void onKeyDown(int index);

        void onKeyUp(int index);
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

    public interface IKeyFunction {
        String getTitle();
    }

    public static class FunctionGroupItem {

        private IKeyFunction function;

        private int index;

        public int getIndex() {
            return index;
        }

        public IKeyFunction getFunction() {
            return function;
        }

        public String getName() {
            return function.getTitle();
        }

        public FunctionGroupItem(int index, IKeyFunction function) {
            this.index = index;
            this.function = function;
        }
    }

    public static class FunctionGroup {

        private Array<FunctionGroupItem> functions = new Array<FunctionGroupItem>();

        public Array<FunctionGroupItem> getFunctions() {
            return functions;
        }

        private String name;

        public String getName() {
            return name;
        }

        public void addItem(IKeyFunction function) {
            int index = getFunctions().size;
            FunctionGroupItem item = new FunctionGroupItem(index, function);
            getFunctions().add(item);
        }

        public FunctionGroup(String name) {
            this.name = name;
        }
    }

}
