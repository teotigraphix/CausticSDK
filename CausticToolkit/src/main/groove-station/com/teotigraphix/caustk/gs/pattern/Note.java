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

    public Note(String data) {
        String[] split = data.split(" ");
        this.beat = Float.valueOf(split[0]);
        this.pitch = Float.valueOf(split[1]).intValue();
        this.velocity = Float.valueOf(split[2]);
        this.gate = Float.valueOf(split[3]) - beat;
        this.flags = Float.valueOf(split[4]).intValue();
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

    public String serialze() {
        final StringBuilder sb = new StringBuilder();
        sb.append(beat);
        sb.append(" ");
        sb.append(pitch);
        sb.append(" ");
        sb.append(velocity);
        sb.append(" ");
        sb.append(beat + gate);
        sb.append(" ");
        sb.append(flags);
        return sb.toString();
    }
}
