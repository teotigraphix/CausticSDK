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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.tablelayout.Cell;

@SuppressWarnings("unchecked")
public class TitleGroup extends ControlTable {

    private Cell<Actor> labelCell;

    private Cell<Actor> contentCell;

    Table contentTable;

    private LabelPlacement labelPlacement;

    private String text;

    private String labelStyleName;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LabelPlacement getLabelPlacement() {
        return labelPlacement;
    }

    public TitleGroup(Skin skin, String text, String labelStyleName) {
        this(skin, text, labelStyleName, LabelPlacement.TOP);
    }

    public TitleGroup(Skin skin, String text, String labelStyleName, LabelPlacement labelPlacement) {
        super(skin);
        this.text = text;
        this.labelStyleName = labelStyleName;
        this.labelPlacement = labelPlacement;
        //debug();
    }

    @Override
    protected void createChildren() {
        if (labelPlacement == LabelPlacement.TOP)
            createTop();
        else
            createBottom();
    }

    private void createTop() {
        Label actor = new Label(text, getSkin(), labelStyleName);
        labelCell = super.add(actor);
        labelCell.center().expandX();
        row();

        contentTable = new Table(getSkin());
        contentCell = super.add(contentTable);
        contentCell.fill().expand();
    }

    private void createBottom() {
        contentTable = new Table(getSkin());
        contentCell = super.add(contentTable);
        contentCell.fill().expand();

        row();
        Label actor = new Label(text, getSkin(), labelStyleName);
        labelCell = super.add(actor);
        labelCell.center();
    }

    @Override
    public Cell<Actor> add(Actor actor) {
        validate();
        return contentTable.add(actor);
    }

    public enum LabelPlacement {
        TOP, BOTTOM
    }
}
