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
 * The {@link ICompressorEffect} API allows setting values on the effect within
 * the {@link IEffectsRack}.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ICompressorEffect extends IEffect {

    //--------------------------------------------------------------------------
    //
    // Constants
    //
    //--------------------------------------------------------------------------

    /**
     * Message: {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} using <code>attack</code> as the
     * control.
     * <p>
     * The time it takes to reach full compression once signal over threshold
     * has been detected.
     * <p>
     * <strong>Default</strong>: <code>0.01</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>message</strong>: See {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} for command message.</li>
     * <li><strong>value</strong>: (0.00001..0.2)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see #getAttack()
     * @see #setAttack(float)
     */
    public static final String CONTROL_ATTACK = "attack";

    /**
     * Message: {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} using <code>ratio</code> as the control.
     * <p>
     * The amount of reduction applied to signal over the threshold.
     * <p>
     * <strong>Default</strong>: <code>1.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>message</strong>: See {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} for command message.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see #getRatio()
     * @see #setRatio(float)
     */
    public static final String CONTROL_RATIO = "ratio";

    /**
     * Message: {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} using <code>release</code> as the
     * control.
     * <p>
     * The time it rakes to shut down compression after the signal has gone
     * below the threshold.
     * <p>
     * <strong>Default</strong>: <code>0.05</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>message</strong>: See {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} for command message.</li>
     * <li><strong>value</strong>: (0.001..0.2)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see #getRelease()
     * @see #setRelease(float)
     */
    public static final String CONTROL_RELEASE = "release";

    /**
     * Message: {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} using <code>sidechain</code> as the
     * control.
     * <p>
     * The signal used as a trigger to the compressor. Default is the current
     * machine channel but it can be changed to any input line in the mixer
     * (1-6).
     * <p>
     * <strong>Default</strong>: <code>-1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>message</strong>: See {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} for command message.</li>
     * <li><strong>value</strong>: (0..6)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see #getSidechain()
     * @see #setSidechain(int)
     */
    public static final String CONTROL_SIDECHAIN = "sidechain";

    /**
     * Message: {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} using <code>threshold</code> as the
     * control.
     * <p>
     * The volume at which the compressor will start modifying the signal.
     * <p>
     * <strong>Default</strong>: <code>0.1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>message</strong>: See {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} for command message.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see #getThreshold()
     * @see #setThreshold(float)
     */
    public static final String CONTROL_THRESHOLD = "threshold";

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // attack
    //----------------------------------

    /**
     * @see #CONTROL_ATTACK
     */
    float getAttack();

    /**
     * @see #getAttack()
     * @see #CONTROL_ATTACK
     */
    void setAttack(float value);

    //----------------------------------
    // ratio
    //----------------------------------

    /**
     * @see #CONTROL_RATIO
     */
    float getRatio();

    /**
     * @see #getRatio()
     * @see #CONTROL_RATIO
     */
    void setRatio(float value);

    //----------------------------------
    // release
    //----------------------------------

    /**
     * @see #CONTROL_RELEASE
     */
    float getRelease();

    /**
     * @see #getRelease()
     * @see #CONTROL_RELEASE
     */
    void setRelease(float value);

    //----------------------------------
    // sidechain
    //----------------------------------

    /**
     * @see #CONTROL_SIDECHAIN
     */
    int getSidechain();

    /**
     * @see #getSidechain()
     * @see #CONTROL_SIDECHAIN
     */
    void setSidechain(int value);

    //----------------------------------
    // threshold
    //----------------------------------

    /**
     * @see #CONTROL_THRESHOLD
     */
    float getThreshold();

    /**
     * @see #getThreshold()
     * @see #CONTROL_THRESHOLD
     */
    void setThreshold(float value);
}
