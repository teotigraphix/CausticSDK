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


/**
 * The {@link EffectsRackMessage} holds all OSC messages associated with the
 * {@link IEffectsRack} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class EffectsRackMessage extends CausticMessage {

    /**
     * Message:
     * <code>/caustic/effects_rack/create [machine_index] [slot] [type]</code>
     * <p>
     * Creates a new effect by type located in the effect slot. If an effect
     * exists within the same slot, the effect will be replaced by the new
     * effect.
     * <p>
     * <strong>Default</strong>: N/A
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>slot</strong>: (0, 1) The effect slot in the effects rack.</li>
     * <li><strong>type</strong>: (2..9) The type of effect to create.
     * <p>
     * Types: <code>Distortion(2)</code>, <code>Compressor(3)</code>,
     * <code>Bitcrusher(4)</code>, <code>Flanger(5)</code>,
     * <code>Phaser(6)</code>, <code>Chorus(7)</code>, <code>Autowah(8)</code>,
     * <code>ParametricEQ(9)</code>.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     * 
     * @see SoundMixerChannel#addEffect(EffectType, int)
     */
    public static final EffectsRackMessage CREATE = new EffectsRackMessage(
            "/caustic/effects_rack/create ${0} ${1} ${2}");

    /**
     * Message: <code>/caustic/effects_rack/remove [machine_index] [slot]</code>
     * <p>
     * Removes an effect for the specified machine at the specified effect slot.
     * <p>
     * <strong>Default</strong>: N/A
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>slot</strong>: (0, 1) The effect slot in the effect rack.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     * 
     * @see SoundMixerChannel#removeEffect(int)
     */
    public static final EffectsRackMessage REMOVE = new EffectsRackMessage(
            "/caustic/effects_rack/remove ${0} ${1}");

    /**
     * Message:
     * <code>/caustic/effects_rack/[machine_index]/[slot]/[control] [value]</code>
     * <p>
     * The message will set any effect property in the effect_rack using the
     * specific parameter of the effect instance.
     * <p>
     * <strong>Default</strong>: N/A
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>slot</strong>: (0, 1) The effect slot in the effect rack.</li>
     * <li><strong>control</strong>: The effect control parameter for the
     * specific effect instance. See the individual effect properties for more
     * information.</li>
     * <li><strong>value</strong>: The effect's acceptable control value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     */
    public static final EffectsRackMessage SET = new EffectsRackMessage(
            "/caustic/effects_rack/${0}/${1}/${2} ${3}");

    /**
     * Message:
     * <code>/caustic/effects_rack/[machine_index]/[slot]/[control]</code>
     * <p>
     * The message will get any effect property in the effect_rack using the
     * specific parameter of the effect instance.
     * <p>
     * <strong>Default</strong>: N/A
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>slot</strong>: (0, 1) The effect slot in the effect rack.</li>
     * <li><strong>control</strong>: The effect control parameter for the
     * specific effect instance. See the individual effect properties for more
     * information.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final EffectsRackMessage GET = new EffectsRackMessage(
            "/caustic/effects_rack/${0}/${1}/${2}");

    /**
     * Message: <code>/caustic/effects_rack/type [machine_index] [slot]</code>
     * <p>
     * The message will return the type of effect located as the specified slot
     * on the machine.
     * <p>
     * <strong>Default</strong>: N/A
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>slot</strong>: (0, 1) The effect slot in the effect rack.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final EffectsRackMessage TYPE = new EffectsRackMessage(
            "/caustic/effects_rack/type ${0} ${1}");

    EffectsRackMessage(String message) {
        super(message);
    }
}
