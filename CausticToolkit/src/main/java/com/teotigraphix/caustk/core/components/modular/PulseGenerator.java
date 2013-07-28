
package com.teotigraphix.caustk.core.components.modular;

import com.teotigraphix.caustk.controller.ICaustkController;

public class PulseGenerator extends ModularComponentBase {

    //----------------------------------
    // octave
    //----------------------------------

    private int octave;

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

    private int semis;

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
    // cents
    //----------------------------------

    private int cents;

    public int getCents() {
        return cents;
    }

    int getCents(boolean restore) {
        return (int)getValue("cents");
    }

    /**
     * @param value (-50..50)
     */
    public void setCents(int value) {
        if (value == cents)
            return;
        cents = value;
        if (value < -50 || value > 50)
            newRangeException("cents", "-50..50", value);
        setValue("cents", value);
    }

    //----------------------------------
    // pulseWidth
    //----------------------------------

    private float pulseWidth;

    public float getPulseWidth() {
        return pulseWidth;
    }

    float getPulseWidth(boolean restore) {
        return getValue("pulse_width");
    }

    /**
     * @param value (0..0.5)
     */
    public void setPulseWidth(float value) {
        if (value == pulseWidth)
            return;
        pulseWidth = value;
        if (value < 0f || value > 0.5f)
            newRangeException("pulse_width", "0..0.5", value);
        setValue("pulse_width", value);
    }

    //----------------------------------
    // width
    //----------------------------------

    private float width;

    public float getWidth() {
        return width;
    }

    float getWidth(boolean restore) {
        return getValue("width_mod");
    }

    /**
     * @param value (0..1)
     */
    public void setWidth(float value) {
        if (value == width)
            return;
        width = value;
        if (value < 0f || value > 1f)
            newRangeException("width_mod", "0..1", value);
        setValue("width_mod", value);
    }

    //----------------------------------
    // outGain
    //----------------------------------

    private float outGain;

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

    public PulseGenerator() {
    }

    public PulseGenerator(ICaustkController controller, int bay) {
        super(controller, bay);
    }

    @Override
    protected int getNumBays() {
        return 2;
    }

    @Override
    public void restore() {
        super.restore();
        setCents(getCents(true));
        setOctave(getOctave(true));
        setOutGain(getOutGain(true));
        setPulseWidth(getPulseWidth(true));
        setSemis(getSemis(true));
        setWidth(getWidth(true));
    }

    public enum PulseGeneratorJack implements IModularJack {

        InNote(0),

        InModulation(1),

        InWidth(2),

        Out(0);

        private int value;

        public final int getValue() {
            return value;
        }

        PulseGeneratorJack(int value) {
            this.value = value;
        }
    }
}
