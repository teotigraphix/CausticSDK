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

import com.teotigraphix.caustic.effect.IEffectComponent;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.common.IPersist;

/**
 * The {@link IMixerDelay} interface is a {@link IMachine} delay effect.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 * @see IMixerPanel#getDelay()
 */
public interface IMixerDelay extends IEffectComponent, IPersist {

    //--------------------------------------------------------------------------
    //
    // Public :: Constants
    //
    //--------------------------------------------------------------------------

    /**
     * Message: <strong>/caustic/mixer/delay/time [value]</strong>
     * <p>
     * Default: <strong>7</strong>
     * </p>
     * 
     * @param value (1..9)
     * @return int
     * @see #getTime()
     * @see #setTime(int)
     */
    public static final String CONTROL_TIME = "time";

    /**
     * Message: <strong>/caustic/mixer/delay/feedback [value]</strong>
     * <p>
     * Default: <strong>0.5</strong>
     * </p>
     * 
     * @param value (0..1)
     * @return float
     * @see #getFeedback()
     * @see #setFeedback(float)
     */
    public static final String CONTROL_FEEDBACK = "feedback";

    /**
     * Message: <strong>/caustic/mixer/delay/stereo [value]</strong>
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
    // feedback
    //----------------------------------

    /**
     * The percentage of feedback in the delay.
     * 
     * @see #CONTROL_FEEDBACK
     */
    float getFeedback();

    /**
     * @see #getFeedback()
     * @see #CONTROL_FEEDBACK
     */
    void setFeedback(float value);

    //----------------------------------
    // stereo
    //----------------------------------

    /**
     * Adds a ping-pong effect to the delay.
     * 
     * @see #CONTROL_STEREO
     */
    boolean isStereo();

    /**
     * @see #isStereo()
     * @see #CONTROL_STEREO
     */
    void setStereo(boolean value);

    //----------------------------------
    // time
    //----------------------------------

    /**
     * The delay time that is map to BPM-sync'ed note types.
     * 
     * @see #CONTROL_TIME
     */
    int getTime();

    /**
     * @see #getTime()
     * @see #CONTROL_TIME
     */
    void setTime(int value);
}
