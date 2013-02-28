
package com.teotigraphix.caustic.internal.sequencer;

import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.sequencer.IStepSequencer;

public class StepSequencer extends PatternSequencer implements IStepSequencer {

    public StepSequencer(IDevice device) {
        super(device);
    }

    @Override
    public void triggerOn(int step, int pitch, float gate, float velocity, int flags) {
    }

    @Override
    public void triggerOff(int step, int pitch) {
    }

    @Override
    public void triggerOff(int step, int pitch, boolean remove) {
    }

}
