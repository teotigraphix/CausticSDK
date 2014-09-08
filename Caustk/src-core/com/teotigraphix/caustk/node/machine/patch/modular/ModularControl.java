
package com.teotigraphix.caustk.node.machine.patch.modular;

public class ModularControl {

    private String name;

    private String osc;

    private int type; // 0,1

    private float min;

    private float max;

    private float defaultValue;

    private IModularComponentControl control;

    public boolean isFront() {
        return control.name().contains("Front");
    }

    public IModularComponentControl getControl() {
        return control;
    }

    public String getName() {
        return name;
    }

    public String getOsc() {
        return osc;
    }

    public int getType() {
        return type;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    public ModularControl(IModularComponentControl control, String name, String osc, int type,
            float min, float max, float defaultValue) {
        super();
        this.control = control;
        this.name = name;
        this.osc = osc;
        this.type = type;
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
    }

    public float getValue(IModularComponent component) {
        return ModularUtils.getValue(component.getMachineIndex(), component.getBay(), osc);
    }

    public void setValue(IModularComponent component, String control, Number value) {
        ModularUtils.setValue(component.getMachineIndex(), component.getBay(), control, value);
    }

    @Override
    public String toString() {
        return name;
    }

}
