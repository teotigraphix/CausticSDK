////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

/**
 * The native OSC messages for the <strong>8BitSynth</strong>.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see EightBitSynthMachine
 */
public class EightBitSynthMessage extends CausticMessage {

    /**
     * Message:
     * <code>/caustic/[machine_index]/expression [index] [string_expr]</code>
     * <p>
     * <strong>Default</strong>: <code>0.5</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>index</strong>: (0,1) A, B
     * <li><strong>string_expr</strong>: The function to evaluate.
     * </ul>
     * <ul>
     * <li><strong>t</strong> - Time variable.
     * <li><strong>^</strong> - Bitwise exclusive OR (XOR). For each bit, result
     * is 0 if both sides are 1 or 0, and result is 1 otherwise.</li>
     * <li><strong>&</strong> - Bitwise AND. For each bit, set to 1 only if the
     * numbers on both sides are 1</li>
     * <li><strong>|</strong> - Bitwise OR. For each bit, set to 1 if one the
     * numbers on either sides is 1</li>
     * <li><strong>&lt;&lt;</strong> - Shift left. Shifts all bits to the left
     * by the amount specified (equivalent of multiplying by powers of 2)</li>
     * <li><strong>&gt;&gt;</strong> - Shift right. Shifts all bits to the right
     * by the amount specified (equivalent of dividing by powers of 2)</li>
     * <li><strong>+</strong> - Standard arithmetic addition.</li>
     * <li><strong>-</strong> - Standard arithmetic subtraction.</li>
     * <li><strong>*</strong> - Standard arithmetic multiplication.</li>
     * <li><strong>/</strong> - Standard arithmetic division.</li>
     * <li><strong>%</strong> - Modulo (aka remainder) part of a division</li>
     * <li><strong>( and )</strong> - Parentheses, used to force the order of
     * evaluation.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code> -1000000 if there is an
     * error in the expression.
     * </p>
     */
    public static final EightBitSynthMessage EXPRESSION = new EightBitSynthMessage(
            "/caustic/${0}/expression ${1} ${2}");

    public static final EightBitSynthMessage QUERY_EXPRESSION = new EightBitSynthMessage(
            "/caustic/${0}/expression ${1}");

    //----------------------------------
    // Controls
    //----------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/ab_blend [value]</code>
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
     */
    public static final EightBitSynthMessage AB_BLEND = new EightBitSynthMessage(
            "/caustic/${0}/ab_blend ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/octave [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-4..4)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     */
    public static final EightBitSynthMessage OCTAVE = new EightBitSynthMessage(
            "/caustic/${0}/octave ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/semis [value]</code>
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
     */
    public static final EightBitSynthMessage SEMIS = new EightBitSynthMessage(
            "/caustic/${0}/semis ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/cents [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-100..100)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     */
    public static final EightBitSynthMessage CENTS = new EightBitSynthMessage(
            "/caustic/${0}/cents ${1}");

    EightBitSynthMessage(String message) {
        super(message);
    }
}
