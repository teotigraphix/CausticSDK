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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

/**
 * @author Michael Schmalle
 */
public class DigitalDisplay extends ControlTable {

    private Array<Label> labels = new Array<Label>();

    private Label statusLabel;

    //--------------------------------------------------------------------------
    // Property :: API
    //--------------------------------------------------------------------------

    //----------------------------------
    // text
    //----------------------------------

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    //----------------------------------
    // status
    //----------------------------------

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        invalidate();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public DigitalDisplay(Skin skin) {
        super(skin);
        styleClass = DigitalDisplayStyle.class;
        create(skin);
    }

    private void create(Skin skin) {
        DigitalDisplayStyle digitalDisplayStyle = new DigitalDisplayStyle();
        digitalDisplayStyle.background = skin.getDrawable("digital_display_background");
        digitalDisplayStyle.font = skin.getFont("digital-dream"); // digital-dream
        digitalDisplayStyle.fontColor = skin.getColor("red");
        skin.add("default", digitalDisplayStyle);
    }

    //--------------------------------------------------------------------------
    // Overridden :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createChildren() {
        align(Align.left | Align.top);

        DigitalDisplayStyle style = getStyle();

        setBackground(style.background);

        pad(null);
        padLeft(10f);

        int numChars = 4;

        for (int i = 0; i < numChars; i++) {
            Label label = new Label("", new LabelStyle(style.font, style.fontColor));
            labels.add(label);
            add(label).top();
        }
        add().expand();
        row();

        statusLabel = new Label("", getSkin());
        add(statusLabel).right().colspan(labels.size + 1).padRight(10f).padBottom(10f).height(25f);
    }

    @Override
    public void layout() {
        super.layout();

        int index = 0;
        if (text != null) {
            for (Label label : labels) {
                if (index < text.length()) {
                    label.setText(String.valueOf(text.charAt(index)));
                } else {
                    label.setText("");
                }
                index++;
            }
        }

        statusLabel.setText(status);
    }

    public void blinkOn() {
        blinkOn(0.3f);
    }

    public void blinkOn(float duration) {
        for (Label label : labels) {
            label.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(duration),
                    Actions.fadeOut(duration))));
        }
    }

    public void blinkOff() {
        for (Label label : labels) {
            label.clearActions();
            label.getColor().a = 1f;
            label.invalidate();
        }
    }

    //--------------------------------------------------------------------------
    // Style
    //--------------------------------------------------------------------------

    public static class DigitalDisplayStyle {

        public Drawable background;

        public BitmapFont font;

        public Color fontColor;

        public DigitalDisplayStyle() {
        }
    }
}
