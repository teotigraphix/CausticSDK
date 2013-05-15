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
 * The {@link IDistortionEffect} API allows setting values on the effect within
 * the {@link IEffectsRack}.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IDistortionEffect extends IEffect {

    //--------------------------------------------------------------------------
    //
    // Constants
    //
    //--------------------------------------------------------------------------

    /**
     * Message: {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} using <code>amount</code> as the
     * control.
     * <p>
     * The amount of distortion applied to signal above the threshold.
     * <p>
     * <strong>Default</strong>: <code>16.3</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>message</strong>: See {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} for command message.</li>
     * <li><strong>value</strong>: (0.0..20.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see #getPreGain()
     * @see #setPreGain(float)
     */
    public static final String CONTROL_AMOUNT = "amount";

    /**
     * Message: {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} using <code>post</code> as the control.
     * <p>
     * Post-amplification used to compensate the distortion algorithm's output
     * volume.
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
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
     * @see #getPostGain()
     * @see #setPostGain(float)
     */
    public static final String CONTROL_POST = "post";

    /**
     * Message: {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} using <code>pre</code> as the control.
     * <p>
     * Pre-amplification used to send signal past the chosen program's
     * threshold.
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>message</strong>: See {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} for command message.</li>
     * <li><strong>value</strong>: (0.0..5.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see #getPreGain()
     * @see #setPreGain(float)
     */
    public static final String CONTROL_PRE = "pre";

    /**
     * Message: {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} using <code>program</code> as the
     * control.
     * <p>
     * The algorithm used to distort the sound. Choices are Overdrive (tube
     * amp), Saturation (Soft-knee limiter), Fuzz (Hard-knee limiter), Foldback
     * (folds the signal onto itself).
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>message</strong>: See {@link IEffectsRack#MESSAGE_SET} or
     * {@link IEffectsRack#MESSAGE_GET} for command message.</li>
     * <li><strong>value</strong>: (0..3)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see #getProgram()
     * @see #setProgram(Program)
     */
    public static final String CONTROL_PROGRAM = "program";

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // amount
    //----------------------------------

    /**
     * @see #CONTROL_AMOUNT
     */
    float getAmount();

    /**
     * @see #getAmount()
     * @see #CONTROL_AMOUNT
     */
    void setAmount(float value);

    //----------------------------------
    // postGain
    //----------------------------------

    /**
     * @see #CONTROL_POST
     */
    float getPostGain();

    /**
     * @see #getPostGain()
     * @see #CONTROL_POST
     */
    void setPostGain(float value);

    //----------------------------------
    // preGain
    //----------------------------------

    /**
     * @see #CONTROL_PRE
     */
    float getPreGain();

    /**
     * @see #getPreGain()
     * @see #CONTROL_PRE
     */
    void setPreGain(float value);

    //----------------------------------
    // program
    //----------------------------------

    /**
     * @see #CONTROL_PROGRAM
     */
    Program getProgram();

    /**
     * @see #getProgram()
     * @see #CONTROL_PROGRAM
     */
    void setProgram(Program value);

    /**
     * The distortion program used with the {@link IDistortionEffect}.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     * @see IDistortionEffect#getProgram();
     */
    public enum Program {

        /**
         * Tube amp simulation.
         */
        OVERDRIVE(0),

        /**
         * Soft-knee limiter.
         */
        SATURATE(1),

        /**
         * Hard-knee limiter.
         */
        FOLDBACK(2),

        /**
         * folds the signal onto itself.
         */
        FUZZ(3);

        private int mValue;

        Program(int value) {
            mValue = value;
        }

        /**
         * Returns the int value for the {@link Program}.
         */
        public int getValue() {
            return mValue;
        }

        /**
         * Returns the {@link Program} from an int value.
         * 
         * @param value The int program value.
         */
        public static Program toType(Integer value) {
            for (Program p : values()) {
                if (p.getValue() == value)
                    return p;
            }
            return null;
        }
    }
}
