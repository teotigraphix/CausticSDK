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

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.teotigraphix.caustic.sequencer.IStepPhrase.Resolution;
import com.teotigraphix.caustic.sequencer.ITrigger;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class TriggerMapUtils
{

    // private static int mDefaultPitch = 60;
    //
    // private static float mDefaultGate = 1.0f;
    //
    // private static float mDefaultVelocity = 1.0f;

    private static int mBeatsInMeasure = 4;

    static void contractResolution(TriggerMap phrase, Resolution oldValue,
            Resolution value)
    {
        float large = Math.max(oldValue.getValue(), value.getValue());
        float small = Math.min(oldValue.getValue(), value.getValue());

        int span = (int) (large / small);

        Map<Integer, Map<Integer, ITrigger>> result = new TreeMap<Integer, Map<Integer, ITrigger>>();
        int i = 0;
        int count = span - 1;

        int steps = phrase.stepMap.size();

        for (int j = 0; j < steps; j++)
        {
            if (count == span - 1)
            {
                Map<Integer, ITrigger> map = phrase.stepMap.get(j);
                result.put(i, map);
                for (Entry<Integer, ITrigger> pitch : map.entrySet())
                {
                    ((Trigger) pitch.getValue()).setIndex(i);
                }
                i++;
                count = 0;
            }
            else
            {
                count++;
            }
        }
        phrase.stepMap = result;
    }

    static void expandResolution(TriggerMap phrase, Resolution oldValue,
            Resolution value)
    {

        float large = Math.max(oldValue.getValue(), value.getValue());
        float small = Math.min(oldValue.getValue(), value.getValue());

        int span = (int) ((large / small) - 1);

        Map<Integer, Map<Integer, ITrigger>> result = new TreeMap<Integer, Map<Integer, ITrigger>>();
        int i = 0;
        int count = span;

        int steps = Resolution.toSteps(value) * phrase.getLength();

        for (int j = 0; j < steps; j++)
        {
            if (count == 0)
            {
                Map<Integer, ITrigger> nmap = new TreeMap<Integer, ITrigger>();
                result.put(j, nmap);

                count = span;
                i++;
            }
            else
            {
                Map<Integer, ITrigger> map = phrase.stepMap.get(i);

                result.put(j, map);
                for (Entry<Integer, ITrigger> pitch : map.entrySet())
                {
                    ((Trigger) pitch.getValue()).setIndex(j);
                }
                count--;
            }
        }

        phrase.stepMap = result;
    }

    static void updateLength(TriggerMap phrase, int oldValue, int newValue)
    {

        if (newValue > oldValue)
        {
            int oldNumSteps = getNumStepsPerLength(phrase) * oldValue;
            int numSteps = getNumSteps(phrase);
            for (int i = oldNumSteps; i < numSteps; i++)
            {
                TreeMap<Integer, ITrigger> pitchMap = new TreeMap<Integer, ITrigger>();
                phrase.stepMap.put(i, pitchMap);
            }
        }
        else
        {
            // truncate
            // simply create a new map with extra triggers tossed
            int numSteps = getNumSteps(phrase);
            int oldNumSteps = getNumStepsPerLength(phrase) * oldValue;
            for (int i = oldNumSteps; i > numSteps - 1; i--)
            {
                // (mschmalle) when a trigger is removed triggerOff() ?
                // or not worry because the length was set and the core should
                // truncate automatically?
                phrase.stepMap.remove(i);
            }
        }
    }

    /**
     * Creates a new trigger at the specified index(step) and pitch.
     * <p>
     * Note that in ployphonic mode, the step AND pitch are needed to locate the
     * trigger since 1 or more triggers can be located on the same step with
     * different pitches.
     * </p>
     * 
     * @param index The step index of the trigger to create.
     * @param pitch The pitch of the new trigger.
     * @return A new Trigger instance.
     */
    static ITrigger createStepTrigger(TriggerMap phrase, int index, int pitch)
    {
        Trigger result = new Trigger();

        result.setIndex(index);
        result.setPitch(pitch);
        result.setPhrase(phrase);

        initTriggerData(phrase, result);

        return result;
    }

    static ITrigger removeBeatTrigger(TriggerMap phrase, float beat, int pitch)
    {
        int step = Resolution.toStep(beat, phrase.getResolution());
        return removeStepTrigger(phrase, step, pitch);
    }

    static ITrigger removeStepTrigger(TriggerMap phrase, int step, int pitch)
    {
        Map<Integer, ITrigger> map = phrase.stepMap.get(step);
        return map.remove(pitch);
    }

    static void initTriggerData(TriggerMap phrase, ITrigger trigger)
    {
        // trigger.setData(null);
    }

    static Trigger initializeNoteTrigger(TriggerMap phrase, float start, int pitch,
            float end, float velocity, int flags)
    {
        int step = Resolution.toStep(start, phrase.getResolution());
        float gate = end - start;
        return initializeStepTrigger(phrase, step, pitch, gate, velocity, flags);
    }

    /**
     * First tries to retrieve the trigger for the step and pitch, if the
     * trigger is not found, a new trigger will be created with
     * {@link #createTrigger(int, int)} using the step and pitch.
     * <p>
     * Once the trigger is created, the get and velocity are set on the new
     * trigger.
     * </p>
     * 
     * @param step The step where the trigger exists or will be created.
     * @param pitch The pitch of the trigger used to find an existing trigger,
     * or create a new trigger.
     * @param gate The gate time of the trigger in beats that is calculated from
     * the step value.
     * @param velocity The velocity of the trigger.
     * @return An existing or newly created trigger.
     */
    static Trigger initializeStepTrigger(TriggerMap phrase, int step, int pitch,
            float gate, float velocity, int flags)
    {
        // if the phrase is mono, if a trigger matches the step
        // it will be returned regardless of the pitch, since the mono is
        // just updating the pitch, gate and velocity
        Trigger trigger = (Trigger) phrase.getTriggerAtStep(step, pitch);
        if (trigger == null)
        {
            trigger = (Trigger) createStepTrigger(phrase, step, pitch);
            // try and find the <pitch,ITrigger> map located at the index
            Map<Integer, ITrigger> pitchMap = phrase.stepMap.get(step);

            // add trigger at the pitch in the pitch map
            pitchMap.put(pitch, trigger);
        }

        trigger.setGate(gate);
        trigger.setVelocity(velocity);
        trigger.setFlags(flags);
        return trigger;
    }

    public static int getNumBeats(TriggerMap phrase)
    {
        return phrase.getLength() * mBeatsInMeasure;
    }

    public static int getNumStepsPerLength(TriggerMap phrase)
    {
        return Resolution.toSteps(phrase.getResolution());
    }

    public static int getNumSteps(TriggerMap phrase)
    {
        return (phrase.getLength() * Resolution.toSteps(phrase.getResolution()));
    }

    static ITrigger addTrigger(TriggerMap phrase, Trigger trigger)
    {
        ITrigger result = initializeStepTrigger(phrase, trigger.getIndex(),
                trigger.getPitch(), trigger.getGate(), trigger.getVelocity(),
                trigger.getFlags());
        return result;
    }
}
