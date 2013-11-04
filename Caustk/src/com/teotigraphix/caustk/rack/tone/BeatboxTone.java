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

package com.teotigraphix.caustk.rack.tone;

import com.teotigraphix.caustk.rack.tone.components.MixerChannel;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent;
import com.teotigraphix.caustk.rack.tone.components.SynthComponent;
import com.teotigraphix.caustk.rack.tone.components.VolumeComponent;
import com.teotigraphix.caustk.rack.tone.components.beatbox.WavSamplerComponent;

/**
 * The tone impl for the native Beatbox machine.
 * 
 * @author Michael Schmalle
 */
public class BeatboxTone extends RhythmTone {

    public VolumeComponent getVolume() {
        return getComponent(VolumeComponent.class);
    }

    public WavSamplerComponent getSampler() {
        return getComponent(WavSamplerComponent.class);
    }

    BeatboxTone() {
    }

    public BeatboxTone(String machineName, int machineIndex) {
        super(machineName, machineIndex);
    }

    @Override
    protected void createComponents() {
        addComponent(MixerChannel.class, new MixerChannel());
        addComponent(SynthComponent.class, new SynthComponent());
        addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());
        addComponent(VolumeComponent.class, new VolumeComponent());
        addComponent(WavSamplerComponent.class, new WavSamplerComponent());
    }
}
