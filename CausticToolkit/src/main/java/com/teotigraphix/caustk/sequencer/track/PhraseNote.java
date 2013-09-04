
package com.teotigraphix.caustk.sequencer.track;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.service.ISerialize;

public class PhraseNote implements ISerialize {

    private int pitch;

    public int getPitch() {
        return pitch;
    }

    private float start;

    public float getStart() {
        return start;
    }

    private float end;

    public float getEnd() {
        return end;
    }

    public float getGate() {
        return end - start;
    }

    private float velocity;

    public float getVelocity() {
        return velocity;
    }

    private int flags;

    public int getFlags() {
        return flags;
    }

    public PhraseNote(int pitch, float start, float end, float velocity, int flags) {
        this.pitch = pitch;
        this.start = start;
        this.end = end;
        this.velocity = velocity;
        this.flags = flags;
    }

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
    }

}
