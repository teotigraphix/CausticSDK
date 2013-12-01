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

package com.teotigraphix.caustk.utils;

import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent.Resolution;
import com.teotigraphix.caustk.workstation.Note;
import com.teotigraphix.caustk.workstation.Phrase;
import com.teotigraphix.caustk.workstation.Trigger;

public class PhraseUtils {

    private static final float STEP_FRACTION = 0.0625f;

    private static final float MIN_STEP = STEP_FRACTION;

    private static final float MAX_STEP = 4.0f;

    private static final float NUM_BEATS = 4.0f;

    public static int toLocalMeasure(float beat, int length) {
        float localBeat = toLocalBeat(beat, length);
        return (int)Math.floor(localBeat / NUM_BEATS);
    }

    /**
     * Returns the beat within a measure (0-3).
     * 
     * @param beat The full beat (0-31).
     */
    public static float toMeasureBeat(float beat) {
        float r = (beat % 4);
        return r;
    }

    /**
     * Returns a beat within the length.
     * 
     * @param beat The full beat (0-31).
     * @param length The length of the pattern.
     */
    public static float toLocalBeat(float beat, int length) {
        float r = (beat % (length * 4));
        return r;
    }

    public static String toStepDecimalString(float stepFraction) {
        return Float.toString(NUM_BEATS * stepFraction);
    }

    public static float incrementGate(Phrase phrase, Note note) {
        //        float beat = Resolution.toBeat(trigger.getIndex(), phrase
        //                .getResolution());
        float gate = note.getGate();
        gate += STEP_FRACTION;
        gate = Math.max(Math.min(gate, MAX_STEP), MIN_STEP);
        phrase.triggerUpdateGate(note.getStep(phrase.getResolution()), gate);
        return note.getGate();
    }

    public static float decrementGate(Phrase phrase, Note trigger) {
        float gate = trigger.getGate();
        gate -= 0.0625f;
        gate = Math.max(Math.min(gate, MAX_STEP), MIN_STEP);
        phrase.triggerUpdateGate(trigger.getStep(phrase.getResolution()), gate);
        return trigger.getGate();
    }

    public static Note createNote(String data) {
        return null;
    }

    public static float getGlobalBeatFromLocalStep(int index, Phrase phrase) {
        int globalBeat = ((phrase.getPosition() - 1) * 16) + index;
        float beat = Resolution.toBeat(globalBeat, phrase.getResolution());
        return beat;
    }

    public static Trigger createInitTrigger(Phrase phrase, int step) {
        float beat = Resolution.toBeat(step, phrase.getResolution());
        Trigger trigger = new Trigger(beat);
        trigger.addNote(beat, 60, 0.25f, 1f, 0);
        return trigger;
    }

    public static void update(Phrase phrase, Note note) {
        PatternSequencerMessage.NOTE_DATA.send(phrase.getMachine().getRackTone().getEngine(),
                phrase.getMachine().getMachineIndex(), note.getStart(), note.getPitch(),
                note.getVelocity(), note.getEnd(), note.getFlags());
    }

}
