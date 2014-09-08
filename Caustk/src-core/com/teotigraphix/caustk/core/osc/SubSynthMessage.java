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
 * The native OSC messages for the <strong>SubSynth</strong>.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see SubSynthMachine
 */
public class SubSynthMessage extends CausticMessage {

    //--------------------------------------------------------------------------
    // Filter
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/filter_cutoff [value]</code>
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
     */
    public static final SubSynthMessage FILTER_CUTOFF = new SubSynthMessage(
            "/caustic/${0}/filter_cutoff ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_resonance [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final SubSynthMessage FILTER_RESONANCE = new SubSynthMessage(
            "/caustic/${0}/filter_resonance ${1}");

    //--------------------------------------------------------------------------
    // IFilter
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/filter_type [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1,2,3,4,5,6) - NONE, LOW_PASS, HIGH_PASS,
     * BAND_PASS, INV_LP, INV_HP, INV_BP.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final SubSynthMessage FILTER_TYPE = new SubSynthMessage(
            "/caustic/${0}/filter_type ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_attack [value]</code>
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
     */
    public static final SubSynthMessage FILTER_ATTACK = new SubSynthMessage(
            "/caustic/${0}/filter_attack ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_decay [value]</code>
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
     */
    public static final SubSynthMessage FILTER_DECAY = new SubSynthMessage(
            "/caustic/${0}/filter_decay ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_sustain [value]</code>
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
     */
    public static final SubSynthMessage FILTER_SUSTAIN = new SubSynthMessage(
            "/caustic/${0}/filter_sustain ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_release [value]</code>
     * <p>
     * <strong>Default</strong>: <code>3.0625</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3.0625)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final SubSynthMessage FILTER_RELEASE = new SubSynthMessage(
            "/caustic/${0}/filter_release ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_kbtrack [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final SubSynthMessage FILTER_KBTRACK = new SubSynthMessage(
            "/caustic/${0}/filter_kbtrack ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/filter_envmod [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.99</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final SubSynthMessage FILTER_ENVMOD = new SubSynthMessage(
            "/caustic/${0}/filter_envmod ${1}");

    //--------------------------------------------------------------------------
    // LFO
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/lfo1_depth [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final SubSynthMessage LFO1_DEPTH = new SubSynthMessage(
            "/caustic/${0}/lfo1_depth ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo2_depth [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final SubSynthMessage LFO2_DEPTH = new SubSynthMessage(
            "/caustic/${0}/lfo2_depth ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo1_rate [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (1..12)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final SubSynthMessage LFO1_RATE = new SubSynthMessage(
            "/caustic/${0}/lfo1_rate ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo2_rate [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (1..12)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final SubSynthMessage LFO2_RATE = new SubSynthMessage(
            "/caustic/${0}/lfo2_rate ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo1_target [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..6)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     */
    public static final SubSynthMessage LFO1_TARGET = new SubSynthMessage(
            "/caustic/${0}/lfo1_target ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo2_target [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..6)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     */
    public static final SubSynthMessage LFO2_TARGET = new SubSynthMessage(
            "/caustic/${0}/lfo2_target ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo1_waveform [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     */
    public static final SubSynthMessage LFO1_WAVEFORM = new SubSynthMessage(
            "/caustic/${0}/lfo1_waveform ${1}");

    //--------------------------------------------------------------------------
    // Osc
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/osc_bend [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * </p>
     */
    public static final SubSynthMessage OSC_BEND = new SubSynthMessage(
            "/caustic/${0}/osc_bend ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc_modulation [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final SubSynthMessage OSC1_MODULATION = new SubSynthMessage(
            "/caustic/${0}/osc_modulation ${1}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/osc_modulation_mode [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1,2) FM, PM, AM
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * </p>
     */
    public static final SubSynthMessage OSC1_MODULATION_MODE = new SubSynthMessage(
            "/caustic/${0}/osc_modulation_mode ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc_mix [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.5</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * </p>
     */
    public static final SubSynthMessage OSC_MIX = new SubSynthMessage("/caustic/${0}/osc_mix ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc1_waveform [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>; <code>SINE</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1,2,3,4,5) - SINE, SAW, TRIANGLE, SQUARE,
     * NOISE
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     */
    public static final SubSynthMessage OSC1_WAVEFORM = new SubSynthMessage(
            "/caustic/${0}/osc1_waveform ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc2_cents [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-50..50)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     */
    public static final SubSynthMessage OSC2_CENTS = new SubSynthMessage(
            "/caustic/${0}/osc2_cents ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc2_cents_mode [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1) Cents, Unison
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     */
    public static final SubSynthMessage OSC2_CENTS_MODE = new SubSynthMessage(
            "/caustic/${0}/osc2_cents_mode ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc2_octave [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-3..3)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     */
    public static final SubSynthMessage OSC2_OCTAVE = new SubSynthMessage(
            "/caustic/${0}/osc2_octave ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc2_phase [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-0.5..0.5)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * </p>
     */
    public static final SubSynthMessage OSC2_PHASE = new SubSynthMessage(
            "/caustic/${0}/osc2_phase ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc2_semis [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-12..12)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     */
    public static final SubSynthMessage OSC2_SEMIS = new SubSynthMessage(
            "/caustic/${0}/osc2_semis ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/osc2_waveform [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1,2,3,4,5,6) - SINE, SAW, TRIANGLE,
     * SQUARE, NOISE, CUSTOM
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     */
    public static final SubSynthMessage OSC2_WAVEFORM = new SubSynthMessage(
            "/caustic/${0}/osc2_waveform ${1}");

    SubSynthMessage(String message) {
        super(message);
    }

    public enum LFO1Target {

        None(0),

        Osc1(1),

        Osc2(2),

        Osc1Plus2(3),

        Phase(4),

        Cutoff(5),

        Volume(6),

        Octave(7),

        Semis(8),

        Osc1Mod(9);

        private final int value;

        LFO1Target(int value) {
            this.value = value;
        }

        /**
         * Returns the int value of the lfo.
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns a {@link LFO1Target} based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static LFO1Target toType(Integer type) {
            for (LFO1Target result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see LFO1Target#toType(Integer)
         */
        public static LFO1Target toType(Float type) {
            return toType(type.intValue());
        }
    }

    public enum LFO1Waveform {

        Sine(0),

        Triangle(1),

        Saw(2),

        Square(3);

        private final int value;

        LFO1Waveform(int value) {
            this.value = value;
        }

        /**
         * Returns the int value of the waveform.
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns a {@link LFO1Waveform} based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static LFO1Waveform toType(Integer type) {
            for (LFO1Waveform result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see LFO1Waveform#toType(Integer)
         */
        public static LFO1Waveform toType(Float type) {
            return toType(type.intValue());
        }
    }

    public enum LFO2Target {

        None(0),

        Osc1(1),

        Osc2(2),

        Osc1Plus2(3),

        Phase(4),

        Cutoff(5),

        Volume(6),

        Octave(7),

        Semis(8),

        Osc1Mod(9);

        private final int value;

        LFO2Target(int value) {
            this.value = value;
        }

        /**
         * Returns the int value of the lof.
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns a {@link LFO2Target} based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static LFO2Target toType(Integer type) {
            for (LFO2Target result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see LFO2Target#toType(Integer)
         */
        public static LFO2Target toType(Float type) {
            return toType(type.intValue());
        }
    }

    public enum ModulationMode {
        Fm(0),

        Pm(1),

        Am(2);

        private final int value;

        /**
         * Returns the integer value of the {@link Osc1Waveform}.
         */
        public int getValue() {
            return value;
        }

        ModulationMode(int value) {
            this.value = value;
        }

        public static ModulationMode toType(Integer type) {
            for (ModulationMode result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }
    }

    /**
     * The {@link ISubSynthOsc1} waveforms.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum Osc1Waveform {

        Sine(0),

        Triangle(1),

        Saw(2),

        SawHQ(3),

        Square(4),

        SquareHQ(5),

        Noise(6),

        Custom1(7),

        Custom2(8);

        private final int value;

        /**
         * Returns the integer value of the {@link Osc1Waveform}.
         */
        public int getValue() {
            return value;
        }

        Osc1Waveform(int value) {
            this.value = value;
        }

        /**
         * Returns a {@link Osc1Waveform} based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static Osc1Waveform toType(Integer type) {
            for (Osc1Waveform result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see #toType(Integer)
         */
        public static Osc1Waveform toType(Float type) {
            return toType(type.intValue());
        }
    }

    public enum Osc2WaveForm {

        None(0),

        Sine(1),

        Triangle(2),

        Saw(3),

        SawHQ(4),

        Square(5),

        SquareHQ(6),

        Noise(7),

        Custom1(8),

        Custom2(9);

        private final int value;

        /**
         * Returns in integer value for the {@link Osc2WaveForm}.
         */
        public int getValue() {
            return value;
        }

        Osc2WaveForm(int value) {
            this.value = value;
        }

        /**
         * Returns a Osc2WaveForm based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static Osc2WaveForm toType(Integer type) {
            for (Osc2WaveForm result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see #toType(Integer)
         */
        public static Osc2WaveForm toType(Float type) {
            return toType(type.intValue());
        }
    }

    public enum CentsMode {
        CENTS(0),

        UNISON(1);

        private final int value;

        /**
         * Returns in integer value for the {@link CentsMode}.
         */
        public int getValue() {
            return value;
        }

        CentsMode(int value) {
            this.value = value;
        }

        public static CentsMode toType(Integer type) {
            for (CentsMode result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }
    }
}
