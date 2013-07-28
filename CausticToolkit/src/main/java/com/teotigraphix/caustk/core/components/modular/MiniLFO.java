
package com.teotigraphix.caustk.core.components.modular;

import com.teotigraphix.caustk.controller.ICaustkController;

public class MiniLFO extends ModularComponentBase {

    //----------------------------------
    // waveForm
    //----------------------------------

    private WaveForm waveForm;

    public WaveForm getWaveForm() {
        return waveForm;
    }

    public void setWaveForm(WaveForm value) {
        this.waveForm = value;
        setValue("waveform", value.getValue());
    }

    //----------------------------------
    // rate
    //----------------------------------

    private int rate;

    public int getRate() {
        return rate;
    }

    /**
     * @param value (1..12)
     */
    public void setRate(int value) {
        rate = value;
        // 1..12
        setValue("rate", value);
    }

    //----------------------------------
    // noteSync
    //----------------------------------

    private int noteSync;

    public int getNoteSync() {
        return noteSync;
    }

    public void setNoteSync(int value) {
        noteSync = value;
        // 0,1
        setValue("note_sync", value);
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

    public MiniLFO() {
    }

    public MiniLFO(ICaustkController controller, int bay) {
        super(controller, bay);
    }

    @Override
    protected int getNumBays() {
        return 1;
    }

    public enum WaveForm {

        Sine(0),

        Triangle(0),

        Saw(0),

        InvSaw(0),

        Square(0),

        Random(0);

        private int value;

        public final int getValue() {
            return value;
        }

        WaveForm(int value) {
            this.value = value;
        }
    }

    public enum MiniLFOJack implements IModularJack {

        OutLeft(0),

        OutRight(1);

        private int value;

        public final int getValue() {
            return value;
        }

        MiniLFOJack(int value) {
            this.value = value;
        }
    }
}
