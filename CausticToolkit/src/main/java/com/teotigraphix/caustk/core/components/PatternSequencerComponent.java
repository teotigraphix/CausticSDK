
package com.teotigraphix.caustk.core.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.teotigraphix.caustic.internal.sequencer.PatternSequencerConstants;
import com.teotigraphix.caustic.osc.PatternSequencerMessage;
import com.teotigraphix.caustic.sequencer.IStepPhrase.Resolution;

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
        int measures = (int)PatternSequencerMessage.NUM_MEASURES.send(getEngine(), getToneIndex());
        return measures;
    }

    public void setLength(int value) {
        PatternSequencerMessage.NUM_MEASURES.send(getEngine(), getToneIndex(), value);
    }

    public int getLength(int bankIndex, int patternIndex) {
        int lastBank = getSelectedBank();
        int lastIndex = getSelectedIndex();
        int measures = (int)PatternSequencerMessage.NUM_MEASURES.send(getEngine(), getToneIndex());
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
            throw newRangeException(PatternSequencerConstants.BANK, "0..15", value);
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
            throw newRangeException(PatternSequencerConstants.PATTERN, "0..15", value);
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

}
