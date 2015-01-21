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

package com.teotigraphix.caustk.gdx.scene2d.ui.auto;

import com.teotigraphix.caustk.core.osc.IAutomatableControl;
import com.teotigraphix.caustk.core.osc.OSCUtils;

public class AutomationItem {

    private IAutomatableControl control;

    private String label;

    private float min;

    private float max;

    private float value;

    private float step = 0.01f;

    public IAutomatableControl getControl() {
        return control;
    }

    public String getLabel() {
        return label;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getStep() {
        return step;
    }

    public AutomationItem(IAutomatableControl control) {
        this.control = control;
        this.label = OSCUtils.optimizeName(control.getDisplayName(), 7);
        this.min = control.getMin();
        this.max = control.getMax();
        this.value = control.getDefaultValue();
    }

    public static AutomationItem create(IAutomatableControl control) {
        return new AutomationItem(control);
    }

    @Override
    public String toString() {
        return "AutomationItem [control=" + control + ", label=" + label + ", min=" + min
                + ", max=" + max + ", value=" + value + "]";
    }

}
