
package com.teotigraphix.caustk.node.machine.patch.modular;

import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.core.osc.ModularMessage;
import com.teotigraphix.caustk.core.osc.ModularMessage.ModularComponentType;

public class ModularUtils {

    private static Map<ModularComponentType, Map<IModularComponentControl, ModularControl>> map = new HashMap<ModularComponentType, Map<IModularComponentControl, ModularControl>>();

    static {
        initialize();
    }

    public enum WaveformGeneratorControl implements IModularComponentControl {
        Front_Knob_Waveform,

        Front_Knob_Octave,

        Front_Knob_Semis,

        Front_Knob_Cents,

        Rear_Knob_FM,

        Rear_Knob_AM,

        Rear_Knob_Pitch,

        Rear_Knob_Out
    }

    public enum ResonantLPControl implements IModularComponentControl {
        Front_Toggle_Slope,

        Front_Knob_Cutoff,

        Front_Knob_Resonance,

        Rear_Knob_Cutoff,

        Rear_Knob_Resonance
    }

    public enum SaturatorControl implements IModularComponentControl {
        Front_Knob_Amount,

        Rear_Knob_In,

        Rear_Knob_Out
    }

    public enum DecayEnvelopeControl implements IModularComponentControl {
        Front_Knob_Decay,

        Rear_Toggle_Slope,

        Rear_Knob_Out
    }

    private static void initialize() {
        // WaveformGenerator
        Map<IModularComponentControl, ModularControl> sub = null;

        sub = new HashMap<IModularComponentControl, ModularControl>();
        put(sub, WaveformGeneratorControl.Front_Knob_Waveform, "Waveform", "waveform", 1, 0f, 3f,
                0f);
        put(sub, WaveformGeneratorControl.Front_Knob_Octave, "Octave", "octave", 1, 0f, 3f, 0f);
        put(sub, WaveformGeneratorControl.Front_Knob_Semis, "Semis", "semis", 1, 0f, 3f, 0f);
        put(sub, WaveformGeneratorControl.Front_Knob_Cents, "Cents", "cents", 1, 0f, 3f, 0f);
        put(sub, WaveformGeneratorControl.Rear_Knob_FM, "FM", "fm_mod", 1, 0f, 3f, 0f);
        put(sub, WaveformGeneratorControl.Rear_Knob_AM, "AM", "am_mod", 1, 0f, 3f, 0f);
        put(sub, WaveformGeneratorControl.Rear_Knob_Pitch, "Pitch", "pitch_mod", 1, 0f, 3f, 0f);
        put(sub, WaveformGeneratorControl.Rear_Knob_Out, "Out", "out_gain", 1, 0f, 3f, 0f);
        map.put(ModularComponentType.WaveformGenerator, sub);

        sub = new HashMap<IModularComponentControl, ModularControl>();
        put(sub, ResonantLPControl.Front_Toggle_Slope, "Slope", "slope", 0, 0f, 1f, 0f);
        put(sub, ResonantLPControl.Front_Knob_Cutoff, "Cutoff", "cutoff", 1, 0f, 1f, 0f);
        put(sub, ResonantLPControl.Front_Knob_Resonance, "Resonance", "resonance", 1, 0f, 1f, 0f);
        put(sub, ResonantLPControl.Rear_Knob_Cutoff, "Cutoff", "cut_mod", 1, 0f, 1f, 0f);
        put(sub, ResonantLPControl.Rear_Knob_Resonance, "Resonance", "res_mod", 1, 0f, 1f, 0f);
        map.put(ModularComponentType.ResonantLP, sub);

        sub = new HashMap<IModularComponentControl, ModularControl>();
        put(sub, SaturatorControl.Front_Knob_Amount, "Amount", "amount", 1, 0f, 1f, 0f);
        put(sub, SaturatorControl.Rear_Knob_In, "In", "in_gain", 1, 0f, 1f, 0f);
        put(sub, SaturatorControl.Rear_Knob_Out, "Out", "out_gain", 1, 0f, 1f, 0f);
        map.put(ModularComponentType.Saturator, sub);

        sub = new HashMap<IModularComponentControl, ModularControl>();
        put(sub, DecayEnvelopeControl.Front_Knob_Decay, "Decay", "decay", 1, 0f, 2f, 0f);
        put(sub, DecayEnvelopeControl.Rear_Toggle_Slope, "Slope", "decay_slope", 0, 0f, 2f, 0f);
        put(sub, DecayEnvelopeControl.Rear_Knob_Out, "Out", "out_gain", 1, 0f, 3f, 0f);
        map.put(ModularComponentType.DecayEnvelope, sub);
    }

    public static float getValue(int machineIndex, int bay, String control) {
        return ModularMessage.SET.query(CaustkRuntime.getInstance().getRack(), machineIndex, bay,
                control);
    }

    public static void setValue(int machineIndex, int bay, String control, Number value) {
        ModularMessage.SET.send(CaustkRuntime.getInstance().getRack(), machineIndex, bay, control,
                value);
    }

    public static Map<IModularComponentControl, ModularControl> getControls(
            ModularComponentType type) {
        return map.get(type);
    }

    private static void put(Map<IModularComponentControl, ModularControl> map,
            IModularComponentControl control, String name, String osc, int type, float min,
            float max, float defaultValue) {
        map.put(control, new ModularControl(control, name, osc, type, min, max, defaultValue));
    }
}
