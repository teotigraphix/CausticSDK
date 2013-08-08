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

package com.teotigraphix.caustk.sound;

import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.BeatboxTone;
import com.teotigraphix.caustk.tone.EightBitSynth;
import com.teotigraphix.caustk.tone.FMSynthTone;
import com.teotigraphix.caustk.tone.ModularTone;
import com.teotigraphix.caustk.tone.OrganTone;
import com.teotigraphix.caustk.tone.PCMSynthTone;
import com.teotigraphix.caustk.tone.PadSynthTone;
import com.teotigraphix.caustk.tone.SubSynthTone;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.VocoderTone;

public class SoundSourceUtils {

    public static void setup(Tone tone) {
        switch (tone.getToneType()) {
            case Bassline:
                BasslineTone.setup(tone);
                break;
            case Beatbox:
                BeatboxTone.setup(tone);
                break;
            case EightBitSynth:
                EightBitSynth.setup(tone);
                break;
            case FMSynth:
                FMSynthTone.setup(tone);
                break;
            case Modular:
                ModularTone.setup(tone);
                break;
            case Organ:
                OrganTone.setup(tone);
                break;
            case PadSynth:
                PadSynthTone.setup(tone);
                break;
            case PCMSynth:
                PCMSynthTone.setup(tone);
                break;
            case SubSynth:
                SubSynthTone.setup(tone);
                break;
            case Vocoder:
                VocoderTone.setup(tone);
                break;
        }
    }
}
