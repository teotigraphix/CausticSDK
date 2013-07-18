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

package com.teotigraphix.caustk.core.osc;

/**
 * The {@link PitchMessage} holds all OSC messages associated with the
 * {@link IPitchTuner} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class PitchMessage extends CausticMessage {

    /**
     * Message: <code>/caustic/[machine_index]/pitch_octave [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-4..4)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see IPitchTuner#getOctave()
     * @see IPitchTuner#setOctave(int)
     */
    public static final PitchMessage PITCH_OCTAVE = new PitchMessage(
            "/caustic/${0}/pitch_octave ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/pitch_semis [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-12..12)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see IPitchTuner#getSemis()
     * @see IPitchTuner#setSemis(int)
     */
    public static final PitchMessage PITCH_SEMIS = new PitchMessage(
            "/caustic/${0}/pitch_semis ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/pitch_cents [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-50..50)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see IPitchTuner#getCents()
     * @see IPitchTuner#setCents(int)
     */
    public static final PitchMessage PITCH_CENTS = new PitchMessage(
            "/caustic/${0}/pitch_cents ${1}");

    PitchMessage(String message) {
        super(message);
    }
}
