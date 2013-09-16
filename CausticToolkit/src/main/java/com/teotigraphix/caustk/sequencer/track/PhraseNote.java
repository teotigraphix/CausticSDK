////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.sequencer.track;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.tone.components.PatternSequencerComponent.Resolution;

public class PhraseNote implements ISerialize {

    private String data;

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

    public final int getStep(Resolution resolution) {
        return Resolution.toStep(start, resolution);
    }

    public PhraseNote(String data) {
        String[] split = data.split(" ");
        this.start = Float.valueOf(split[0]);
        this.pitch = Float.valueOf(split[1]).intValue();
        this.velocity = Float.valueOf(split[2]);
        this.end = Float.valueOf(split[3]);
        this.flags = Float.valueOf(split[4]).intValue();
    }

    public PhraseNote(int pitch, float start, float end, float velocity, int flags) {
        this.pitch = pitch;
        this.start = start;
        this.end = end;
        this.velocity = velocity;
        this.flags = flags;
    }

    public void update(int pitch, float start, float end, float velocity, int flags) {
        this.pitch = pitch;
        this.start = start;
        this.end = end;
        this.velocity = velocity;
        this.flags = flags;
    }

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

    public String serialze() {
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
    public String toString() {
        return "[" + start + "]Pitch:" + pitch + " Gate:" + getGate();
    }

}
