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

package com.teotigraphix.caustic.mixer;

import com.teotigraphix.caustic.core.IPersist;
import com.teotigraphix.caustic.effect.IEffectComponent;
import com.teotigraphix.caustic.machine.IMachine;

/**
 * The {@link IMixerReverb} interface is a {@link IMachine} reverb effect.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 * @see IMixerPanel#getReverb()
 */
public interface IMixerReverb extends IEffectComponent, IPersist {

    //--------------------------------------------------------------------------
    //
    // Public :: Constants
    //
    //--------------------------------------------------------------------------

    /**
     * Message: <strong>/caustic/mixer/reverb/room [value]</strong>
     * <p>
     * Default: <strong>0.75</strong>
     * </p>
     * 
     * @param value (0..1)
     * @return float
     * @see #getRoom()
     * @see #setRoom(float)
     */
    public static final String CONTROL_ROOM = "room";

    /**
     * Message: <strong>/caustic/mixer/reverb/damping [value]</strong>
     * <p>
     * Default: <strong>0.2</strong>
     * </p>
     * 
     * @param value (0..1)
     * @return float
     * @see #getDamping()
     * @see #setDamping(float)
     */
    public static final String CONTROL_DAMPING = "damping";

    /**
     * Message: <strong>/caustic/mixer/reverb/stereo [value]</strong>
     * <p>
     * Default: <strong>0</strong>
     * </p>
     * 
     * @param value (0,1)
     * @return int
     * @see #isStereo()
     * @see #setStereo(int)
     */
    public static final String CONTROL_STEREO = "stereo";

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // damping
    //----------------------------------

    /**
     * The amount of damping, the higher the value the more "padding" on the
     * walls which softens the reverbs reflection.
     * <p>
     * 
     * @see #CONTROL_DAMPING
     */
    float getDamping();

    /**
     * @see #getDamping()
     * @see #CONTROL_DAMPING
     */
    void setDamping(float value);

    //----------------------------------
    // room
    //----------------------------------

    /**
     * The amount of reverberation, the higher the value the larger the room
     * sounds.
     * 
     * @see #CONTROL_ROOM
     */
    float getRoom();

    /**
     * @see #getRoom()
     * @see #CONTROL_ROOM
     */
    void setRoom(float value);

    //----------------------------------
    // stereo
    //----------------------------------

    /**
     * Adds a more <em>spacious</em> sound to the reverb.
     * 
     * @see #CONTROL_STEREO
     */
    boolean isStereo();

    /**
     * @see #isStereo()
     * @see #CONTROL_STEREO
     */
    void setStereo(boolean value);
}
