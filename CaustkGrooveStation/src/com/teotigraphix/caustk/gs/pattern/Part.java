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

import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.machine.part.sound.Patch;
import com.teotigraphix.caustk.sequencer.track.Phrase;
import com.teotigraphix.caustk.tone.Tone;

public class Part {

    private GrooveMachine machine;

    public final GrooveMachine getMachine() {
        return machine;
    }

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // index
    //----------------------------------

    /**
     * The {@link Part}'s index within it's parent {@link Pattern} and also
     * represents the {@link Tone}s index within the native rack.
     */
    public int getIndex() {
        return tone.getIndex();
    }

    //----------------------------------
    // tone
    //----------------------------------

    private Tone tone;

    /**
     * The decorated {@link Tone}.
     */
    protected Tone getTone() {
        return tone;
    }

    //----------------------------------
    // pattern
    //----------------------------------

    private Pattern pattern;

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern value) {
        pattern = value;
    }

    //----------------------------------
    // patch
    //----------------------------------

    private Patch patch;

    public Patch getPatch() {
        return patch;
    }

    public void setPatch(Patch value) {
        patch = value;
    }

    //----------------------------------
    // phrase
    //----------------------------------

    private Phrase phrase;

    public Phrase getPhrase() {
        return phrase;
    }

    public void setPhrase(Phrase value) {
        phrase = value;
    }

    //----------------------------------
    // isRhythm
    //----------------------------------

    public boolean isRhythm() {
        return this instanceof RhythmPart;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Part(GrooveMachine machine, Tone tone) {
        this.machine = machine;
        this.tone = tone;
    }

    /**
     * Transposes the {@link Phrase} by the delta positive or negative.
     * <p>
     * The {@link Phrase} will track the original, the value of 0 will set the
     * original pitch on the phrase.
     * 
     * @param delta
     */
    public void transpose(int delta) {
        //        getPhrase().transpose(delta);
    }

    public boolean istMute() {
        return getMachine().getRack().getSoundMixer().getChannel(getIndex()).isMute();
    }

    public void setMute(boolean muted) {
        getMachine().getRack().getSoundMixer().getChannel(getIndex()).setMute(muted);
    }
}
