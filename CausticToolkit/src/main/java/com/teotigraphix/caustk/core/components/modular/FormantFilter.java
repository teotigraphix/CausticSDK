
package com.teotigraphix.caustk.core.components.modular;

import com.teotigraphix.caustk.controller.ICaustkController;

public class FormantFilter extends ModularComponentBase {

    //----------------------------------
    // formant1
    //----------------------------------

    private int formant1;

    public int getFormant1() {
        return formant1;
    }

    int getFormant1(boolean restore) {
        return (int)getValue("formant_1");
    }

    /**
     * Representing AY(0), AH(1), OW(2), OO(3), ER(4), EE(5), and IH(6).
     * 
     * @param value (0..6)
     */
    public void setFormant1(int value) {
        if (value == formant1)
            return;
        formant1 = value;
        if (value < 0 || value > 1)
            newRangeException("formant_1", "0..6", value);
        setValue("formant_1", value);
    }

    //----------------------------------
    // morph
    //----------------------------------

    private float morph;

    public float getMorph() {
        return morph;
    }

    float getMorph(boolean restore) {
        return getValue("morph");
    }

    /**
     * @param value (0..1)
     */
    public void setMorph(float value) {
        if (value == morph)
            return;
        morph = value;
        if (value < 0f || value > 1f)
            newRangeException("morph", "0..1", value);
        setValue("morph", value);
    }

    //----------------------------------
    // formant2
    //----------------------------------

    private int formant2;

    public int getFormant2() {
        return formant2;
    }

    int getFormant2(boolean restore) {
        return (int)getValue("formant_2");
    }

    /**
     * Representing AY(0), AH(1), OW(2), OO(3), ER(4), EE(5), and IH(6).
     * 
     * @param value (0..6)
     */
    public void setFormant2(int value) {
        if (value == formant2)
            return;
        formant2 = value;
        if (value < 0 || value > 1)
            newRangeException("formant_2", "0..6", value);
        setValue("formant_2", value);
    }

    //----------------------------------
    // gain
    //----------------------------------

    private float gain;

    public float getGain() {
        return gain;
    }

    float getGain(boolean restore) {
        return getValue("gain");
    }

    /**
     * @param value (0..1)
     */
    public void setGain(float value) {
        if (value == gain)
            return;
        gain = value;
        if (value < 0f || value > 1f)
            newRangeException("gain", "0..1", value);
        setValue("gain", value);
    }

    //----------------------------------
    // inGain
    //----------------------------------

    private float inGain;

    public float getInGain() {
        return inGain;
    }

    float getInGain(boolean restore) {
        return getValue("in_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setInGain(float value) {
        if (value == inGain)
            return;
        inGain = value;
        if (value < 0f || value > 1f)
            newRangeException("in_gain", "0..1", value);
        setValue("in_gain", value);
    }

    //----------------------------------
    // morphModulation
    //----------------------------------

    private float morphModulation;

    public float getMorphModulation() {
        return morphModulation;
    }

    float getMorphModulation(boolean restore) {
        return getValue("morph_mod");
    }

    /**
     * @param value (0..1)
     */
    public void setMorphModulation(float value) {
        if (value == morphModulation)
            return;
        morphModulation = value;
        if (value < 0f || value > 1f)
            newRangeException("morph_mod", "0..1", value);
        setValue("morph_mod", value);
    }

    //----------------------------------
    // gainModulation
    //----------------------------------

    private float gainModulation;

    public float getGainModulation() {
        return gainModulation;
    }

    float getGainModulation(boolean restore) {
        return getValue("gain_mod");
    }

    /**
     * @param value (0..1)
     */
    public void setGainModulation(float value) {
        if (value == gainModulation)
            return;
        gainModulation = value;
        if (value < 0f || value > 1f)
            newRangeException("gain_mod", "0..1", value);
        setValue("gain_mod", value);
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

    public FormantFilter() {
    }

    public FormantFilter(ICaustkController controller, int bay) {
        super(controller, bay);
    }

    @Override
    protected int getNumBays() {
        return 2;
    }

    public enum FormantFilterJack implements IModularJack {

        InInput(0),

        InMorph(1),

        InGain(2),

        OutOutput(0);

        private int value;

        public final int getValue() {
            return value;
        }

        FormantFilterJack(int value) {
            this.value = value;
        }
    }
}
