
package com.teotigraphix.caustk.sequencer.track;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.service.ISerialize;

public class PhraseNote implements ISerialize {

    private transient int pitch;

    public int getPitch() {
        return pitch;
    }

    private transient float start;

    public float getStart() {
        return start;
    }

    private transient float end;

    public float getEnd() {
        return end;
    }

    public float getGate() {
        return end - start;
    }

    private transient float velocity;

    public float getVelocity() {
        return velocity;
    }

    private transient int flags;

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

    private String data;

    public String getNoteData() {
        // [start] [note] [velocity] [end] [flags]
        final StringBuilder sb = new StringBuilder();
        sb.append(start);
        sb.append(" ");
        sb.append(pitch);
        sb.append(" ");
        sb.append(velocity);
        sb.append(" ");
        sb.append(end);
        sb.append(" ");
        sb.append(flags);
        return sb.toString();
    }

    @Override
    public void sleep() {
        data = getNoteData();
    }

    @Override
    public void wakeup(ICaustkController controller) {
        if (data != null) {
            String[] split = data.split("\\|");
            for (String notes : split) {
                String[] chunks = notes.split(" ");
                start = Float.valueOf(chunks[0]);
                pitch = Integer.valueOf(chunks[1]);
                velocity = Float.valueOf(chunks[2]);
                end = Float.valueOf(chunks[3]);
                flags = Integer.valueOf(chunks[4]);
            }
            data = null;
        }
    }
}
