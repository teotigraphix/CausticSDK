////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.tone;

import com.teotigraphix.caustic.internal.sequencer.StepSequencer;
import com.teotigraphix.caustic.machine.IMachine;

public class SynthTone extends Tone {
    private IMachine machine;

    @Override
    public IMachine getMachine() {
        return machine;
    }

    public SynthTone(IMachine machine) {
        this.machine = machine;
    }

    public void _setLength(int value) {
        //StepSequencer s = (StepSequencer) machine.getSequencer();
        //s.getTriggerMap().setLength(value);
    }

    public int _getLength() {
        //StepSequencer s = (StepSequencer) machine.getSequencer();
        //return s.getTriggerMap().getLength();
        return -1;
    }

    /**
     * Called from the {@link SystemSequencer} in the triggerOn observer.
     * 
     * @param step
     * @param pitch
     * @param gate
     * @param velocity
     * @param flags
     */
    public void _triggerOn(int step, int pitch, float gate, float velocity, int flags) {
        StepSequencer s = (StepSequencer)machine.getSequencer();
        s.triggerOn(step, pitch, gate, velocity, flags);
    }

    /**
     * Called from the {@link SystemSequencer} in the triggerOff observer.
     * 
     * @param step
     * @param pitch
     */
    public void _triggerOff(int step, int pitch) {
        StepSequencer s = (StepSequencer)machine.getSequencer();
        s.triggerOff(step, pitch);
    }

}
