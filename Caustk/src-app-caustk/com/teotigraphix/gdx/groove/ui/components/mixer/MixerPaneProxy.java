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

package com.teotigraphix.gdx.groove.ui.components.mixer;

import java.util.List;

import com.teotigraphix.caustk.core.osc.MixerControls;
import com.teotigraphix.caustk.core.osc.OSCUtils;
import com.teotigraphix.caustk.node.BehaviorUtils;
import com.teotigraphix.gdx.groove.app.GrooveBehavior;
import com.teotigraphix.gdx.groove.ui.components.mixer.MixerPane.MixerPaneListener;

/**
 * @see MixerPane
 * @see MixerPaneItem
 * @see #create()
 * @see #redraw()
 */
public class MixerPaneProxy {

    private GrooveBehavior behavior;

    private MixerPane pane;

    public MixerPane getPane() {
        return pane;
    }

    private MixerPanePropertyProvider provider;

    public MixerPaneProxy(GrooveBehavior behavior, MixerPanePropertyProvider provider) {
        this.behavior = behavior;
        this.provider = provider;
    }

    public MixerPane create() {
        pane = new MixerPane(behavior.getSkin(), provider);
        pane.setMixerPaneListener(new MixerPaneListener() {
            @Override
            public void onSend(int index, MixerControls control, float value) {
                send(index, control, value);
            }
        });
        pane.create("default");
        return pane;
    }

    public void redraw() {
        pane.redraw(behavior.getRack().machines());
        pane.onMachineSelection(behavior.getProjectModel().getMachineAPI().getSelectedMachine());
    }

    public void redrawTriggers(List<Integer> machines) {
        pane.redrawTriggers(machines);
    }

    protected void send(int index, MixerControls control, float value) {
        BehaviorUtils.send(behavior.getRack(), index, control, value);
        if (provider.getDisplay() != null)
            provider.getDisplay().showNotification(
                    OSCUtils.optimizeName(control.getDisplayName(), 8) + " "
                            + OSCUtils.precision(value, 2), 1f, true);
    }

}
