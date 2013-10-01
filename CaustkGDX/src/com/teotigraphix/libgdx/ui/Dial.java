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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Dial extends ControlTable {

    private Image background;

    private Image knob;

    private float dragStartDeg;

    private int totalNicks = 12;

    private int currentNick = 0;

    private int lastNick = 0;

    //--------------------------------------------------------------------------
    // Property :: API
    //--------------------------------------------------------------------------

    //----------------------------------
    // totalNicks
    //----------------------------------

    public final int getTotalNicks() {
        return totalNicks;
    }

    //----------------------------------
    // currentNicks
    //----------------------------------

    public final int getCurrentNick() {
        return currentNick;
    }

    //----------------------------------
    // degrees
    //----------------------------------

    public final float getRotationInDegrees() {
        return (360.0f / totalNicks) * currentNick;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Dial(Skin skin) {
        super(skin);
        styleClass = DialStyle.class;
        create(skin);
    }

    private void create(Skin skin) {
        DialStyle dialStyle = new DialStyle();
        dialStyle.background = skin.getDrawable("dial_background");
        dialStyle.knob = skin.getDrawable("dial_knob");
        skin.add("default", dialStyle);
    }

    //--------------------------------------------------------------------------
    // Overridden :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void initialize() {
        super.initialize();

        addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                x -= getWidth() / 2;
                y -= getHeight() / 2;
                dragStartDeg = xyToDegrees(x, y);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                x -= getWidth() / 2;
                y -= getHeight() / 2;

                if (!Float.isNaN(dragStartDeg)) {
                    float currentDeg = xyToDegrees(x, y);

                    if (!Float.isNaN(currentDeg)) {
                        float degPerNick = 360.0f / getTotalNicks();
                        float deltaDeg = dragStartDeg - currentDeg;

                        final int nicks = (int)(Math.signum(deltaDeg) * Math.floor(Math
                                .abs(deltaDeg) / degPerNick));

                        if (nicks != 0) {
                            dragStartDeg = currentDeg;
                            rotate(nicks);
                        }
                    }
                } else {
                }
            }
        });
    }

    @Override
    protected void createChildren() {
        DialStyle style = getStyle();
        background = new Image(style.background);

        knob = new Image(style.knob);

        //style.knob.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        knob.setTouchable(Touchable.disabled);

        Stack stack = new Stack();
        stack.add(background);
        stack.add(knob);

        add(stack).size(background.getPrefWidth(), background.getPrefHeight());
    }

    @Override
    public void layout() {
        super.layout();
        knob.setOrigin(getWidth() / 2, getHeight() / 2);
        knob.setRotation(getRotationInDegrees());
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    public final void rotate(int nicks) {
        lastNick = currentNick;

        currentNick = (currentNick + nicks);
        if (currentNick >= totalNicks) {
            currentNick %= totalNicks;
        } else if (currentNick < 0) {
            currentNick = (totalNicks + currentNick);
        }

        int change = currentNick - lastNick;
        if (change > 0)
            change = Math.min(change, 1);
        else if (change < 0)
            change = Math.max(change, -1);

        if (change > 0) {
            if (onDialListener != null)
                onDialListener.onDecrement();
        }

        if (change < 0) {
            if (onDialListener != null)
                onDialListener.onIncrement();
        }

        if (onDialListener != null)
            onDialListener.onDialPositionChanged(this, currentNick);
        invalidate();
    }

    //--------------------------------------------------------------------------
    // Listener
    //--------------------------------------------------------------------------

    private OnDialListener onDialListener;

    public void setOnDialListener(OnDialListener l) {
        onDialListener = l;
    }

    public interface OnDialListener {
        void onDialPositionChanged(Dial sender, int nicksChanged);

        void onIncrement();

        void onDecrement();
    }

    //--------------------------------------------------------------------------
    // Style
    //--------------------------------------------------------------------------

    public static class DialStyle {
        public Drawable background;

        public Drawable knob;

        public DialStyle() {
        }
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    private float xyToDegrees(float x, float y) {
        Vector2 vector = new Vector2(x, y);
        float distanceFromCenter = vector.len();
        if (distanceFromCenter < 10f/* || distanceFromCenter > 100f*/) {
            return Float.NaN;
        }
        float degrees = (float)Math.toDegrees(Math.atan2(x, y));
        return degrees;
    }
}
