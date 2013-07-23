
package com.teotigraphix.caustk.core.components.pcmsynth;

import com.teotigraphix.caustk.core.components.ToneComponent;
import com.teotigraphix.caustk.core.osc.PitchMessage;

public class PCMTunerComponent extends ToneComponent {

    //--------------------------------------------------------------------------
    //
    // IPitchTuner API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // cents
    //----------------------------------

    private int cents = 0;

    public int getCents() {
        return cents;
    }

    int getCents(boolean restore) {
        return (int)PitchMessage.PITCH_CENTS.query(getEngine(), getToneIndex());
    }

    public void setCents(int value) {
        if (value == cents)
            return;
        if (value < -50 || value > 50)
            throw newRangeException("pitch_cents", "-50..50", value);
        cents = value;
        PitchMessage.PITCH_CENTS.send(getEngine(), getToneIndex(), cents);
    }

    //----------------------------------
    // octave
    //----------------------------------

    private int octave = 0;

    public int getOctave() {
        return octave;
    }

    int getOctave(boolean restore) {
        return (int)PitchMessage.PITCH_OCTAVE.query(getEngine(), getToneIndex());
    }

    public void setOctave(int value) {
        if (value == octave)
            return;
        if (value < -4 || value > 4)
            throw newRangeException("pitch_octave", "-4..4", value);
        octave = value;
        PitchMessage.PITCH_OCTAVE.send(getEngine(), getToneIndex(), octave);
    }

    //----------------------------------
    // semis
    //----------------------------------

    private int semis = 0;

    public int getSemis() {
        return semis;
    }

    int getSemis(boolean restore) {
        return (int)PitchMessage.PITCH_SEMIS.query(getEngine(), getToneIndex());
    }

    public void setSemis(int value) {
        if (value == semis)
            return;
        if (value < -12 || value > 12)
            throw newRangeException("pitch_semis", "-12..12", value);
        semis = value;
        PitchMessage.PITCH_SEMIS.send(getEngine(), getToneIndex(), semis);
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
