
package com.teotigraphix.caustk.library;

import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustk.library.vo.MetadataInfo;

public class TagUtils {

    public static void addDefaultTags(IMachine machine, LibraryPatch item) {
        MetadataInfo info = item.getMetadataInfo();
        info.getTags().add(item.getMachineType().getValue());
        info.getTags().add(machine.getId());
        String preset = machine.getPresetName();
        if (preset != null && !preset.isEmpty())
            info.getTags().add(preset);  
    }

    public static void addDefaultTags(LibraryPhrase item) {
        MetadataInfo info = item.getMetadataInfo();
        info.getTags().add("length-" + item.getLength());
        info.getTags().add(item.getTempo() + "");
        info.getTags().add(item.getMachineType().getValue());
        info.getTags().add(item.getResolution().toString().toLowerCase());
    }

    public static void addDefaultTags(String name, IRack rack, LibraryScene item) {
        // add original song name, 
        MetadataInfo info = item.getMetadataInfo();
        info.getTags().add(name);
        info.getTags().add(rack.getOutputPanel().getBPM() + "");
    }

}
