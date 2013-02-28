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

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IStepSequencer extends IPatternSequencer {

    /**
     * Will trigger a step within the phrase to selected and update that
     * trigger's values based on the new values passed.
     * 
     * @param step The step trigger to turn on.
     * @param pitch The pitch of the step.
     * @param gate The gate time (how long the step plays).
     * @param velocity The velocity (how hard the step is played).
     * @param flags The bit flag holding trigger flags.
     * @see IPhraseSignals#triggerDataChanged()
     */
    void triggerOn(int step, int pitch, float gate, float velocity, int flags);

    /**
     * Will trigger a step off (non selected) within the phrase.
     * <p>
     * This method does not update any values except for the selected property
     * of the trigger. The phrase will also notify clients through the
     * {@link IPhraseSignals#triggerDataChanged()} signal.
     * </p>
     * 
     * @param step The step trigger to turn off.
     * @param pitch The pitch of the step.
     * @see IPhraseSignals#triggerDataChanged()
     */
    void triggerOff(int step, int pitch);

    /**
     * @see #triggerOff(int, int)
     * @param step The step trigger to turn off.
     * @param pitch The pitch of the step.
     * @param remove Whether to remove the step IE monophonic synths would need
     *            to pass <code>false</code> since they only want one trigger
     *            registered as each step.
     */
    void triggerOff(int step, int pitch, boolean remove);

}
