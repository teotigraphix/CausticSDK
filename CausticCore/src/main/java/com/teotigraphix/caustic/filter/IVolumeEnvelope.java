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

package com.teotigraphix.caustic.filter;

import com.teotigraphix.caustic.osc.VolumeMessage;

/**
 * The {@link IVolumeEnvelope} API allows a machine to adjust volume settings
 * such as attack, decay, sustain and release (ADSR).
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IVolumeEnvelope extends IVolumeComponent {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // attack
    //----------------------------------

    /**
     * The amount of time before volume reaches 100%, stating at 0.
     * 
     * @see VolumeMessage#VOLUME_ATTACK
     */
    float getAttack();

    /**
     * @see #getAttack()
     * @see VolumeMessage#VOLUME_ATTACK
     */
    void setAttack(float value);

    //----------------------------------
    // decay
    //----------------------------------

    /**
     * The amount of time, after the attack period, for the volume to go from
     * 100% to the sustain level.
     * 
     * @see VolumeMessage#VOLUME_DECAY
     */
    float getDecay();

    /**
     * @see #getDecay()
     * @see VolumeMessage#VOLUME_DECAY
     */
    void setDecay(float value);

    //----------------------------------
    // sustain
    //----------------------------------

    /**
     * The volume at which to settle, once the attack and decay period have
     * elapsed and the note is held.
     * 
     * @see VolumeMessage#VOLUME_SUSTAIN
     */
    float getSustain();

    /**
     * @see #getSustain()
     * @see VolumeMessage#VOLUME_SUSTAIN
     */
    void setSustain(float value);

    //----------------------------------
    // release
    //----------------------------------

    /**
     * The amount of time for the volume to go from the sustain level down to 0.
     * 
     * @see VolumeMessage#VOLUME_RELEASE
     */
    float getRelease();

    /**
     * @see #getRelease()
     * @see VolumeMessage#VOLUME_RELEASE
     */
    void setRelease(float value);
}
