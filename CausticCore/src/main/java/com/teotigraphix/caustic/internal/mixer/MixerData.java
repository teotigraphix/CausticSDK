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

package com.teotigraphix.caustic.internal.mixer;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.core.IPersist;
import com.teotigraphix.caustic.core.IRestore;
import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.internal.utils.MementoUtil;
import com.teotigraphix.caustic.mixer.IMixerPanel;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class MixerData implements IPersist, IRestore
{

    public float bass = 0f;

    public float mid = 0f;

    public float high = 0f;

    public float delay = 0f;

    public float reverb = 0f;

    public float pan = 0f;

    public float stereoWidth = 0f;

    public boolean mute = false;

    public boolean solo = false;

    public float volume = 0f;

    public int index = -1;

    MixerPanel mPanel;

    MixerData(MixerPanel panel)
    {
        mPanel = panel;
    }

    MixerData(MixerPanel panel, IDevice device)
    {
        mPanel = panel;
        index = device.getIndex();
    }

    @Override
    public void restore()
    {
        if (index != -1)
        {
            mPanel.setBass(index, mPanel.getBass(index, true));
            mPanel.setMid(index, mPanel.getMid(index, true));
            mPanel.setHigh(index, mPanel.getHigh(index, true));
            mPanel.setVolume(index, mPanel.getVolume(index, true));

            mPanel.setDelaySend(index, mPanel.getDelaySend(index, true));
            mPanel.setReverbSend(index, mPanel.getReverbSend(index, true));
            mPanel.setPan(index, mPanel.getPan(index, true));
            mPanel.setStereoWidth(index, mPanel.getStereoWidth(index, true));
            mPanel.setMute(index, mPanel.isMute(index, true));
            mPanel.setSolo(index, mPanel.isSolo(index, true));
        }
        else
        {
            mPanel.setMasterBass(mPanel.getMasterBass(true));
            mPanel.setMasterMid(mPanel.getMasterMid(true));
            mPanel.setMasterHigh(mPanel.getMasterHigh(true));
            mPanel.setMasterVolume(mPanel.getMasterVolume(true));
        }
    }

    @Override
    public void copy(IMemento memento)
    {
        memento.putInteger(MixerPanelConstants.ATT_ID, index);

        if (index != -1)
        {
            memento.putFloat(IMixerPanel.CONTROL_EQ_BASS, bass);
            memento.putFloat(IMixerPanel.CONTROL_EQ_MID, mid);
            memento.putFloat(IMixerPanel.CONTROL_EQ_HIGH, high);
            memento.putFloat(IMixerPanel.CONTROL_VOLUME, volume);

            memento.putFloat(IMixerPanel.CONTROL_DELAY_SEND, delay);
            memento.putFloat(IMixerPanel.CONTROL_REVERB_SEND, reverb);
            memento.putFloat(IMixerPanel.CONTROL_PAN, pan);
            memento.putFloat(IMixerPanel.CONTROL_STEREO_WIDTH, stereoWidth);

            memento.putInteger(IMixerPanel.CONTROL_MUTE, MementoUtil
                    .booleanToInt(mute));
            memento.putInteger(IMixerPanel.CONTROL_SOLO, MementoUtil
                    .booleanToInt(solo));
        }
        else
        {
            memento.putFloat(IMixerPanel.CONTROL_EQ_BASS, bass);
            memento.putFloat(IMixerPanel.CONTROL_EQ_MID, mid);
            memento.putFloat(IMixerPanel.CONTROL_EQ_HIGH, high);

            memento.putFloat(IMixerPanel.CONTROL_VOLUME, volume);
        }
    }

    @Override
    public void paste(IMemento memento)
    {
        index = memento.getInteger(MixerPanelConstants.ATT_ID);

        if (index != -1)
        {
            float bass = memento.getFloat(IMixerPanel.CONTROL_EQ_BASS);
            mPanel.setBass(index, bass);
            float mid = memento.getFloat(IMixerPanel.CONTROL_EQ_MID);
            mPanel.setMid(index, mid);
            float high = memento.getFloat(IMixerPanel.CONTROL_EQ_HIGH);
            mPanel.setHigh(index, high);
            float volume = memento.getFloat(IMixerPanel.CONTROL_VOLUME);
            mPanel.setVolume(index, volume);

            float delay = memento.getFloat(IMixerPanel.CONTROL_DELAY_SEND);
            mPanel.setDelaySend(index, delay);
            float reverb = memento.getFloat(IMixerPanel.CONTROL_REVERB_SEND);
            mPanel.setReverbSend(index, reverb);
            float pan = memento.getFloat(IMixerPanel.CONTROL_PAN);
            mPanel.setPan(index, pan);
            float stereoWidth = memento
                    .getFloat(IMixerPanel.CONTROL_STEREO_WIDTH);
            mPanel.setStereoWidth(index, stereoWidth);

            boolean mute = MementoUtil.intToBoolean(memento
                    .getInteger(IMixerPanel.CONTROL_MUTE));
            mPanel.setMute(index, mute);
            boolean solo = MementoUtil.intToBoolean(memento
                    .getInteger(IMixerPanel.CONTROL_SOLO));
            mPanel.setSolo(index, solo);
        }
        else
        {
            float bass = memento.getFloat(IMixerPanel.CONTROL_EQ_BASS);
            mPanel.setMasterBass(bass);
            float mid = memento.getFloat(IMixerPanel.CONTROL_EQ_MID);
            mPanel.setMasterMid(mid);
            float high = memento.getFloat(IMixerPanel.CONTROL_EQ_HIGH);
            mPanel.setMasterHigh(high);
            float volume = memento.getFloat(IMixerPanel.CONTROL_VOLUME);
            mPanel.setMasterVolume(volume);
        }
    }
}
