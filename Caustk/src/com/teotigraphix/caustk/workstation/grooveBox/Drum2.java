
package com.teotigraphix.caustk.workstation.grooveBox;

import com.teotigraphix.caustk.workstation.ComponentInfo;
import com.teotigraphix.caustk.workstation.GrooveBox;
import com.teotigraphix.caustk.workstation.GrooveBoxDescriptor;
import com.teotigraphix.caustk.workstation.GrooveBoxType;
import com.teotigraphix.caustk.workstation.GrooveBoxUtils;
import com.teotigraphix.caustk.workstation.GrooveSet;
import com.teotigraphix.caustk.workstation.MachineType;

public class Drum2 extends GrooveBox {

    public Drum2() {
    }

    public Drum2(ComponentInfo info, GrooveSet grooveSet) {
        super(info, grooveSet);

        GrooveBoxDescriptor descriptor = GrooveBoxUtils.createDescriptor(GrooveBoxType.Drum2);
        descriptor.addPart("p1", MachineType.Beatbox, null);
        descriptor.addPart("p2", MachineType.Beatbox, null);

        setDescriptor(descriptor);
    }

}
