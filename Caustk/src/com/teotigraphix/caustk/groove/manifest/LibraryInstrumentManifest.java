
package com.teotigraphix.caustk.groove.manifest;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.groove.library.LibraryItemFormat;
import com.teotigraphix.caustk.node.machine.MachineNode;

public class LibraryInstrumentManifest extends LibraryItemManifest {

    @Tag(10)
    private MachineType machineType;

    public MachineType getMachineType() {
        return machineType;
    }

    public LibraryInstrumentManifest(String name, String relativePath, MachineNode machineNode) {
        super(LibraryItemFormat.Instrument, name, relativePath);
        this.machineType = machineNode.getType();
    }

}
