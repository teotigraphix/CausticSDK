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

import java.io.Serializable;

import com.teotigraphix.caustk.tone.components.PatternSequencerComponent.Resolution;

/**
 * A single note held within a {@link TriggerMap}.
 */
public class Note implements Serializable {

    private static final long serialVersionUID = -3142765703303002851L;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // pitch
    //----------------------------------

    private int pitch;

    public int getPitch() {
        return pitch;
    }

    //----------------------------------
    // start
    //----------------------------------

    private float start;

    public float getStart() {
        return start;
    }

    //----------------------------------
    // end
    //----------------------------------

    private float end;

    public float getEnd() {
        return end;
    }

    //----------------------------------
    // gate
    //----------------------------------

    public float getGate() {
        return end - start;
    }

    //----------------------------------
    // velocity
    //----------------------------------

    private float velocity;

    public float getVelocity() {
        return velocity;
    }

    //----------------------------------
    // flags
    //----------------------------------

    private int flags;

    public int getFlags() {
        return flags;
    }

    //----------------------------------
    // step
    //----------------------------------

    public final int getStep(Resolution resolution) {
        return Resolution.toStep(start, resolution);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public Note(String data) {
        String[] split = data.split(" ");
        this.start = Float.valueOf(split[0]);
        this.pitch = Float.valueOf(split[1]).intValue();
        this.velocity = Float.valueOf(split[2]);
        this.end = Float.valueOf(split[3]);
        this.flags = Float.valueOf(split[4]).intValue();
    }

    public Note(int pitch, float start, float end, float velocity, int flags) {
        update(pitch, start, end, velocity, flags);
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

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
