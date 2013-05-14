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

import com.teotigraphix.caustic.core.IRestore;
import com.teotigraphix.caustic.machine.IMachineComponent;

/**
 * The {@link IPCMSampler} interface allows a machine to load and manage
 * external sound sample files such as .WAV.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IPCMSampler extends IMachineComponent, IRestore
{

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    /**
     * Returns a space deliminated String of current sample index numbers loaded
     * in the sampler.
     */
    String getSampleIndicies();

    //----------------------------------
    // activeIndex
    //----------------------------------

    /**
     * The current channel index being edited by the sampler.
     * <p>
     * Note; The properties below are dependent on the index value;
     * <ul>
     * <li>level - {@link IPCMSamplerChannel#getLevel()}</li>
     * <li>tune - {@link IPCMSamplerChannel#getTune()}</li>
     * <li>rootkey - {@link IPCMSamplerChannel#getRootKey()}</li>
     * <li>lowkey - {@link IPCMSamplerChannel#getLowKey()}</li>
     * <li>highkey - {@link IPCMSamplerChannel#getHighKey()}</li>
     * <li>start - {@link IPCMSamplerChannel#getStart()}</li>
     * <li>end - {@link IPCMSamplerChannel#getEnd()}</li>
     * </ul>
     * <p>
     * Values; (0..63)
     * 
     * @see #setActiveIndex(int)
     * @see #loadSample(String)
     * @see #loadChannel(int, String)
     */
    int getActiveIndex();

    /**
     * @see #getActiveIndex()
     */
    void setActiveIndex(int value);

    //----------------------------------
    // activeChannel
    //----------------------------------

    /**
     * Returns the current {@link IPCMSamplerChannel} for the
     * {@link #getActiveIndex()}.
     * <p>
     * This can never be null.
     */
    IPCMSamplerChannel getActiveChannel();

    //----------------------------------
    // name
    //----------------------------------

    /**
     * Returns the name of the sample loaded at the specified channel.
     * <p>
     * Note; This only returns the name of the .wav file that was originally
     * loaded into the sampler and does not contain ".wav" at the end.
     * 
     * @param channel The sampler channel to query against.
     */
    String getSampleName(int channel);

    //--------------------------------------------------------------------------
    //
    // Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Sets the start and end sample points of the channel.
     * 
     * @param channel The sampler channel to edit.
     * @param start The new start in samples.
     * @param end The new end in samples.
     */
    void setChannelSamplePoints(int channel, int start, int end);

    /**
     * Sets the low, high and root keys of the channel.
     * 
     * @param channel The sampler channel to edit.
     * @param low The new low key.
     * @param high The new high key.
     * @param root The new root key.
     */
    void setChannelKeys(int channel, int low, int high, int root);

    /**
     * Sets the level, tune and play mode of the channel.
     * 
     * @param channel The sampler channel to edit.
     * @param level The new level.
     * @param tune The new tune.
     * @param mode The new mode.
     */
    void setChannelProperties(int channel, float level, int tune, PlayMode mode);

    /**
     * Loads a sample at the specified index slot of the sampler.
     * <p>
     * Note: This method will change the current index of the sampler using
     * {@link #setActiveIndex(int)}.
     * </p>
     * 
     * @param channel The channel location of the sample to be loaded.
     * @param path Full path to WAV file.
     */
    IPCMSamplerChannel loadChannel(int channel, String path);

    /**
     * The {@link IPCMSampler} play mode types.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum PlayMode
    {

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
        public int getValue()
        {
            return mValue;
        }

        PlayMode(int value)
        {
            mValue = value;
        }

        /**
         * Returns the PlayMode based on the int value passed.
         * 
         * @param type The play mode integer value.
         */
        public static PlayMode toType(Integer type)
        {
            for (PlayMode result : values())
            {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }
    }

    /**
     * The {@link IPCMSynthSamplerListener} interface allows callbacks when the
     * {@link IPCMSampler} changes the active sample channel.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public interface IPCMSynthSamplerListener
    {

        /**
         * Dispatched when a channel has been changed in the {@link IPCMSampler}
         * using the {@link IPCMSampler#setActiveIndex(int)} method.
         * 
         * @param channel The new channel.
         * @param sample The new channel's sample.
         */
        void onChannelChanged(int channel, IPCMSamplerChannel sample);
    }
}
