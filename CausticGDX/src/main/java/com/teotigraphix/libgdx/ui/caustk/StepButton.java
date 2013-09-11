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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.teotigraphix.libgdx.ui.ControlTable;

public class StepButton extends ControlTable {

    private StepButtonItem item;

    private TextButton button;

    private boolean noEvent = false;

    private OnStepButtonListener onStepButtonListener;

    private Stack stack;

    private StepButtonOverlayGroup overlay;

    private ButtonGroup buttonGroup;

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    public void setButtonGroup(ButtonGroup value) {
        buttonGroup = value;
    }

    public int getIndex() {
        return item.getIndex();
    }

    public StepButton(StepButtonItem item, Skin skin) {
        super(skin);
        this.item = item;
    }

    @Override
    protected void createChildren() {
        stack = new Stack();

        button = new TextButton(item.getText(), getSkin(), item.getStyleName());
        if (buttonGroup != null)
            buttonGroup.add(button);
        button.getLabel().setAlignment(Align.top);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (noEvent || !item.isToggle())
                    event.cancel();
                onStepButtonListener.onChange(getIndex(), button.isChecked());

            }
        });
        button.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onStepButtonListener.onTouchDown(getIndex());
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                onStepButtonListener.onTouchUp(getIndex());
            }
        });
        stack.add(button);
        add(stack).size(50f, 70f);

        overlay = new StepButtonOverlayGroup(getSkin());
        stack.add(overlay);
    }

    public void select(boolean selected) {
        noEvent = true;
        button.setChecked(selected);
        noEvent = false;

    }

    public void toggle() {
        noEvent = true;
        button.toggle();
        noEvent = false;
    }

    public void setOnStepButtonListener(OnStepButtonListener l) {
        onStepButtonListener = l;
    }

    public interface OnStepButtonListener {
        void onChange(int index, boolean selected);

        void onTouchUp(int index);

        void onTouchDown(int index);
    }

    public static class StepButtonItem {

        private String text;

        private String styleName;

        private int index;

        private boolean isToggle;

        public boolean isToggle() {
            return isToggle;
        }

        public int getIndex() {
            return index;
        }

        public String getStyleName() {
            return styleName;
        }

        public String getText() {
            return text;
        }

        public StepButtonItem(int index, String text, boolean isToggle, String styleName) {
            this.index = index;
            this.text = text;
            this.isToggle = isToggle;
            this.styleName = styleName;
        }

    }

    public void selectActive(boolean selected) {
        overlay.select(selected);
    }

}
