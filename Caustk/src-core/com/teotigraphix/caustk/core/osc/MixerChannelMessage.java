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

import com.teotigraphix.caustk.node.machine.patch.MixerChannel;

/**
 * The {@link MixerChannelMessage} holds all OSC messages associated with the
 * {@link MixerChannel} API for each machine.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class MixerChannelMessage extends CausticMessage {

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
     */
    public static final MixerChannelMessage EQ_BASS = new MixerChannelMessage(
            "/caustic/mixer/${0}/eq_bass ${1}");

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
     */
    public static final MixerChannelMessage EQ_MID = new MixerChannelMessage(
            "/caustic/mixer/${0}/eq_mid ${1}");

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
     */
    public static final MixerChannelMessage EQ_HIGH = new MixerChannelMessage(
            "/caustic/mixer/${0}/eq_high ${1}");

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
     */
    public static final MixerChannelMessage DELAY_SEND = new MixerChannelMessage(
            "/caustic/mixer/${0}/delay_send ${1}");

    /**
     * Message: <code>/caustic/mixer/[machine_index]/reverb_send [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index in the rack.</li>
     * <li><strong>value</strong>: (0.0..0.5) The new value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MixerChannelMessage REVERB_SEND = new MixerChannelMessage(
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
     */
    public static final MixerChannelMessage PAN = new MixerChannelMessage(
            "/caustic/mixer/${0}/pan ${1}");

    /**
     * Message: <code>/caustic/mixer/[machine_index]/stereo_width [value]</code>
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
     */
    public static final MixerChannelMessage STEREO_WIDTH = new MixerChannelMessage(
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
     */
    public static final MixerChannelMessage MUTE = new MixerChannelMessage(
            "/caustic/mixer/${0}/mute ${1}");

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
     */
    public static final MixerChannelMessage SOLO = new MixerChannelMessage(
            "/caustic/mixer/${0}/solo ${1}");

    /**
     * Message: <code>/caustic/mixer/[machine_index]/eq_volume [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index in the rack or the
     * <code>"master"</code> identifier.</li>
     * <li><strong>value</strong>: (0.0..2.0) The new value.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final MixerChannelMessage VOLUME = new MixerChannelMessage(
            "/caustic/mixer/${0}/volume ${1}");

    public MixerChannelMessage(String message) {
        super(message);
    }

    /**
     * Controls for the {@link MixerChannelMessage}.
     * 
     * @author Michael Schmalle
     * @since 1.0
     */
    public enum MixerChannelControl implements IOSCControl {

        DelaySend,

        Bass,

        High,

        Mid,

        Mute,

        Pan,

        ReverbSend,

        Solo,

        StereoWidth,

        Volume;

        private MixerChannelControl() {
        }
    }
}
