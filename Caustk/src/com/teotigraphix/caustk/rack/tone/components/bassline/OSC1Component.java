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

package com.teotigraphix.caustk.rack.tone.components.bassline;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.BasslineMessage;
import com.teotigraphix.caustk.rack.tone.ToneComponent;

public class OSC1Component extends ToneComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float accent = 0.5f;

    @Tag(101)
    private float pulseWidth = 0.5f;

    @Tag(102)
    private int tune = 0;

    @Tag(103)
    private Waveform waveForm = Waveform.SAW;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // accent
    //----------------------------------

    public float getAccent() {
        return accent;
    }

    float getAccent(boolean restore) {
        return BasslineMessage.ACCENT.query(getEngine(), getToneIndex());

    }

    public void setAccent(float value) {
        if (value == accent)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(BasslineMessage.ACCENT.toString(), "0..1", value);
        accent = value;
        BasslineMessage.ACCENT.send(getEngine(), getToneIndex(), accent);
    }

    //----------------------------------
    // pulseWidth
    //----------------------------------

    public float getPulseWidth() {
        return pulseWidth;
    }

    float getPulseWidth(boolean restore) {
        return BasslineMessage.PULSE_WIDTH.query(getEngine(), getToneIndex());
    }

    public void setPulseWidth(float value) {
        if (value == pulseWidth)
            return;
        if (value < 0.05f || value > 0.5f)
            throw newRangeException(BasslineMessage.PULSE_WIDTH.toString(), "0.05..0.5", value);
        pulseWidth = value;
        BasslineMessage.PULSE_WIDTH.send(getEngine(), getToneIndex(), pulseWidth);
    }

    //----------------------------------
    // tune
    //----------------------------------

    public int getTune() {
        return tune;
    }

    int getTune(boolean restore) {
        return (int)BasslineMessage.TUNE.query(getEngine(), getToneIndex());
    }

    public void setTune(int value) {
        if (value == tune)
            return;
        if (value < -12 || value > 12)
            throw newRangeException(BasslineMessage.TUNE.toString(), "-12..12", value);
        tune = value;
        BasslineMessage.TUNE.send(getEngine(), getToneIndex(), tune);
    }

    //----------------------------------
    // waveform
    //----------------------------------

    public Waveform getWaveForm() {
        return waveForm;
    }

    Waveform getWaveForm(boolean restore) {
        return Waveform.toType(BasslineMessage.WAVEFORM.query(getEngine(), getToneIndex()));
    }

    public void setWaveForm(Waveform value) {
        if (value == waveForm)
            return;
        waveForm = value;
        BasslineMessage.WAVEFORM.send(getEngine(), getToneIndex(), waveForm.getValue());
    }

    public OSC1Component() {
    }

    @Override
    public void restore() {
        setAccent(getAccent(true));
        setPulseWidth(getPulseWidth(true));
        setTune(getTune(true));
        setWaveForm(getWaveForm(true));
    }

    /**
     * The {@link IBasslineOSC1} waveforms.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum Waveform {

        /**
         * A saw wave (0).
         */
        SAW(0),

        /**
         * A square wave (1).
         */
        SQUARE(1);

        private final int mValue;

        /**
         * Returns the integer value of the {@link Waveform}.
         */
        public int getValue() {
            return mValue;
        }

        Waveform(int value) {
            mValue = value;
        }

        /**
         * Returns a {@link Waveform} based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static Waveform toType(Integer type) {
            for (Waveform result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see #toType(Integer)
         */
        public static Waveform toType(Float type) {
            return toType(type.intValue());
        }
    }

}
