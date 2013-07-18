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

import com.teotigraphix.caustk.core.components.subsynth.Osc1Component;
import com.teotigraphix.caustk.core.components.subsynth.Osc2Component;
import com.teotigraphix.caustk.core.components.subsynth.Osc2Component.WaveForm;

/**
 * The {@link SubSynthOscMessage} holds all OSC messages associated with the
 * {@link Osc1Component} and {@link Osc2Component} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class SubSynthOscMessage extends CausticMessage {

    //--------------------------------------------------------------------------
    // Osc1Component
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/osc_bend [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * </p>
     * 
     * @see Osc1Component#getBend()
     * @see Osc1Component#setBend(float)
     */
    public static final SubSynthOscMessage OSC_BEND = new SubSynthOscMessage(
            "/caustic/${0}/osc_bend ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc1_fm [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * </p>
     * 
     * @see Osc1Component#getFM()
     * @see Osc1Component#setFM(float)
     */
    public static final SubSynthOscMessage OSC1_FM = new SubSynthOscMessage(
            "/caustic/${0}/osc1_fm ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc_mix [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.5</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * </p>
     * 
     * @see Osc1Component#getMix()
     * @see Osc1Component#setMix(float)
     */
    public static final SubSynthOscMessage OSC_MIX = new SubSynthOscMessage(
            "/caustic/${0}/osc_mix ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc1_waveform [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>; <code>SINE</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1,2,3,4,5) - SINE, SAW, TRIANGLE, SQUARE,
     * NOISE
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     * 
     * @see Osc1Component#getWaveform()
     * @see Osc1Component#setWaveform(Waveform)
     */
    public static final SubSynthOscMessage OSC1_WAVEFORM = new SubSynthOscMessage(
            "/caustic/${0}/osc1_waveform ${1}");

    //--------------------------------------------------------------------------
    // Osc2Component
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/osc2_cents [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-50..50)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     * 
     * @see Osc2Component#getCents()
     * @see Osc2Component#setCents(int)
     */
    public static final SubSynthOscMessage OSC2_CENTS = new SubSynthOscMessage(
            "/caustic/${0}/osc2_cents ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc2_octave [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-3..3)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     * 
     * @see Osc2Component#getOctave()
     * @see Osc2Component#setOctave(int)
     */
    public static final SubSynthOscMessage OSC2_OCTAVE = new SubSynthOscMessage(
            "/caustic/${0}/osc2_octave ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc2_phase [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-0.5..0.5)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * </p>
     * 
     * @see Osc2Component#getPhase()
     * @see Osc2Component#setPhase(float)
     */
    public static final SubSynthOscMessage OSC2_PHASE = new SubSynthOscMessage(
            "/caustic/${0}/osc2_phase ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc2_semis [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-12..12)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     * 
     * @see Osc2Component#getSemis()
     * @see Osc2Component#setSemis(int)
     */
    public static final SubSynthOscMessage OSC2_SEMIS = new SubSynthOscMessage(
            "/caustic/${0}/osc2_semis ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc2_waveform [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1,2,3,4,5,6) - SINE, SAW, TRIANGLE,
     * SQUARE, NOISE, CUSTOM
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     * 
     * @see Osc2Component#getWaveform()
     * @see Osc2Component#setWaveform(WaveForm)
     */
    public static final SubSynthOscMessage OSC2_WAVEFORM = new SubSynthOscMessage(
            "/caustic/${0}/osc2_waveform ${1}");

    SubSynthOscMessage(String message) {
        super(message);
    }

}
