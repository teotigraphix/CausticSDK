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

import com.teotigraphix.caustk.core.components.FilterComponentBase;
import com.teotigraphix.caustk.core.components.SynthFilterComponent;
import com.teotigraphix.caustk.core.components.SynthFilterComponent.FilterType;
import com.teotigraphix.caustk.core.components.bassline.FilterComponent;

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
     * 
     * @see FilterComponentBase#getCutoff()
     * @see FilterComponentBase#setCutoff(float)
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
     * 
     * @see FilterComponentBase#getResonance()
     * @see FilterComponentBase#setResonance(float)
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
     * 
     * @see SynthFilterComponent#getType()
     * @see SynthFilterComponent#setType(FilterType)
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
     * <li><strong>value</strong>: (0..3.0625)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see SynthFilterComponent#getAttack()
     * @see SynthFilterComponent#setAttack(float)
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
     * <li><strong>value</strong>: (0..3.0625)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see SynthFilterComponent#getDecay()
     * @see SynthFilterComponent#setDecay(float)
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
     * <li><strong>value</strong>: (0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see SynthFilterComponent#getSustain()
     * @see SynthFilterComponent#setSustain(float)
     */
    public static final FilterMessage FILTER_SUSTAIN = new FilterMessage(
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
     * 
     * @see SynthFilterComponent#getTrack()
     * @see SynthFilterComponent#setTrack(float)
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
     * 
     * @see FilterComponent#getEnvMod()
     * @see FilterComponent#setEnvMod()
     */
    public static final FilterMessage FILTER_ENVMOD = new FilterMessage(
            "/caustic/${0}/filter_envmod ${1}");

    FilterMessage(String message) {
        super(message);
    }
}
