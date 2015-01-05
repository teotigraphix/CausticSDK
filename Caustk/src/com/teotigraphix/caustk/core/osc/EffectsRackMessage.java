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

    //--------------------------------------------------------------------------
    // Controls
    //--------------------------------------------------------------------------

    public enum ChorusMode {
        TriangleFull(0),

        SineFull(1),

        TriangleHalf(2),

        SineHalf(3),

        TriangleFullOpposed(4),

        SineFullOpposed(5),

        TriangleHalfOpposed(6),

        SineHalfOpposed(7);

        private int value;

        public int getValue() {
            return value;
        }

        private ChorusMode(int value) {
            this.value = value;
        }

        public static ChorusMode fromInt(int mode) {
            for (ChorusMode chorusMode : values()) {
                if (chorusMode.getValue() == mode)
                    return chorusMode;
            }
            return null;
        }
    }

    public enum DelayMode {

        Mono(0),

        MonoLR(1),

        MonoRL(2),

        DualMono(3),

        PingPong(4);

        private int value;

        public int getValue() {
            return value;
        }

        private DelayMode(int value) {
            this.value = value;
        }

        public static DelayMode fromInt(int mode) {
            for (DelayMode item : values()) {
                if (item.getValue() == mode)
                    return item;
            }
            return null;
        }
    }

    public enum FlangerMode {
        TriangleFull(0),

        SineFull(1),

        TriangleHalf(2),

        SineHalf(3),

        TriangleFullOpposed(4),

        SineFullOpposed(5),

        TriangleHalfOpposed(6),

        SineHalfOpposed(7);

        private int value;

        public int getValue() {
            return value;
        }

        private FlangerMode(int value) {
            this.value = value;
        }

        public static FlangerMode fromInt(int mode) {
            for (FlangerMode flangerMode : values()) {
                if (flangerMode.getValue() == mode)
                    return flangerMode;
            }
            return null;
        }
    }

    public enum StaticFlangerMode {
        TriangleFull(0),

        TriangleFullOpposed(4);

        private int value;

        public int getValue() {
            return value;
        }

        private StaticFlangerMode(int value) {
            this.value = value;
        }

        public static StaticFlangerMode fromInt(int mode) {
            for (StaticFlangerMode staticFlangerMode : values()) {
                if (staticFlangerMode.getValue() == mode)
                    return staticFlangerMode;
            }
            return null;
        }
    }

    public enum DistortionProgram {

        /**
         * Tube amp simulation.
         */
        Overdrive(0),

        /**
         * Soft-knee limiter.
         */
        Saturate(1),

        /**
         * Hard-knee limiter.
         */
        Foldback(2),

        /**
         * folds the signal onto itself.
         */
        Fuzz(3);

        private int value;

        DistortionProgram(int value) {
            this.value = value;
        }

        /**
         * Returns the int value for the
         * {@link com.teotigraphix.caustk.core.osc.EffectsRackMessage.DistortionProgram}
         * .
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns the
         * {@link com.teotigraphix.caustk.core.osc.EffectsRackMessage.DistortionProgram}
         * from an int value.
         * 
         * @param value The int program value.
         */
        public static DistortionProgram fromInt(Integer value) {
            for (DistortionProgram program : values()) {
                if (program.getValue() == value)
                    return program;
            }
            return null;
        }
    }

    public enum MultiFilterMode {

        LowShelf(0),

        HighShelf(1),

        LowPass(2),

        HighPass(3),

        BandPass(4),

        BandIsolate(5);

        private int value;

        MultiFilterMode(int value) {
            this.value = value;
        }

        /**
         * Returns the int value for the
         * {@link com.teotigraphix.caustk.core.osc.EffectsRackMessage.MultiFilterMode}
         * .
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns the
         * {@link com.teotigraphix.caustk.core.osc.EffectsRackMessage.MultiFilterMode}
         * from an int value.
         * 
         * @param value The int mode value.
         */
        public static MultiFilterMode fromInt(Integer value) {
            for (MultiFilterMode p : values()) {
                if (p.getValue() == value)
                    return p;
            }
            return null;
        }
    }

    /**
     * Controls for the {@link EffectsRackMessage}.
     * 
     * @author Michael Schmalle
     * @since 1.0
     */
    public enum EffectsRackControl implements IOSCControl {

        Create,

        Get,

        Remove,

        Set;

        @Override
        public String getDisplayName() {
            return name();
        }
    }
}
