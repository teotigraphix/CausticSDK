
package com.teotigraphix.caustk.core.components;

import com.teotigraphix.caustk.core.osc.SynthMessage;

public class SynthComponent extends ToneComponent {

    private transient String absolutePresetPath;

    public String getPresetPath() {
        return absolutePresetPath;
    }

    //--------------------------------------------------------------------------
    // API :: Properties
    //--------------------------------------------------------------------------

    public String getPresetName() {
        return SynthMessage.QUERY_PRESET.queryString(getEngine(), getTone().getIndex());
    }

    //----------------------------------
    // polyphony
    //----------------------------------

    private int polyphony = 4;

    public int getPolyphony() {
        return polyphony;
    }

    int getPolyphony(boolean restore) {
        return (int)SynthMessage.POLYPHONY.query(getEngine(), getToneIndex());
    }

    public void setPolyphony(int value) {
        if (value == polyphony)
            return;
        if (value < 1 || value > 16)
            throw newRangeException(SynthMessage.POLYPHONY.toString(), "1..16", value);
        polyphony = value;
        SynthMessage.POLYPHONY.send(getEngine(), getToneIndex(), polyphony);
    }

    public SynthComponent() {
    }

    //--------------------------------------------------------------------------
    //
    // API :: Methods
    //
    //--------------------------------------------------------------------------

    public void loadPreset(String path) {
        absolutePresetPath = path;
        SynthMessage.LOAD_PRESET.send(getEngine(), getToneIndex(), path);
    }

    public void savePreset(String name) {
        // calculate the preset path
        SynthMessage.SAVE_PRESET.send(getEngine(), getToneIndex(), name);
    }

    public void noteOn(int pitch) {
        noteOn(pitch, 1f);
    }

    public void noteOn(int pitch, float velocity) {
        SynthMessage.NOTE.send(getEngine(), getToneIndex(), pitch, 1, velocity);
    }

    public void noteOff(int pitch) {
        SynthMessage.NOTE.send(getEngine(), getToneIndex(), pitch, 0);
    }

    public void notePreview(int pitch, boolean oneshot) {
        SynthMessage.NOTE_PREVIEW.send(getEngine(), getToneIndex(), pitch, oneshot);
    }

    @Override
    public void restore() {
        setPolyphony(getPolyphony(true));
    }

}
