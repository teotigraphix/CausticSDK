
package com.teotigraphix.caustk.tone;

import com.teotigraphix.caustic.machine.IBassline;

public class BasslineTone extends SynthTone {
    @SuppressWarnings("unused")
    private IBassline bassline;

    public BasslineTone(IBassline machine) {
        super(machine);
        bassline = machine;
    }

}
