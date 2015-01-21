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

package com.teotigraphix.caustk.gdx.scene2d.ui.mixer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.caustk.core.osc.MixerControls;
import com.teotigraphix.caustk.gdx.scene2d.ui.app.UITable;
import com.teotigraphix.caustk.gdx.scene2d.ui.mixer.MixerPaneItem.MixerPaneItemListener;
import com.teotigraphix.caustk.node.machine.MachineNode;

public class MixerPane extends UITable {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private MixerPanePropertyProvider povider;

    private List<MixerPaneItem> mixers = new ArrayList<MixerPaneItem>();

    private MixerPaneListener listener;

    //--------------------------------------------------------------------------
    // Private Component :: Variables
    //--------------------------------------------------------------------------

    private ScrollPane scrollPane;

    private MixerPaneItem masterItem;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MixerPane(Skin skin, MixerPanePropertyProvider povider) {
        super(skin);
        this.povider = povider;
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    public void scrollLeft(Rectangle bounds, boolean down) {
        doScroll(bounds);
    }

    public void scrollRight(Rectangle bounds, boolean down) {
        doScroll(bounds);
    }

    public void redraw(Collection<? extends MachineNode> machines) {
        for (MachineNode machineNode : machines) {
            redraw(machineNode);
        }
    }

    public void redrawTriggers(List<Integer> machines) {
        for (MixerPaneItem item : mixers) {
            item.setTriggered(false);
        }
        for (Integer index : machines) {
            mixers.get(index).setTriggered(true);
        }
    }

    public void onMachineSelection(MachineNode machineNode) {
        for (MixerPaneItem item : mixers) {
            item.setSelected(false);
        }
        if (machineNode != null) {
            mixers.get(machineNode.getIndex()).setSelected(true);
        }
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createChildren() {
        Table root = new Table();

        scrollPane = new ScrollPane(root);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFlickScroll(false);

        for (int i = 0; i < 14; i++) {
            MixerPaneItem item = new MixerPaneItem(getSkin(), i);
            item.setMixerPaneItemListener(new MixerPaneItemListener() {
                @Override
                public void onSend(int index, MixerControls control, float value) {
                    listener.onSend(index, control, value);
                }
            });
            item.create("default");
            root.add(item).size(50f, 400f);
            mixers.add(item);
        }

        if (povider.hasMaster()) {
            masterItem = new MixerPaneItem(getSkin(), -1);
            masterItem.setMixerPaneItemListener(new MixerPaneItemListener() {
                @Override
                public void onSend(int index, MixerControls control, float value) {
                    listener.onSend(index, control, value);
                }
            });
            masterItem.create("default");
            root.add(masterItem).size(60f, 400f);
        }

        add(scrollPane);//.size(400f);
    }

    private void doScroll(Rectangle bounds) { // the mixerpaneitem bounds
        scrollPane.scrollTo(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void _refreshSolo(Collection<? extends MachineNode> machines) {
        for (MachineNode machineNode : machines) {
            MixerPaneItem item = mixers.get(machineNode.getIndex());
            item.redrawSolo(machineNode);
        }
    }

    private void redraw(MachineNode machineNode) {
        MixerPaneItem item = mixers.get(machineNode.getIndex());
        item.setMachineColor(povider.getItemColor(machineNode.getIndex()));
        item.redraw(machineNode);
        // TODO needs to be moved
        if (povider.hasMaster()) {
            masterItem.redraw(povider.getRack().getRackNode().getMaster());
        }
    }

    public static interface MixerPaneListener {
        void onSend(int index, MixerControls control, float value);
    }

    public void setMixerPaneListener(MixerPaneListener l) {
        this.listener = l;

    }

}
