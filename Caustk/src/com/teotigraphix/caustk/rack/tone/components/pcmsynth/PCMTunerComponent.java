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

package com.teotigraphix.caustk.rack.tone.components.pcmsynth;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.PCMSynthMessage;
import com.teotigraphix.caustk.rack.tone.ToneComponent;

public class PCMTunerComponent extends ToneComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private int cents = 0;

    @Tag(101)
    private int octave = 0;

    @Tag(102)
    private int semis = 0;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // cents
    //----------------------------------

    public int getCents() {
        return cents;
    }

    int getCents(boolean restore) {
        return (int)PCMSynthMessage.PITCH_CENTS.query(getEngine(), getToneIndex());
    }

    public void setCents(int value) {
        if (value == cents)
            return;
        if (value < -50 || value > 50)
            throw newRangeException("pitch_cents", "-50..50", value);
        cents = value;
        PCMSynthMessage.PITCH_CENTS.send(getEngine(), getToneIndex(), cents);
    }

    //----------------------------------
    // octave
    //----------------------------------

    public int getOctave() {
        return octave;
    }

    int getOctave(boolean restore) {
        return (int)PCMSynthMessage.PITCH_OCTAVE.query(getEngine(), getToneIndex());
    }

    public void setOctave(int value) {
        if (value == octave)
            return;
        if (value < -4 || value > 4)
            throw newRangeException("pitch_octave", "-4..4", value);
        octave = value;
        PCMSynthMessage.PITCH_OCTAVE.send(getEngine(), getToneIndex(), octave);
    }

    //----------------------------------
    // semis
    //----------------------------------

    public int getSemis() {
        return semis;
    }

    int getSemis(boolean restore) {
        return (int)PCMSynthMessage.PITCH_SEMIS.query(getEngine(), getToneIndex());
    }

    public void setSemis(int value) {
        if (value == semis)
            return;
        if (value < -12 || value > 12)
            throw newRangeException("pitch_semis", "-12..12", value);
        semis = value;
        PCMSynthMessage.PITCH_SEMIS.send(getEngine(), getToneIndex(), semis);
    }

    public PCMTunerComponent() {
    }

    @Override
    public void restore() {
        setCents(getCents(true));
        setOctave(getOctave(true));
        setSemis(getSemis(true));
    }

}
