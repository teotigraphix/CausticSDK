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

/**
 * The {@link VolumeMessage} holds all OSC messages associated with the
 * {@link IVolumeEnvelope} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class VolumeMessage extends CausticMessage {

    //--------------------------------------------------------------------------
    // IVolumeComponent
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
     * @see IVolumeComponent#getOut()
     * @see IVolumeComponent#setOut(float)
     */
    public static final VolumeMessage VOLUME_OUT = new VolumeMessage(
            "/caustic/${0}/volume_out ${1}");

    //--------------------------------------------------------------------------
    // IVolumeEnvelope
    //--------------------------------------------------------------------------

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
     * @see IVolumeEnvelope#getAttack()
     * @see IVolumeEnvelope#setAttack(float)
     */
    public static final VolumeMessage VOLUME_ATTACK = new VolumeMessage(
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
     * @see IVolumeEnvelope#getDecay()
     * @see IVolumeEnvelope#setDecay(float)
     */
    public static final VolumeMessage VOLUME_DECAY = new VolumeMessage(
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
     * @see IVolumeEnvelope#getSustain()
     * @see IVolumeEnvelope#setSustain(float)
     */
    public static final VolumeMessage VOLUME_SUSTAIN = new VolumeMessage(
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
     * @see IVolumeEnvelope#getRelease()
     * @see IVolumeEnvelope#setRelease(float)
     */
    public static final VolumeMessage VOLUME_RELEASE = new VolumeMessage(
            "/caustic/${0}/volume_release ${1}");

    VolumeMessage(String message) {
        super(message);
    }
}
