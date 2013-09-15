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

package com.teotigraphix.caustk.gs.machine;

import com.teotigraphix.caustk.gs.machine.part.bassline.BasslineMachineSound;
import com.teotigraphix.caustk.gs.pattern.PartUtils;
import com.teotigraphix.caustk.tone.BasslineTone;

/*
 * Part1 - Bassline
 * Part2 - Bassline
 */
public class BasslineMachine extends GrooveMachine {

    public BasslineMachine() {
    }

    @Override
    protected void createComponentParts() {
        setSound(new BasslineMachineSound(this));
    }

    public float getSynthProperty(int channel, SynthProperty property) {
        BasslineTone tone = (BasslineTone)PartUtils.getTone(getParts().get(channel));
        float value = Float.NaN;
        switch (property) {
            case Accent:
                value = tone.getOsc1().getAccent();
                break;
            case Cutoff:
                value = tone.getFilter().getCutoff();
                break;
            case Decay:
                value = tone.getFilter().getDecay();
                break;
            case EnvMod:
                value = tone.getFilter().getEnvMod();
                break;
            case PulseWidth:
                value = tone.getOsc1().getPulseWidth();
                break;
            case Resonance:
                value = tone.getFilter().getResonance();
                break;
            case Tune:
                value = tone.getOsc1().getTune();
                break;
            case Volume:
                value = tone.getVolume().getOut();
                break;
        }
        return value;
    }

    public void setSynthProperty(int channel, SynthProperty property, float value) {
        BasslineTone tone = (BasslineTone)PartUtils.getTone(getParts().get(channel));
        switch (property) {
            case Accent:
                tone.getOsc1().setAccent(value);
                break;
            case Cutoff:
                tone.getFilter().setCutoff(value);
                break;
            case Decay:
                tone.getFilter().setDecay(value);
                break;
            case EnvMod:
                tone.getFilter().setEnvMod(value);
                break;
            case PulseWidth:
                tone.getOsc1().setPulseWidth(value);
                break;
            case Resonance:
                tone.getFilter().setResonance(value);
                break;
            case Tune:
                tone.getOsc1().setTune((int)value);
                break;
            case Volume:
                tone.getVolume().setOut(value);
                break;
        }
    }

    public enum SynthProperty {
        PulseWidth,

        Tune,

        Cutoff,

        Resonance,

        EnvMod,

        Decay,

        Accent,

        Volume;
    }

    public enum LFOProperty {
        Target,

        Rate,

        Depth,

        Phase;
    }

    public enum DistorionProperty {
        Program,

        Pre,

        Amount,

        Post;
    }
}
