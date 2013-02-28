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

package com.teotigraphix.caustic.part;

import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.song.IPreset;

/**
 * The ITone interface represents a single playable tone in an {@link IPatch}
 * where the tone generator is an {@link IMachine}.
 * <p>
 * The tone uses the {@link IToneListener} to dispatch {@link TonePropertyKind}
 * messages to the listener about property changes.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ITone extends IMachine, IPreset {

    //--------------------------------------------------------------------------
    // 
    //  Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  type
    //----------------------------------

    /**
     * Returns the machine tone type.
     */
    // XXX can I get rid of this?
    //MachineType getType();

    //----------------------------------
    //  index
    //----------------------------------

    /**
     * Returns the index of the tone within the parent IPatch.
     */
    //int getIndex();

    //----------------------------------
    //  machine
    //----------------------------------

    /**
     * Returns the reference to the {@link IMachine} sound source.
     */
    //IMachine getMachine();

    /**
     * Returns the {@link IPatternSequencer} for the tone.
     * <p>
     * There might be implementations of {@link ITone} where there could be a
     * strategy used with pattern sequencers, it's important to access the
     * pattern sequencer through this getter.
     * </p>
     */
    //IPatternSequencer getSequencer();

    //----------------------------------
    //  enabled
    //----------------------------------

    /**
     * Whether the tone is enabled.
     * <p>
     * Implementations will vary on what exactly enabled means for the tone.
     * </p>
     */
    boolean isEnabled();

    /**
     * @see #isEnabled()
     */
    void setEnabled(boolean value);

    //----------------------------------
    //  muted
    //----------------------------------

    /**
     * Whether the tone is muted or playing.
     */
    boolean isMuted();

    /**
     * @see #isMuted()
     */
    void setMuted(boolean value);

    //----------------------------------
    //  selected
    //----------------------------------

    /**
     * Whether the tone is selected.
     */
    boolean getSelected();

    /**
     * @see #getSelected()
     */
    void setSelected(boolean value);

    //----------------------------------
    //  data
    //----------------------------------

    /**
     * The extra data associated with the tone.
     */
    //ToneData getData();

    /**
     * @see #getData()
     */
    //void setData(ToneData value);

    //--------------------------------------------------------------------------
    // 
    //  Listeners
    // 
    //--------------------------------------------------------------------------

    /**
     * Tone property change kinds.
     */
    public enum TonePropertyKind {
        ENABLED, SELECTED, MUTE, SOLO, DATA, NAME, PRESET_BANK, MACHINE;
    }

    // XXX Put property change event in Machine? then anything
    // that wanted to dispatch property change events in the framework could
    // without creating a million interfaces? But what about overkill where
    // one listener is listeneing to things they don't want?

    /**
     * Sets the single listener for the tone.
     * 
     * @param listener The {@link IToneListener}.
     */
    //void setToneListener(IToneListener listener);

    /**
     * The IToneListener listens for property changes on the {@link ITone}.
     */
    public interface IToneListener {

        /**
         * Fired when a property of {@link TonePropertyKind} changes on the
         * tone.
         * 
         * @param kind The property kind.
         * @param value The new value.
         * @param oldValue The old value.
         */
        void onPropertyChange(TonePropertyKind kind, ITone tone, Object value);
    }
}
