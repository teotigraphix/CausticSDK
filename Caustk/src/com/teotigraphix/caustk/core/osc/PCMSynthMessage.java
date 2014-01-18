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


public class PCMSynthMessage extends CausticMessage {

    //--------------------------------------------------------------------------
    // Volume
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/volume_out [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1.0</code> {@link ISubSynth},
     * <code>2.0</code> {@link IPCMSynth}.
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..2); (0..8 {@link IPCMSynth}) The volume
     * out value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PCMSynthMessage VOLUME_OUT = new PCMSynthMessage(
            "/caustic/${0}/volume_out ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/volume_attack [value]</code>
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
    public static final PCMSynthMessage VOLUME_ATTACK = new PCMSynthMessage(
            "/caustic/${0}/volume_attack ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/volume_decay [value]</code>
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
    public static final PCMSynthMessage VOLUME_DECAY = new PCMSynthMessage(
            "/caustic/${0}/volume_decay ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/volume_sustain [value]</code>
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
    public static final PCMSynthMessage VOLUME_SUSTAIN = new PCMSynthMessage(
            "/caustic/${0}/volume_sustain ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/volume_release [value]</code>
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
    public static final PCMSynthMessage VOLUME_RELEASE = new PCMSynthMessage(
            "/caustic/${0}/volume_release ${1}");

    //--------------------------------------------------------------------------
    // LFO
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/lfo_depth [value]</code>
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
    public static final PCMSynthMessage LFO_DEPTH = new PCMSynthMessage(
            "/caustic/${0}/lfo_depth ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_rate [value]</code>
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
    public static final PCMSynthMessage LFO_RATE = new PCMSynthMessage(
            "/caustic/${0}/lfo_rate ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_target [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final PCMSynthMessage LFO_TARGET = new PCMSynthMessage(
            "/caustic/${0}/lfo_target ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_waveform [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final PCMSynthMessage LFO_WAVEFORM = new PCMSynthMessage(
            "/caustic/${0}/lfo_waveform ${1}");

    //--------------------------------------------------------------------------
    // Pitch
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/pitch_octave [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-4..4)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final PCMSynthMessage PITCH_OCTAVE = new PCMSynthMessage(
            "/caustic/${0}/pitch_octave ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/pitch_semis [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-12..12)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final PCMSynthMessage PITCH_SEMIS = new PCMSynthMessage(
            "/caustic/${0}/pitch_semis ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/pitch_cents [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-50..50)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final PCMSynthMessage PITCH_CENTS = new PCMSynthMessage(
            "/caustic/${0}/pitch_cents ${1}");

    //--------------------------------------------------------------------------
    // Sampler
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/sample_index [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0-60)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final PCMSynthMessage SAMPLE_INDEX = new PCMSynthMessage(
            "/caustic/${0}/sample_index ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_load [path]</code>
     * <p>
     * <strong>Default</strong>: <code>1.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>path</strong>: Full path to the WAV file.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     */
    public static final PCMSynthMessage SAMPLE_LOAD = new PCMSynthMessage(
            "/caustic/${0}/sample_load ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_level [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PCMSynthMessage SAMPLE_LEVEL = new PCMSynthMessage(
            "/caustic/${0}/sample_level ${1}");

    // XXX Impl SAMPLE_PAN
    /**
     * Message: <code>/caustic/[machine_index]/sample_pan [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.5</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PCMSynthMessage SAMPLE_PAN = new PCMSynthMessage(
            "/caustic/${0}/sample_pan ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_tune [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-50.0..50.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PCMSynthMessage SAMPLE_TUNE = new PCMSynthMessage(
            "/caustic/${0}/sample_tune ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_rootkey [value]</code>
     * <p>
     * <strong>Default</strong>: <code>60</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (24..108)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final PCMSynthMessage SAMPLE_ROOTKEY = new PCMSynthMessage(
            "/caustic/${0}/sample_rootkey ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_lowkey [value]</code>
     * <p>
     * <strong>Default</strong>: <code>24</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (24..108)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final PCMSynthMessage SAMPLE_LOWKEY = new PCMSynthMessage(
            "/caustic/${0}/sample_lowkey ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_highkey [value]</code>
     * <p>
     * <strong>Default</strong>: <code>108</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (24..108)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final PCMSynthMessage SAMPLE_HIGHKEY = new PCMSynthMessage(
            "/caustic/${0}/sample_highkey ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_mode [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..5)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final PCMSynthMessage SAMPLE_MODE = new PCMSynthMessage(
            "/caustic/${0}/sample_mode ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_start [value]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: Sample start.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PCMSynthMessage SAMPLE_START = new PCMSynthMessage(
            "/caustic/${0}/sample_start ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sample_end [value]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: Sample end.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final PCMSynthMessage SAMPLE_END = new PCMSynthMessage(
            "/caustic/${0}/sample_end ${1}");

    /**
     * Query: <code>/caustic/[machine_index]/[channel_num]/sample_name</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>String</code> The name of the sample
     * located at the active index number, <code>""</code> if a sample has not
     * been assigned.
     */
    public static final PCMSynthMessage QUERY_SAMPLE_NAME = new PCMSynthMessage(
            "/caustic/${0}/sample_name");

    /**
     * Query: <code>/caustic/[machine_index]/sample_indices</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>String</code> A space deliminated list of
     * index numbers that currently hold samples within the sampler.
     */
    public static final PCMSynthMessage QUERY_SAMPLE_INDICIES = new PCMSynthMessage(
            "/caustic/${0}/sample_indices");

    PCMSynthMessage(String message) {
        super(message);
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

    public enum LFO1Target {

        /**
         * No LFO.
         */
        None(0),

        /**
         * Pitch oscillation.
         */
        Pitch(1),

        /**
         * Cutoff frequency oscillation.
         */
        Cutoff(2),

        /**
         * Volume amplitude oscillation.
         */
        Voume(3);

        private final int value;

        LFO1Target(int value) {
            this.value = value;
        }

        /**
         * Returns int the value of the FLO.
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

    public enum PlayMode {

        /**
         * Plays the full sample each time a note is played.
         */
        PLAY_ONCE(0),

        /**
         * Plays the sample as long as the note is held.
         */
        NOTE_ON_OFF(1),

        /**
         * Plays the sample starting at the Loop Start Point(13) until it
         * reaches the Loop End Point (14), at which point it loops around to
         * the Start Loop Point again, and does this until the note is released.
         */
        LOOP_FWD(2),

        /**
         * Plays the sample starting at the Loop Start Point(13) until it
         * reaches the Loop End Point (14), at which point it start playing in
         * reverse until it reaches the Start Loop Point again, and does this
         * until the note is released.
         */
        LOOP_FWD_BACK(3),

        /**
         * Same looping behavior as "Loop Forward" except playback always starts
         * at the beginning of the sample when the note is first played.
         */
        INTRO_LOOP_FWD(4),

        /**
         * Same looping behavior as "Loop Forward-Back" except playback always
         * starts at the beginning of the sample when the note is first played.
         */
        INTRO_LOOP_FWD_BACK(5);

        private final int mValue;

        /**
         * Returns the int value of the play mode.
         */
        public int getValue() {
            return mValue;
        }

        PlayMode(int value) {
            mValue = value;
        }

        /**
         * Returns the PlayMode based on the int value passed.
         * 
         * @param type The play mode integer value.
         */
        public static PlayMode toType(Integer type) {
            for (PlayMode result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }
    }

}
