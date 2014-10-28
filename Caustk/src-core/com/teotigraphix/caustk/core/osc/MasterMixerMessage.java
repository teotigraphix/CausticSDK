////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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
 * The {@link MasterMixerMessage} holds all OSC messages associated with the
 * {@link Master} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class MasterMixerMessage extends CausticMessage {

    //--------------------------------------------------------------------------
    // Master Delay
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/master/delay/bypass [value]</code>
     * <p>
     * All effects have this parameter.
     * <p>
     * <strong>Default</strong>: <code>1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0,1) 0 <code>Off</code>, 1 <code>On</code>.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final MasterMixerMessage DELAY_BYPASS = new MasterMixerMessage(
            "/caustic/master/delay/bypass ${0}");

    /**
     * Message: <code>/caustic/master/delay/steps [value]</code>
     * <p>
     * <strong>Default</strong>: <code>2</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (1,2,3,4,5).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final MasterMixerMessage DELAY_STEPS = new MasterMixerMessage(
            "/caustic/master/delay/steps ${0}");

    /**
     * Message: <code>/caustic/master/delay/loop [value]</code>
     * <p>
     * <strong>Default</strong>: <code>2</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0,1).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final MasterMixerMessage DELAY_LOOP = new MasterMixerMessage(
            "/caustic/master/delay/loop ${0}");

    /**
     * Message: <code>/caustic/master/delay/time [value]</code>
     * <p>
     * <strong>Default</strong>: <code>8</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (1..12).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final MasterMixerMessage DELAY_TIME = new MasterMixerMessage(
            "/caustic/master/delay/time ${0}");

    /**
     * Message: <code>/caustic/master/delay/sync [value]</code>
     * <p>
     * Toggles between tempo-sync'ed integer values for time or non-sync'ed
     * float values
     * <p>
     * <strong>Default</strong>: <code>1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0,1).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final MasterMixerMessage DELAY_SYNC = new MasterMixerMessage(
            "/caustic/master/delay/sync ${0}");

    /**
     * Message: <code>/caustic/master/delay/feedback [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.5</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0,1).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final MasterMixerMessage DELAY_FEEDBACK = new MasterMixerMessage(
            "/caustic/master/delay/feedback ${0}");

    /**
     * Message: <code>/caustic/master/delay/feedback_first [value]</code>
     * <p>
     * Controls whether the first echo gets attenuated by the feedback amount or
     * not
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0,1).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final MasterMixerMessage DELAY_FEEDBACK_FIRST = new MasterMixerMessage(
            "/caustic/master/delay/feedback_first ${0}");

    /**
     * Message: <code>/caustic/master/delay/damping [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..1).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage DELAY_DAMPING = new MasterMixerMessage(
            "/caustic/master/delay/damping ${0}");

    /**
     * Message: <code>/caustic/master/delay/wet [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.5</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..1).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage DELAY_WET = new MasterMixerMessage(
            "/caustic/master/delay/wet ${0}");

    /**
     * Message: <code>/caustic/master/delay/pan [step] [value]</code>
     * <p>
     * StepIndex panning where step index is 0,1,2,3,4 depending on how many
     * steps are enabled and panning is [-1..1] with 0 = dead center.
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>step</strong>: (0,1,2,3,4).</li>
     * <li><strong>value</strong>: (-1..1).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage DELAY_PAN = new MasterMixerMessage(
            "/caustic/master/delay/pan ${0} ${1}");

    public static final MasterMixerMessage QUERY_DELAY_PAN = new MasterMixerMessage(
            "/caustic/master/delay/pan ${0}");

    //--------------------------------------------------------------------------
    // Master Reverb
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/master/reverb/bypass [value]</code>
     * <p>
     * All effects have this parameter.
     * <p>
     * <strong>Default</strong>: <code>1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0,1) 0 <code>Off</code>, 1 <code>On</code>.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final MasterMixerMessage REVERB_BYPASS = new MasterMixerMessage(
            "/caustic/master/reverb/bypass ${0}");

    /**
     * Message: <code>/caustic/master/reverb/pre_delay [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.02</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0.. 0.1) seconds.
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage REVERB_PRE_DELAY = new MasterMixerMessage(
            "/caustic/master/reverb/pre_delay ${0}");

    /**
     * Message: <code>/caustic/master/reverb/room_size [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.75</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..1).
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage REVERB_ROOM_SIZE = new MasterMixerMessage(
            "/caustic/master/reverb/room_size ${0}");

    /**
     * Message: <code>/caustic/master/reverb/hf_damping [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.156</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..0.8).
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage REVERB_HF_DAMPING = new MasterMixerMessage(
            "/caustic/master/reverb/hf_damping ${0}");

    /**
     * Message: <code>/caustic/master/reverb/diffuse [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.7</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..0.7).
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage REVERB_DIFFUSE = new MasterMixerMessage(
            "/caustic/master/reverb/diffuse ${0}");

    /**
     * Message: <code>/caustic/master/reverb/dither_echoes [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0,1).
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final MasterMixerMessage REVERB_DITHER_ECHOS = new MasterMixerMessage(
            "/caustic/master/reverb/dither_echoes ${0}");

    /**
     * Message: <code>/caustic/master/reverb/er_gain [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..1).
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage REVERB_ER_GAIN = new MasterMixerMessage(
            "/caustic/master/reverb/er_gain ${0}");

    /**
     * Message: <code>/caustic/master/reverb/er_decay [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.25</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..1).
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage REVERB_ER_DECAY = new MasterMixerMessage(
            "/caustic/master/reverb/er_decay ${0}");

    /**
     * Message: <code>/caustic/master/reverb/stereo_delay [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.5</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..1).
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage REVERB_STEREO_DELAY = new MasterMixerMessage(
            "/caustic/master/reverb/stereo_delay ${0}");

    /**
     * Message: <code>/caustic/master/reverb/stereo_spread [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.25</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..1).
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage REVERB_STEREO_SPREAD = new MasterMixerMessage(
            "/caustic/master/reverb/wet ${0}");

    /**
     * Message: <code>/caustic/master/reverb/stereo_spread [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.25</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..0.5) note this one doesn't go to 1, 0.5
     * is plenty.
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage REVERB_WET = new MasterMixerMessage(
            "/caustic/master/reverb/wet ${0}");

    //--------------------------------------------------------------------------
    // Master EQ
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/master/eq/bypass [value]</code>
     * <p>
     * All effects have this parameter.
     * <p>
     * <strong>Default</strong>: <code>1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0,1) 0 <code>Off</code>, 1 <code>On</code>.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final MasterMixerMessage EQ_BYPASS = new MasterMixerMessage(
            "/caustic/master/eq/bypass ${0}");

    /**
     * Message: <code>/caustic/master/eq/bass [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1.1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..2).
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage EQ_BASS = new MasterMixerMessage(
            "/caustic/master/eq/bass ${0}");

    /**
     * Message: <code>/caustic/master/eq/bassmid_freq [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.5</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..1).
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage EQ_BASSMID_FREQ = new MasterMixerMessage(
            "/caustic/master/eq/bassmid_freq ${0}");

    /**
     * Message: <code>/caustic/master/eq/mid [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..2).
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage EQ_MID = new MasterMixerMessage(
            "/caustic/master/eq/mid ${0}");

    /**
     * Message: <code>/caustic/master/eq/midhigh_freq [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.5</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..1).
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage EQ_MIDHIGH_FREQ = new MasterMixerMessage(
            "/caustic/master/eq/midhigh_freq ${0}");

    /**
     * Message: <code>/caustic/master/eq/high [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1.1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..2).
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage EQ_HIGH = new MasterMixerMessage(
            "/caustic/master/eq/high ${0}");

    //--------------------------------------------------------------------------
    // Master Limiter
    //--------------------------------------------------------------------------

    /**
     * Message: <code>/caustic/master/limiter/bypass [value]</code>
     * <p>
     * All effects have this parameter.
     * <p>
     * <strong>Default</strong>: <code>1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0,1) 0 <code>Off</code>, 1 <code>On</code>.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final MasterMixerMessage LIMITER_BYPASS = new MasterMixerMessage(
            "/caustic/master/limiter/bypass ${0}");

    /**
     * Message: <code>/caustic/master/limiter/pre [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..8).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final MasterMixerMessage LIMITER_PRE = new MasterMixerMessage(
            "/caustic/master/limiter/pre ${0}");

    /**
     * Message: <code>/caustic/master/limiter/attack [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.02</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..0.1) seconds.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage LIMITER_ATTACK = new MasterMixerMessage(
            "/caustic/master/limiter/attack ${0}");

    /**
     * Message: <code>/caustic/master/limiter/release [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.25</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..0.5) seconds.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage LIMITER_RELEASE = new MasterMixerMessage(
            "/caustic/master/limiter/release ${0}");

    /**
     * Message: <code>/caustic/master/limiter/post [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..2).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage LIMITER_POST = new MasterMixerMessage(
            "/caustic/master/limiter/post ${0}");

    /**
     * Message: <code>/caustic/master/volume [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0..2).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MasterMixerMessage VOLUME = new MasterMixerMessage(
            "/caustic/master/volume ${0}");

    /**
     * Not IMPL.
     */
    public static final MasterMixerMessage VOLUME_BYPASS = new MasterMixerMessage(
            "/caustic/master/volume/bypass ${0}");

    MasterMixerMessage(String message) {
        super(message);
    }

    public enum MasterMixerControl implements IOSCControl {

        DelayBypass,

        DelayDamping,

        DelayFeedback,

        DelayFeedbackFirst,

        DelayLoop,

        DelayPan,

        DelaySteps,

        DelaySync,

        DelayTime,

        DelayWet,

        EqBass,

        EqBassMidFreq,

        EqBypass,

        EqHigh,

        EqMid,

        EqMidHighFreq,

        LimiterAttack,

        LimiterBypass,

        LimiterPost,

        LimiterPre,

        LimiterRelease,

        ReverbBypass,

        ReverbDiffuse,

        ReverbDitherEchos,

        ReverbErDecay,

        ReverbErGain,

        ReverbHfDamping,

        ReverbPreDelay,

        ReverbRoomSize,

        ReverbStereoDelay,

        ReverbStereoSpread,

        ReverbWet,

        Volume,

        VolumeBypass;

        @Override
        public String getDisplayName() {
            return name();
        }
    }
}
