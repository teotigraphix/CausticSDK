////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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
 * The {@link MixerMessage} holds all OSC messages associated with the
 * {@link IMixerPanel} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class MixerMessage extends CausticMessage {

    //----------------------------------
    // Delay
    //----------------------------------

    /**
     * Message: <code>/caustic/mixer/delay/feedback [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0.0..1.0) The new value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IMixerPanel#getDelay()
     * @see IMixerDelay#getFeedback()
     * @see IMixerDelay#setFeedback(float)
     */
    public static final MixerMessage DELAY_UNIT_FEEDBACK = new MixerMessage(
            "/caustic/mixer/delay/feedback ${0}");

    /**
     * Message: <code>/caustic/mixer/delay/stereo [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0,1) 0 for <code>off</code>, 1 for
     * <code>on</code>.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see IMixerPanel#getDelay()
     * @see IMixerDelay#isStereo()
     * @see IMixerDelay#setStereo(boolean)
     */
    public static final MixerMessage DELAY_UNIT_STEREO = new MixerMessage(
            "/caustic/mixer/delay/stereo ${0}");

    /**
     * Message: <code>/caustic/mixer/delay/time [value]</code>
     * <p>
     * <strong>Default</strong>: <code>7</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (1..8) The new value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see IMixerPanel#getDelay()
     * @see IMixerDelay#getTime()
     * @see IMixerDelay#setTime(int)
     */
    public static final MixerMessage DELAY_UNIT_TIME = new MixerMessage(
            "/caustic/mixer/delay/time ${0}");

    //----------------------------------
    // Reverb
    //----------------------------------

    /**
     * Message: <code>/caustic/mixer/reverb/damping [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.2</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0.0..1.0) The new value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IMixerPanel#getReverb()
     * @see IMixerReverb#getDamping()
     * @see IMixerReverb#setDamping(float)
     */
    public static final MixerMessage REVERB_UNIT_DAMPING = new MixerMessage(
            "/caustic/mixer/reverb/damping ${0}");

    /**
     * Message: <code>/caustic/mixer/reverb/room [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.75</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0.0..1.0) The new value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IMixerPanel#getReverb()
     * @see IMixerReverb#getRoom()
     * @see IMixerReverb#setRoom(float)
     */
    public static final MixerMessage REVERB_UNIT_ROOM = new MixerMessage(
            "/caustic/mixer/reverb/room ${0}");

    /**
     * Message: <code>/caustic/mixer/reverb/stereo [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0,1) 0 for <code>off</code>, 1 for
     * <code>on</code>.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see IMixerPanel#getReverb()
     * @see IMixerReverb#isStereo()
     * @see IMixerReverb#setStereo(int)
     */
    public static final MixerMessage REVERB_UNIT_STEREO = new MixerMessage(
            "/caustic/mixer/reverb/stereo ${0}");

    //----------------------------------
    // EQ
    //----------------------------------

    /**
     * Message: <code>/caustic/mixer/[machine_index]/eq_bass [value]</code>
     * <p>
     * <strong>Note:</strong> To target the master channel
     * <code>machine_index</code> will need to contain the String
     * <code>"master"</code>.
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index in the rack or the
     * <code>"master"</code> identifier.</li>
     * <li><strong>value</strong>: (-1.0..1.0) The new value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IMixerPanel#getMasterBass()
     * @see IMixerPanel#setMasterBass(float)
     * @see IMixerPanel#getBass(int)
     * @see IMixerPanel#setBass(int, float)
     */
    public static final MixerMessage EQ_BASS = new MixerMessage("/caustic/mixer/${0}/eq_bass ${1}");

    /**
     * Message: <code>/caustic/mixer/[machine_index]/eq_mid [value]</code>
     * <p>
     * <strong>Note:</strong> To target the master channel
     * <code>machine_index</code> will need to contain the String
     * <code>"master"</code>.
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index in the rack or the
     * <code>"master"</code> identifier.</li>
     * <li><strong>value</strong>: (-1.0..1.0) The new value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IMixerPanel#getMasterMid()
     * @see IMixerPanel#setMasterMid(float)
     * @see IMixerPanel#getMid(int)
     * @see IMixerPanel#setMid(int, float)
     */
    public static final MixerMessage EQ_MID = new MixerMessage("/caustic/mixer/${0}/eq_mid ${1}");

    /**
     * Message: <code>/caustic/mixer/[machine_index]/eq_high [value]</code>
     * <p>
     * <strong>Note:</strong> To target the master channel
     * <code>machine_index</code> will need to contain the String
     * <code>"master"</code>.
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index in the rack or the
     * <code>"master"</code> identifier.</li>
     * <li><strong>value</strong>: (-1.0..1.0) The new value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IMixerPanel#getMasterHigh()
     * @see IMixerPanel#setMasterHigh(float)
     * @see IMixerPanel#getHigh(int)
     * @see IMixerPanel#setHigh(int, float)
     */
    public static final MixerMessage EQ_HIGH = new MixerMessage("/caustic/mixer/${0}/eq_high ${1}");

    /**
     * Message: <code>/caustic/mixer/[machine_index]/delay_send [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index in the rack.</li>
     * <li><strong>value</strong>: (0.0..1.0) The new value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IMixerPanel#getDelaySend(int)
     * @see IMixerPanel#setDelaySend(int, float)
     */
    public static final MixerMessage DELAY_SEND = new MixerMessage(
            "/caustic/mixer/${0}/delay_send ${1}");

    /**
     * Message: <code>/caustic/mixer/[machine_index]/reverb_send [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index in the rack.</li>
     * <li><strong>value</strong>: (0.0..1.0) The new value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IMixerPanel#getReverbSend(int)
     * @see IMixerPanel#setReverbSend(int, float)
     */
    public static final MixerMessage REVERB_SEND = new MixerMessage(
            "/caustic/mixer/${0}/reverb_send ${1}");

    /**
     * Message: <code>/caustic/mixer/[machine_index]/pan [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index in the rack.</li>
     * <li><strong>value</strong>: (-1.0..1.0) The new value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IMixerPanel#getPan(int)
     * @see IMixerPanel#setPan(int, float)
     */
    public static final MixerMessage PAN = new MixerMessage("/caustic/mixer/${0}/pan ${1}");

    /**
     * Message: <code>/caustic/mixer/[machine_index]/stereo_width [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index in the rack.</li>
     * <li><strong>value</strong>: (0.0..1.0) The new value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IMixerPanel#getStereoWidth(int)
     * @see IMixerPanel#setStereoWidth(int, float)
     */
    public static final MixerMessage STEREO_WIDTH = new MixerMessage(
            "/caustic/mixer/${0}/stereo_width ${1}");

    /**
     * Message: <code>/caustic/mixer/[machine_index]/mute [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index in the rack.</li>
     * <li><strong>value</strong>: (0,1) The new value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see IMixerPanel#isMute(int)
     * @see IMixerPanel#setMute(int, boolean)
     */
    public static final MixerMessage MUTE = new MixerMessage("/caustic/mixer/${0}/mute ${1}");

    /**
     * Message: <code>/caustic/mixer/[machine_index]/solo [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index in the rack.</li>
     * <li><strong>value</strong>: (0,1) The new value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see IMixerPanel#isSolo(int)
     * @see IMixerPanel#setSolo(int, boolean)
     */
    public static final MixerMessage SOLO = new MixerMessage("/caustic/mixer/${0}/solo ${1}");

    /**
     * Message: <code>/caustic/mixer/[machine_index]/eq_volume [value]</code>
     * <p>
     * <strong>Note:</strong> To target the master channel
     * <code>machine_index</code> will need to contain the String
     * <code>"master"</code>.
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index in the rack or the
     * <code>"master"</code> identifier.</li>
     * <li><strong>value</strong>: (0.0..2.0) The new value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IMixerPanel#getMasterVolume()
     * @see IMixerPanel#setMasterVolume(float)
     * @see IMixerPanel#getVolume(int)
     * @see IMixerPanel#setVolume(int, float)
     */
    public static final MixerMessage VOLUME = new MixerMessage("/caustic/mixer/${0}/volume ${1}");

    public MixerMessage(String message) {
        super(message);
    }

}
