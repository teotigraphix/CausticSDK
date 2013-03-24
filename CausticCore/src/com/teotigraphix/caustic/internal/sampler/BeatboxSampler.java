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

import com.teotigraphix.caustic.internal.machine.MachineComponent;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.BeatboxSamplerMessage;
import com.teotigraphix.caustic.sampler.IBeatboxSampler;
import com.teotigraphix.caustic.sampler.IBeatboxSamplerChannel;
import com.teotigraphix.common.IMemento;

/**
 * The default implementation of {@link IBeatboxSampler} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class BeatboxSampler extends MachineComponent implements IBeatboxSampler
{

    private static final String TAG_INDEX = "index";

    private static final String TAG_CHANNEL = "channel";

    private static final int NUM_CHANNELS = 8;

    private final Map<Integer, IBeatboxSamplerChannel> mMap;

    public BeatboxSampler(IMachine machine)
    {
        super(machine);

        mMap = new TreeMap<Integer, IBeatboxSamplerChannel>();

        for (int i = 0; i < NUM_CHANNELS; i++)
        {
            BeatboxSamplerChannel channel = new BeatboxSamplerChannel(this);
            channel.setIndex(i);
            mMap.put(i, channel);
        }
    }

    //--------------------------------------------------------------------------
    //
    // IBeatboxSampler API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public String getSampleName(int channel)
    {
        return BeatboxSamplerMessage.QUERY_CHANNEL_SAMPLE_NAME.queryString(
                getEngine(), getMachineIndex(), channel);
    }

    @Override
    public IBeatboxSamplerChannel getChannel(int index)
    {
        return mMap.get(index);
    }

    @Override
    public IBeatboxSamplerChannel loadChannel(int index, String path)
    {
        BeatboxSamplerMessage.CHANNEL_LOAD.send(getEngine(), getMachineIndex(),
                index, path);
        return getChannel(index);
    }

    //--------------------------------------------------------------------------
    //
    // IPersit API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento)
    {
        for (IBeatboxSamplerChannel sample : mMap.values())
        {
            if (sample.hasSample())
            {
                sample.copy(memento.createChild(TAG_CHANNEL));
            }
        }
    }

    @Override
    public void paste(IMemento memento)
    {
        IMemento[] samples = memento.getChildren(TAG_CHANNEL);
        for (IMemento sample : samples)
        {
            IBeatboxSamplerChannel channel = getChannel(sample
                    .getInteger(TAG_INDEX));
            channel.paste(sample);
        }
    }

    //--------------------------------------------------------------------------
    //
    // IRestore API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void restore()
    {
        for (IBeatboxSamplerChannel channel : mMap.values())
        {
            channel.restore();
        }
    }
}
