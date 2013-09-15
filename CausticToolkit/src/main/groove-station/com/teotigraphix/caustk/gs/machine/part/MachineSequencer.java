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

package com.teotigraphix.caustk.gs.machine.part;

import com.sun.jna.Memory;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.machine.part.sequencer.StepSequencer;
import com.teotigraphix.caustk.gs.memory.TemporaryMemory;
import com.teotigraphix.caustk.gs.pattern.Part;
import com.teotigraphix.caustk.gs.pattern.Pattern;
import com.teotigraphix.caustk.utils.PatternUtils;

/*
 * - KeyboardToggleButton
 * - ShiftToggleButton
 * - SelectControl
 * - StepKeys
 */

/**
 * Holds all controls that control the machine's step sequencer.
 */
public class MachineSequencer extends MachineComponentPart {

    @SuppressWarnings("unused")
    private StepSequencer stepSequencer;

    //----------------------------------
    // pattern
    //----------------------------------

    // We should not have this ref, we should proxy straight back to
    // the temp memory that actually owns the instance. This way there
    // is no mistake when saving happens or changing, there is nothing to clean
    // up, only in the temp memory area
    private Pattern pattern;

    private Pattern pendingPattern;

    public Pattern getPattern() {
        return pattern;
    }

    private void setPattern(Pattern value) {
        pendingPattern = null;

        @SuppressWarnings("unused")
        Pattern oldPattern = pattern;

        pattern = value;

        // getDispatcher().trigger(new OnPatternSequencerPatternChange(pattern, oldPattern));
    }

    public Pattern getPendingPattern() {
        return pendingPattern;
    }

    private int nextPattern = -1;

    public void setNextPattern(int pattern) {
        if (pattern == nextPattern)
            return;

        // if the sequencer is locked, return
        // the sequencer is locked when we are in the last beat of the pattern
        if (getPattern() != null) {
            float beat = getCurrentBeat() + 1;
            int measure = getCurrentMeasure() + 1;
            int position = getPattern().getSelectedPart().getPhrase().getPosition();
            if (beat % 4 == 1 && position == measure - 1) // last beat, last measure
                throw new RuntimeException("Pattern change locked");
        }

        updateName(pattern);

        nextPattern = pattern;

        // calls #configure(Pattern) from TemporaryMemory > UserMemory
        pendingPattern = getMemoryManager().getTemporaryMemory().copyPattern(pattern);

        // getDispatcher().trigger(new OnPatternSequencerPatternChangePending());

        // if the sequencer is not playing, advance automatically.
        if (!getController().getSystemSequencer().isPlaying()) {
            playNextPattern();
        }
    }

    public void playNextPattern() {
        if (pendingPattern == null)
            return;

        getMemoryManager().getTemporaryMemory().commit();
        setPattern(pendingPattern);
    }

    public Pattern playPattern(int pattern) {
        setNextPattern(pattern);
        playNextPattern();
        return getPattern();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MachineSequencer(GrooveMachine grooveMachine) {
        super(grooveMachine);

        stepSequencer = new StepSequencer();
    }

    //-----------------------------------------------------------------------------
    // CONFIGURATION

    /**
     * @see #setNextPattern(int)
     * @see TemporaryMemory#copyPattern(int)
     * @see Memory#copyPattern(int)
     */
    public void configure(Pattern pattern) {
        // copies serialized data from disk into the pattern model.
        // nothing happens anywhere except inside the  Pattern right now,
        // this the 'configure', when 'commit' is called, all settings
        // of the Pattern model that apply to global devices get instantly applied

        // configure length, it may have changed since the original init
        pattern.setLength(pattern.getMemoryItem().getLength());

        try {
            configureParts(pattern);
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    public void commit(Pattern pattern) {
        for (Part part : pattern.getParts()) {
            part.getPatch().commit();
            // part.getPhrase().commit();
        }

        commitPropertySettings(pattern);
    }

    protected void configureParts(Pattern pattern) throws CausticException {
        // add parts the the pattern
        for (Part part : getGrooveMachine().getParts()) {
            pattern.addPart(part);
        }

        for (Part part : getGrooveMachine().getParts()) {
            getMemoryManager().getSelectedMemoryBank().copyPatch(part, 0);
            //getMemoryManager().getSelectedMemoryBank().copyPhrase(part, 0);
            //part.getPhrase().configure();
        }
    }

    protected void commitPropertySettings(Pattern pattern) {
        // set the default tempo/bpm
        pattern.setTempo(pattern.getMemoryItem().getTempo());

        for (Part part : pattern.getParts()) {
            setBankPattern(part, pattern.getBankIndex(), pattern.getPatternIndex());
        }

        pattern.setSelectedPart(pattern.getPart(0));
    }

    private void setBankPattern(Part part, int bank, int pattern) {
        part.getTone().getPatternSequencer().setSelectedBankPattern(bank, pattern);
    }

    //--------------------------------------------------------------------------

    private int currentMeasure;

    public int getCurrentMeasure() {
        return currentMeasure;
    }

    public void setCurrentMeasure(int currentMeasure) {
        this.currentMeasure = currentMeasure;
    }

    private float currentBeat;

    public float getCurrentBeat() {
        return currentBeat;
    }

    public void setCurrentBeat(float currentBeat) {
        this.currentBeat = currentBeat;
    }

    public void beatChange(int measure, float beat) {
        // CausticCore > IGame > ISystemSequencer > GrooveStation > GrooveMachine
        setCurrentMeasure(measure);
        setCurrentBeat(beat);
    }

    private void updateName(int pattern) {
        int bank = PatternUtils.getBank(pattern);
        int index = PatternUtils.getBank(pattern);
        String text = PatternUtils.toString(bank, index);
        System.out.println("Pattern:" + text);
    }
}
