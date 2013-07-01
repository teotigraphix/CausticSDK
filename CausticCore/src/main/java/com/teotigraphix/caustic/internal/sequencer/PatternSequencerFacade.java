
package com.teotigraphix.caustic.internal.sequencer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.internal.device.DeviceComponent;
import com.teotigraphix.caustic.osc.PatternSequencerMessage;
import com.teotigraphix.caustic.sequencer.IPatternSequencer2;
import com.teotigraphix.caustic.sequencer.IStepPhrase.Resolution;

public class PatternSequencerFacade extends DeviceComponent implements IPatternSequencer2 {

    //--------------------------------------------------------------------------
    //
    // IPatternSequencer API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // length
    //----------------------------------

    @Override
    public int getLength() {
        int measures = (int)PatternSequencerMessage.NUM_MEASURES
                .send(getEngine(), getDeviceIndex());
        return measures;
    }

    @Override
    public void setLength(int value) {
        PatternSequencerMessage.NUM_MEASURES.send(getEngine(), getDeviceIndex(), value);
    }

    @Override
    public int getLength(int bankIndex, int patternIndex) {
        int lastBank = getSelectedBank();
        int lastIndex = getSelectedIndex();
        int measures = (int)PatternSequencerMessage.NUM_MEASURES
                .send(getEngine(), getDeviceIndex());
        setSelectedPattern(lastBank, lastIndex);
        return measures;
    }

    @Override
    public void setLength(int bankIndex, int patternIndex, int value) {
        int lastBank = getSelectedBank();
        int lastIndex = getSelectedIndex();
        setSelectedPattern(bankIndex, patternIndex);
        PatternSequencerMessage.NUM_MEASURES.send(getEngine(), getDeviceIndex(), value);
        setSelectedPattern(lastBank, lastIndex);
    }

    //----------------------------------
    // selectedBank
    //----------------------------------

    @Override
    public void setSelectedPattern(int bank, int pattern) {
        setSelectedBank(bank);
        setSelectedIndex(pattern);
    }

    //----------------------------------
    // selectedBank
    //----------------------------------

    private int selectedBank = 0;

    @Override
    public int getSelectedBank() {
        return selectedBank;
    }

    @Override
    public int getSelectedBank(boolean restore) {
        return (int)PatternSequencerMessage.BANK.query(getEngine(), getDeviceIndex());
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

    @Override
    public int getSelectedIndex() {
        return selectedIndex;
    }

    @Override
    public int getSelectedIndex(boolean restore) {
        return (int)PatternSequencerMessage.PATTERN.query(getEngine(), getDeviceIndex());
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

    @Override
    public List<String> getPatternListing() {
        String patterns = PatternSequencerMessage.QUERY_PATTERNS_WITH_DATA.queryString(getEngine(),
                getDeviceIndex());
        if (patterns == null || "".equals(patterns))
            return new ArrayList<String>();
        return Arrays.asList(patterns.split(" "));
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    public PatternSequencerFacade(IDevice device) {
        super(device);
    }

    //--------------------------------------------------------------------------
    //
    // IPatternSequencer API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void clear() {
        for (int bank = 0; bank < 4; bank++) {
            clearBank(bank);
        }
    }

    @Override
    public void clearBank(int index) {
        PatternSequencerMessage.CLEAR_BANK.send(getEngine(), getDeviceIndex(), index);
    }

    @Override
    public void clearIndex(int index) {
        PatternSequencerMessage.CLEAR_PATTERN.send(getEngine(), getDeviceIndex(), index);
    }

    final void sendBankOSC(int bank) {
        PatternSequencerMessage.BANK.send(getEngine(), getDeviceIndex(), bank);
    }

    final void sendcPatternOSC(int pattern) {
        PatternSequencerMessage.PATTERN.send(getEngine(), getDeviceIndex(), pattern);
    }

    @Override
    public void addNote(int pitch, float start, float end, float velocity, int flags) {
        PatternSequencerMessage.NOTE_DATA.send(getEngine(), getDeviceIndex(), start, pitch,
                velocity, end, flags);
    }

    @Override
    public void addNote(int bankIndex, int patternIndex, int pitch, float start, float end,
            float velocity, int flags) {
        int lastBank = getSelectedBank();
        int lastIndex = getSelectedIndex();
        setSelectedPattern(bankIndex, patternIndex);
        addNote(pitch, start, end, velocity, flags);
        setSelectedPattern(lastBank, lastIndex);
    }

    @Override
    public void removeNote(int pitch, float start) {
        PatternSequencerMessage.NOTE_DATA_REMOVE.send(getEngine(), getDeviceIndex(), start, pitch);
    }

    @Override
    public void removeNote(int bankIndex, int patternIndex, int pitch, float start) {
        int lastBank = getSelectedBank();
        int lastIndex = getSelectedIndex();
        setSelectedPattern(bankIndex, patternIndex);
        removeNote(pitch, start);
        setSelectedPattern(lastBank, lastIndex);
    }

    @Override
    public void triggerOn(Resolution resolution, int step, int pitch, float gate, float velocity,
            int flags) {
        float start = Resolution.toBeat(step, resolution);
        float end = start + gate;
        addNote(pitch, start, end, velocity, flags);
    }

    @Override
    public void triggerOn(int bankIndex, int patternIndex, Resolution resolution, int step,
            int pitch, float gate, float velocity, int flags) {
        int lastBank = getSelectedBank();
        int lastIndex = getSelectedIndex();
        setSelectedPattern(bankIndex, patternIndex);
        triggerOn(resolution, step, pitch, gate, velocity, flags);
        setSelectedPattern(lastBank, lastIndex);
    }

    @Override
    public void triggerOff(Resolution resolution, int step, int pitch) {
        float start = Resolution.toBeat(step, resolution);
        removeNote(pitch, start);
    }

    @Override
    public void triggerOff(int bankIndex, int patternIndex, Resolution resolution, int step,
            int pitch) {
        int lastBank = getSelectedBank();
        int lastIndex = getSelectedIndex();
        setSelectedPattern(bankIndex, patternIndex);
        triggerOff(resolution, step, pitch);
        setSelectedPattern(lastBank, lastIndex);
    }
}
