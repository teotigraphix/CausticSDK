
package com.teotigraphix.caustk.tone;

import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.machine.IPCMSynth;

public class PCMSynthTone extends SynthTone {
    @SuppressWarnings("unused")
    private IPCMSynth pcmsynth;

    public PCMSynthTone(IMachine machine) {
        super(machine);
        pcmsynth = (IPCMSynth)machine;
    }

}
