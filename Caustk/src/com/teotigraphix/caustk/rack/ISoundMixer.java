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

package com.teotigraphix.caustk.rack;

import com.teotigraphix.caustk.rack.mixer.MasterMixer;
import com.teotigraphix.caustk.rack.mixer.SoundMixer.MixerInput;
import com.teotigraphix.caustk.rack.mixer.SoundMixerChannel;
import com.teotigraphix.caustk.rack.tone.Tone;

public interface ISoundMixer extends IRackComponent {

    MasterMixer getMasterMixer();

    boolean hasChannel(int index);

    SoundMixerChannel getChannel(Tone tone);

    SoundMixerChannel getChannel(int index);

    /**
     * @see #executeSetValue(int, MixerInput, Number)
     */
    public static final String COMMAND_SET_VALUE = "sound_mixer/set_value";

    /**
     * Executes the {@link #COMMAND_SET_VALUE} command.
     * 
     * @param toneIndex The tone target's index.
     * @param input The {@link MixerInput} value that will be set.
     * @param value The value of the input's adjustment.
     */
    void executeSetValue(int toneIndex, MixerInput input, Number value);

    public static class OnSoundMixerChannelValueChange {

        private MixerInput mixerInput;

        public MixerInput getMixerInput() {
            return mixerInput;
        }

        private Number value;

        public Number getValue() {
            return value;
        }

        public OnSoundMixerChannelValueChange(MixerInput mixerInput, Number value) {
            this.mixerInput = mixerInput;
            this.value = value;
        }
    }
}
