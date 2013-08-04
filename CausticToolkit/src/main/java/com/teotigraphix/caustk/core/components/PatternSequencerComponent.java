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

package com.teotigraphix.caustk.core.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;

public class PatternSequencerComponent extends ToneComponent {

    //--------------------------------------------------------------------------
    //
    // IPatternSequencer API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // length
    //----------------------------------

    public int getLength() {
        int measures = (int)PatternSequencerMessage.NUM_MEASURES.query(getEngine(), getToneIndex());
        return measures;
    }

    public void setLength(int value) {
        PatternSequencerMessage.NUM_MEASURES.send(getEngine(), getToneIndex(), value);
    }

    public int getLength(int bankIndex, int patternIndex) {
        int lastBank = getSelectedBank();
        int lastIndex = getSelectedIndex();
        setSelectedPattern(bankIndex, patternIndex);
        int measures = (int)PatternSequencerMessage.NUM_MEASURES.query(getEngine(), getToneIndex());
        setSelectedPattern(lastBank, lastIndex);
        return measures;
    }

    public void setLength(int bankIndex, int patternIndex, int value) {
        int lastBank = getSelectedBank();
        int lastIndex = getSelectedIndex();
        setSelectedPattern(bankIndex, patternIndex);
        PatternSequencerMessage.NUM_MEASURES.send(getEngine(), getToneIndex(), value);
        setSelectedPattern(lastBank, lastIndex);
    }

    //----------------------------------
    // selectedBank
    //----------------------------------

    public void setSelectedPattern(int bank, int pattern) {
        setSelectedBank(bank);
        setSelectedIndex(pattern);
    }

    //----------------------------------
    // selectedBank
    //----------------------------------

    private int selectedBank = 0;

    public int getSelectedBank() {
        return selectedBank;
    }

    public int getSelectedBank(boolean restore) {
        return (int)PatternSequencerMessage.BANK.query(getEngine(), getToneIndex());
    }

    void setSelectedBank(int value) {
        if (value < 0 || value > 15)
            throw newRangeException("bank", "0..15", value);
        selectedBank = value;
        sendBankOSC(selectedBank);
    }

    //----------------------------------
    // selectedIndex
    //----------------------------------

    private int selectedIndex = 0;

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public int getSelectedIndex(boolean restore) {
        return (int)PatternSequencerMessage.PATTERN.query(getEngine(), getToneIndex());
    }

    void setSelectedIndex(int value) {
        if (value < 0 || value > 15)
            throw newRangeException("pattern", "0..15", value);
        selectedIndex = value;
        sendcPatternOSC(selectedIndex);
    }

    //----------------------------------
    // patternListing
    //----------------------------------

    public List<String> getPatternListing() {
        String patterns = PatternSequencerMessage.QUERY_PATTERNS_WITH_DATA.queryString(getEngine(),
                getToneIndex());
        if (patterns == null || "".equals(patterns))
            return new ArrayList<String>();
        return Arrays.asList(patterns.split(" "));
    }

    public void initializeData(String data) {
        if (data.equals(""))
            return;

        // push the notes into the machines sequencer
        String[] notes = data.split("\\|");
        for (String noteData : notes) {
            String[] split = noteData.split(" ");

            float start = Float.valueOf(split[0]);
            int pitch = Float.valueOf(split[1]).intValue();
            float velocity = Float.valueOf(split[2]);
            float end = Float.valueOf(split[3]);
            int flags = Float.valueOf(split[4]).intValue();
            addNote(pitch, start, end, velocity, flags);
        }
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    public PatternSequencerComponent() {
    }

    //--------------------------------------------------------------------------
    //
    // IPatternSequencer API :: Methods
    //
    //--------------------------------------------------------------------------

    public void clear() {
        for (int bank = 0; bank < 4; bank++) {
            clearBank(bank);
        }
    }

    public void clearBank(int index) {
        PatternSequencerMessage.CLEAR_BANK.send(getEngine(), getToneIndex(), index);
    }

    public void clearIndex(int index) {
        PatternSequencerMessage.CLEAR_PATTERN.send(getEngine(), getToneIndex(), index);
    }

    final void sendBankOSC(int bank) {
        PatternSequencerMessage.BANK.send(getEngine(), getToneIndex(), bank);
    }

    final void sendcPatternOSC(int pattern) {
        PatternSequencerMessage.PATTERN.send(getEngine(), getToneIndex(), pattern);
    }

    public void addNote(int pitch, float start, float end, float velocity, int flags) {
        PatternSequencerMessage.NOTE_DATA.send(getEngine(), getToneIndex(), start, pitch, velocity,
                end, flags);
    }

    public void addNote(int bankIndex, int patternIndex, int pitch, float start, float end,
            float velocity, int flags) {
        int lastBank = getSelectedBank();
        int lastIndex = getSelectedIndex();
        setSelectedPattern(bankIndex, patternIndex);
        addNote(pitch, start, end, velocity, flags);
        setSelectedPattern(lastBank, lastIndex);
    }

    public void removeNote(int pitch, float start) {
        PatternSequencerMessage.NOTE_DATA_REMOVE.send(getEngine(), getToneIndex(), start, pitch);
    }

    public void removeNote(int bankIndex, int patternIndex, int pitch, float start) {
        int lastBank = getSelectedBank();
        int lastIndex = getSelectedIndex();
        setSelectedPattern(bankIndex, patternIndex);
        removeNote(pitch, start);
        setSelectedPattern(lastBank, lastIndex);
    }

    public void triggerOn(Resolution resolution, int step, int pitch, float gate, float velocity,
            int flags) {
        float start = Resolution.toBeat(step, resolution);
        float end = start + gate;
        addNote(pitch, start, end, velocity, flags);
    }

    public void triggerOn(int bankIndex, int patternIndex, Resolution resolution, int step,
            int pitch, float gate, float velocity, int flags) {
        int lastBank = getSelectedBank();
        int lastIndex = getSelectedIndex();
        setSelectedPattern(bankIndex, patternIndex);
        triggerOn(resolution, step, pitch, gate, velocity, flags);
        setSelectedPattern(lastBank, lastIndex);
    }

    public void triggerOff(Resolution resolution, int step, int pitch) {
        float start = Resolution.toBeat(step, resolution);
        removeNote(pitch, start);
    }

    public void triggerOff(int bankIndex, int patternIndex, Resolution resolution, int step,
            int pitch) {
        int lastBank = getSelectedBank();
        int lastIndex = getSelectedIndex();
        setSelectedPattern(bankIndex, patternIndex);
        triggerOff(resolution, step, pitch);
        setSelectedPattern(lastBank, lastIndex);
    }

    @Override
    public void restore() {
    }

    /**
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum Resolution {

        /**
         * A whole note.
         */
        WHOLE(1f), // 1

        /**
         * A half note.
         */
        HALF(0.5f), // 2

        /**
         * A quarter note.
         */
        QUATER(0.25f), // 4

        /**
         * An eighth note.
         */
        EIGHTH(0.125f), // 8

        /**
         * A sixteenth note.
         */
        SIXTEENTH(0.0625f), // 16

        /**
         * A thirtysecond note.
         */
        THIRTYSECOND(0.03125f), // 32

        /**
         * A sixtyfourth note.
         */
        SIXTYFOURTH(0.015625f); // 1 / 0.015625f = 64

        Resolution(float value) {
            mValue = value;
        }

        private float mValue;

        /**
         * Returns the amount of steps in a measure for the given phrase
         * resolution.
         * 
         * @param resolution The note resolution.
         * @return The number of steps in a measure for the given phrase
         *         resolution.
         */
        public final static int toSteps(Resolution resolution) {
            return (int)(1 / resolution.getValue());
        }

        public float getValue() {
            return mValue;
        }

        private static int beatsInMeasure = 4;

        public static int toStep(float beat, Resolution resolution) {
            // (beat(5) / 0.0625) / 4
            return (int)(beat / resolution.getValue()) / beatsInMeasure;
        }

        public static float toBeat(int step, Resolution resolution) {
            return (step * resolution.getValue()) * beatsInMeasure;
        }
    }

}
