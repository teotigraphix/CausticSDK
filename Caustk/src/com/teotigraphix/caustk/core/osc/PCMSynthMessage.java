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


public class PCMSynthMessage extends CausticMessage {

    //--------------------------------------------------------------------------
    // Volume
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/volume_out [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1.0</code> {@link ISubSynth},
     * <code>2.0</code> {@link IPCMSynth}.
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..2); (0..8 {@link IPCMSynth}) The volume
     * out value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see VolumeEnvelopeComponent#getOut()
     * @see VolumeEnvelopeComponent#setOut(float)
     */
    public static final PCMSynthMessage VOLUME_OUT = new PCMSynthMessage(
            "/caustic/${0}/volume_out ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/volume_attack [value]</code>
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
     * @see VolumeEnvelopeComponent#getAttack()
     * @see VolumeEnvelopeComponent#setAttack(float)
     */
    public static final PCMSynthMessage VOLUME_ATTACK = new PCMSynthMessage(
            "/caustic/${0}/volume_attack ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/volume_decay [value]</code>
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
     * @see VolumeEnvelopeComponent#getDecay()
     * @see VolumeEnvelopeComponent#setDecay(float)
     */
    public static final PCMSynthMessage VOLUME_DECAY = new PCMSynthMessage(
            "/caustic/${0}/volume_decay ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/volume_sustain [value]</code>
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
     * @see VolumeEnvelopeComponent#getSustain()
     * @see VolumeEnvelopeComponent#setSustain(float)
     */
    public static final PCMSynthMessage VOLUME_SUSTAIN = new PCMSynthMessage(
            "/caustic/${0}/volume_sustain ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/volume_release [value]</code>
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
     * @see VolumeEnvelopeComponent#getRelease()
     * @see VolumeEnvelopeComponent#setRelease(float)
     */
    public static final PCMSynthMessage VOLUME_RELEASE = new PCMSynthMessage(
            "/caustic/${0}/volume_release ${1}");

    //--------------------------------------------------------------------------
    // LFO
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/lfo_depth [value]</code>
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
    public static final PCMSynthMessage LFO_DEPTH = new PCMSynthMessage(
            "/caustic/${0}/lfo_depth ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_rate [value]</code>
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
    public static final PCMSynthMessage LFO_RATE = new PCMSynthMessage(
            "/caustic/${0}/lfo_rate ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_target [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see LFO1Component#getTarget()
     * @see LFO1Component#setTarget(LFOTarget)
     */
    public static final PCMSynthMessage LFO_TARGET = new PCMSynthMessage(
            "/caustic/${0}/lfo_target ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_waveform [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see LFO1Component#getWaveform()
     * @see LFO1Component#setWaveForm(Osc2WaveForm)
     */
    public static final PCMSynthMessage LFO_WAVEFORM = new PCMSynthMessage(
            "/caustic/${0}/lfo_waveform ${1}");

    //--------------------------------------------------------------------------
    // Pitch
    //--------------------------------------------------------------------------

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
     * @see PCMTunerComponent#getOctave()
     * @see PCMTunerComponent#setOctave(int)
     */
    public static final PCMSynthMessage PITCH_OCTAVE = new PCMSynthMessage(
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
     * @see PCMTunerComponent#getSemis()
     * @see PCMTunerComponent#setSemis(int)
     */
    public static final PCMSynthMessage PITCH_SEMIS = new PCMSynthMessage(
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
     * @see PCMTunerComponent#getCents()
     * @see PCMTunerComponent#setCents(int)
     */
    public static final PCMSynthMessage PITCH_CENTS = new PCMSynthMessage(
            "/caustic/${0}/pitch_cents ${1}");

    //--------------------------------------------------------------------------
    // Sampler
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/sample_index [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0-60)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see PCMSamplerComponent#getActiveIndex()
     * @see PCMSamplerComponent#setActiveIndex(int)
     */
    public static final PCMSynthMessage SAMPLE_INDEX = new PCMSynthMessage(
            "/caustic/${0}/sample_index ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_load [path]</code>
     * <p>
     * <strong>Default</strong>: <code>1.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>path</strong>: Full path to the WAV file.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     * 
     * @see PCMSamplerComponent#loadChannel(int, String)
     */
    public static final PCMSynthMessage SAMPLE_LOAD = new PCMSynthMessage(
            "/caustic/${0}/sample_load ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_level [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see PCMSamplerChannel#getLevel()
     * @see PCMSamplerChannel#setLevel(float)
     */
    public static final PCMSynthMessage SAMPLE_LEVEL = new PCMSynthMessage(
            "/caustic/${0}/sample_level ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_tune [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-50.0..50.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see PCMSamplerChannel#getTune()
     * @see PCMSamplerChannel#setTune(int)
     */
    public static final PCMSynthMessage SAMPLE_TUNE = new PCMSynthMessage(
            "/caustic/${0}/sample_tune ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_rootkey [value]</code>
     * <p>
     * <strong>Default</strong>: <code>60</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (24..108)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see PCMSamplerChannel#getRootKey()
     * @see PCMSamplerChannel#setRootKey(int)
     */
    public static final PCMSynthMessage SAMPLE_ROOTKEY = new PCMSynthMessage(
            "/caustic/${0}/sample_rootkey ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_lowkey [value]</code>
     * <p>
     * <strong>Default</strong>: <code>24</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (24..108)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see PCMSamplerChannel#getLowKey()
     * @see PCMSamplerChannel#setLowKey(int)
     */
    public static final PCMSynthMessage SAMPLE_LOWKEY = new PCMSynthMessage(
            "/caustic/${0}/sample_lowkey ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_highkey [value]</code>
     * <p>
     * <strong>Default</strong>: <code>108</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (24..108)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see PCMSamplerChannel#getHighKey()
     * @see PCMSamplerChannel#setHighKey(int)
     */
    public static final PCMSynthMessage SAMPLE_HIGHKEY = new PCMSynthMessage(
            "/caustic/${0}/sample_highkey ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_mode [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..5)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see PCMSamplerComponent.PlayMode
     * @see PCMSamplerChannel#getMode()
     * @see PCMSamplerChannel#setMode(PlayMode)
     */
    public static final PCMSynthMessage SAMPLE_MODE = new PCMSynthMessage(
            "/caustic/${0}/sample_mode ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_start [value]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: Sample start.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see PCMSamplerChannel#getStart()
     * @see PCMSamplerChannel#setStart(int)
     */
    public static final PCMSynthMessage SAMPLE_START = new PCMSynthMessage(
            "/caustic/${0}/sample_start ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_end [value]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: Sample end.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see PCMSamplerChannel#getEnd()
     * @see PCMSamplerChannel#setEnd(int)
     */
    public static final PCMSynthMessage SAMPLE_END = new PCMSynthMessage(
            "/caustic/${0}/sample_end ${1}");

    /**
     * Query: <code>/caustic/[machine_index]/[channel_num]/sample_name</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>String</code> The name of the sample
     * located at the active index number, <code>""</code> if a sample has not
     * been assigned.
     * 
     * @see PCMSamplerComponent#getSampleName(int)
     */
    public static final PCMSynthMessage QUERY_SAMPLE_NAME = new PCMSynthMessage(
            "/caustic/${0}/sample_name");

    /**
     * Query: <code>/caustic/[machine_index]/sample_indices</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>String</code> A space deliminated list of
     * index numbers that currently hold samples within the sampler.
     * 
     * @see PCMSamplerComponent#getSampleIndicies()
     */
    public static final PCMSynthMessage QUERY_SAMPLE_INDICIES = new PCMSynthMessage(
            "/caustic/${0}/sample_indices");

    PCMSynthMessage(String message) {
        super(message);
    }

}
