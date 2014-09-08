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

    /**
     * @author Michael Schmalle
     * @since 1.0
     */
    public static interface IEffectControl extends IOSCControl {
        String getControl();
    }

    public enum AutowahControl implements IEffectControl {

        /**
         * Values <code>0.5..4.0</code>; default <code>2.23</code>
         */
        Cutoff("cutoff"),

        /**
         * Values <code>0.0..1.0</code>; default <code>1.0</code>
         */
        Depth("depth"),

        /**
         * Values <code>0.0..0.1</code>; default <code>0.5</code>
         */
        Resonance("resonance"),

        /**
         * Values <code>0.0.5.0</code>; default <code>0.4</code>
         */
        Speed("speed"),

        /**
         * Values <code>0.0..1.0</code>; default <code>1.0</code>
         */
        Wet("wet");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private AutowahControl(String control) {
            this.control = control;
        }
    }

    public enum BitcrusherControl implements IEffectControl {

        /**
         * Values <code>1..16</code>; default <code>3</code>
         */
        Depth("depth"),

        /**
         * Values <code>0.0..1.0</code>; default <code>0.0</code>
         */
        Jitter("jitter"),

        /**
         * Values <code>0.01..0.5</code>; default <code>1.0</code>
         */
        Rate("rate"),

        /**
         * Values <code>0.0..1.0</code>; default <code>1.0</code>
         */
        Wet("wet");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private BitcrusherControl(String control) {
            this.control = control;
        }
    }

    public enum CabinetSimulatorControl implements IEffectControl {

        /**
         * Values <code>0.25..1</code>; default <code>1.0</code>
         */
        Damping("damping"),

        /**
         * Values <code>0..1</code>; default <code>0</code>
         */
        Height("height"),

        /**
         * Values <code>0..1</code>; default <code>0.5</code>
         */
        Tone("tone"),

        /**
         * Values <code>0..1</code>; default <code>0.5</code>
         */
        Wet("wet"),

        /**
         * Values <code>0..1</code>; default <code>0</code>
         */
        Width("width");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private CabinetSimulatorControl(String control) {
            this.control = control;
        }
    }

    public enum ChorusControl implements IEffectControl {

        /**
         * Values <code>0.1..0.95</code>; default <code>0.25</code>
         */
        Depth("depth"),

        /**
         * Values <code>0.0..1.0</code>; default <code>0.4</code>
         */
        Rate("rate"),

        /**
         * Values <code>0.0..0.5</code>; default <code>0.5</code>
         */
        Wet("wet"),

        /**
         * Values <code>0.0..0.7</code>; default <code>0.0</code>
         */
        Delay("delay"),

        /**
         * Values <code>0,1,2,3,4,5,6,7</code>; default <code>0</code>
         * <p>
         * triangleFull, sineFull, triangleHalf, sineHalf, triangleFullOpposed,
         * sineFullOpposed, triangleHalfOpposed, sineHalfOpposed
         * 
         * @see ChorusMode
         */
        Mode("mode");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private ChorusControl(String control) {
            this.control = control;
        }
    }

    public enum CombFilterControl implements IEffectControl {

        /**
         * Values <code>2..50</code>; default <code>10</code>
         */
        Freq("rate"), // rate

        /**
         * Values <code>0.1..0.95</code>; default <code>0.475</code>
         */
        Reso("feedback"), // feedback

        /**
         * Values <code>0.1..0.95</code>; default <code>0.8</code>
         */
        Wet("depth"); // depth

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private CombFilterControl(String control) {
            this.control = control;
        }
    }

    public enum CompressorControl implements IEffectControl {

        /**
         * Values <code>0.00001..0.2</code>; default <code>0.01</code>
         */
        Attack("attack"),

        /**
         * Values <code>0.0..1.0</code>; default <code>1.0</code>
         */
        Ratio("ratio"),

        /**
         * Values <code>0.001..0.2</code>; default <code>0.05</code>
         */
        Release("release"),

        /**
         * Values <code>0..13</code>; default <code>-1</code>
         */
        Sidechain("sidechain"),

        /**
         * Values <code>0.0..1.0</code>; default <code>0.1</code>
         */
        Threshold("threshold");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private CompressorControl(String control) {
            this.control = control;
        }
    }

    public enum DelayControl implements IEffectControl {

        /**
         * Values <code>0.0..1.0</code>; default <code>0.5</code>
         */
        Feedback("feedback"),

        /**
         * Values <code>1..12</code>; default <code>8</code>
         */
        Time("time"),

        /**
         * Values <code>0.0..1.0</code>; default <code>0.5</code>
         */
        Wet("wet"),

        /**
         * Values <code>0,1,2,3,4</code> Mono, MonoLR, MonoRL, DualMono,
         * PingPong; default <code>0</code>
         */
        Mode("mode");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private DelayControl(String control) {
            this.control = control;
        }
    }

    public enum DistortionControl implements IEffectControl {

        /**
         * Values <code>0..3</code>; default <code>0</code>
         */
        Program("program"),

        /**
         * Values <code>0.0..5.0</code>; default <code>4.05</code>
         */
        PreGain("pre_gain"),

        /**
         * Values <code>0.0..20.0</code>; default <code>16.3</code>
         */
        Amount("amount"),

        /**
         * Values <code>0.0..1.0</code>; default <code>0.1</code>
         */
        PostGain("post_gain");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private DistortionControl(String control) {
            this.control = control;
        }
    }

    public enum FlangerControl implements IEffectControl {

        /**
         * Values <code>0.1..0.95</code>; default <code>0.25</code>
         */
        Depth("depth"),

        /**
         * Values <code>0.25..0.8</code>; default <code>0.4</code>
         */
        Feedback("feedback"),

        /**
         * Values <code>0.04..2.0</code>; default <code>0.4</code>
         */
        Rate("rate"),

        /**
         * Values <code>0.0..1.0</code>; default <code>0.5</code>
         */
        Wet("wet"),

        /**
         * Values <code>0,1,2,3,4,5,6,7</code>; default <code>0</code>
         * <p>
         * triangleFull, sineFull, triangleHalf, sineHalf, triangleFullOpposed,
         * sineFullOpposed, triangleHalfOpposed, sineHalfOpposed
         * 
         * @see FlangerMode
         */
        Mode("mode");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private FlangerControl(String control) {
            this.control = control;
        }
    }

    public enum LimiterControl implements IEffectControl {

        /**
         * Values <code>0..0.05</code>; default <code>0.01</code>
         */
        Attack("attack"),

        /**
         * Values <code>0..2</code>; default <code>0.5</code>
         */
        PostGain("post_gain"),

        /**
         * Values <code>0..4</code>; default <code>2</code>
         */
        PreGain("pre_gain"),

        /**
         * Values <code>0.01..0.5</code>; default <code>0.5</code>
         */
        Release("release");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private LimiterControl(String control) {
            this.control = control;
        }
    }

    public enum MultiFilterControl implements IEffectControl {

        /**
         * Values <code>0.1..1.0</code>; default <code>0.54</code>
         */
        Frequency("frequency"),

        /**
         * Values <code>-12..12</code>; default <code>0.0</code>
         */
        Gain("gain"),

        /**
         * Values <code>0,1,2,3,4,5</code>; default <code>0</code>
         */
        Mode("mode"),

        /**
         * Values <code>0.0..1.0</code>; default <code>0.5</code>
         */
        Resonance("resonance");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private MultiFilterControl(String control) {
            this.control = control;
        }
    }

    public enum ParametricEQControl implements IEffectControl {

        /**
         * Values <code>0.0..1.0</code>; default <code>0.54</code>
         */
        Frequency("frequency"),

        /**
         * Values <code>-12.0..12.0</code>; default <code>0.0</code>
         */
        Gain("gain"),

        /**
         * Values <code>0.0..10.0</code>; default <code>0.5</code>
         */
        Width("width");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private ParametricEQControl(String control) {
            this.control = control;
        }
    }

    public enum PhaserControl implements IEffectControl {

        /**
         * Values <code>0.1..0.95</code>; default <code>0.8</code>
         */
        Depth("depth"),

        /**
         * Values <code>0.1..0.95</code>; default <code>0.47</code>
         */
        Feedback("feedback"),

        /**
         * Values <code>0.002..0.5</code>; default <code>0.09</code>
         */
        HighFreq("highfreq"),

        /**
         * Values <code>0.002..0.5</code>; default <code>0.01</code>
         */
        LowFreq("lowfreq"),

        /**
         * Values <code>2..50</code>; default <code>10</code>
         */
        Rate("rate");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private PhaserControl(String control) {
            this.control = control;
        }
    }

    public enum ReverbControl implements IEffectControl {

        /**
         * Values <code>0.0..0.8</code>; default <code>0.25</code>
         */
        Damping("damping"),

        /**
         * Values <code>0.0..0.0925</code>; default <code>0.04625</code>
         */
        Delay("delay"),

        /**
         * Values <code>0.0..0.925</code>; default <code>0.85</code>
         */
        Room("room"),

        /**
         * Values <code>0.0..1.0</code>; default <code>0.195</code>
         */
        Wet("wet"),

        /**
         * Values <code>0.0..1.0</code>; default <code>1.0</code>
         */
        Width("width");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private ReverbControl(String control) {
            this.control = control;
        }
    }

    public enum StaticFlangerControl implements IEffectControl {

        /**
         * Values <code>-0.95 .. 0.95</code>; default <code>0.0</code>
         */
        Depth("depth"),

        /**
         * Values <code>0.25..0.9</code>; default <code>0.575</code>
         */
        Feedback("feedback"),

        /**
         * Values <code>0.0..1.0</code>; default <code>0.5</code>
         */
        Wet("wet"),

        /**
         * Values <code>0,4</code>; default <code>0</code>
         * <p>
         * triangleFull, triangleFullOpposed
         * 
         * @see StaticFlangerMode
         */
        Mode("mode");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private StaticFlangerControl(String control) {
            this.control = control;
        }
    }

    public enum VinylSimulatorControl implements IEffectControl {

        /**
         * Values <code>0.0..1.0</code>; default <code>0.5</code>
         */
        Age("age"),

        /**
         * Values <code>0.0..1.0</code>; default <code>0.75</code>
         */
        Dust("dust"),

        /**
         * Values <code>0.0..1.0</code>; default <code>0.33</code>
         */
        Noise("noise"),

        /**
         * Values <code>0.0..1.0</code>; default <code>0.25</code>
         */
        Scratch("scratch"),

        /**
         * Values <code>0.0..2.0</code>; default <code>1.0</code>
         */
        Wet("wet");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private VinylSimulatorControl(String control) {
            this.control = control;
        }
    }

    public enum EffectControl implements IEffectControl {

        /**
         * Whether the effect is bypassed. (0,1)
         */
        Bypass("bypass");

        private String control;

        @Override
        public String getControl() {
            return control;
        }

        private EffectControl(String control) {
            this.control = control;
        }
    }

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
         * Returns the int value for the {@link DistortionProgram}.
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns the {@link DistortionProgram} from an int value.
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
         * Returns the int value for the {@link MultiFilterMode}.
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns the {@link MultiFilterMode} from an int value.
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

        Set
    }
}
