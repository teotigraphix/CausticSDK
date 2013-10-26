
package com.teotigraphix.caustk.machine;

import java.io.IOException;

import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.osc.EffectRackMessage;
import com.teotigraphix.caustk.core.osc.SynthMessage;
import com.teotigraphix.caustk.rack.effect.EffectType;

public final class CaustkLibraryUtils {

    static void assignAndUpdatePresetFile(CaustkMachine machine, CaustkPatch livePatch,
            ICausticEngine engine) throws IOException {
        // get the preset name if machine has a loaded preset
        String presetName = SynthMessage.QUERY_PRESET.queryString(engine, machine.getIndex());
        // create the preset file
        MachinePreset presetFile = new MachinePreset(presetName, livePatch);
        livePatch.setMachinePreset(presetFile);
        // get the bytes of the machine's preset and put them into the preset file
        presetFile.update();
    }

    static void assignEffects(CaustkMachine machine, CaustkPatch livePatch, ICausticEngine engine) {
        final int machineIndex = machine.getIndex();
        EffectType effect0 = EffectType.fromInt((int)EffectRackMessage.TYPE.send(engine,
                machineIndex, 0));
        EffectType effect1 = EffectType.fromInt((int)EffectRackMessage.TYPE.send(engine,
                machineIndex, 1));
        if (effect0 != null) {
            CaustkEffect liveEffect = new CaustkEffect(0, effect0);
            livePatch.putEffect(0, liveEffect);
        }
        if (effect1 != null) {
            CaustkEffect liveEffect = new CaustkEffect(1, effect1);
            livePatch.putEffect(1, liveEffect);
        }
    }
}
