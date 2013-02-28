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

package com.teotigraphix.caustic.output;

import com.singlecellsoftware.causticcore.CausticCore;
import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.osc.OutputPanelMessage;
import com.teotigraphix.caustic.rack.IRackAware;
import com.teotigraphix.common.IPersist;

/**
 * The {@link IOutputPanel} interface controls output from the sequencer.
 * <p>
 * The {@link IOutputPanel} can control BPM(Beats per minute), song
 * mode(pattern, song) and start and stop the master sequencer.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IOutputPanel extends IDevice, IPersist, IRackAware {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // bmp
    //----------------------------------

    /**
     * The beats per minute of the master output sequencer.
     * 
     * @see OutputPanelMessage#BPM
     */
    float getBPM();

    /**
     * @see OutputPanelMessage#BPM
     * @see #getBPM()
     */
    void setBPM(float value);

    //----------------------------------
    // mode
    //----------------------------------

    /**
     * The output sequencer mode.
     * 
     * @see #CONTROL_MODE
     */
    Mode getMode();

    /**
     * @see OutputPanelMessage#MODE
     * @see #getMode()
     */
    void setMode(Mode value);

    //--------------------------------------------------------------------------
    //
    // Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Returns whether the outputpanel is playing (<code>true</code>) or stopped
     * (<code>false</code>).
     * <p>
     * The outputpanel is considered playing when the {@link #play()} method has
     * been called. When the {@link #stop()} method is called, the outputpanel
     * is considered not playing and the core sequencer whether in song or
     * pattern mode is not outputing audio data.
     * <p>
     * Note: The {@link #isPlaying()} is managed by the {@link IOutputPanel}
     * interface and is not an OSC command or query from the {@link CausticCore}.
     */
    boolean isPlaying();

    /**
     * Starts the output sequencer in it's current mode (pattern or song) and
     * bpm.
     * 
     * @see #getBPM()
     * @see #getMode()
     * @see Mode
     */
    void play();

    /**
     * Stops the output sequencer.
     */
    void stop();

    /**
     * The outputpanel's song mode, {@link #PATTERN} or {@link #SONG}.
     * 
     * @see IOutputPanel#getMode()
     */
    public enum Mode {

        /**
         * The pattern mode for the outputpanel.
         */
        PATTERN(0),

        /**
         * The song mode for the outputpanel.
         */
        SONG(1);

        private final int mValue;

        Mode(int value) {
            mValue = value;
        }

        /**
         * Returns the int value.
         */
        public int getValue() {
            return mValue;
        }

        /**
         * Returns a {@link Mode} from and int.
         * 
         * @param value The int value.
         */
        public static final Mode toType(int value) {
            return (value == 0) ? PATTERN : SONG;
        }

        /**
         * @see #toType(int)
         */
        public static final Mode toType(float value) {
            return (value == 0.0f) ? PATTERN : SONG;
        }
    }
}
