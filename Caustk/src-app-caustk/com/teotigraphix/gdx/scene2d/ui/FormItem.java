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

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

/**
 * @author Michael Schmalle
 */
public class FormItem extends ControlTable {

    private TextField textField;

    private Label label;

    private String labelText;

    private String initText;

    private float columnWidth;

    @Override
    public String getHelpText() {
        return null;
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String value) {
        labelText = value;
        invalidate();
    }

    public String getInitText() {
        return initText;
    }

    public void setInitText(String value) {
        initText = value;
    }

    public void updateText(String text) {
        textField.setText(text);
    }

    public String getText() {
        return textField.getText();
    }

    public FormItem(String labelText, String initText, float columnWidth, Skin skin) {
        super(skin);
        this.labelText = labelText;
        this.initText = initText;
        this.columnWidth = columnWidth;
        createChildren();
    }

    protected void createChildren() {
        label = new Label(labelText, getSkin());
        add(label).align(Align.left).width(columnWidth);
        row();

        textField = new TextField(initText, getSkin());
        add(textField).expandX().fillX();
    }

    @Override
    public void layout() {
        super.layout();

        label.setText(labelText);
    }

}
