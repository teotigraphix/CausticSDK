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

package com.teotigraphix.caustic.internal.sampler;

import java.util.Map;
import java.util.TreeMap;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.internal.machine.MachineComponent;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.PCMSamplerMessage;
import com.teotigraphix.caustic.sampler.IPCMSampler;
import com.teotigraphix.caustic.sampler.IPCMSamplerChannel;

/**
 * The default implementation of the {@link IPCMSampler} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class PCMSampler extends MachineComponent implements IPCMSampler
{

    private static final String ATT_CHANNEL = "channel";

    private static final String TAG_SAMPLE = "sample";

    private static final int NUM_SAMPLER_CHANNELS = 64;

    private Map<Integer, IPCMSamplerChannel> mSamples;

    @Override
    public void setDevice(IDevice value)
    {
        super.setDevice(value);
        createChannels();
    }

    //--------------------------------------------------------------------------
    //
    // IPCMSampler API :: Properties
    //
    //--------------------------------------------------------------------------

    @Override
    public String getSampleIndicies()
    {
        return PCMSamplerMessage.QUERY_SAMPLE_INDICIES.queryString(getEngine(),
                getMachineIndex());
    }

    //----------------------------------
    // currentChannel
    //----------------------------------

    private int mActiveIndex = 0;

    private IPCMSamplerChannel mCurrentSample;

    private IPCMSynthSamplerListener mListener;

    @Override
    public int getActiveIndex()
    {
        return mActiveIndex;
    }

    @Override
    public void setActiveIndex(int value)
    {
        if (value == mActiveIndex)
            return;
        if (value < 0 || value >= NUM_SAMPLER_CHANNELS)
            throw newRangeException(PCMSamplerMessage.SAMPLE_INDEX.toString(),
                    "0..63", value);

        mActiveIndex = value;
        mCurrentSample = getPCMSample(mActiveIndex);

        PCMSamplerMessage.SAMPLE_INDEX.send(getEngine(), getMachineIndex(),
                mActiveIndex);

        fireSampleChanged(mActiveIndex, mCurrentSample);
    }

    //----------------------------------
    // currentSample
    //----------------------------------

    @Override
    public IPCMSamplerChannel getActiveChannel()
    {
        return mCurrentSample;
    }

    @Override
    public String getSampleName(int channel)
    {
        setActiveIndex(channel);
        return PCMSamplerMessage.QUERY_SAMPLE_NAME.queryString(getEngine(),
                getMachineIndex());
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public PCMSampler(IMachine machine)
    {
        super(machine);
    }

    //--------------------------------------------------------------------------
    //
    // IPCMSampler API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void setChannelSamplePoints(int channel, int start, int end)
    {
        setActiveIndex(channel);
        getActiveChannel().setStart(start);
        getActiveChannel().setEnd(end);
    }

    @Override
    public void setChannelKeys(int channel, int low, int high, int root)
    {
        setActiveIndex(channel);
        getActiveChannel().setLowKey(low);
        getActiveChannel().setHighKey(high);
        getActiveChannel().setRootKey(root);
    }

    @Override
    public void setChannelProperties(int channel, float level, int tune,
            PlayMode mode)
    {
        setActiveIndex(channel);
        getActiveChannel().setLevel(level);
        getActiveChannel().setTune(tune);
        getActiveChannel().setMode(mode);
    }

    @Override
    public IPCMSamplerChannel loadChannel(int index, String path)
    {
        setActiveIndex(index);
        IPCMSamplerChannel result = getActiveChannel();
        loadSample(path);
        result.restore();
        return result;
    }

    //--------------------------------------------------------------------------
    //
    // IRestore API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void restore()
    {
        // the OSC for the sampler is kindof weird in that you have to
        // set the active index first and then issue commands. During the
        // restore, this index gets changed, we need to put it back where it was
        int old = getActiveIndex();
        String samples = getSampleIndicies();
        if (samples == null || samples.equals(""))
            return;

        String[] split = samples.split(" ");
        for (int i = 0; i < split.length; i++)
        {
            mSamples.get(Integer.parseInt(split[i])).restore();
        }
        setActiveIndex(old);
    }

    //--------------------------------------------------------------------------
    //
    // IPersit API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento)
    {
        for (IPCMSamplerChannel sample : mSamples.values())
        {
            if (sample.hasSample())
            {
                sample.copy(memento.createChild(TAG_SAMPLE));
            }
        }
    }

    @Override
    public void paste(IMemento memento)
    {
        IMemento[] samples = memento.getChildren(TAG_SAMPLE);
        for (IMemento sample : samples)
        {
            IPCMSamplerChannel pcm = getPCMSample(sample
                    .getInteger(ATT_CHANNEL));
            pcm.paste(sample);
        }
    }

    //--------------------------------------------------------------------------
    //
    // Private :: Methods
    //
    //--------------------------------------------------------------------------

    void loadSample(String absolutPath)
    {
        PCMSamplerMessage.SAMPLE_LOAD.send(getEngine(), getMachineIndex(),
                absolutPath);
    }

    protected final IPCMSamplerChannel getPCMSample(int index)
    {
        return mSamples.get(index);
    }

    protected final void fireSampleChanged(int channel,
            IPCMSamplerChannel sample)
    {
        if (mListener != null)
        {
            mListener.onChannelChanged(channel, sample);
        }
    }

    protected void createChannels()
    {
        int numChannels = 64;
        mSamples = new TreeMap<Integer, IPCMSamplerChannel>();
        for (int i = 0; i < numChannels; i++)
        {
            PCMSamplerChannel sample = new PCMSamplerChannel(this);
            sample.setIndex(i);
            mSamples.put(i, sample);
        }
        // do this manually at startup
        mCurrentSample = getPCMSample(mActiveIndex);
    }

}
