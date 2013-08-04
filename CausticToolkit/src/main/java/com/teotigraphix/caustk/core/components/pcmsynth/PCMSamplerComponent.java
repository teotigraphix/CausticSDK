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

package com.teotigraphix.caustk.core.components.pcmsynth;

import java.util.Map;
import java.util.TreeMap;

import com.teotigraphix.caustk.core.components.ToneComponent;
import com.teotigraphix.caustk.core.osc.PCMSamplerMessage;
import com.teotigraphix.caustk.tone.Tone;

public class PCMSamplerComponent extends ToneComponent {

    private static final int NUM_SAMPLER_CHANNELS = 64;

    private Map<Integer, PCMSamplerChannel> channels;

    @Override
    public void setTone(Tone value) {
        super.setTone(value);
        if (channels == null) {
            createChannels();
        }
    }

    //--------------------------------------------------------------------------
    //
    // IPCMSampler API :: Properties
    //
    //--------------------------------------------------------------------------

    public String getSampleIndicies() {
        return PCMSamplerMessage.QUERY_SAMPLE_INDICIES.queryString(getEngine(), getToneIndex());
    }

    //----------------------------------
    // currentChannel
    //----------------------------------

    private int activeIndex = 0;

    private PCMSamplerChannel currentSample;

    //private IPCMSynthSamplerListener mListener;

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int value) {
        currentSample = getPCMSample(activeIndex);

        if (value == activeIndex)
            return;
        if (value < 0 || value >= NUM_SAMPLER_CHANNELS)
            throw newRangeException(PCMSamplerMessage.SAMPLE_INDEX.toString(), "0..63", value);

        activeIndex = value;

        PCMSamplerMessage.SAMPLE_INDEX.send(getEngine(), getToneIndex(), activeIndex);

        //        fireSampleChanged(mActiveIndex, mCurrentSample);
    }

    //----------------------------------
    // currentSample
    //----------------------------------

    public PCMSamplerChannel getActiveChannel() {
        return currentSample;
    }

    public String getSampleName(int channel) {
        setActiveIndex(channel);
        return PCMSamplerMessage.QUERY_SAMPLE_NAME.queryString(getEngine(), getToneIndex());
    }

    public PCMSamplerComponent() {
    }

    //--------------------------------------------------------------------------
    // API :: Methods
    //--------------------------------------------------------------------------

    public void setChannelSamplePoints(int channel, int start, int end) {
        setActiveIndex(channel);
        getActiveChannel().setStart(start);
        getActiveChannel().setEnd(end);
    }

    public void setChannelKeys(int channel, int low, int high, int root) {
        setActiveIndex(channel);
        getActiveChannel().setLowKey(low);
        getActiveChannel().setHighKey(high);
        getActiveChannel().setRootKey(root);
    }

    public void setChannelProperties(int channel, float level, int tune, PlayMode mode) {
        setActiveIndex(channel);
        getActiveChannel().setLevel(level);
        getActiveChannel().setTune(tune);
        getActiveChannel().setMode(mode);
    }

    public PCMSamplerChannel loadChannel(int index, String path) {
        setActiveIndex(index);
        PCMSamplerChannel result = getActiveChannel();
        loadSample(path);
        result.restore();
        return result;
    }

    @Override
    public void restore() {
        // the OSC for the sampler is kindof weird in that you have to
        // set the active index first and then issue commands. During the
        // restore, this index gets changed, we need to put it back where it was
        int old = getActiveIndex();
        String indicies = getSampleIndicies();
        if (indicies == null || indicies.equals(""))
            return;

        String[] split = indicies.split(" ");
        for (int i = 0; i < split.length; i++) {
            channels.get(Integer.parseInt(split[i])).restore();
        }
        setActiveIndex(old);
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    void loadSample(String absolutPath) {
        PCMSamplerMessage.SAMPLE_LOAD.send(getEngine(), getToneIndex(), absolutPath);
    }

    protected final PCMSamplerChannel getPCMSample(int index) {
        return channels.get(index);
    }

    protected final void fireSampleChanged(int channel, PCMSamplerChannel sample) {
        //        if (mListener != null) {
        //            mListener.onChannelChanged(channel, sample);
        //        }
    }

    protected void createChannels() {
        int numChannels = 64;
        channels = new TreeMap<Integer, PCMSamplerChannel>();
        for (int i = 0; i < numChannels; i++) {
            PCMSamplerChannel channel = new PCMSamplerChannel(this);
            channel.setTone(getTone());
            channel.setIndex(i);
            channels.put(i, channel);
        }
        // do this manually at startup
        currentSample = getPCMSample(activeIndex);
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
