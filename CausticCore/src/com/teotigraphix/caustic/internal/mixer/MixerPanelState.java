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

package com.teotigraphix.caustic.internal.mixer;

import java.util.Map.Entry;

import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.IPersist;

/**
 * Saves and restores the state of the MixerPanel class.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class MixerPanelState implements IPersist
{
    MixerPanel mMixerPanel;

    public MixerPanelState(MixerPanel mixerPanel)
    {
        mMixerPanel = mixerPanel;
    }

    @Override
    public void copy(IMemento memento)
    {
        saveMasterChannel(memento.createChild("master"));
        // save channels
        saveChannels(memento.createChild(MixerPanelConstants.TAG_CHANNELS));
        // save effects
        saveDelay(memento.createChild("delay"));
        saveReverb(memento.createChild("reverb"));
    }

    private void saveMasterChannel(IMemento memento)
    {
        saveChannel(mMixerPanel.getMasterData(), memento);
    }

    private void loadMasterChannel(IMemento memento)
    {
        loadChannel(mMixerPanel.getMasterData(), memento);
    }

    @Override
    public void paste(IMemento memento)
    {
        loadMasterChannel(memento.getChild("master"));
        // !!! The rack or client needs to call addMachine() on the mixer
        // BEFORE the state is loaded.
        loadChannels(memento.getChild(MixerPanelConstants.TAG_CHANNELS));
        // load effects
        loadDelay(memento.getChild("delay"));
        loadReverb(memento.getChild("reverb"));
    }

    public void copyChannel(IMachine machine, IMemento memento)
    {
        MixerData data = mMixerPanel.getMixerInfo(machine.getIndex());
        saveChannel(data, memento);
    }

    public void pasteChannel(IMachine machine, IMemento memento)
    {
        MixerData data = mMixerPanel.getMixerInfo(machine.getIndex());
        loadChannel(data, memento);
    }

    protected void saveChannels(IMemento memento)
    {
        for (Entry<Integer, MixerData> entry : mMixerPanel.getMixerInfoSet())
        {
            saveChannel(entry.getValue(),
                    memento.createChild(MixerPanelConstants.TAG_CHANNEL));
        }
    }

    private void saveChannel(MixerData data, IMemento memento)
    {
        data.copy(memento);
    }

    protected void loadChannels(IMemento memento)
    {
        IMemento[] channels = memento
                .getChildren(MixerPanelConstants.TAG_CHANNEL);
        for (IMemento channel : channels)
        {
            int id = channel.getInteger("id");
            if (!mMixerPanel.hasMixerInfo(id))
                continue;

            MixerData data = mMixerPanel.getMixerInfo(id);
            loadChannel(data, channel);
        }
    }

    private void loadChannel(MixerData data, IMemento channel)
    {
        data.paste(channel);
    }

    protected void saveDelay(IMemento memento)
    {
        mMixerPanel.getDelay().copy(memento);
    }

    protected void saveReverb(IMemento memento)
    {
        mMixerPanel.getReverb().copy(memento);
    }

    protected void loadDelay(IMemento memento)
    {
        mMixerPanel.getDelay().paste(memento);
    }

    protected void loadReverb(IMemento memento)
    {
        mMixerPanel.getReverb().paste(memento);
    }
}
