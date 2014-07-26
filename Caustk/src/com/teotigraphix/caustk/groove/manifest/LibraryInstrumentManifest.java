
package com.teotigraphix.caustk.groove.manifest;

import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.groove.LibraryBank;
import com.teotigraphix.caustk.node.machine.MachineNode;

public class LibraryInstrumentManifest extends LibraryItemManifest {

    private MachineType type;

    public MachineType getType() {
        return type;
    }

    public LibraryInstrumentManifest(String name, MachineNode machineNode) {
        super(name);
        this.type = machineNode.getType();
    }

    public LibraryInstrumentManifest(String name, LibraryBank libraryBank) {
        super(name, libraryBank);
    }

}
