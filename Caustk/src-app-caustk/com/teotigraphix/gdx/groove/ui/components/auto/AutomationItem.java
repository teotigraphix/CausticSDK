
package com.teotigraphix.gdx.groove.ui.components.auto;

import com.teotigraphix.caustk.core.osc.IOSCControl;

public class AutomationItem {

    private IOSCControl control;

    private String label;

    private float min;

    private float max;

    private float value;

    private float step = 0.01f;

    public IOSCControl getControl() {
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

    void setValue(float value) {
        this.value = value;
    }

    public float getStep() {
        return step;
    }

    public AutomationItem(IOSCControl control, String label, float min, float max, float value) {
        this.control = control;
        this.label = label;
        this.min = min;
        this.max = max;
        this.value = value;
    }

    public static AutomationItem create(IOSCControl id, String label, float min, float max,
            float value) {
        return new AutomationItem(id, label, min, max, value);
    }

    @Override
    public String toString() {
        return "AutomationItem [control=" + control + ", label=" + label + ", min=" + min
                + ", max=" + max + ", value=" + value + "]";
    }

}
