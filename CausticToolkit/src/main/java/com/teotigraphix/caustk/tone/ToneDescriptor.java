
package com.teotigraphix.caustk.tone;

import com.teotigraphix.caustic.machine.MachineType;

/**
 * Each application must define the {@link Tone} instances that will get created
 * at startup.
 */
public class ToneDescriptor {
    private final int index;

    public int getIndex() {
        return index;
    }

    private final String id;

    public String getId() {
        return id;
    }

    private final MachineType machineType;

    public MachineType getMachineType() {
        return machineType;
    }

    public ToneDescriptor(int index, String name, MachineType machineType) {
        this.index = index;
        this.id = name;
        this.machineType = machineType;
    }
}
