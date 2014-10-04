
package com.teotigraphix.caustk.groove.importer;

import com.teotigraphix.caustk.core.MachineType;

public class CausticInstrument extends CausticItem {

    private MachineType type;

    public MachineType getType() {
        return type;
    }

    public CausticInstrument(String path, String displayName, MachineType type) {
        super(path, displayName);
        this.type = type;
    }

}
