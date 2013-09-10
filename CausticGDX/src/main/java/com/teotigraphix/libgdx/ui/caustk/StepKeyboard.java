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

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.libgdx.ui.ControlTable;
import com.teotigraphix.libgdx.ui.caustk.StepButton.OnStepButtonListener;
import com.teotigraphix.libgdx.ui.caustk.StepButton.StepButtonItem;

public class StepKeyboard extends ControlTable {

    private StepButton lastStepButton;

    private Stack stack;

    private Table stepGroup;

    private Table keyGroup;

    int[] sharps = new int[] {
            1, 4, 6, 9, 11, 13
    };

    private OnStepKeyboardListener onStepKeyboardListener;

    public enum StepKeyboardMode {

        Step(0),

        Key(1);

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

    public StepKeyboard(Skin skin) {
        super(skin);
    }

    //--------------------------------------------------------------------------
    // Overridden :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createChildren() {
        stack = new Stack();

        stepGroup = createStepGroup();
        keyGroup = createKeyGroup();

        stack.add(stepGroup);
        stack.add(keyGroup);

        add(stack);
    }

    @Override
    public void validate() {
        super.validate();

        if (mode == StepKeyboardMode.Key) {
            stepGroup.setVisible(false);
            keyGroup.setVisible(true);
        } else if (mode == StepKeyboardMode.Step) {
            stepGroup.setVisible(true);
            keyGroup.setVisible(false);
        }
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
    }

    protected final StepButton getStepButton(int index) {
        if (mode == StepKeyboardMode.Key)
            return (StepButton)keyGroup.getChildren().get(index);
        else if (mode == StepKeyboardMode.Step)
            return (StepButton)stepGroup.getChildren().get(index);
        return null;
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

}
