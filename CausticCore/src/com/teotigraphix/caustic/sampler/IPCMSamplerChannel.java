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

package com.teotigraphix.caustic.sampler;

import com.teotigraphix.caustic.machine.IMachineComponent;
import com.teotigraphix.caustic.sampler.IPCMSampler.PlayMode;
import com.teotigraphix.common.IPersist;
import com.teotigraphix.common.IRestore;

/**
 * The {@link IPCMSamplerChannel} API allows the {@link IPCMSampler} to abstract
 * it's contained channel samples into restorable and persistable entities.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IPCMSamplerChannel extends IMachineComponent, IPersist, IRestore {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    /**
     * Returns whether the channel a sample associated with it.
     */
    boolean hasSample();

    //----------------------------------
    // index
    //----------------------------------

    /**
     * The channel index locating the channel within it's parent
     * {@link IPCMSampler}.
     */
    int getIndex();

    //----------------------------------
    // name
    //----------------------------------

    /**
     * The name of the sample held within the channel.
     */
    String getName();

    //----------------------------------
    // level
    //----------------------------------

    /**
     * The current channel's level setting.
     */
    float getLevel();

    /**
     * @see #getLevel()
     */
    void setLevel(float value);

    //----------------------------------
    // tune
    //----------------------------------

    /**
     * The current channel's tuning setting in cents.
     */
    int getTune();

    /**
     * @see #getTune()
     */
    void setTune(int value);

    //----------------------------------
    // rootKey
    //----------------------------------

    /**
     * The current channel's root key setting MIDI note number.
     */
    int getRootKey();

    /**
     * @see #getRootKey()
     */
    void setRootKey(int value);

    //----------------------------------
    // lowKey
    //----------------------------------

    /**
     * The current channel's low key setting MIDI note number.
     */
    int getLowKey();

    /**
     * @see #getLowKey()
     */
    void setLowKey(int value);

    //----------------------------------
    // highKey
    //----------------------------------

    /**
     * The current channel's high key setting MIDI note number.
     */
    int getHighKey();

    /**
     * @see #getHighKey()
     */
    void setHighKey(int value);

    //----------------------------------
    // mode
    //----------------------------------

    /**
     * The current channel's play mode setting.
     * <p>
     * Values; (0..5), default; 0
     * </p>
     * 
     * @see PlayMode#PLAY_ONCE
     * @see PlayMode#NOTE_ON_OFF
     * @see PlayMode#LOOP_FWD
     * @see PlayMode#LOOP_FWD_BACK
     * @see PlayMode#INTRO_LOOP_FWD
     * @see PlayMode#INTRO_LOOP_FWD_BACK
     */
    PlayMode getMode();

    /**
     * @see #getMode()
     */
    void setMode(PlayMode value);

    //----------------------------------
    // start
    //----------------------------------

    /**
     * The current channel's start sample value.
     */
    int getStart();

    /**
     * @see #getStart()
     */
    void setStart(int value);

    //----------------------------------
    // end
    //----------------------------------

    /**
     * The current channel's end sample value.
     */
    int getEnd();

    /**
     * @see #getEnd()
     */
    void setEnd(int value);
}
