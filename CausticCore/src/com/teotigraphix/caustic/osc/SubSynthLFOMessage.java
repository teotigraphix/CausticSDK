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

import com.teotigraphix.caustic.core.CausticMessage;
import com.teotigraphix.caustic.filter.IBasslineLFO1.LFOTarget;
import com.teotigraphix.caustic.filter.ILFOComponent;
import com.teotigraphix.caustic.filter.ILFOComponent.WaveForm;
import com.teotigraphix.caustic.filter.ISubSynthLFO1;
import com.teotigraphix.caustic.filter.ISubSynthLFO2;

/**
 * The {@link SubSynthLFOMessage} holds all OSC messages associated with the
 * {@link ISubSynthLFO1} and {@link ISubSynthLFO2} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class SubSynthLFOMessage extends CausticMessage {

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
     * @see ILFOComponent#getDepth()
     * @see ILFOComponent#setDepth(float)
     */
    public static final SubSynthLFOMessage LFO1_DEPTH = new SubSynthLFOMessage(
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
     * @see ILFOComponent#getDepth()
     * @see ILFOComponent#setDepth(float)
     */
    public static final SubSynthLFOMessage LFO2_DEPTH = new SubSynthLFOMessage(
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
     * @see ILFOComponent#getRate()
     * @see ILFOComponent#setRate(int)
     */
    public static final SubSynthLFOMessage LFO1_RATE = new SubSynthLFOMessage(
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
     * @see ILFOComponent#getRate()
     * @see ILFOComponent#setRate(int)
     */
    public static final SubSynthLFOMessage LFO2_RATE = new SubSynthLFOMessage(
            "/caustic/${0}/lfo2_rate ${1}");

    //--------------------------------------------------------------------------
    // ISubSynthLFO1
    //--------------------------------------------------------------------------

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
     * @see ISubSynthLFO1#getTarget()
     * @see ISubSynthLFO1#setTarget(LFOTarget)
     */
    public static final SubSynthLFOMessage LFO1_TARGET = new SubSynthLFOMessage(
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
     * @see ISubSynthLFO2#getTarget()
     * @see ISubSynthLFO2#setTarget(LFOTarget)
     */
    public static final SubSynthLFOMessage LFO2_TARGET = new SubSynthLFOMessage(
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
     * @see ISubSynthLFO1#getWaveform()
     * @see ISubSynthLFO1#setWaveForm(WaveForm)
     */
    public static final SubSynthLFOMessage LFO1_WAVEFORM = new SubSynthLFOMessage(
            "/caustic/${0}/lfo1_waveform ${1}");

    SubSynthLFOMessage(String message) {
        super(message);
    }

}
