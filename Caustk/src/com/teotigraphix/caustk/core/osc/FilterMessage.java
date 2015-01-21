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

import com.teotigraphix.caustk.node.machine.patch.SynthFilterComponent;

/**
 * The {@link FilterMessage} holds all OSC messages associated with the
 * {@link FilterComponentBase} and {@link SynthFilterComponent} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class FilterMessage extends CausticMessage {

    //--------------------------------------------------------------------------
    // IFilterComponent
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
     */
    public static final FilterMessage FILTER_CUTOFF = new FilterMessage(
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
     */
    public static final FilterMessage FILTER_RESONANCE = new FilterMessage(
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
     */
    public static final FilterMessage FILTER_TYPE = new FilterMessage(
            "/caustic/${0}/filter_type ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_attack [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1.75)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final FilterMessage FILTER_ATTACK = new FilterMessage(
            "/caustic/${0}/filter_attack ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_decay [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1.75)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final FilterMessage FILTER_DECAY = new FilterMessage(
            "/caustic/${0}/filter_decay ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_sustain [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1.75)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final FilterMessage FILTER_SUSTAIN = new FilterMessage(
            "/caustic/${0}/filter_sustain ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_release [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1.75</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1.75)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final FilterMessage FILTER_RELEASE = new FilterMessage(
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
     */
    public static final FilterMessage FILTER_KBTRACK = new FilterMessage(
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
     */
    public static final FilterMessage FILTER_ENVMOD = new FilterMessage(
            "/caustic/${0}/filter_envmod ${1}");

    FilterMessage(String message) {
        super(message);
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     */
    public enum FilterType {

        /**
         * No filter applied.
         */
        None(0),

        /**
         * The low pass filter.
         */
        LowPass(1),

        /**
         * The high pass filter.
         */
        HighPass(2),

        /**
         * The band pass filter.
         */
        BandPass(3),

        /**
         * The inverted low pass filter.
         */
        InvLP(4),

        /**
         * The inverted high pass filter.
         */
        InvHP(5),

        /**
         * The inverted band pass filter.
         */
        InvBP(6);

        //--------------------------------------------------------------------------
        //
        // Public :: Properties
        //
        //--------------------------------------------------------------------------

        //----------------------------------
        // value
        //----------------------------------

        private final int value;

        /**
         * The Integer value for the filter type.
         */
        public int getValue() {
            return value;
        }

        FilterType(int type) {
            value = type;
        }

        /**
         * Returns a
         * {@link com.teotigraphix.caustk.core.osc.FilterMessage.FilterType} for
         * the integer passed, null if not found.
         * 
         * @param type The filter type.
         */
        public static FilterType toType(Integer type) {
            for (FilterType result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see #toType(Integer)
         */
        public static FilterType toType(Float type) {
            return toType(type.intValue());
        }
    }
}
