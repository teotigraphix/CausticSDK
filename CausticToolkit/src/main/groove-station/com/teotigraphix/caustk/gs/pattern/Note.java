
package com.teotigraphix.caustk.gs.pattern;

import com.teotigraphix.caustk.tone.components.PatternSequencerComponent.Resolution;

public class Note {

    private float beat;

    private int pitch;

    private float gate;

    private float velocity;

    private int flags = 0;

    public final float getBeat() {
        return beat;
    }

    public final int getPitch() {
        return pitch;
    }

    public final float getGate() {
        return gate;
    }

    public final float getVelocity() {
        return velocity;
    }

    public final int getFlags() {
        return flags;
    }

    public Note(float beat, int pitch, float gate, float velocity, int flags) {
        this.beat = beat;
        this.pitch = pitch;
        this.gate = gate;
        this.velocity = velocity;
        this.flags = flags;
    }

    public void update(float beat, int pitch, float gate, float velocity, int flags) {
        this.beat = beat;
        this.pitch = pitch;
        this.gate = gate;
        this.velocity = velocity;
        this.flags = flags;
    }

    /**
     * Returns the step value relative to the containing
     * {@link Phrase#getResolution()}.
     */
    public int getStep(Resolution resolution) {
        return Resolution.toStep(beat, resolution);
    }

    @Override
    public String toString() {
        return "[" + beat + "] " + beat + ":" + pitch;
    }
}
