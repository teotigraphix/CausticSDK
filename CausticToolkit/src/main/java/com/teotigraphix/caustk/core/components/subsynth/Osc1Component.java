
package com.teotigraphix.caustk.core.components.subsynth;

import com.teotigraphix.caustk.core.components.ToneComponent;
import com.teotigraphix.caustk.core.osc.SubSynthOscMessage;

public class Osc1Component extends ToneComponent {

    //--------------------------------------------------------------------------
    //
    // ISubSynthOsc1 API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // bend
    //----------------------------------

    private float bend = 0.0f;

    public float getBend() {
        return bend;
    }

    float getBend(boolean restore) {
        return SubSynthOscMessage.OSC_BEND.query(getEngine(), getToneIndex());
    }

    public void setBend(float value) {
        if (value == bend)
            return;
        if (value < 0 || value > 1f)
            throw newRangeException(SubSynthOscMessage.OSC_BEND.toString(), "0..1", value);
        bend = value;
        SubSynthOscMessage.OSC_BEND.send(getEngine(), getToneIndex(), bend);
    }

    //----------------------------------
    // fm
    //----------------------------------

    private float fm = 0.0f;

    public float getFM() {
        return fm;
    }

    float getFM(boolean restore) {
        return SubSynthOscMessage.OSC1_FM.query(getEngine(), getToneIndex());
    }

    public void setFM(float value) {
        if (value == fm)
            return;
        if (value < 0 || value > 1f)
            throw newRangeException(SubSynthOscMessage.OSC1_FM.toString(), "0..1", value);
        fm = value;
        SubSynthOscMessage.OSC1_FM.send(getEngine(), getToneIndex(), fm);
    }

    //----------------------------------
    // mix
    //----------------------------------

    private float mix = 0.5f;

    public float getMix() {
        return mix;
    }

    float getMix(boolean restore) {
        return SubSynthOscMessage.OSC_MIX.query(getEngine(), getToneIndex());
    }

    public void setMix(float value) {
        if (value == mix)
            return;
        if (value < 0 || value > 1.0f)
            throw newRangeException(SubSynthOscMessage.OSC_MIX.toString(), "0..1", value);
        mix = value;
        SubSynthOscMessage.OSC_MIX.send(getEngine(), getToneIndex(), mix);
    }

    //----------------------------------
    // waveform
    //----------------------------------

    private Waveform waveform = Waveform.SINE;

    public Waveform getWaveform() {
        return waveform;
    }

    Waveform getWaveform(boolean restore) {
        return Waveform.toType(SubSynthOscMessage.OSC1_WAVEFORM.query(getEngine(), getToneIndex()));
    }

    public void setWaveform(Waveform value) {
        // XXX OSC ERROR
        if (value == null)
            return;
        if (value == waveform)
            return;
        waveform = value;
        SubSynthOscMessage.OSC1_WAVEFORM.send(getEngine(), getToneIndex(), waveform.getValue());
    }

    public Osc1Component() {
    }

    @Override
    public void restore() {
        setBend(getBend(true));
        setFM(getFM(true));
        setMix(getMix(true));
        setWaveform(getWaveform(true));
    }

    /**
     * The {@link ISubSynthOsc1} waveforms.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum Waveform {

        /**
         * A sine wave (0).
         */
        SINE(0),

        /**
         * A saw tooth wave (1).
         */
        SAW(1),

        /**
         * A triangle wave (2).
         */
        TRIANGLE(2),

        /**
         * A square wave (3).
         */
        SQUARE(3),

        /**
         * A noise wave (4).
         */
        NOISE(4),

        /**
         * N/A. (5)
         */
        CUSTOM(5);

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
