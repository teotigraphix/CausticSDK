
package com.teotigraphix.gdx.groove.ui.components.auto;

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
