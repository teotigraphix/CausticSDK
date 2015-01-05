////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

import com.teotigraphix.caustk.node.machine.FMSynthMachine;

/**
 * The native OSC messages for the <strong>FMSynth</strong>.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see FMSynthMachine
 */
public class FMSynthMessage extends CausticMessage {

    /**
     * Message: <code>/caustic/[machine_index]/algorithm [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1,2,3,4) 3->2->1, 1+(3->2), (2+3)->1,
     * 1+2+3, (3->1)+(3->2)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage.FMAlgorithm
     */
    public static final FMSynthMessage ALGORITHM = new FMSynthMessage(
            "/caustic/${0}/algorithm ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/feedback [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0.25)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final FMSynthMessage FEEDBACK = new FMSynthMessage("/caustic/${0}/feedback ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/feedback_vel [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final FMSynthMessage FEEDBACK_VEL = new FMSynthMessage(
            "/caustic/${0}/feedback_vel ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/volume_vel [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final FMSynthMessage VOLUME_VEL = new FMSynthMessage(
            "/caustic/${0}/volume_vel ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_a1 [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final FMSynthMessage LFO_A1 = new FMSynthMessage("/caustic/${0}/lfo_a1 ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_a2 [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final FMSynthMessage LFO_A2 = new FMSynthMessage("/caustic/${0}/lfo_a2 ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_a3 [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final FMSynthMessage LFO_A3 = new FMSynthMessage("/caustic/${0}/lfo_a3 ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_aOut [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final FMSynthMessage LFO_AOUT = new FMSynthMessage("/caustic/${0}/lfo_aOut ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_f1 [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final FMSynthMessage LFO_F1 = new FMSynthMessage("/caustic/${0}/lfo_f1 ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_f2 [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final FMSynthMessage LFO_F2 = new FMSynthMessage("/caustic/${0}/lfo_f2 ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_f3 [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final FMSynthMessage LFO_F3 = new FMSynthMessage("/caustic/${0}/lfo_f3 ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_rate [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..12)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final FMSynthMessage LFO_RATE = new FMSynthMessage("/caustic/${0}/lfo_depth ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_depth [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final FMSynthMessage LFO_DEPTH = new FMSynthMessage(
            "/caustic/${0}/lfo_depth ${1}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/operator [op_index] [command] [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>op_index</strong>: The operator index (0,1,2).</li>
     * <li><strong>control</strong>: The operator control
     * {@link com.teotigraphix.caustk.core.osc.FMSynthMessage.FMOperatorControl}
     * .</li>
     * <li><strong>value</strong>: The specific value for the operator command</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final FMSynthMessage OPERATOR = new FMSynthMessage(
            "/caustic/${0}/operator ${1} ${2} ${3}");

    public static final FMSynthMessage QUERY_OPERATOR = new FMSynthMessage(
            "/caustic/${0}/operator ${1} ${2}");

    FMSynthMessage(String tokenMessage) {
        super(tokenMessage);
    }

    public enum FMOperatorControl {

        /**
         * 0.0..1.0
         */
        Level("level"),

        /**
         * 0,1
         */
        LevelVal("level_vel"),

        /**
         * -4..4
         */
        Octave("octave"),

        /**
         * 0,1
         */
        Fixed("fixed"),

        /**
         * -6..6
         */
        Semis("semis"),

        /**
         * 0,1
         */
        AttackExp("attack_exp"),

        /**
         * 0.0..1.0
         */
        Attack("attack"),

        /**
         * 0,1
         */
        DecayExp("decay_exp"),

        /**
         * 0.0..1.0
         */
        Decay("decay"),

        /**
         * 0,1
         */
        Link("link"),

        /**
         * 0.0..1.0
         */
        Sustain("sustain"),

        /**
         * 0,1
         */
        ReleaseExp("release_exp"),

        /**
         * 0.0..1.0
         */
        Release("release");

        private final String value;

        FMOperatorControl(String value) {
            this.value = value;
        }

        /**
         * Returns the int value of the waveform.
         */
        public String getValue() {
            return value;
        }

        /**
         * Returns a
         * {@link com.teotigraphix.caustk.core.osc.FMSynthMessage.FMOperatorControl}
         * based off the passed String name.
         * 
         * @param type The int type.
         */
        public static FMOperatorControl toType(String type) {
            for (FMOperatorControl result : values()) {
                if (result.getValue().equals(type))
                    return result;
            }
            return null;
        }
    }

    public enum FMAlgorithm {

        /**
         * 3->2->1
         */
        ThreeTwoOne(0),

        /**
         * 1+(3->2)
         */
        OneThreeTwo(1),

        /**
         * (2+3)->1
         */
        TwoThreeOne(2),

        /**
         * 1+2+3
         */
        OneTwoThree(3),

        /**
         * (3->1)+(3->2)
         */
        ThreeOneThreeTwo(4);

        private final int value;

        FMAlgorithm(int value) {
            this.value = value;
        }

        /**
         * Returns the int value of the waveform.
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns a
         * {@link com.teotigraphix.caustk.core.osc.FMSynthMessage.FMAlgorithm}
         * based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static FMAlgorithm toType(Integer type) {
            for (FMAlgorithm result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see com.teotigraphix.caustk.core.osc.FMSynthMessage.FMAlgorithm#toType(Integer)
         */
        public static FMAlgorithm toType(Float type) {
            return toType(type.intValue());
        }
    }
}
