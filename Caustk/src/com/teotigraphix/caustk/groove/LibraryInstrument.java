
package com.teotigraphix.caustk.groove;

import java.io.File;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.node.machine.MachineNode;

public class LibraryInstrument extends LibraryItem {

    private transient LibrarySound sound;

    private transient File pendingPresetFile;

    public File getPendingPresetFile() {
        return pendingPresetFile;
    }

    public void setPendingPresetFile(File pendingPresetFile) {
        this.pendingPresetFile = pendingPresetFile;
    }

    public LibrarySound getSound() {
        return sound;
    }

    public void setSound(LibrarySound sound) {
        this.sound = sound;
    }

    @Tag(10)
    private MachineNode machineNode;

    public MachineNode getMachineNode() {
        return machineNode;
    }

    public void setMachineNode(MachineNode machineNode) {
        this.machineNode = machineNode;
    }

    public LibraryInstrument(UUID id, FileInfo fileInfo, LibraryItemManifest manifest) {
        super(id, fileInfo, manifest);
    }
}
