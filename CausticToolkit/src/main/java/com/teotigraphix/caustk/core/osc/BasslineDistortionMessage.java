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

import com.teotigraphix.caustk.core.components.bassline.DistortionComponent.Program;

/**
 * The {@link BasslineDistortionMessage} holds all OSC messages associated with
 * the {@link IBasslineDistortionUnit} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class BasslineDistortionMessage extends CausticMessage {

    /**
     * Message: <code>/caustic/[machine_index]/distortion_amount [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (1.0..20.0).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IBasslineDistortionUnit#getAmount()
     * @see IBasslineDistortionUnit#setAmount(float)
     */
    public static final BasslineDistortionMessage DISTORTION_AMOUNT = new BasslineDistortionMessage(
            "/caustic/${0}/distortion_amount ${1}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/distortion_postgain [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IBasslineDistortionUnit#getPostGain()
     * @see IBasslineDistortionUnit#setPostGain(float)
     */
    public static final BasslineDistortionMessage DISTORTION_POSTGAIN = new BasslineDistortionMessage(
            "/caustic/${0}/distortion_postgain ${1}");

    /**
     * Message: <code>/caustic/[machine_name]/distortion_pregain [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..5.0).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IBasslineDistortionUnit#getPreGain()
     * @see IBasslineDistortionUnit#setPreGain(float)
     */
    public static final BasslineDistortionMessage DISTORTION_PREGAIN = new BasslineDistortionMessage(
            "/caustic/${0}/distortion_pregain ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/distortion_program [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..4).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see IBasslineDistortionUnit#getProgram()
     * @see IBasslineDistortionUnit#setProgram(Program)
     */
    public static final BasslineDistortionMessage DISTORTION_PROGRAM = new BasslineDistortionMessage(
            "/caustic/${0}/distortion_program ${1}");

    BasslineDistortionMessage(String message) {
        super(message);
    }

}
