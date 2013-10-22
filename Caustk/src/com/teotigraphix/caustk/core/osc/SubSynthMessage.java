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

package com.teotigraphix.caustk.core.osc;

import com.teotigraphix.caustk.rack.tone.components.FilterComponentBase;
import com.teotigraphix.caustk.rack.tone.components.SynthFilterComponent;
import com.teotigraphix.caustk.rack.tone.components.SynthFilterComponent.FilterType;
import com.teotigraphix.caustk.rack.tone.components.bassline.FilterComponent;
import com.teotigraphix.caustk.rack.tone.components.bassline.OSC1Component.Waveform;
import com.teotigraphix.caustk.rack.tone.components.subsynth.Osc1Component;
import com.teotigraphix.caustk.rack.tone.components.subsynth.Osc2Component;
import com.teotigraphix.caustk.rack.tone.components.subsynth.Osc2Component.Osc2WaveForm;

public class SubSynthMessage extends CausticMessage {

    //--------------------------------------------------------------------------
    // Filter
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/filter_cutoff [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see FilterComponentBase#getCutoff()
     * @see FilterComponentBase#setCutoff(float)
     */
    public static final SubSynthMessage FILTER_CUTOFF = new SubSynthMessage(
            "/caustic/${0}/filter_cutoff ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_resonance [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see FilterComponentBase#getResonance()
     * @see FilterComponentBase#setResonance(float)
     */
    public static final SubSynthMessage FILTER_RESONANCE = new SubSynthMessage(
            "/caustic/${0}/filter_resonance ${1}");

    //--------------------------------------------------------------------------
    // IFilter
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/filter_type [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1,2,3,4,5,6) - NONE, LOW_PASS, HIGH_PASS,
     * BAND_PASS, INV_LP, INV_HP, INV_BP.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see SynthFilterComponent#getType()
     * @see SynthFilterComponent#setType(FilterType)
     */
    public static final SubSynthMessage FILTER_TYPE = new SubSynthMessage(
            "/caustic/${0}/filter_type ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_attack [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3.0625)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see SynthFilterComponent#getAttack()
     * @see SynthFilterComponent#setAttack(float)
     */
    public static final SubSynthMessage FILTER_ATTACK = new SubSynthMessage(
            "/caustic/${0}/filter_attack ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_decay [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3.0625)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see SynthFilterComponent#getDecay()
     * @see SynthFilterComponent#setDecay(float)
     */
    public static final SubSynthMessage FILTER_DECAY = new SubSynthMessage(
            "/caustic/${0}/filter_decay ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_sustain [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see SynthFilterComponent#getSustain()
     * @see SynthFilterComponent#setSustain(float)
     */
    public static final SubSynthMessage FILTER_SUSTAIN = new SubSynthMessage(
            "/caustic/${0}/filter_sustain ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_release [value]</code>
     * <p>
     * <strong>Default</strong>: <code>3.0625</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3.0625)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see SynthFilterComponent#getDecay()
     * @see SynthFilterComponent#setDecay(float)
     */
    public static final SubSynthMessage FILTER_RELEASE = new SubSynthMessage(
            "/caustic/${0}/filter_release ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_kbtrack [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see SynthFilterComponent#getTrack()
     * @see SynthFilterComponent#setTrack(float)
     */
    public static final SubSynthMessage FILTER_KBTRACK = new SubSynthMessage(
            "/caustic/${0}/filter_kbtrack ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_envmod [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.99</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see FilterComponent#getEnvMod()
     * @see FilterComponent#setEnvMod()
     */
    public static final SubSynthMessage FILTER_ENVMOD = new SubSynthMessage(
            "/caustic/${0}/filter_envmod ${1}");

    //--------------------------------------------------------------------------
    // LFO
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/lfo1_depth [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see LFO1Component#getDepth()
     * @see LFO1Component#setDepth(float)
     */
    public static final SubSynthMessage LFO1_DEPTH = new SubSynthMessage(
            "/caustic/${0}/lfo1_depth ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo2_depth [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see LFO1Component#getDepth()
     * @see LFO1Component#setDepth(float)
     */
    public static final SubSynthMessage LFO2_DEPTH = new SubSynthMessage(
            "/caustic/${0}/lfo2_depth ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo1_rate [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (1..12)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see LFO1Component#getRate()
     * @see LFO1Component#setRate(int)
     */
    public static final SubSynthMessage LFO1_RATE = new SubSynthMessage(
            "/caustic/${0}/lfo1_rate ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo2_rate [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (1..12)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see LFO1Component#getRate()
     * @see LFO1Component#setRate(int)
     */
    public static final SubSynthMessage LFO2_RATE = new SubSynthMessage(
            "/caustic/${0}/lfo2_rate ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo1_target [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..6)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     * 
     * @see LFO1Component#getTarget()
     * @see LFO1Component#setTarget(LFOTarget)
     */
    public static final SubSynthMessage LFO1_TARGET = new SubSynthMessage(
            "/caustic/${0}/lfo1_target ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo2_target [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..6)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     * 
     * @see LFO2Component#getTarget()
     * @see LFO2Component#setTarget(LFOTarget)
     */
    public static final SubSynthMessage LFO2_TARGET = new SubSynthMessage(
            "/caustic/${0}/lfo2_target ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo1_waveform [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     * 
     * @see LFO2Component#getWaveform()
     * @see LFO2Component#setWaveForm(Osc2WaveForm)
     */
    public static final SubSynthMessage LFO1_WAVEFORM = new SubSynthMessage(
            "/caustic/${0}/lfo1_waveform ${1}");

    //--------------------------------------------------------------------------
    // Osc
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
    public static final SubSynthMessage OSC_BEND = new SubSynthMessage(
            "/caustic/${0}/osc_bend ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc_modulation [value]</code>
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
     * @see Osc1Component#getModulation()
     * @see Osc1Component#setModulation(float)
     */
    public static final SubSynthMessage OSC1_MODULATION = new SubSynthMessage(
            "/caustic/${0}/osc_modulation ${1}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/osc_modulation_mode [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1,2) FM, PM, AM
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * </p>
     */
    public static final SubSynthMessage OSC1_MODULATION_MODE = new SubSynthMessage(
            "/caustic/${0}/osc_modulation_mode ${1}");

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
    public static final SubSynthMessage OSC_MIX = new SubSynthMessage("/caustic/${0}/osc_mix ${1}");

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
    public static final SubSynthMessage OSC1_WAVEFORM = new SubSynthMessage(
            "/caustic/${0}/osc1_waveform ${1}");

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
    public static final SubSynthMessage OSC2_CENTS = new SubSynthMessage(
            "/caustic/${0}/osc2_cents ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc2_cents_mode [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1) Cents, Unison
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     */
    public static final SubSynthMessage OSC2_CENTS_MODE = new SubSynthMessage(
            "/caustic/${0}/osc2_cents_mode ${1}");

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
    public static final SubSynthMessage OSC2_OCTAVE = new SubSynthMessage(
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
    public static final SubSynthMessage OSC2_PHASE = new SubSynthMessage(
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
    public static final SubSynthMessage OSC2_SEMIS = new SubSynthMessage(
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
     * @see Osc2Component#setWaveform(Osc2WaveForm)
     */
    public static final SubSynthMessage OSC2_WAVEFORM = new SubSynthMessage(
            "/caustic/${0}/osc2_waveform ${1}");

    SubSynthMessage(String message) {
        super(message);
    }
}
