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

package com.teotigraphix.caustk.rack.tone.components.modular;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public class SubOscillator extends ModularComponentBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private int octave;

    @Tag(101)
    private int semis;

    @Tag(102)
    private float outGain;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // octave
    //----------------------------------

    public int getOctave() {
        return octave;
    }

    int getOctave(boolean restore) {
        return (int)getValue("octave");
    }

    /**
     * @param value (-4..4)
     */
    public void setOctave(int value) {
        if (value == octave)
            return;
        octave = value;
        if (value < -4 || value > 4)
            newRangeException("octave", "-4..4", value);
        setValue("octave", value);
    }

    //----------------------------------
    // semis
    //----------------------------------

    public int getSemis() {
        return semis;
    }

    int getSemis(boolean restore) {
        return (int)getValue("semis");
    }

    /**
     * @param value (-6..6)
     */
    public void setSemis(int value) {
        if (value == semis)
            return;
        semis = value;
        if (value < -6 || value > 6)
            newRangeException("semis", "-6..6", value);
        setValue("semis", value);
    }

    //----------------------------------
    // outGain
    //----------------------------------

    public float getOutGain() {
        return outGain;
    }

    float getOutGain(boolean restore) {
        return getValue("out_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setOutGain(float value) {
        if (value == outGain)
            return;
        outGain = value;
        if (value < 0f || value > 1f)
            newRangeException("out_gain", "0..1", value);
        setValue("out_gain", value);
    }

    public SubOscillator() {
        super();
    }

    public SubOscillator(int bay) {
        super(bay);
    }

    @Override
    protected int getNumBays() {
        return 1;
    }

    @Override
    public void restore() {
        super.restore();
        setOctave(getOctave(true));
        setOutGain(getOutGain(true));
        setSemis(getSemis(true));
    }

    public enum SubOscillatorJack implements IModularJack {

        InNote(0),

        InModulation(1),

        Out(0);

        private int value;

        @Override
        public final int getValue() {
            return value;
        }

        SubOscillatorJack(int value) {
            this.value = value;
        }
    }
}
