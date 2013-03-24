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

package com.teotigraphix.caustic.osc;

import com.teotigraphix.caustic.core.CausticMessage;
import com.teotigraphix.caustic.sampler.IBeatboxSampler;
import com.teotigraphix.caustic.sampler.IBeatboxSamplerChannel;

/**
 * The {@link BeatboxSamplerMessage} holds all OSC messages associated with the
 * {@link IBeatboxSampler} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class BeatboxSamplerMessage extends CausticMessage
{

    /**
     * Message:
     * <code>/caustic/[machine_index]/channel/[channel_num]/load [path]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>channel_num</strong>: The channel number in the beatbox.</li>
     * <li><strong>path</strong>: The absolute path to the WAV sample file.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     * 
     * @see IBeatboxSampler#loadChannel(int, String)
     */
    public static final PCMSamplerMessage CHANNEL_LOAD = new PCMSamplerMessage(
            "/caustic/${0}/channel/${1}/load ${2}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/channel/[channel_num]/tune [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>channel_num</strong>: The channel number in the beatbox.</li>
     * <li><strong>value</strong>: (-6..6)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IBeatboxSamplerChannel#getTune()
     * @see IBeatboxSamplerChannel#setTune(float)
     */
    public static final PCMSamplerMessage CHANNEL_TUNE = new PCMSamplerMessage(
            "/caustic/${0}/channel/${1}/tune ${2}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/channel/[channel_num]/mute [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>channel_num</strong>: The channel number in the beatbox.</li>
     * <li><strong>value</strong>: (0,1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see IBeatboxSamplerChannel#isMute()
     * @see IBeatboxSamplerChannel#setMute(boolean)
     */
    public static final PCMSamplerMessage CHANNEL_MUTE = new PCMSamplerMessage(
            "/caustic/${0}/channel/${1}/mute ${2}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/channel/[channel_num]/solo [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>channel_num</strong>: The channel number in the beatbox.</li>
     * <li><strong>value</strong>: (0,1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see IBeatboxSamplerChannel#isSolo()
     * @see IBeatboxSamplerChannel#setSolo(boolean)
     */
    public static final PCMSamplerMessage CHANNEL_SOLO = new PCMSamplerMessage(
            "/caustic/${0}/channel/${1}/solo ${2}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/channel/[channel_num]/punch [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>channel_num</strong>: The channel number in the beatbox.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IBeatboxSamplerChannel#getPunch()
     * @see IBeatboxSamplerChannel#setPunch(float)
     */
    public static final PCMSamplerMessage CHANNEL_PUNCH = new PCMSamplerMessage(
            "/caustic/${0}/channel/${1}/punch ${2}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/channel/[channel_num]/decay [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>channel_num</strong>: The channel number in the beatbox.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IBeatboxSamplerChannel#getDecay()
     * @see IBeatboxSamplerChannel#setDecay(float)
     */
    public static final PCMSamplerMessage CHANNEL_DECAY = new PCMSamplerMessage(
            "/caustic/${0}/channel/${1}/decay ${2}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/channel/[channel_num]/pan [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>channel_num</strong>: The channel number in the beatbox.</li>
     * <li><strong>value</strong>: (-1.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IBeatboxSamplerChannel#getPan()
     * @see IBeatboxSamplerChannel#setPan(float)
     */
    public static final PCMSamplerMessage CHANNEL_PAN = new PCMSamplerMessage(
            "/caustic/${0}/channel/${1}/pan ${2}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/channel/[channel_num]/volume [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>channel_num</strong>: The channel number in the beatbox.</li>
     * <li><strong>value</strong>: (0.0..2.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IBeatboxSamplerChannel#getPan()
     * @see IBeatboxSamplerChannel#setPan(float)
     */
    public static final PCMSamplerMessage CHANNEL_VOLUME = new PCMSamplerMessage(
            "/caustic/${0}/channel/${1}/volume ${2}");

    /**
     * Query:
     * <code>/caustic/[machine_index]/channel/[channel_num]/sample_name</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>channel_num</strong>: The channel number in the beatbox.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>String</code> The name of the sample
     * located at the channel number, <code>""</code> if a sample has not been
     * assigned.
     * 
     * @see IBeatboxSamplerChannel#getName()
     */
    public static final PCMSamplerMessage QUERY_CHANNEL_SAMPLE_NAME = new PCMSamplerMessage(
            "/caustic/${0}/channel/${1}/sample_name");

    BeatboxSamplerMessage(String message)
    {
        super(message);
    }
}
