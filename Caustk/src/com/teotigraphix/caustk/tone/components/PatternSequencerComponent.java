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

package com.teotigraphix.caustk.tone.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.sequencer.track.Note;
import com.teotigraphix.caustk.tone.ToneComponent;

public class PatternSequencerComponent extends ToneComponent {

    private static final long serialVersionUID = -6509903131350285187L;

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
        int lastIndex = getSelectedPattern();
        sendBankPatternOSC(bankIndex, patternIndex);
        int measures = (int)PatternSequencerMessage.NUM_MEASURES.query(getEngine(), getToneIndex());
        sendBankPatternOSC(lastBank, lastIndex);
        return measures;
    }

    public void setLength(int bankIndex, int patternIndex, int value) {
        int lastBank = getSelectedBank();
        int lastIndex = getSelectedPattern();
        setSelectedBankPattern(bankIndex, patternIndex);
        PatternSequencerMessage.NUM_MEASURES.send(getEngine(), getToneIndex(), value);
        setSelectedBankPattern(lastBank, lastIndex);
    }

    //----------------------------------
    // selectedBank
    //----------------------------------

    public void setSelectedBankPattern(int bank, int pattern) {
        setSelectedBank(bank);
        setSelectedPattern(pattern);
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

    public void setSelectedBank(int value) {
        if (value < 0 || value > 15)
            throw newRangeException("bank", "0..15", value);
        selectedBank = value;
        sendBankOSC(selectedBank);
    }

    //----------------------------------
    // selectedPattern
    //----------------------------------

    private int selectedPattern = 0;

    public int getSelectedPattern() {
        return selectedPattern;
    }

    public int getSelectedPattern(boolean restore) {
        return (int)PatternSequencerMessage.PATTERN.query(getEngine(), getToneIndex());
    }

    public void setSelectedPattern(int value) {
        if (value < 0 || value > 15)
            throw newRangeException("pattern", "0..15", value);
        selectedPattern = value;
        sendcPatternOSC(selectedPattern);
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

    /**
     * Returns a collection of {@link Note}s for the bank and pattern.
     * <p>
     * The sequencer will temporarily set the bank and pattern, then rest to the
     * original values.
     * 
     * @param bank
     * @param pattern
     */
    public Collection<Note> getNotes(int bank, int pattern) {
        int oldBank = getSelectedBank();
        int oldPattern = getSelectedPattern();
        sendBankPatternOSC(bank, pattern);

        List<Note> result = new ArrayList<Note>();
        String data = PatternSequencerMessage.QUERY_NOTE_DATA.queryString(getEngine(),
                getToneIndex());

        if (data != null && !data.equals("")) {
            String[] notes = data.split("\\|");
            for (String noteData : notes) {
                String[] split = noteData.split(" ");

                float start = Float.valueOf(split[0]);
                int pitch = Float.valueOf(split[1]).intValue();
                float velocity = Float.valueOf(split[2]);
                float end = Float.valueOf(split[3]);
                int flags = Float.valueOf(split[4]).intValue();
                Note note = new Note(pitch, start, end, velocity, flags);
                result.add(note);
            }
        }

        sendBankPatternOSC(oldBank, oldPattern);

        return result;
    }

    public void assignNoteData(int bankIndex, int patternIndex, String data) {
        int lastBank = getSelectedBank();
        int lastIndex = getSelectedPattern();
        setSelectedBankPattern(bankIndex, patternIndex);
        assignNoteData(data);
        setSelectedBankPattern(lastBank, lastIndex);
    }

    public void assignNoteData(String data) {
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

    public ShuffleMode getShuffleMode() {
        return ShuffleMode.fromInt((int)PatternSequencerMessage.SHUFFLE_MODE.query(getEngine(),
                getToneIndex()));
    }

    public void setShuffleMode(ShuffleMode value) {
        PatternSequencerMessage.SHUFFLE_MODE.send(getEngine(), getToneIndex(), value.getValue());
    }

    public float getShuffleAmount() {
        return PatternSequencerMessage.SHUFFLE_AMOUNT.query(getEngine(), getToneIndex());
    }

    public void setShuffleAmount(float value) {
        PatternSequencerMessage.SHUFFLE_AMOUNT.send(getEngine(), getToneIndex(), value);
    }

    public enum ShuffleMode {

        DEFAULT(0),

        EIGTH(1),

        SIXTEENTH(2);

        private final int value;

        ShuffleMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ShuffleMode fromInt(int value) {
            for (ShuffleMode mode : values()) {
                if (mode.getValue() == value)
                    return mode;
            }
            return null;
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

    /**
     * Clears the bank and index while holding the selected bank and pattern
     * index.
     * <p>
     * Use this is the selected bank and pattern cannot change.
     * 
     * @param bank The bank to clear.
     * @param index The pattern to clear.
     */
    public void clearIndex(int bank, int index) {
        int oldBank = getSelectedBank(true);
        sendBankOSC(bank);
        PatternSequencerMessage.CLEAR_PATTERN.send(getEngine(), getToneIndex(), index);
        sendBankOSC(oldBank);
    }

    /**
     * Clears the pattern using the current bank selected.
     * 
     * @param index The pattern index to clear.
     */
    public void clearIndex(int index) {
        PatternSequencerMessage.CLEAR_PATTERN.send(getEngine(), getToneIndex(), index);
    }

    final void sendBankPatternOSC(int bank, int pattern) {
        PatternSequencerMessage.BANK.send(getEngine(), getToneIndex(), bank);
        PatternSequencerMessage.PATTERN.send(getEngine(), getToneIndex(), pattern);
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
        int lastIndex = getSelectedPattern();
        setSelectedBankPattern(bankIndex, patternIndex);
        addNote(pitch, start, end, velocity, flags);
        setSelectedBankPattern(lastBank, lastIndex);
    }

    public void removeNote(int pitch, float start) {
        PatternSequencerMessage.NOTE_DATA_REMOVE.send(getEngine(), getToneIndex(), start, pitch);
    }

    public void removeNote(int bankIndex, int patternIndex, int pitch, float start) {
        int lastBank = getSelectedBank();
        int lastIndex = getSelectedPattern();
        setSelectedBankPattern(bankIndex, patternIndex);
        removeNote(pitch, start);
        setSelectedBankPattern(lastBank, lastIndex);
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
