
package com.teotigraphix.caustk.core.components;

import com.teotigraphix.caustic.osc.MachineMessage;

/*
 * - presets load/save
 * - preset name
 */
public class MachineComponent extends ToneComponent {

    public String getPresetName() {
        return MachineMessage.QUERY_PRESET.queryString(getEngine(), getTone().getIndex());
    }

    public MachineComponent() {
    }

    @Override
    public String serialize() {
        return super.serialize();
    }

    @Override
    public void restore() {
    }
}
