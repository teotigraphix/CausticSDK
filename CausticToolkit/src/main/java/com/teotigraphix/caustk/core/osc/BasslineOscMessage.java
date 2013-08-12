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

import com.teotigraphix.caustk.tone.bassline.OSC1Component;

/**
 * The {@link BasslineOscMessage} holds all OSC messages associated with the
 * {@link OSC1Component} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class BasslineOscMessage extends CausticMessage {

    /**
     * Message: <code>/caustic/[machine_index]/waveform [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1) SAW(<code>0</code>), SQUARE(
     * <code>1</code>).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     * 
     * @see OSC1Component#getWaveform()
     * @see OSC1Component#setWaveForm(Waveform)
     */
    public static final BasslineOscMessage WAVEFORM = new BasslineOscMessage(
            "/caustic/${0}/waveform ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/accent [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.5</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * </p>
     * 
     * @see OSC1Component#getAccent()
     * @see OSC1Component#setAccent(float)
     */
    public static final BasslineOscMessage ACCENT = new BasslineOscMessage(
            "/caustic/${0}/accent ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/tune [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-12..12)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     * 
     * @see OSC1Component#getTune()
     * @see OSC1Component#setTune(int)
     */
    public static final BasslineOscMessage TUNE = new BasslineOscMessage("/caustic/${0}/tune ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/pulse_width [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.5</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.05..0.5)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * </p>
     * 
     * @see OSC1Component#getPulseWidth()
     * @see OSC1Component#setPulseWidth(float)
     */
    public static final BasslineOscMessage PULSE_WIDTH = new BasslineOscMessage(
            "/caustic/${0}/pulse_width ${1}");

    BasslineOscMessage(String message) {
        super(message);
    }
}
