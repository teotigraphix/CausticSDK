
package com.teotigraphix.caustk.groove.manifest;

import java.io.File;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.node.machine.MachineNode;

public class LibraryInstrumentManifest extends LibraryItemManifest {

    @Tag(10)
    private MachineType machineType;

    public MachineType getMachineType() {
        return machineType;
    }

    public LibraryInstrumentManifest(String displayName, File archiveFile, String relativePath,
            MachineNode machineNode) {
        super(displayName, archiveFile, relativePath);
        this.machineType = machineNode.getType();
    }

}
