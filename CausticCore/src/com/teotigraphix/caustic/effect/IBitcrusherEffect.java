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

package com.teotigraphix.caustic.effect;

/**
 * The {@link IBitcrusherEffect} API allows setting values on the effect within
 * the {@link IEffectsRack}.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IBitcrusherEffect extends IEffect
{

    //--------------------------------------------------------------------------
    //
    // Constants
    //
    //--------------------------------------------------------------------------

    /**
     * Message: {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} using <code>depth</code> as the control.
     * <p>
     * The bit depth, from 0 to 16 bits
     * <p>
     * <strong>Default</strong>: <code>3</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>message</strong>: See {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} for command message.</li>
     * <li><strong>value</strong>: (1..16)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see #getDepth()
     * @see #setDepth(int)
     */
    public static final String CONTROL_DEPTH = "depth";

    /**
     * Message: {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} using <code>jitter</code> as the
     * control.
     * <p>
     * The amount of randomness on the Rate control
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>message</strong>: See {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} for command message.</li>
     * <li><strong>value</strong>: (0..1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see #getJitter()
     * @see #setJitter(float)
     */
    public static final String CONTROL_JITTER = "jitter";

    /**
     * Message: {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} using <code>rate</code> as the control.
     * <p>
     * The sampling rate, from 1Hz to 44KHz
     * <p>
     * <strong>Default</strong>: <code>0.1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>message</strong>: See {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} for command message.</li>
     * <li><strong>value</strong>: (0.01..0.5)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see #getRate()
     * @see #setRate(float)
     */
    public static final String CONTROL_RATE = "rate";

    /**
     * Message: {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} using <code>wet</code> as the control.
     * <p>
     * The ratio of modified signal to original signal, from 0% to 100%
     * <p>
     * <strong>Default</strong>: <code>1.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>message</strong>: See {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} for command message.</li>
     * <li><strong>value</strong>: (0..1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see #getWet()
     * @see #setWet(float)
     */
    public static final String CONTROL_WET = "wet";

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see #CONTROL_DEPTH
     */
    int getDepth();

    /**
     * @see #getDepth()
     * @see #CONTROL_DEPTH
     */
    void setDepth(int value);

    //----------------------------------
    // jitter
    //----------------------------------

    /**
     * @see #CONTROL_JITTER
     */
    float getJitter();

    /**
     * @see #getJitter()
     * @see #CONTROL_JITTER
     */
    void setJitter(float value);

    //----------------------------------
    // range
    //----------------------------------

    /**
     * @see #CONTROL_RATE
     */
    float getRate();

    /**
     * @see #getRate()
     * @see #CONTROL_RATE
     */
    void setRate(float value);

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see #CONTROL_WET
     */
    float getWet();

    /**
     * @see #getWet()
     * @see #CONTROL_WET
     */
    void setWet(float value);
}
