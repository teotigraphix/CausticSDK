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
 * The {@link IParametricEQEffect} API allows setting values on the effect
 * within the {@link IEffectsRack}.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IParametricEQEffect extends IEffect {

    //--------------------------------------------------------------------------
    //
    // Constants
    //
    //--------------------------------------------------------------------------

    /**
     * Message: {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} using <code>frequency</code> as the
     * control.
     * <p>
     * The frequency at which to apply the equalization.
     * <p>
     * <strong>Default</strong>: <code>0.54</code>
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
     * @see #getFrequency()
     * @see #setFrequency(float)
     */
    public static final String CONTROL_FREQUENCY = "frequency";

    /**
     * Message: {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} using <code>gain</code> as the control.
     * <p>
     * The volume gain to apply around the selected frequency (boost or cut).
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>message</strong>: See {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} for command message.</li>
     * <li><strong>value</strong>: (-12..12)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see #getGain()
     * @see #setGain(int)
     */
    public static final String CONTROL_GAIN = "gain";

    /**
     * Message: {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} using <code>width</code> as the control.
     * <p>
     * The size of the surrounding frequencies that will be affected by the
     * equalization.
     * <p>
     * <strong>Default</strong>: <code>0.49999994</code>
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
     * @see #getWidth()
     * @see #setWidth(float)
     */
    public static final String CONTROL_WIDTH = "width";

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // frequency
    //----------------------------------

    /**
     * @see #CONTROL_DEPTH
     */
    float getFrequency();

    /**
     * @see #getFrequency()
     * @see #CONTROL_FREQUENCY
     */
    void setFrequency(float value);

    //----------------------------------
    // gain
    //----------------------------------

    /**
     * @see #CONTROL_GAIN
     */
    int getGain();

    /**
     * @see #getGain()
     * @see #CONTROL_GAIN
     */
    void setGain(int value);

    //----------------------------------
    // banidwidth
    //----------------------------------

    /**
     * @see #CONTROL_WIDTH
     */
    float getWidth();

    /**
     * @see #getWidth()
     * @see #CONTROL_WIDTH
     */
    void setWidth(float value);

}
