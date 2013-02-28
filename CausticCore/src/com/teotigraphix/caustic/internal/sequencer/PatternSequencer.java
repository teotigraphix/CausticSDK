////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.internal.sequencer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.teotigraphix.caustic.core.CausticError;
import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.internal.device.DeviceComponent;
import com.teotigraphix.caustic.internal.util.PatternUtils;
import com.teotigraphix.caustic.machine.ISubSynth;
import com.teotigraphix.caustic.osc.PatternSequencerMessage;
import com.teotigraphix.caustic.sequencer.IPatternSequencer;
import com.teotigraphix.caustic.sequencer.IPhrase;
import com.teotigraphix.caustic.sequencer.IPhrase.IPhraseListener;
import com.teotigraphix.caustic.sequencer.IPhrase.Resolution;
import com.teotigraphix.caustic.sequencer.IPhrase.TriggerChangeKind;
import com.teotigraphix.caustic.sequencer.ITrigger;
import com.teotigraphix.common.IMemento;

/*

 - A Bank holds Phrases that have pattern number assignments in the sequencer.
 - the getSelectedPattern() is an index that points to a Phrase instance

 */

/**
 * The default implementation of the IPatternSequencer.
 * <p>
 * The IPatternSequencer is the main controller for a
 * {@link IPatternSequencerAware} machine such as the {@link ISubSynth}. To use
 * this correctly, the calling client needs to first select the correct pattern
 * bank index that addNote() and removeNote() will be targeting. Second, the
 * current pattern index in the bank must be selected. With these two settings
 * initialized, the sequencer can correctly target the new note triggers that
 * come in and that are sent to the core audio system.
 * </p>
 * <p>
 * The default bank is 0 and the default pattern is 0. These will not change
 * when addNote() and removeNote() are called until the bank and pattern are
 * explicitly changed.
 * </p>
 * <p>
 * When changing the bank and pattern, the caustic core's sequencer for the
 * machine updates it's current bank and pattern.
 * </p>
 * <p>
 * Note: The default implementation of this class does NOT error check
 * overlapping notes or incorrect placements. This checking is the
 * responsibility of the client such as the phrase.
 * </p>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class PatternSequencer extends DeviceComponent implements IPatternSequencer, IPhraseListener {

    private static final String ATT_ACTIVE = "active";

    private static final String ATT_INDEX = "index";

    private static final String ATT_BANK = "bank";

    private static final String TAG_PHRASE = "phrase";

    // <bankIndex, <patternIndex, IPhrase>>
    private final Map<Integer, Map<Integer, IPhrase>> mPhraseMap = new TreeMap<Integer, Map<Integer, IPhrase>>();

    //--------------------------------------------------------------------------
    //
    // IPatternSequencer API :: Properties
    //
    //--------------------------------------------------------------------------

    @Override
    public void setBankPattern(int bank, int pattern) {
        Phrase oldPhrase = getActivePhrase();
        if (!oldPhrase.hasTriggers()) {
            PatternSequencerUtils.removePhrase(this, oldPhrase.getBank(), oldPhrase.getIndex());
        }

        setSelectedBank(bank);
        setSelectedPattern(pattern);

        Map<Integer, IPhrase> map = getBankMap(mSelectedBank);
        Phrase phrase = (Phrase)map.get(mSelectedPattern);
        // create the phrase, the phrase will be removed if the pattern
        // or bank switches and there is no triggers
        if (phrase == null) {
            phrase = PatternSequencerUtils.addPhrase(this, bank, pattern);
        }

        setActivePhrase(phrase);

        if (phrase != null) {
            firePhraseChange(phrase, oldPhrase);
        }
    }

    //----------------------------------
    // selectedBank
    //----------------------------------

    private int mSelectedBank = 0;

    @Override
    public int getSelectedBank() {
        return mSelectedBank;
    }

    @Override
    public int getSelectedBank(boolean restore) {
        return (int)PatternSequencerMessage.BANK.query(getEngine(), getDeviceIndex());
    }

    void setSelectedBank(int value) {
        if (value < 0 || value > 15)
            throw newRangeException(PatternSequencerConstants.BANK, "0..15", value);
        mSelectedBank = value;
        setBank(mSelectedBank);
    }

    //----------------------------------
    // selectedPattern
    //----------------------------------

    private int mSelectedPattern = 0;

    @Override
    public int getSelectedPattern() {
        return mSelectedPattern;
    }

    @Override
    public int getSelectedPattern(boolean restore) {
        return (int)PatternSequencerMessage.PATTERN.query(getEngine(), getDeviceIndex());
    }

    void setSelectedPattern(int value) {
        if (value < 0 || value > 15)
            throw newRangeException(PatternSequencerConstants.PATTERN, "0..15", value);
        mSelectedPattern = value;
        setPattern(mSelectedPattern);
    }

    //----------------------------------
    // patternListing
    //----------------------------------

    @Override
    public List<String> getPatternListing() {
        String patterns = PatternSequencerMessage.QUERY_PATTERNS_WITH_DATA.queryString(getEngine(),
                getDeviceIndex());
        if (patterns == null)
            return new ArrayList<String>();
        return Arrays.asList(patterns.split(" "));
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public PatternSequencer(IDevice device) {
        super(device);
        setName(PatternSequencerConstants.COMPONENT_ID);

        mPhraseMap.put(0, new TreeMap<Integer, IPhrase>());
        mPhraseMap.put(1, new TreeMap<Integer, IPhrase>());
        mPhraseMap.put(2, new TreeMap<Integer, IPhrase>());
        mPhraseMap.put(3, new TreeMap<Integer, IPhrase>());

        // add the default phrase
        Phrase active = PatternSequencerUtils.addPhrase(this, getSelectedBank(),
                getSelectedPattern());
        setActivePhrase(active);
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
        // a bank clear removes all phrases from the bank map
        // the core takes care of deleting the data, we just need to update the
        // model
        getBankMap(index).clear();
        PatternSequencerMessage.CLEAR_BANK.send(getEngine(), getDeviceIndex(), index);
    }

    @Override
    public IPhrase clearPhrase(int index) {
        if (!hasPhrase(mSelectedBank, index))
            return null;
        // the selected bank will be cleared of phrases
        IPhrase phrase = PatternSequencerUtils.removePhrase(this, getSelectedBank(), index);
        if (phrase == null)
            return null;
        PatternSequencerMessage.CLEAR_PATTERN.send(getEngine(), getDeviceIndex(), index);
        return phrase;
    }

    @Override
    public IPhrase clearPhrase(int bank, int index) {
        if (!hasPhrase(bank, index))
            return null;
        final int lastBank = getSelectedBank();
        setSelectedBank(bank);
        final IPhrase phrase = clearPhrase(index);
        setSelectedBank(lastBank);
        return phrase;
    }

    @Override
    public boolean hasPhrase(final int bank, final int pattern) {
        final Map<Integer, IPhrase> map = getBankMap(bank);
        if (map.size() == 0)
            return false;
        return map.containsKey(pattern);
    }

    @Override
    public IPhrase getPhrase() {
        return mActivePhrase;
    }

    @Override
    public IPhrase getPhraseAt(int bank, int pattern) {
        return getBankMap(bank).get(pattern);
    }

    @Override
    public List<IPhrase> getPhrases() {
        List<IPhrase> result = new ArrayList<IPhrase>();
        for (Map<Integer, IPhrase> bankMap : mPhraseMap.values()) {
            for (IPhrase phrase : bankMap.values()) {
                result.add(phrase);
            }
        }
        return result;
    }

    @Override
    public List<IPhrase> getPhrases(int bank) {
        List<IPhrase> result = new ArrayList<IPhrase>();
        Map<Integer, IPhrase> bankMap = getBankMap(bank);
        for (IPhrase phrase : bankMap.values()) {
            result.add(phrase);
        }
        return result;
    }

    @Override
    public void addNote(int pitch, float start, float end, float velocity, int flags) {
        // do some pre checks
        if (pitch < 0 || pitch > 255) {
            throw new CausticError("pitch out of bounds 0..255 was [" + pitch + "]");
        }

        if (start >= end) {
            throw new CausticError("start out of bounds greater then end [start:" + start
                    + ", end:" + end + "]");
        }

        if (end <= start) {
            throw new CausticError("end out of bounds less then start [start:" + start + ", end:"
                    + end + "]");
        }

        if (velocity < 0f || velocity > 1f) {
            throw new CausticError("velocity out of bounds 0.0..1.0 [" + velocity + "]");
        }

        Phrase active = getActivePhrase();
        if (active == null) {
            active = PatternSequencerUtils.addPhrase(this, mSelectedBank, mSelectedPattern);
            setActivePhrase(active);
        }
        getActivePhrase().addNote(pitch, start, end, velocity, flags);
        PatternSequencerMessage.NOTE_DATA.send(getEngine(), getDeviceIndex(), start, pitch,
                velocity, end, flags);
    }

    @Override
    public void removeNote(int pitch, float start) {
        //ITrigger trigger = getActivePhrase().getTriggerAtBeat(start, pitch);
        getActivePhrase().removeNote(pitch, start);
        PatternSequencerMessage.NOTE_DATA_REMOVE.send(getEngine(), getDeviceIndex(), start, pitch);
    }

    //--------------------------------------------------------------------------
    //
    // IPersist API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        memento.putString("id", getDevice().getId());
        memento.putInteger(ATT_BANK, getSelectedBank());
        memento.putInteger(ATT_INDEX, getSelectedPattern());
        memento.putString(ATT_ACTIVE, getActivePhrase().getId());
        for (IPhrase phrase : getPhrases()) {
            phrase.copy(memento.createChild(TAG_PHRASE));
        }
    }

    @Override
    public void paste(IMemento memento) {
        IMemento[] phrases = memento.getChildren(TAG_PHRASE);
        for (IMemento child : phrases) {
            int bank = child.getInteger(ATT_BANK);
            int pattern = child.getInteger(ATT_INDEX);
            Phrase phrase = PatternSequencerUtils.addPhrase(this, bank, pattern);
            phrase.paste(child);
        }

        // the LOAD event from the loop above should remove the need for the
        // below loop but there is a problem with the LOAD right now
        for (IPhrase phrase : getPhrases()) {
            setBankPattern(phrase.getBank(), phrase.getIndex());
            for (Map<Integer, ITrigger> triggers : phrase.getStepMap().values()) {
                for (ITrigger trigger : triggers.values()) {
                    float start = Resolution.toBeat(trigger.getIndex(), phrase.getResolution());
                    float end = start + trigger.getGate();
                    float velocity = trigger.getVelocity();
                    int flags = 0;

                    addNote(trigger.getPitch(), start, end, velocity, flags);
                }
            }
        }

        int activeBank = memento.getInteger(ATT_BANK);
        int activePattern = memento.getInteger(ATT_INDEX);

        setBankPattern(activeBank, activePattern);
    }

    @Override
    public void restore() {
        super.restore();

        final int oldBank = getSelectedBank(true);
        final int oldPattern = getSelectedPattern(true);

        final String name = getDevice().getId();
        final List<String> patterns = getPatternListing();
        if (patterns.size() == 0)
            return;

        for (String pattern : patterns) {
            int bank = PatternUtils.toBank(pattern);
            int index = PatternUtils.toPattern(pattern);
            // (mschmalle) We do not need to send messages since this is a
            // restore and the core already has this note_data and
            // bank/pattern structures BUT since we are query the core
            // for note_data the bank and pattern need to be set
            setBankPattern(bank, index);
            String data = getNoteData(name);
            Phrase phrase = PatternSequencerUtils.addPhrase(this, bank, index, null);
            // the phrase does not implement IRestore, so the legnth needs to be
            // restored here and we don't know anything about the resolution
            // since
            // caustic dosn't save that
            int length = (int)PatternSequencerMessage.NUM_MEASURES.query(getEngine(),
                    getDeviceIndex());
            phrase.setLength(length);
            // et the data AFTER the legnth is correct
            phrase.setStringData(data);
        }

        setBankPattern(oldBank, oldPattern);
    }

    protected final String getNoteData(String name) {
        return PatternSequencerMessage.QUERY_NOTE_DATA.queryString(getEngine(), getDeviceIndex());
    }

    final void setBank(int bank) {
        PatternSequencerMessage.BANK.send(getEngine(), getDeviceIndex(), bank);
    }

    final void setPattern(int pattern) {
        PatternSequencerMessage.PATTERN.send(getEngine(), getDeviceIndex(), pattern);
    }

    protected final Map<Integer, IPhrase> getBankMap(int bank) {
        return mPhraseMap.get(bank);
    }

    private Phrase mActivePhrase;

    protected final Phrase getActivePhrase() {
        return mActivePhrase;
    }

    protected final void setActivePhrase(Phrase value) {
        Phrase oldPhrase = mActivePhrase;
        if (oldPhrase != null) {
            oldPhrase.setActive(false);
        }

        mActivePhrase = value;
        if (mActivePhrase == null)
            return;

        mActivePhrase.setActive(true);
    }

    //----------------------------------
    // Listeners
    //----------------------------------

    private final List<IPatternSequencerListener> mPatternSequencerListeners = new ArrayList<IPatternSequencerListener>();

    @Override
    public final void addPatternSequencerListener(IPatternSequencerListener value) {
        if (mPatternSequencerListeners.contains(value))
            return;
        mPatternSequencerListeners.add(value);
    }

    @Override
    public final void removePatternSequencerListener(IPatternSequencerListener value) {
        if (!mPatternSequencerListeners.contains(value))
            return;
        mPatternSequencerListeners.remove(value);
    }

    protected final void firePhraseChange(IPhrase phrase, IPhrase oldPhrase) {
        for (IPatternSequencerListener listener : mPatternSequencerListeners) {
            listener.onPhraseChange(phrase, oldPhrase);
        }
    }

    //--------------------------------------------------------------------------
    //
    // PhraseListener API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void onLengthChange(IPhrase phrase, int length) {
        // update the sequencer, check to see we are setting the correct bank,
        // pattern
        // int bank = phrase.
        // NOTE We are not duplicating bank and pattern assigments with a
        // message
        // if they are the same here, not OSC message gets set
        int bank = getSelectedBank();
        int pattern = getSelectedPattern();
        boolean resetBank = false;
        boolean resetPattern = false;
        if (bank != phrase.getBank()) {
            setBank(phrase.getBank());
            resetBank = true;
        }
        if (pattern != phrase.getIndex()) {
            setPattern(phrase.getIndex());
            resetPattern = true;
        }
        PatternSequencerMessage.NUM_MEASURES.send(getEngine(), getDeviceIndex(), length);
        if (resetBank)
            setBank(bank);
        if (resetPattern)
            setPattern(pattern);
    }

    @Override
    public void onPositionChange(IPhrase phrase, int position) {
    }

    @Override
    public void onResolutionChange(IPhrase phrase, Resolution resolution) {
    }

    @Override
    public void onTriggerDataChange(ITrigger trigger, TriggerChangeKind kind) {
        // if (kind.equals(TriggerChangeKind.LOAD)) {
        // float start = Resolution.toBeat(trigger.getIndex(), trigger
        // .getPhrase().getResolution());
        // float end = start + trigger.getGate();
        // float velocity = trigger.getVelocity();
        // int flags = 0;
        //
        // addNote(trigger.getPitch(), start, end, velocity, flags);
        // }
    }

}
