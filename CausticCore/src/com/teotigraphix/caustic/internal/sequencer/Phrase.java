////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.teotigraphix.caustic.internal.util.PatternUtils;
import com.teotigraphix.caustic.sequencer.IPatternSequencer;
import com.teotigraphix.caustic.sequencer.IPhrase;
import com.teotigraphix.caustic.sequencer.ITrigger;
import com.teotigraphix.caustic.sequencer.PatternMeasures;
import com.teotigraphix.caustic.sequencer.data.PhraseData;
import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.utils.MementoUtil;

/*

 This class seems like its doing way to freaking much!

 What are the main aspects that are core to a phrase (now without the IPart dep)

 - represents a map of triggers
 => Polyphonic - only tirggers of same beat and pitch replace eachother
 => Monophonic - triggers of same beat always replace eachother

 The triggerMap
 - Map<Step, Map<Pitch, ITrigger>>

 */

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class Phrase implements IPhrase
{

    private static final String ATT_ACTIVE = "active";

    private static final String ATT_INDEX = "index";

    private static final String ATT_BANK = "bank";

    private static final String ATT_RESOLUTION = "resolution";

    private static final String ATT_LENGTH = "length";

    private static final String TAG_DATA = "data";

    private static final String TAG_TRIGGER = "trigger";

    private boolean mHasTriggers = false;

    // Map<Step, Map<Pitch, ITrigger>>
    Map<Integer, Map<Integer, ITrigger>> mStepMap;

    //----------------------------------
    // sequencer
    //----------------------------------

    private IPatternSequencer mSequencer;

    @Override
    public IPatternSequencer getSequencer()
    {
        return mSequencer;
    }

    @Override
    public void setSequencer(IPatternSequencer value)
    {
        mSequencer = value;
    }

    //----------------------------------
    // active
    //----------------------------------

    @Override
    public String getId()
    {
        return PatternUtils.toString(mBank, mIndex);
    }

    //----------------------------------
    // active
    //----------------------------------

    private boolean mActive;

    @Override
    public final boolean isActive()
    {
        return mActive;
    }

    @Override
    public final void setActive(boolean value)
    {
        mActive = value;
    }

    //----------------------------------
    // bank
    //----------------------------------

    private int mBank;

    @Override
    public final int getBank()
    {
        return mBank;
    }

    //----------------------------------
    // index
    //----------------------------------

    private int mIndex;

    @Override
    public final int getIndex()
    {
        return mIndex;
    }

    //----------------------------------
    // stringData
    //----------------------------------

    private String mStringData;

    @Override
    public final String getStringData()
    {
        return mStringData;
    }

    public final void setStringData(String value)
    {
        mStringData = value;
        if (mStringData == null)
            return;
        initializeData(mStringData);
    }

    private void initializeData(String data)
    {
        // push the notes into the machines sequencer
        String[] notes = data.split("\\|");
        for (String noteData : notes)
        {
            String[] split = noteData.split(" ");

            float start = Float.valueOf(split[0]);
            int pitch = Float.valueOf(split[1]).intValue();
            float velocity = Float.valueOf(split[2]);
            float end = Float.valueOf(split[3]);
            float gate = end - start;
            int flags = Float.valueOf(split[4]).intValue();
            int step = Resolution.toStep(start, getResolution());

            triggerOn(step, pitch, gate, velocity, flags);
        }
    }

    //----------------------------------
    // resolution
    //----------------------------------

    private Resolution mResolution = Resolution.SIXTEENTH;

    @Override
    public Resolution getResolution()
    {
        return mResolution;
    }

    @Override
    public void setResolution(Resolution value)
    {
        if (value == mResolution)
            return;

        Resolution oldValue = mResolution;
        mResolution = value;
        if (value.getValue() < oldValue.getValue())
        {
            PhraseUtils.expandResolution(this, oldValue, value);
        }
        else
        {
            PhraseUtils.contractResolution(this, oldValue, value);
        }

        fireResolutionChange(mResolution);
    }

    //----------------------------------
    // length
    //----------------------------------

    private int mLength = 1;

    @Override
    public int getLength()
    {
        return mLength;
    }

    @Override
    public void setLength(int value)
    {
        if (value == mLength)
            return;

        if (!PatternMeasures.isValid(value))
        {
            return;
        }

        int oldValue = mLength;
        mLength = value;

        // the value has changed so the trigger list needs to
        // be truncated or extended
        // extension dosn't affect the data but truncation erases
        // existing trigger data
        PhraseUtils.updateLength(this, oldValue, value);

        fireLengthChange(mLength);
    }

    //----------------------------------
    // position
    //----------------------------------

    private int mPosition = 0;

    @Override
    public int getPosition()
    {
        return mPosition;
    }

    // (mschmalle) This might be deprecated now since it could just be passed
    // to getViewTriggers() and everything would be calculated
    // based on the resolution, this way the client is in total control
    // of the result with on method call
    @Override
    public void setPosition(int value)
    {
        if (value == mPosition)
            return;

        // the position can only be at the most one less than the current length
        // times the resolution
        // value = Math.max(0, Math.min(value, mLength - 1));

        mPosition = value;

        firePositionChange(mPosition);
    }

    //----------------------------------
    // numSteps
    //----------------------------------

    @Override
    public int getNumSteps()
    {
        return mStepMap.size();
    }

    //----------------------------------
    // stepMap
    //----------------------------------

    @Override
    public Map<Integer, Map<Integer, ITrigger>> getStepMap()
    {
        return Collections.unmodifiableMap(mStepMap);
    }

    @Override
    public boolean hasTriggers()
    {
        return mHasTriggers;
    }

    //----------------------------------
    // data
    //----------------------------------

    private PhraseData mData;

    @Override
    public PhraseData getData()
    {
        return mData;
    }

    @Override
    public void setData(PhraseData value)
    {
        mData = value;
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    public Phrase(int bank, int index)
    {
        mStepMap = new TreeMap<Integer, Map<Integer, ITrigger>>();

        mBank = bank;
        mIndex = index;

        clear();
    }

    //--------------------------------------------------------------------------
    //
    // IPhrase API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public final ITrigger getTriggerAtStep(int step, int pitch)
    {
        Map<Integer, ITrigger> triggers = mStepMap.get(step);
        Trigger trigger = null;
        try
        {
            trigger = (Trigger) triggers.get(pitch);
        }
        catch (Exception e)
        {
            //XXX  Log.e("Phrase", "no triggers at step " + step + ":" + pitch);
        }

        return trigger;
    }

    @Override
    public final ITrigger getTriggerAtBeat(float beat, int pitch)
    {
        int step = Resolution.toStep(beat, getResolution());
        return getTriggerAtStep(step, pitch);
    }

    @Override
    public Collection<ITrigger> getTriggersAtStep(int step)
    {
        Map<Integer, ITrigger> triggers = mStepMap.get(step);
        return triggers.values();
    }

    @Override
    public Collection<ITrigger> getTriggersAtBeat(float beat)
    {
        int step = Resolution.toStep(beat, getResolution());
        return getTriggersAtStep(step);
    }

    @Override
    public Collection<ITrigger> getTriggers()
    {
        ArrayList<ITrigger> result = new ArrayList<ITrigger>();
        for (Map<Integer, ITrigger> map : mStepMap.values())
        {
            for (ITrigger trigger : map.values())
            {
                result.add(trigger);
            }
        }
        return result;
    }

    @Override
    public void clear()
    {
        mStepMap.clear();
        mHasTriggers = false;

        int numSteps = PhraseUtils.getNumSteps(this);
        for (int i = 0; i < numSteps; i++)
        {
            // a TreeMap will sort the keys numerically
            mStepMap.put(i, new TreeMap<Integer, ITrigger>());
        }
    }

    void addNote(int pitch, float start, float end, float velocity, int flags)
    {
        mHasTriggers = true;

        Trigger trigger = PhraseUtils.initializeNoteTrigger(this, start, pitch,
                end, velocity, flags);
        // make the trigger selected (on)
        trigger.setSelected(true);

        fireTriggerDataChange(trigger, TriggerChangeKind.RESET);
    }

    void removeNote(int pitch, float start)
    {
        Trigger trigger = (Trigger) getTriggerAtBeat(start, pitch);
        if (trigger == null)
            return;

        trigger.setSelected(false);
        PhraseUtils.removeBeatTrigger(this, start, pitch);

        fireTriggerDataChange(trigger, TriggerChangeKind.RESET);
    }

    void triggerOn(int step, int pitch, float gate, float velocity, int flags)
    {
        mHasTriggers = true;

        // either create or return an existing trigger for the step and pitch
        // if returning an existing trigger, the method will have updated
        // the gate and velocity
        Trigger trigger = PhraseUtils.initializeStepTrigger(this, step, pitch,
                gate, velocity, flags);
        // make the trigger selected (on)
        trigger.setSelected(true);

        fireTriggerDataChange(trigger, TriggerChangeKind.RESET);
    }

    void triggerOff(int step, int pitch)
    {
        triggerOff(step, pitch, false);
    }

    void triggerOff(int step, int pitch, boolean polyphonic)
    {
        Trigger trigger = (Trigger) getTriggerAtStep(step, pitch);
        if (trigger == null)
            return;

        // if the phrase is polyphonic, remove the trigger since we are
        // not in the traditional step sequencer implementation, if in mono
        // we are in step sequencer and need to keep the trigger around
        // because the step key was just toggled off, but could be toggled
        // back on (selected) using this existing trigger and it's data
        if (polyphonic)
        {
            trigger.setSelected(false);
            PhraseUtils.removeStepTrigger(this, step, pitch);
        }
        else
        {
            trigger.setSelected(false);
        }

        fireTriggerDataChange(trigger, TriggerChangeKind.RESET);
    }

    //--------------------------------------------------------------------------
    //
    // IPersist API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento)
    {
        if (mData != null)
        {
            mData.copy(memento.createChild(TAG_DATA));
        }

        memento.putInteger(ATT_LENGTH, getLength());
        memento.putString(ATT_RESOLUTION, getResolution().toString());
        memento.putInteger(ATT_BANK, getBank());
        memento.putInteger(ATT_INDEX, getIndex());
        memento.putInteger(ATT_ACTIVE, MementoUtil.booleanToInt(isActive()));

        for (Map<Integer, ITrigger> pitchMap : getStepMap().values())
        {
            for (ITrigger trigger : pitchMap.values())
            {
                trigger.copy(memento.createChild(TAG_TRIGGER));
            }
        }
    }

    @Override
    public void paste(IMemento memento)
    {
        if (mData != null)
        {
            mData.paste(memento.getChild(TAG_DATA));
        }

        mBank = memento.getInteger(ATT_BANK);
        mIndex = memento.getInteger(ATT_INDEX);

        setLength(memento.getInteger(ATT_LENGTH));
        setResolution(Resolution.valueOf(memento.getString(ATT_RESOLUTION)));
        setActive(MementoUtil.intToBoolean(memento.getInteger(ATT_ACTIVE)));

        IMemento[] triggers = memento.getChildren(TAG_TRIGGER);
        for (int i = 0; i < triggers.length; i++)
        {
            mHasTriggers = true;

            IMemento trigger = triggers[i];

            Trigger result = new Trigger();
            result.setPhrase(this);
            result.paste(trigger);

            ITrigger extisting = PhraseUtils.addTrigger(this, result);
            // since we are pasting NOT restoring, the parent sequencer
            // needs to send note_data messages, where to do this?
            fireTriggerDataChange(extisting, TriggerChangeKind.LOAD);
        }
    }

    @Override
    public void restore()
    {
        // the length has to be restored but we don't know what the
        // resolution was in a restore, any ideas? could scan the trigger
        // distances to find the smallest value but that is not full proof
        // either
        // setLength(getLength(true));
    }

    //--------------------------------------------------------------------------
    //
    // Listeners :: Methods
    //
    //--------------------------------------------------------------------------

    private final List<IPhraseListener> mPhraseListeners = new ArrayList<IPhraseListener>();

    @Override
    public final void addPhraseListener(IPhraseListener value)
    {
        if (mPhraseListeners.contains(value))
            return;
        mPhraseListeners.add(value);
    }

    @Override
    public final void removePhraseListener(IPhraseListener value)
    {
        if (!mPhraseListeners.contains(value))
            return;
        mPhraseListeners.remove(value);
    }

    @Override
    public String toString()
    {
        return PatternUtils.toString(mBank, mIndex);
    }

    //--------------------------------------------------------------------------
    //
    // Protected :: Methods
    //
    //--------------------------------------------------------------------------

    protected final void fireLengthChange(int length)
    {
        for (IPhraseListener listener : mPhraseListeners)
        {
            listener.onLengthChange(this, length);
        }
    }

    protected final void firePositionChange(int position)
    {
        for (IPhraseListener listener : mPhraseListeners)
        {
            listener.onPositionChange(this, position);
        }
    }

    protected final void fireResolutionChange(Resolution resolution)
    {
        for (IPhraseListener listener : mPhraseListeners)
        {
            listener.onResolutionChange(this, resolution);
        }
    }

    protected final void fireTriggerDataChange(ITrigger trigger,
            TriggerChangeKind kind)
    {
        for (IPhraseListener listener : mPhraseListeners)
        {
            listener.onTriggerDataChange(trigger, kind);
        }
    }

    //--------------------------------------------------------------------------
    //
    // Protected :: Methods
    //
    //--------------------------------------------------------------------------

    private Map<Float, KeyFrame> keyframes = new HashMap<Float, KeyFrame>();

    // the automation is saved in the Pattern
    // is it save in a Part?

    // Before I go any farther the ONLY way this is going to happen is if there
    // is someway to calculate fractional beats inbetween the onBeatChanged()
    // event from the core.

    // 1. save the time at the beat changed event
    // 2. start a handler looping in the background at the calculated keyframe
    // interval
    // 3. on each interval save the last unique OSC messages to the phrase
    // automation
    // 4. clear the stack at the end of the interval

    // !!! The above would at the same time be playing keyframes(OSC messages)
    // found at the
    // current beat of the handler. This is going to take some major testing
    // to get right but at least the thought has started

    // * Anything that is "replayed" is a straight getEngine().sendMessage()
    // call
    // due to performance and that we don't want those messages rerecorded

    // How to deal with messages that have machine ids?
    // What if the machine id/index is defferent when the Pattern is loaded into
    // something else?
    // - "/caustic/${0}/filter_cutoff ${1}"

    // 0:cutoff:0.3|0:resonance:1.3
    // or
    // filter_cutoff:0.3|filter_resonance:1.3
    // /caustic/[machine_index]/filter_cutoff 0.3
    public void putKeyFrame(float beat, String key, float value)
    {
        keyframes.put(beat, new KeyFrame(beat, key, value));
    }

    public boolean hasKeyFrame(float beat, String key)
    {
        return keyframes.containsKey(beat);
    }

    public float getKeyFrameFloat(float beat, String key)
    {
        if (!keyframes.containsKey(beat))
            return Float.NaN;
        // KeyFrame frame = keyframes.get(beat);

        return keyframes.get(beat).getValue();
    }

    public static class KeyFrame
    {

        private final float mBeat;

        private final float mValue;

        private final String mKey;

        public final float getBeat()
        {
            return mBeat;
        }

        public final float getValue()
        {
            return mValue;
        }

        public final String getKey()
        {
            return mKey;
        }

        public KeyFrame(float beat, String key, float value)
        {
            mBeat = beat;
            mKey = key;
            mValue = value;
        }

    }

}
