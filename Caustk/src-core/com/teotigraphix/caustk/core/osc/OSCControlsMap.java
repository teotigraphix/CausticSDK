
package com.teotigraphix.caustk.core.osc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.node.effect.EffectNode;
import com.teotigraphix.caustk.node.effect.EffectType;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.PCMSynthMachine;

public final class OSCControlsMap {

    @SuppressWarnings("unused")
    private static Class<EffectControls> effectControls = EffectControls.class;

    @SuppressWarnings("unused")
    private static Class<PCMSynthControls> pcmSynthControls = PCMSynthControls.class;

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private static Map<EffectType, Collection<IEffectControl>> effectMap = new HashMap<EffectType, Collection<IEffectControl>>();

    private static Map<MachineType, Collection<IMachineControl>> machineMap = new HashMap<MachineType, Collection<IMachineControl>>();

    //--------------------------------------------------------------------------
    // Public API
    //--------------------------------------------------------------------------

    public static Collection<IEffectControl> get(EffectType type) {
        return effectMap.get(type);
    }

    public static float getValue(int machineIndex, int slot, IEffectControl control) {
        float value = EffectsRackMessage.GET.query(CaustkRuntime.getInstance().getRack(),
                machineIndex, slot, control.getControl());
        if (value < control.getMin()) {
            System.err.println(control + " MIN EffectNode " + value);
            value = control.getMin();
        } else if (value > control.getMax()) {
            System.err.println(control + " MAX EffectNode " + value);
            value = control.getMax();
        }

        return value;
    }

    public static void setValue(EffectNode effectNode, IEffectControl control, float value) {
        String methodName = control.getControl();
        int index = methodName.indexOf("_");
        if (index != -1) {
            String start = methodName.substring(0, index);
            String end = methodName.substring(index + 1);
            String cap = String.valueOf(end.charAt(0)).toUpperCase(Locale.getDefault());
            methodName = start + cap + end.substring(1);
        }
        String setterName = methodName.substring(1);
        String first = String.valueOf(methodName.charAt(0)).toUpperCase(Locale.getDefault());
        setterName = "set" + first + setterName;

        try {
            Method m = effectNode.getClass().getMethod(setterName, float.class);
            m.invoke(effectNode, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static Collection<IMachineControl> get(MachineType type) {
        return machineMap.get(type);
    }

    public static float getValue(MachineNode machineNode, IMachineControl control) {
        float value = CaustkRuntime.getInstance().getRack()
                .sendMessage("/caustic/" + machineNode.getIndex() + "/" + control.getControl());
        if (value < control.getMin()) {
            System.err.println(control + " MIN EffectNode " + value);
            value = control.getMin();
        } else if (value > control.getMax()) {
            System.err.println(control + " MAX EffectNode " + value);
            value = control.getMax();
        }
        return value;
    }

    public static void setValue(PCMSynthMachine machine, IMachineControl control, float value) {
        String methodName = control.getControl();
        String[] split = methodName.split("_");
        String component = split[0];
        String name = split[1];

        String setterName = name.substring(1);
        String first = String.valueOf(name.charAt(0)).toUpperCase(Locale.getDefault());
        setterName = "set" + first + setterName;

        MachineComponent machineComponent = null;
        if (methodName.equals("volume_out")) {
            machineComponent = machine.getVolume();
        } else if (component.equals("volume")) {
            machineComponent = machine.getVolumeEnvelope();
        } else if (component.equals("lfo")) {
            machineComponent = machine.getLFO1();
        } else if (component.equals("pitch")) {
            machineComponent = machine.getTuner();
        } else if (component.equals("filter")) {
            machineComponent = machine.getFilter();
        }

        try {
            Method m = machineComponent.getClass().getMethod(setterName, float.class);
            m.invoke(machineComponent, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------
    // Internal :: Methods
    //--------------------------------------------------------------------------

    static void add(EffectType type, IEffectControl control) {
        Collection<IEffectControl> collection = effectMap.get(type);
        if (collection == null) {
            collection = new ArrayList<IEffectControl>();
            effectMap.put(type, collection);
        }
        collection.add(control);
    }

    static void add(MachineType type, IMachineControl control) {
        Collection<IMachineControl> collection = machineMap.get(type);
        if (collection == null) {
            collection = new ArrayList<IMachineControl>();
            machineMap.put(type, collection);
        }
        collection.add(control);
    }
}
