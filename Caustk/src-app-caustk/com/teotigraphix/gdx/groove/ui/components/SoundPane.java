////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.gdx.groove.ui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.gdx.groove.ui.components.PatternPane.PatternPaneStyle;
import com.teotigraphix.gdx.groove.ui.components.SoundSelectionListener.SoundSelectionEvent;
import com.teotigraphix.gdx.groove.ui.components.SoundSelectionListener.SoundSelectionEventKind;
import com.teotigraphix.gdx.groove.ui.factory.StylesDefault;

public class SoundPane extends UITable {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private ButtonGroup gridGroup;

    private boolean updating;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SoundPane(Skin skin) {
        super(skin);
        setStyleClass(PatternPaneStyle.class);
    }

    public void refresh(MachineNode machineNode, boolean isConnected) {
        int index = machineNode.getIndex();
        if (isConnected) {
            setMachineName(index, machineNode.getName());
        } else {
            setMachineName(index, "");
        }
        disable(index, !isConnected);
    }

    public void setMachineName(int index, String name) {
        ((TextButton)gridGroup.getButtons().get(index)).setText(name);
    }

    public void disable(int index, boolean disabled) {
        gridGroup.getButtons().get(index).setDisabled(disabled);
    }

    public void disable(boolean disabled) {
        for (int i = 0; i < 14; i++) {
            disable(i, disabled);
        }
    }

    public void select(int index) {
        gridGroup.getButtons().get(index).setChecked(true);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createChildren() {
        gridGroup = new ButtonGroup();

        Table topRow = new Table();
        topRow.pad(4f);

        createPatternGrid(topRow);

        add(topRow).expand().fill();

        disable(true);
        disable(14, true);
        disable(15, true);
    }

    private ButtonGroup createPatternGrid(Table parent) {
        PatternPaneStyle style = getStyle();
        parent.pad(4f);
        // XXX until you get a new ToggleButton impl, buttonGroup is trying to check in add()
        updating = true;
        for (int i = 0; i < 16; i++) {
            final int index = i;
            TextButton button = new TextButton("", style.padButtonStyle);
            if (i < 14) {
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (updating)
                            return;
                        SoundSelectionEvent e = new SoundSelectionEvent(
                                SoundSelectionEventKind.selectedIndexChange, index);
                        fire(e);
                    }
                });
            }

            parent.add(button).expand().fill().space(4f);
            gridGroup.add(button);
            if (i % 4 == 3)
                parent.row();
        }
        updating = false;
        return null;
    }

    public void redraw(MachineNode machineNode) {
        TextButton button = (TextButton)gridGroup.getButtons().get(machineNode.getIndex());
        Color color = StylesDefault.getMachineColor(machineNode.getType());
        if (color != null)
            button.setColor(color);

    }

}
