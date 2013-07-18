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

import com.teotigraphix.caustk.core.components.pcmsynth.PCMSamplerChannel;
import com.teotigraphix.caustk.core.components.pcmsynth.PCMSamplerComponent;
import com.teotigraphix.caustk.core.components.pcmsynth.PCMSamplerComponent.PlayMode;

/**
 * The {@link PCMSamplerMessage} holds all OSC messages associated with the
 * {@link IPCMSynth} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class PCMSamplerMessage extends CausticMessage {

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
    public static final PCMSamplerMessage SAMPLE_INDEX = new PCMSamplerMessage(
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
    public static final PCMSamplerMessage SAMPLE_LOAD = new PCMSamplerMessage(
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
    public static final PCMSamplerMessage SAMPLE_LEVEL = new PCMSamplerMessage(
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
    public static final PCMSamplerMessage SAMPLE_TUNE = new PCMSamplerMessage(
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
    public static final PCMSamplerMessage SAMPLE_ROOTKEY = new PCMSamplerMessage(
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
    public static final PCMSamplerMessage SAMPLE_LOWKEY = new PCMSamplerMessage(
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
    public static final PCMSamplerMessage SAMPLE_HIGHKEY = new PCMSamplerMessage(
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
    public static final PCMSamplerMessage SAMPLE_MODE = new PCMSamplerMessage(
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
    public static final PCMSamplerMessage SAMPLE_START = new PCMSamplerMessage(
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
    public static final PCMSamplerMessage SAMPLE_END = new PCMSamplerMessage(
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
    public static final PCMSamplerMessage QUERY_SAMPLE_NAME = new PCMSamplerMessage(
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
    public static final PCMSamplerMessage QUERY_SAMPLE_INDICIES = new PCMSamplerMessage(
            "/caustic/${0}/sample_indices");

    PCMSamplerMessage(String message) {
        super(message);
    }
}
