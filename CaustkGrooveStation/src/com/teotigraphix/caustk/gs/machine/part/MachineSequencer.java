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

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.memory.Memory;
import com.teotigraphix.caustk.gs.memory.MemoryBank;
import com.teotigraphix.caustk.gs.memory.TemporaryMemory;
import com.teotigraphix.caustk.gs.memory.item.PhraseMemoryItem;
import com.teotigraphix.caustk.gs.pattern.Part;
import com.teotigraphix.caustk.gs.pattern.PartUtils;
import com.teotigraphix.caustk.gs.pattern.Pattern;
import com.teotigraphix.caustk.gs.pattern.PhraseUtils;
import com.teotigraphix.caustk.sequencer.track.Phrase;
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

    public final Phrase getSelectedPhrase() {
        return getMachine().getSound().getSelectedPart().getPhrase();
    }

    //--------------------------------------------------------------------------
    // Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // mode
    //----------------------------------

    private StepKeyboardMode mode = StepKeyboardMode.Step;

    public StepKeyboardMode getMode() {
        return mode;
    }

    public void setMode(StepKeyboardMode value) {
        if (value == mode)
            return;
        StepKeyboardMode oldMode = mode;
        mode = value;
        for (OnMachineSequencerListener listener : onMachineSequencerListener) {
            listener.onModeChange(mode, oldMode);
        }
    }

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
            int position = getSelectedPhrase().getPosition();
            if (beat % 4 == 1 && position == measure - 1) // last beat, last measure
                throw new RuntimeException("Pattern change locked");
        }

        updateName(pattern);

        nextPattern = pattern;

        // calls #configure(Pattern) from TemporaryMemory > UserMemory
        pendingPattern = getTemporaryMemory().copyPattern(pattern);

        // getDispatcher().trigger(new OnPatternSequencerPatternChangePending());

        // if the sequencer is not playing, advance automatically.
        if (!getRack().getSystemSequencer().isPlaying()) {
            playNextPattern();
        }
    }

    public void playNextPattern() {
        if (pendingPattern == null)
            return;

        getTemporaryMemory().commit();
        setPattern(pendingPattern);
    }

    public Pattern playPattern(int pattern) {
        setNextPattern(pattern);
        playNextPattern();
        return getPattern();
    }

    public void write() throws CausticException {
        // a write operation on the sequencer saves the current pattern 
        // into memory from the temporary memory

        // for now this is User
        MemoryBank memoryBank = getMemoryBank();
        memoryBank.writePattern(getPattern());
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MachineSequencer(GrooveMachine grooveMachine) {
        super(grooveMachine);
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

        getMachine().getSound().configure(pattern);

        // configure length, it may have changed since the original init
        pattern.setLength(pattern.getMemoryItem().getLength());
    }

    public void commit(Pattern pattern) {
        final MachineSound machineSound = getMachine().getSound();

        for (Part part : machineSound.getParts()) {

            PhraseMemoryItem phraseMemoryItem = pattern.getMemoryItem().getPhrase(part.getIndex());

            part.getPatch().commit();

            if (!pattern.isInMemory()) {
                part.getPhrase().getTrack()
                        .setCurrentBankPattern(pattern.getBankIndex(), pattern.getPatternIndex());
                part.getPhrase().setNoteData(phraseMemoryItem.getInitNoteData());
            }
        }

        commitPropertySettings(pattern);
    }

    protected void commitPropertySettings(Pattern pattern) {
        final MachineSound machineSound = getMachine().getSound();

        // set the default tempo/bpm
        pattern.setTempo(pattern.getMemoryItem().getTempo());

        for (Part part : machineSound.getParts()) {
            PartUtils.setBankPattern(part, pattern.getBankIndex(), pattern.getPatternIndex());
        }

        machineSound.setSelectedPart(0);
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

    /**
     * Returns the current beat based on the sequencer mode.
     * <p>
     * In Pattern, will be 0-31, Song will be the current beat in the song
     * sequencer.
     */
    public float getCurrentBeat() {
        return currentBeat;
    }

    public void setCurrentBeat(float currentBeat) {
        this.currentBeat = currentBeat;
    }

    /**
     * Returns 0-3.
     */
    public float getMeasureBeat() {
        return PhraseUtils.toMeasureBeat(currentBeat);
    }

    /**
     * Returns 0-31(8 bars), 0-15(4 bars), 0-7(2 bars), 0-3(1 bar) based on the
     * length.
     */
    public float getLocalBeat() {
        return PhraseUtils.toLocalBeat(currentBeat, getPattern().getLength());
    }

    /**
     * Returns the local measure calculated from the {@link #getLocalBeat()}.
     */
    public int getLocalMeasure() {
        return PhraseUtils.toLocalMeasure(currentBeat, getPattern().getLength());
    }

    public void beatChange(int measure, float beat) {
        // CausticCore > IGame > ISystemSequencer > GrooveStation > GrooveMachine
        setCurrentMeasure(measure);
        setCurrentBeat(beat);

        //float localBeat = PhraseUtils.toLocalBeat(beat, getPattern().getLength());
        //int localMeasure = PhraseUtils.toLocalMeasure(beat, getPattern().getLength());

        //System.out.println("LocalBeat:" + localBeat + " LocalMeasure:" + localMeasure);
        for (OnMachineSequencerListener listener : onMachineSequencerListener) {
            listener.onBeatChange(this);
        }
    }

    private void updateName(int pattern) {
        int bank = PatternUtils.getBank(pattern);
        int index = PatternUtils.getBank(pattern);
        String text = PatternUtils.toString(bank, index);
        System.out.println("Pattern:" + text);
    }

    public void refresh() {
        for (OnMachineSequencerListener listener : onMachineSequencerListener) {
            listener.onRefresh();
        }
    }

    private List<OnMachineSequencerListener> onMachineSequencerListener = new ArrayList<OnMachineSequencerListener>();

    public void addOnMachineSequencerListener(OnMachineSequencerListener l) {
        onMachineSequencerListener.add(l);
    }

    public interface OnMachineSequencerListener {
        void onBeatChange(MachineSequencer machineSequencer);

        void onModeChange(StepKeyboardMode mode, StepKeyboardMode oldMode);

        void onRefresh();
    }

    public enum StepKeyboardMode {

        Step(0),

        Key(1),

        Shift(1);

        private int index;

        public int getIndex() {
            return index;
        }

        StepKeyboardMode(int index) {
            this.index = index;
        }
    }

}
