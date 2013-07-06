
package com.teotigraphix.caustk.library.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustk.tone.ToneDescriptor;

public class RackInfo extends MementoInfo {

    public RackInfo() {
    }

    /**
     * Returns a lazy List of the {@link RackInfoItem}s found in the rack info.
     */
    public List<RackInfoItem> getItems() {
        List<RackInfoItem> result = new ArrayList<RackInfoItem>();
        IMemento[] children = getMemento().getChildren("machine");
        for (IMemento machine : children) {
            result.add(new RackInfoItem(machine));
        }
        return result;
    }

    public static class RackInfoItem {
        // <machine active="1" id="DRUMSIES" index="5" 
        // patchId="a146b131-d14d-4828-97b0-369c1accfa2d" type="beatbox"/>

        private UUID patchId;

        /**
         * Returns the patch {@link UUID} that the machine had when its library
         * was created.
         * <p>
         * This id may become invalid over time if libraries are merged.
         */
        public UUID getPatchId() {
            return patchId;
        }

        private String id;

        /**
         * Returns the String id of the machine in the rack.
         */
        public String getId() {
            return id;
        }

        private boolean active;

        public boolean isActive() {
            return active;
        }

        private int index;

        public int getIndex() {
            return index;
        }

        private MachineType machineType;

        public MachineType getMachineType() {
            return machineType;
        }

        public RackInfoItem(IMemento memento) {
            active = memento.getInteger("active") == 0 ? false : true;
            index = memento.getInteger("index");
            machineType = MachineType.fromString(memento.getString("type"));
            id = memento.getString("id");
            String pid = memento.getString("patchId");
            if (pid != null)
                patchId = UUID.fromString(pid);
        }

        public ToneDescriptor createDescriptor() {
            ToneDescriptor descriptor = new ToneDescriptor(index, id, machineType);
            return descriptor;
        }

    }
}
