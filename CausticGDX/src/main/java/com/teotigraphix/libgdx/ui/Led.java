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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Led extends ControlTable {

    LedPlacement ledPlacement;

    private String text;

    public String getText() {
        if (item != null)
            return item.getText();
        return text;
    }

    private Drawable drawable;

    public Drawable getDrawable() {
        if (item != null)
            return item.getDrawable();
        return drawable;
    }

    private boolean isOn;

    private Image offImage;

    private Image onImage;

    private Stack stack;

    private Label label;

    private Image image;

    private LedItem item;

    public Led(Skin skin) {
        super(skin);
    }

    public Led(LedItem item, LedPlacement ledPlacement, Skin skin) {
        this(skin);
        this.item = item;
        this.ledPlacement = ledPlacement;
    }

    public Led(String text, LedPlacement ledPlacement, Skin skin) {
        this(skin);
        this.text = text;
        this.ledPlacement = ledPlacement;
    }

    public Led(Drawable drawable, LedPlacement ledPlacement, Skin skin) {
        this(skin);
        this.drawable = drawable;
        this.ledPlacement = ledPlacement;
    }

    @Override
    protected void initialize() {
        super.initialize();
        styleClass = LedStyle.class;
    }

    @Override
    protected void createChildren() {
        stack = new Stack();
        add(stack);

        LedStyle style = getStyle();

        offImage = new Image(style.off);
        onImage = new Image(style.on);
        onImage.setVisible(false);

        stack.add(offImage);
        stack.add(onImage);

        if (ledPlacement != null) {
            switch (ledPlacement) {
                case TOP:
                    if (getText() != null) {
                        label = new Label(getText(), new LabelStyle(style.font, style.fontColor));
                        row();
                        add(label);
                    } else if (getDrawable() != null) {
                        image = new Image(getDrawable());
                        row();
                        add(image);
                    }
                    break;

                case LEFT:
                    if (getText() != null) {
                        label = new Label(getText(), new LabelStyle(style.font, style.fontColor));
                        add(label).padLeft(5f);
                    } else if (getDrawable() != null) {
                        image = new Image(getDrawable());
                        add(image).padLeft(5f);
                    }
                    break;
            }
        }

    }

    @Override
    public void layout() {
        super.layout();

        onImage.setVisible(isOn);
    }

    public void turnOn(float millis) {
        validate();
        turnOn();
        onImage.addAction(Actions.sequence(Actions.fadeIn(millis), Actions.delay(0.05f),
                Actions.fadeOut(millis), new Action() {
                    @Override
                    public boolean act(float delta) {
                        onImage.setVisible(false);
                        return true;
                    }
                }));
    }

    /**
     * Called by client to turn on the led for the specified amount.
     * 
     * @param millis
     */
    public void toggle(float millis) {
        validate();
        turnOn();
        onImage.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(millis),
                Actions.delay(0.05f), Actions.fadeOut(millis), new Action() {
                    @Override
                    public boolean act(float delta) {
                        return true;
                    }
                })));
    }

    public void turnOn() {
        isOn = true;
        invalidate();
    }

    public void turnOff() {
        isOn = false;
        invalidate();
    }

    public static class LedStyle {
        public Drawable on;

        public Drawable off;

        public BitmapFont font;

        public Color fontColor;

        public LedStyle() {
        }

    }

    public enum LedPlacement {
        TOP, LEFT
    }

    public static class LedItem {

        private String text;

        private Drawable image;

        private Object data;

        public Object getData() {
            return data;
        }

        public Drawable getDrawable() {
            return image;
        }

        public String getText() {
            return text;
        }

        public LedItem(String text) {
            this.text = text;
        }

        public LedItem(String text, Object data) {
            this.text = text;
            this.data = data;
        }

        public LedItem(Drawable image) {
            this.image = image;
        }

        public LedItem(Drawable image, Object data) {
            this.image = image;
            this.data = data;
        }
    }
}
