
package com.teotigraphix.caustk.tone;

import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.machine.ISubSynth;

public class SubSynthTone extends SynthTone {

    @SuppressWarnings("unused")
    private ISubSynth subsynth;

    public SubSynthTone(IMachine machine) {
        super(machine);
        subsynth = (ISubSynth)machine;
    }

}
