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

import java.util.Collection;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.core.IDispatcher;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.ToneDescriptor;

/**
 * @author Michael Schmalle
 */
public interface ICaustkSoundSource {

    IDispatcher getDispatcher();

    SoundMode getSoundMode();

    void setSoundMode(SoundMode value);

    int getOctave();

    void setOctave(int value);

    int getToneCount();

    Collection<Tone> getTones();

    Tone getTone(int index);

    void noteOn(Tone tone, int pitch, float velocity);

    void noteOff(Tone tone, int pitch);

    Tone create(ToneDescriptor descriptor) throws CausticException;

    public enum SoundMode {
        KEYBOARD, STEP;
    }
}
