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

import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.internal.device.DeviceComponent;
import com.teotigraphix.caustic.internal.utils.PatternUtils;
import com.teotigraphix.caustic.machine.ISubSynth;
import com.teotigraphix.caustic.osc.PatternSequencerMessage;
import com.teotigraphix.caustic.sequencer.IPatternSequencer;
import com.teotigraphix.caustic.sequencer.ITrigger;
import com.teotigraphix.caustic.sequencer.IStepPhrase;
import com.teotigraphix.caustic.sequencer.IStepPhrase.IStepPhraseListener;
import com.teotigraphix.caustic.sequencer.IStepPhrase.Resolution;
import com.teotigraphix.caustic.sequencer.IStepPhrase.TriggerChangeKind;

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
 * <p>
 * The default bank is 0 and the default pattern is 0. These will not change
 * when addNote() and removeNote() are called until the bank and pattern are
 * explicitly changed.
 * <p>
 * When changing the bank and pattern, the caustic core's sequencer for the
 * machine updates it's current bank and pattern.
 * <p>
 * Note: The default implementation of this class does NOT error check
 * overlapping notes or incorrect placements. This checking is the
 * responsibility of the client such as the phrase.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class PatternSequencer extends DeviceComponent implements
        IPatternSequencer, IStepPhraseListener
{

    // <bankIndex, <patternIndex, IPhrase>>
    private final Map<Integer, Map<Integer, IStepPhrase>> stepPhraseMap = new TreeMap<Integer, Map<Integer, IStepPhrase>>();

    //--------------------------------------------------------------------------
    //
    // IPatternSequencer API :: Properties
    //
    //--------------------------------------------------------------------------

    @Override
    public void setBankPattern(int bank, int pattern)
    {
        IStepPhrase oldPhrase = getActivePhrase();
        if (!oldPhrase.hasTriggers())
        {
            PatternSequencerUtils.removeStepPhrase(this, oldPhrase.getBank(),
                    oldPhrase.getIndex());
        }

        setSelectedBank(bank);
        setSelectedPattern(pattern);

        Map<Integer, IStepPhrase> map = getBankMap(selectedBank);
        IStepPhrase phrase = map.get(selectedPattern);
        // create the phrase, the phrase will be removed if the pattern
        // or bank switches and there is no triggers
        if (phrase == null)
        {
            phrase = PatternSequencerUtils.addStepPhrase(this, bank, pattern);
        }

        setActiveStepPhrase(phrase);

        if (phrase != null)
        {
            firePhraseChange(phrase, oldPhrase);
        }
    }

    //----------------------------------
    // selectedBank
    //----------------------------------

    private int selectedBank = 0;

    @Override
    public int getSelectedBank()
    {
        return selectedBank;
    }

    @Override
    public int getSelectedBank(boolean restore)
    {
        return (int) PatternSequencerMessage.BANK.query(getEngine(),
                getDeviceIndex());
    }

    void setSelectedBank(int value)
    {
        if (value < 0 || value > 15)
            throw newRangeException(PatternSequencerConstants.BANK, "0..15",
                    value);
        selectedBank = value;
        setBank(selectedBank);
    }

    //----------------------------------
    // selectedPattern
    //----------------------------------

    private int selectedPattern = 0;

    @Override
    public int getSelectedPattern()
    {
        return selectedPattern;
    }

    @Override
    public int getSelectedPattern(boolean restore)
    {
        return (int) PatternSequencerMessage.PATTERN.query(getEngine(),
                getDeviceIndex());
    }

    void setSelectedPattern(int value)
    {
        if (value < 0 || value > 15)
            throw newRangeException(PatternSequencerConstants.PATTERN, "0..15",
                    value);
        selectedPattern = value;
        setPattern(selectedPattern);
    }

    //----------------------------------
    // patternListing
    //----------------------------------

    @Override
    public List<String> getPatternListing()
    {
        String patterns = PatternSequencerMessage.QUERY_PATTERNS_WITH_DATA
                .queryString(getEngine(), getDeviceIndex());
        if (patterns == null || "".equals(patterns))
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
    public PatternSequencer(IDevice device)
    {
        super(device);
        setName(PatternSequencerConstants.COMPONENT_ID);

        stepPhraseMap.put(0, new TreeMap<Integer, IStepPhrase>());
        stepPhraseMap.put(1, new TreeMap<Integer, IStepPhrase>());
        stepPhraseMap.put(2, new TreeMap<Integer, IStepPhrase>());
        stepPhraseMap.put(3, new TreeMap<Integer, IStepPhrase>());

        // add the default phrase
        IStepPhrase active = PatternSequencerUtils.addStepPhrase(this,
                getSelectedBank(), getSelectedPattern());
        setActiveStepPhrase(active);
    }

    //--------------------------------------------------------------------------
    //
    // IPatternSequencer API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void clear()
    {
        for (int bank = 0; bank < 4; bank++)
        {
            clearBank(bank);
        }
    }

    @Override
    public void clearBank(int index)
    {
        // a bank clear removes all phrases from the bank map
        // the core takes care of deleting the data, we just need to update the
        // model
        getBankMap(index).clear();
        PatternSequencerMessage.CLEAR_BANK.send(getEngine(), getDeviceIndex(),
                index);
    }

    @Override
    public IStepPhrase clearStepPhrase(int index)
    {
        if (!hasStepPhrase(selectedBank, index))
            return null;
        // the selected bank will be cleared of phrases
        IStepPhrase phrase = PatternSequencerUtils.removeStepPhrase(this,
                getSelectedBank(), index);
        if (phrase == null)
            return null;
        PatternSequencerMessage.CLEAR_PATTERN.send(getEngine(),
                getDeviceIndex(), index);
        return phrase;
    }

    @Override
    public IStepPhrase clearStepPhrase(int bank, int index)
    {
        if (!hasStepPhrase(bank, index))
            return null;
        final int lastBank = getSelectedBank();
        setSelectedBank(bank);
        final IStepPhrase phrase = clearStepPhrase(index);
        setSelectedBank(lastBank);
        return phrase;
    }

    @Override
    public boolean hasStepPhrase(final int bank, final int pattern)
    {
        final Map<Integer, IStepPhrase> map = getBankMap(bank);
        if (map.size() == 0)
            return false;
        return map.containsKey(pattern);
    }

    @Deprecated
    @Override
    public IStepPhrase getTriggerMap()
    {
        return activePhrase;
    }

    @Override
    public IStepPhrase getActiveStepPhrase()
    {
        return activePhrase;
    }

    @Override
    public IStepPhrase getStepPhraseAt(int bank, int pattern)
    {
        return getBankMap(bank).get(pattern);
    }

    @Override
    public List<IStepPhrase> getStepPhrases()
    {
        List<IStepPhrase> result = new ArrayList<IStepPhrase>();
        for (Map<Integer, IStepPhrase> bankMap : stepPhraseMap.values())
        {
            for (IStepPhrase phrase : bankMap.values())
            {
                result.add(phrase);
            }
        }
        return result;
    }

    @Override
    public List<IStepPhrase> getStepPhraseForBank(int bank)
    {
        List<IStepPhrase> result = new ArrayList<IStepPhrase>();
        Map<Integer, IStepPhrase> bankMap = getBankMap(bank);
        for (IStepPhrase phrase : bankMap.values())
        {
            result.add(phrase);
        }
        return result;
    }

    //@Override
    void addNote(int pitch, float start, float end, float velocity, int flags)
    {
        //        // do some pre checks
        //        if (pitch < 0 || pitch > 255)
        //        {
        //            throw new CausticError("pitch out of bounds 0..255 was [" + pitch
        //                    + "]");
        //        }
        //
        //        if (start >= end)
        //        {
        //            throw new CausticError(
        //                    "start out of bounds greater then end [start:" + start
        //                            + ", end:" + end + "]");
        //        }
        //
        //        if (end <= start)
        //        {
        //            throw new CausticError("end out of bounds less then start [start:"
        //                    + start + ", end:" + end + "]");
        //        }
        //
        //        if (velocity < 0f || velocity > 1f)
        //        {
        //            throw new CausticError("velocity out of bounds 0.0..1.0 ["
        //                    + velocity + "]");
        //        }
        //
        //        TriggerMap active = getActivePhrase();
        //        if (active == null)
        //        {
        //            active = PatternSequencerUtils.addPhrase(this, selectedBank,
        //                    selectedPattern);
        //            setActiveTriggerMap(active);
        //        }
        //        getActivePhrase().addNote(pitch, start, end, velocity, flags);
        //        PatternSequencerMessage.NOTE_DATA.send(getEngine(), getDeviceIndex(),
        //                start, pitch, velocity, end, flags);
    }

    //@Override
    void removeNote(int pitch, float start)
    {
        //        //ITrigger trigger = getActivePhrase().getTriggerAtBeat(start, pitch);
        //        getActivePhrase().removeNote(pitch, start);
        //        PatternSequencerMessage.NOTE_DATA_REMOVE.send(getEngine(),
        //                getDeviceIndex(), start, pitch);
    }

    protected final String getNoteData(String name)
    {
        return PatternSequencerMessage.QUERY_NOTE_DATA.queryString(getEngine(),
                getDeviceIndex());
    }

    final void setBank(int bank)
    {
        PatternSequencerMessage.BANK.send(getEngine(), getDeviceIndex(), bank);
    }

    final void setPattern(int pattern)
    {
        PatternSequencerMessage.PATTERN.send(getEngine(), getDeviceIndex(),
                pattern);
    }

    protected final Map<Integer, IStepPhrase> getBankMap(int bank)
    {
        return stepPhraseMap.get(bank);
    }

    protected IStepPhrase activePhrase;

    public final IStepPhrase getActivePhrase()
    {
        return activePhrase;
    }

    @Override
    public void setActiveStepPhrase(IStepPhrase value)
    {
        IStepPhrase oldPhrase = activePhrase;
        if (oldPhrase != null)
        {
            oldPhrase.setActive(false);
        }

        activePhrase = value;
        if (activePhrase == null)
            return;

        activePhrase.setActive(true);
    }

    //----------------------------------
    // Listeners
    //----------------------------------

    private final List<IPatternSequencerListener> mPatternSequencerListeners = new ArrayList<IPatternSequencerListener>();

    @Override
    public final void addPatternSequencerListener(
            IPatternSequencerListener value)
    {
        if (mPatternSequencerListeners.contains(value))
            return;
        mPatternSequencerListeners.add(value);
    }

    @Override
    public final void removePatternSequencerListener(
            IPatternSequencerListener value)
    {
        if (!mPatternSequencerListeners.contains(value))
            return;
        mPatternSequencerListeners.remove(value);
    }

    protected final void firePhraseChange(IStepPhrase phrase,
            IStepPhrase oldPhrase)
    {
        for (IPatternSequencerListener listener : mPatternSequencerListeners)
        {
            listener.onTriggerMapChange(phrase, oldPhrase);
        }
    }

    @Override
    public void restore()
    {

        final int oldBank = getSelectedBank(true);
        final int oldPattern = getSelectedPattern(true);

        final String name = getDevice().getId();
        final List<String> patterns = getPatternListing();
        if (patterns.size() == 0)
            return;

        for (String pattern : patterns)
        {
            int bank = PatternUtils.toBank(pattern);
            int index = PatternUtils.toPattern(pattern);
            // (mschmalle) We do not need to send messages since this is a
            // restore and the core already has this note_data and
            // bank/pattern structures BUT since we are query the core
            // for note_data the bank and pattern need to be set
            setBankPattern(bank, index);
            String data = getNoteData(name);
            IStepPhrase phrase = PatternSequencerUtils.addStepPhrase(this,
                    bank, index, null);
            // the phrase does not implement IRestore, so the legnth needs to be
            // restored here and we don't know anything about the resolution
            // since
            // caustic dosn't save that
            int length = (int) PatternSequencerMessage.NUM_MEASURES.query(
                    getEngine(), getDeviceIndex());
            phrase.setLength(length);
            // et the data AFTER the legnth is correct
            phrase.setStringData(data);
        }

        setBankPattern(oldBank, oldPattern);
    }

    //--------------------------------------------------------------------------
    //
    // PhraseListener API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void onLengthChange(IStepPhrase phrase, int length)
    {
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
        if (bank != phrase.getBank())
        {
            setBank(phrase.getBank());
            resetBank = true;
        }
        if (pattern != phrase.getIndex())
        {
            setPattern(phrase.getIndex());
            resetPattern = true;
        }
        PatternSequencerMessage.NUM_MEASURES.send(getEngine(),
                getDeviceIndex(), length);
        if (resetBank)
            setBank(bank);
        if (resetPattern)
            setPattern(pattern);
    }

    @Override
    public void onPositionChange(IStepPhrase phrase, int position)
    {
    }

    @Override
    public void onResolutionChange(IStepPhrase phrase, Resolution resolution)
    {
    }

    @Override
    public void onTriggerDataChange(ITrigger trigger, TriggerChangeKind kind)
    {
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
