
package com.teotigraphix.caustk.core.components.bassline;

import com.teotigraphix.caustk.core.components.ToneComponent;
import com.teotigraphix.caustk.core.osc.BasslineOscMessage;

public class OSC1Component extends ToneComponent {

    //--------------------------------------------------------------------------
    // API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // accent
    //----------------------------------

    private float accent = 0.5f;

    public float getAccent() {
        return accent;
    }

    float getAccent(boolean restore) {
        return BasslineOscMessage.ACCENT.query(getEngine(), getToneIndex());

    }

    public void setAccent(float value) {
        if (value == accent)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(BasslineOscMessage.ACCENT.toString(), "0..1", value);
        accent = value;
        BasslineOscMessage.ACCENT.send(getEngine(), getToneIndex(), accent);
    }

    //----------------------------------
    // pulseWidth
    //----------------------------------

    private float pulseWidth = 0.5f;

    public float getPulseWidth() {
        return pulseWidth;
    }

    float getPulseWidth(boolean restore) {
        return BasslineOscMessage.PULSE_WIDTH.query(getEngine(), getToneIndex());
    }

    public void setPulseWidth(float value) {
        if (value == pulseWidth)
            return;
        if (value < 0.05f || value > 0.5f)
            throw newRangeException(BasslineOscMessage.PULSE_WIDTH.toString(), "0.05..0.5", value);
        pulseWidth = value;
        BasslineOscMessage.PULSE_WIDTH.send(getEngine(), getToneIndex(), pulseWidth);
    }

    //----------------------------------
    // tune
    //----------------------------------

    private int tune = 0;

    public int getTune() {
        return tune;
    }

    int getTune(boolean restore) {
        return (int)BasslineOscMessage.TUNE.query(getEngine(), getToneIndex());
    }

    public void setTune(int value) {
        if (value == tune)
            return;
        if (value < -12 || value > 12)
            throw newRangeException(BasslineOscMessage.TUNE.toString(), "-12..12", value);
        tune = value;
        BasslineOscMessage.TUNE.send(getEngine(), getToneIndex(), tune);
    }

    //----------------------------------
    // waveform
    //----------------------------------

    private Waveform waveForm = Waveform.SAW;

    public Waveform getWaveForm() {
        return waveForm;
    }

    Waveform getWaveForm(boolean restore) {
        return Waveform.toType(BasslineOscMessage.WAVEFORM.query(getEngine(), getToneIndex()));
    }

    public void setWaveForm(Waveform value) {
        if (value == waveForm)
            return;
        waveForm = value;
        BasslineOscMessage.WAVEFORM.send(getEngine(), getToneIndex(), waveForm.getValue());
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
