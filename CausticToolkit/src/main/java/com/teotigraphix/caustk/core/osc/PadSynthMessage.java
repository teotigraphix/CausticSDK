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

package com.teotigraphix.caustk.core.osc;

import com.teotigraphix.caustk.tone.PadSynthTone;

/**
 * The {@link PadSynthMessage} holds all OSC messages associated with the
 * {@link PadSynthTone} API.
 * 
 * @author Michael Schmalle
 * @since 3.0
 */
public class PadSynthMessage extends CausticMessage {

    /**
     * Message:
     * <code>/caustic/[machine_index]/harmonics [table_index] [index] [amplitude]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>table_index</strong>: (0,1)</li>
     * <li><strong>index</strong>: (0..23)</li>
     * <li><strong>amplitude</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     */
    public static final PadSynthMessage HARMONICS = new PadSynthMessage(
            "/caustic/${0}/harmonics ${1} ${2} ${3}");

    /**
     * Query:
     * <code>/caustic/[machine_index]/harmonics [table_index] [index] [amplitude]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>table_index</strong>: (0,1)</li>
     * <li><strong>index</strong>: (0..23)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage QUERY_HARMONICS = new PadSynthMessage(
            "/caustic/${0}/harmonics ${1} ${2}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/harmonics [table_index] width [amplitude]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>table_index</strong>: (0,1)</li>
     * <li><strong>amplitude</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     */
    public static final PadSynthMessage WIDTH = new PadSynthMessage(
            "/caustic/${0}/harmonics ${1} width ${2}");

    /**
     * Query:
     * <code>/caustic/[machine_index]/harmonics [table_index] width</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>table_index</strong>: (0,1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage QUERY_WIDTH = new PadSynthMessage(
            "/caustic/${0}/harmonics ${1} width");

    /**
     * Message: <code>/caustic/[machine_index]/lfo1_target [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3) Off, Morph, Pitch, Volume</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final PadSynthMessage LFO1_TARGET = new PadSynthMessage(
            "/caustic/${0}/lfo1_target ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo1_rate [value]</code>
     * <p>
     * <strong>Default</strong>: <code>6</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..12)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final PadSynthMessage LFO1_RATE = new PadSynthMessage(
            "/caustic/${0}/lfo1_rate ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo1_depth [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage LFO1_DEPTH = new PadSynthMessage(
            "/caustic/${0}/lfo1_depth ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo1_phase [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage LFO1_PHASE = new PadSynthMessage(
            "/caustic/${0}/lfo1_phase ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo2_target [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3) Off, Morph, Pitch, Volume</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final PadSynthMessage LFO2_TARGET = new PadSynthMessage(
            "/caustic/${0}/lfo2_target ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo2_rate [value]</code>
     * <p>
     * <strong>Default</strong>: <code>6</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..12)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final PadSynthMessage LFO2_RATE = new PadSynthMessage(
            "/caustic/${0}/lfo2_rate ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo2_depth [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage LFO2_DEPTH = new PadSynthMessage(
            "/caustic/${0}/lfo2_depth ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo2_phase [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage LFO2_PHASE = new PadSynthMessage(
            "/caustic/${0}/lfo2_phase ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/morph [value]</code>
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage MORPH = new PadSynthMessage("/caustic/${0}/morph ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/morph_env [value]</code>
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final PadSynthMessage MORPH_ENV = new PadSynthMessage(
            "/caustic/${0}/morph_env ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/morph_attack [value]</code>
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage MORPH_ATTACK = new PadSynthMessage(
            "/caustic/${0}/morph_attack ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/morph_decay [value]</code>
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage MORPH_DECAY = new PadSynthMessage(
            "/caustic/${0}/morph_decay ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/morph_sustain [value]</code>
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage MORPH_SUSTAIN = new PadSynthMessage(
            "/caustic/${0}/morph_sustain ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/morph_release [value]</code>
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage MORPH_RELEASE = new PadSynthMessage(
            "/caustic/${0}/morph_release ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/gain1 [value]</code>
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage GAIN1 = new PadSynthMessage("/caustic/${0}/gain1 ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/gain2 [value]</code>
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage GAIN2 = new PadSynthMessage("/caustic/${0}/gain2 ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/volume_attack [value]</code>
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage VOLUME_ATTACK = new PadSynthMessage(
            "/caustic/${0}/volume_attack ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/volume_decay [value]</code>
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage VOLUME_DECAY = new PadSynthMessage(
            "/caustic/${0}/volume_decay ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/volume_sustain [value]</code>
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage VOLUME_SUSTAIN = new PadSynthMessage(
            "/caustic/${0}/volume_sustain ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/volume_release [value]</code>
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage VOLUME_RELEASE = new PadSynthMessage(
            "/caustic/${0}/volume_release ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/volume_out [value]</code>
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..2)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PadSynthMessage VOLUME_OUT = new PadSynthMessage(
            "/caustic/${0}/volume_out ${1}");

    PadSynthMessage(String message) {
        super(message);
    }

}
