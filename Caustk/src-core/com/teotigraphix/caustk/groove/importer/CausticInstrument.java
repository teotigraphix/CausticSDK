
package com.teotigraphix.caustk.groove.importer;

import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;

public class CausticInstrument extends CausticItem {

    private MachineType type;

    public MachineType getType() {
        return type;
    }

    public CausticInstrument(LibraryInstrument item) {
        super(item);
        this.type = item.getManifest().getMachineType();
    }

}
