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

package com.teotigraphix.caustic.osc;

/**
 * The {@link SubSynthOscMessage} holds all OSC messages associated with the
 * {@link ISubSynthOsc1} and {@link ISubSynthOsc1} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class SubSynthOscMessage extends CausticMessage
{

    //--------------------------------------------------------------------------
    // ISubSynthOsc1
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
     * @see ISubSynthOsc1#getBend()
     * @see ISubSynthOsc1#setBend(float)
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
     * @see ISubSynthOsc1#getFM()
     * @see ISubSynthOsc1#setFM(float)
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
     * @see ISubSynthOsc1#getMix()
     * @see ISubSynthOsc1#setMix(float)
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
     * @see ISubSynthOsc1#getWaveform()
     * @see ISubSynthOsc1#setWaveform(Waveform)
     */
    public static final SubSynthOscMessage OSC1_WAVEFORM = new SubSynthOscMessage(
            "/caustic/${0}/osc1_waveform ${1}");

    //--------------------------------------------------------------------------
    // ISubSynthOsc2
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
     * @see ISubSynthOsc2#getCents()
     * @see ISubSynthOsc2#setCents(int)
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
     * @see ISubSynthOsc2#getOctave()
     * @see ISubSynthOsc2#setOctave(int)
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
     * @see ISubSynthOsc2#getPhase()
     * @see ISubSynthOsc2#setPhase(float)
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
     * @see ISubSynthOsc2#getSemis()
     * @see ISubSynthOsc2#setSemis(int)
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
     * @see ISubSynthOsc2#getWaveform()
     * @see ISubSynthOsc2#setWaveform(WaveForm)
     */
    public static final SubSynthOscMessage OSC2_WAVEFORM = new SubSynthOscMessage(
            "/caustic/${0}/osc2_waveform ${1}");

    SubSynthOscMessage(String message)
    {
        super(message);
    }

}
