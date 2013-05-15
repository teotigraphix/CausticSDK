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

package com.teotigraphix.caustic.sequencer;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.teotigraphix.caustic.sequencer.data.StepPhraseData;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IStepPhrase {
    // ADDED 04-26-2013

    /**
     * Adds notes to the map in a polyphonic implementation, not for use with
     * step sequencing.
     * 
     * @param pitch
     * @param start
     * @param end
     * @param velocity
     * @param flags
     */
    void addNote(int pitch, float start, float end, float velocity, int flags);

    /**
     * Removes a note from the map in a polyphonic implementation.
     * 
     * @param pitch
     * @param start
     */
    void removeNote(int pitch, float start);

    /**
     * Triggers on a note using a monophonic step sequencer implementation.
     * 
     * @param step
     * @param pitch
     * @param gate
     * @param velocity
     * @param flags
     */
    void triggerOn(int step, int pitch, float gate, float velocity, int flags);

    /**
     * Triggers off a note using a monophonic step sequencer implementatnon.
     * 
     * @param step
     * @param pitch
     */
    void triggerOff(int step, int pitch);

    IPatternSequencer getSequencer();

    void setSequencer(IPatternSequencer value);

    //----------------------------------
    // id
    //----------------------------------

    /**
     * Returns the id of the {@link IStepPhrase} which is the String
     * representation of the bank and pattern such as <code>A01</code> or
     * <code>D11</code>.
     * <p>
     * Uses the {@link PatternUtils#toString(int, int)} to encode and the
     * {@link PatternUtils#toBank(String)} and
     * {@link PatternUtils#toPattern(String)} to decode the id.
     */
    String getId();

    //----------------------------------
    // active
    //----------------------------------

    /**
     * Whether the phrase is active or not.
     * <p>
     * A phrase is considered active when it is the current phrase found within
     * the target {@link IPatternSequencer}.
     */
    boolean isActive();

    void setActive(boolean value);

    //----------------------------------
    // bank
    //----------------------------------

    /**
     * Returns the patterns bank (0-3).
     */
    int getBank();

    //----------------------------------
    // index
    //----------------------------------

    /**
     * Returns the patterns index (0-15);
     * 
     * @return
     */
    int getIndex();

    //----------------------------------
    // stringData
    //----------------------------------

    String getStringData();

    void setStringData(String data);

    //----------------------------------
    // resolution
    //----------------------------------

    /**
     * The current resolution of the steps found in the phrase.
     * <p>
     * The resolution of a phrase is how triggers are divided within the length
     * of the phrase. 16th resolution means that calls to
     * {@link #triggerOn(int, int, float, float)} will use values 0-15 for the
     * start value, 32nd would use values 0-31 for steps.
     * </p>
     * <p>
     * Dealing with steps makes writing client code much less complicated, but
     * with the resolution allows the client to access any level of detail
     * needed for the current phrase.
     * </p>
     * <p>
     * The resolution can be changed but keep in mind that having a 32nd
     * resolution and changing to 16th will loose half the trigger data and that
     * data is not recoverable.
     * </p>
     * <p>
     * Default; {@link Resolution#SIXTEENTH}.
     * </p>
     */
    Resolution getResolution();

    /**
     * @see #getResolution()
     */
    void setResolution(Resolution value);

    //----------------------------------
    // data
    //----------------------------------

    /**
     * Return data associated with the phrase.
     * 
     * @return
     */
    StepPhraseData getData();

    /**
     * @see #getData()
     */
    void setData(StepPhraseData value);

    //----------------------------------
    // length
    //----------------------------------

    /**
     * The length of the phrase in measures, currently with 4 beats per measure.
     * <p>
     * The length of the phrase is not determined by it's part's pattern. The
     * framework allows for part phrases to be less than or equal to the set
     * pattern length.
     * </p>
     * <p>
     * If the length of the pattern changes and the phrase is longer than the
     * new pattern length, the trigger data greater than the new pattern length
     * will be truncated to fit.
     * </p>
     * <p>
     * In most cases, the IPart will be responsible for managing this property.
     * This means that the part has the relationship to the pattern and even
     * though this property is public, should be managed by the part - pattern
     * contract.
     * </p>
     */
    int getLength();

    /**
     * @see #getLength()
     */
    void setLength(int value);

    //----------------------------------
    // postion
    //----------------------------------

    int getPosition();

    void setPosition(int position);

    /**
     * Returns the number of steps in the phrase this will return a real value
     * every time even if there have been no
     * {@link #triggerOn(int, int, float, float, int)} calls.
     */
    int getNumSteps();

    /**
     * Whether there are any triggers defined for the phrase using the
     * {@link #triggerOn(int, int, float, float, int)} call.
     */
    boolean hasTriggers();

    /**
     * The trigger map of step maps that hold pitch maps of triggers.
     */
    Map<Integer, Map<Integer, ITrigger>> getStepMap();

    /**
     * Clears all note data triggers from the phrase.
     */
    void clear();

    /**
     * Returns an {@link ITrigger} at the specified step, null if the step or
     * pitch is not found.
     * 
     * @param step The step for the trigger.
     * @param pitch The pitch of the trigger based on the step passed.
     */
    ITrigger getTriggerAtStep(int step, int pitch);

    /**
     * Returns an {@link ITrigger} at the specified beat, null if the step or
     * pitch is not found.
     * 
     * @param beat The beat for the trigger.
     * @param pitch The pitch of the trigger based on the beat passed.
     */
    ITrigger getTriggerAtBeat(float beat, int pitch);

    /**
     * Returns a List of all existing {@link ITrigger} instance found in the
     * {@link IStepPhrase} at the specified step.
     * 
     * @param step The int step location.
     */
    Collection<ITrigger> getTriggersAtStep(int step);

    /**
     * Returns a List of all existing {@link ITrigger} instance found in the
     * {@link IStepPhrase} at the specified beat.
     * 
     * @param beat The float beat location.
     */
    Collection<ITrigger> getTriggersAtBeat(float beat);

    /**
     * Returns a List of all existing {@link ITrigger} instance found in the
     * {@link IStepPhrase}.
     * <p>
     * The order of triggers is <em>as found</em>.
     */
    List<ITrigger> getTriggers();

    /**
     * Returns a string version of the pattern IE <strong>A04</strong>.
     * <p>
     * The String version will add 1 to the index, so pattern bank(1), index(0)
     * will return <strong>B04</strong>.
     */
    @Override
    String toString();

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

        public static float toBeat(float step, Resolution resolution) {
            return (step * resolution.getValue()) * beatsInMeasure;
        }
    }

    //--------------------------------------------------------------------------
    //
    // Listeners
    //
    //--------------------------------------------------------------------------

    void addStepPhraseListener(IStepPhraseListener value);

    void removeStepPhraseListener(IStepPhraseListener value);

    public interface IStepPhraseListener {

        /**
         * Dispatched when the phrase length has changed.
         * 
         * @param phrase The IPhrase.
         * @see IStepPhrase#setLength(int)
         */
        void onLengthChange(IStepPhrase phrase, int length);

        /**
         * Dispatched when the phrase position has changed.
         * 
         * @param phrase The IPhrase.
         * @see IStepPhrase#setPosition(int)
         */
        void onPositionChange(IStepPhrase phrase, int position);

        /**
         * Dispatched when the phrase resolution has changed.
         * 
         * @param phrase The IPhrase.
         * @see IStepPhrase#setResolution(com.teotigraphix.caustic.part.Resolution)
         */
        void onResolutionChange(IStepPhrase phrase, Resolution resolution);

        /**
         * Dispatched when some part of the trigger gets dirty.
         * 
         * @param trigger The ITrigger.
         * @param kind The TriggerChangeKind
         */
        void onTriggerDataChange(ITrigger trigger, TriggerChangeKind kind);
    }

    /**
     * The event kind for a trigger change.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum TriggerChangeKind {

        /**
         * The trigger index kind.
         */
        INDEX,

        /**
         * The trigger gate kind.
         */
        GATE,

        /**
         * The trigger pitch kind.
         */
        PITCH,

        /**
         * The trigger selected kind.
         */
        SELECTED,

        /**
         * The trigger velocity kind.
         */
        VELOCITY,

        /**
         * Signal for a full trigger reset.
         * 
         * @see IPhrase#triggerOn(int, int, float, float, int)
         * @see IPhrase#triggerOff(int, int, boolean)
         */
        RESET,

        /**
         * Signal for a trigger load if selected. This is fired on a loadState()
         * call.
         */
        LOAD;
    }

}
