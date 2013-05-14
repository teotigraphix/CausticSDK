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

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.internal.machine.MachineComponent;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.BeatboxSamplerMessage;
import com.teotigraphix.caustic.sampler.IBeatboxSampler;
import com.teotigraphix.caustic.sampler.IBeatboxSamplerChannel;

/**
 * The default implementation of {@link IBeatboxSamplerChannel} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class BeatboxSamplerChannel extends MachineComponent implements
        IBeatboxSamplerChannel
{

    private boolean mIsSelected;

    private boolean mIsMute;

    private int mIndex;

    private boolean mIsSolo;

    private float mTune;

    private float mPunch;

    private float mDecay;

    private float mPan;

    private float mVolume;

    private String mName;

    private IBeatboxSampler mSampler;

    //--------------------------------------------------------------------------
    //
    // IBeatboxSampler API :: Properties
    //
    //--------------------------------------------------------------------------

    @Override
    public boolean hasSample()
    {
        return mName != null;
    }

    //----------------------------------
    // index
    //----------------------------------

    @Override
    public int getIndex()
    {
        return mIndex;
    }

    public void setIndex(int value)
    {
        mIndex = value;
    }

    //----------------------------------
    // name
    //----------------------------------

    @Override
    public String getName()
    {
        return mName;
    }

    public void setName(String value)
    {
        mName = value;
    }

    //----------------------------------
    // selected
    //----------------------------------

    @Override
    public boolean isSelected()
    {
        return mIsSelected;
    }

    @Override
    public void setSelected(boolean value)
    {
        mIsSelected = value;
    }

    //----------------------------------
    // mute
    //----------------------------------

    @Override
    public boolean isMute()
    {
        return mIsMute;
    }

    @Override
    public void setMute(boolean value)
    {
        if (value == mIsMute)
            return;
        mIsMute = value;
        BeatboxSamplerMessage.CHANNEL_MUTE.send(getEngine(), getMachineIndex(),
                mIndex, mIsMute ? 1 : 0);
    }

    //----------------------------------
    // solo
    //----------------------------------

    @Override
    public boolean isSolo()
    {
        return mIsSolo;
    }

    @Override
    public void setSolo(boolean value)
    {
        if (value == mIsSolo)
            return;
        mIsSolo = value;
        BeatboxSamplerMessage.CHANNEL_SOLO.send(getEngine(), getMachineIndex(),
                mIndex, mIsSolo ? 1 : 0);
    }

    //----------------------------------
    // tune
    //----------------------------------

    @Override
    public float getTune()
    {
        return mTune;
    }

    @Override
    public void setTune(float value)
    {
        if (value == mTune)
            return;
        mTune = value;
        BeatboxSamplerMessage.CHANNEL_TUNE.send(getEngine(), getMachineIndex(),
                mIndex, mTune);
    }

    //----------------------------------
    // punch
    //----------------------------------

    @Override
    public float getPunch()
    {
        return mPunch;
    }

    @Override
    public void setPunch(float value)
    {
        if (value == mPunch)
            return;
        mPunch = value;
        BeatboxSamplerMessage.CHANNEL_PUNCH.send(getEngine(),
                getMachineIndex(), mIndex, mPunch);
    }

    //----------------------------------
    // decay
    //----------------------------------

    @Override
    public float getDecay()
    {
        return mDecay;
    }

    @Override
    public void setDecay(float value)
    {
        if (value == mDecay)
            return;
        mDecay = value;
        BeatboxSamplerMessage.CHANNEL_DECAY.send(getEngine(),
                getMachineIndex(), mIndex, mDecay);
    }

    //----------------------------------
    // pan
    //----------------------------------

    @Override
    public float getPan()
    {
        return mPan;
    }

    @Override
    public void setPan(float value)
    {
        if (value == mPan)
            return;
        mPan = value;
        BeatboxSamplerMessage.CHANNEL_PAN.send(getEngine(), getMachineIndex(),
                mIndex, mPan);
    }

    //----------------------------------
    // volume
    //----------------------------------

    @Override
    public float getVolume()
    {
        return mVolume;
    }

    @Override
    public void setVolume(float value)
    {
        if (value == mVolume)
            return;
        mVolume = value;
        BeatboxSamplerMessage.CHANNEL_VOLUME.send(getEngine(),
                getMachineIndex(), mIndex, mVolume);
    }

    public BeatboxSamplerChannel(IBeatboxSampler sampler)
    {
        super((IMachine) sampler.getDevice());
        mSampler = sampler;
    }

    @Override
    public void restore()
    {
        mName = mSampler.getSampleName(mIndex);
        if (mName == null)
            return;

        mDecay = BeatboxSamplerMessage.CHANNEL_DECAY.query(getEngine(),
                getMachineIndex(), mIndex);
        mPan = BeatboxSamplerMessage.CHANNEL_PAN.query(getEngine(),
                getMachineIndex(), mIndex);
        mPunch = BeatboxSamplerMessage.CHANNEL_PUNCH.query(getEngine(),
                getMachineIndex(), mIndex);
        mTune = BeatboxSamplerMessage.CHANNEL_TUNE.query(getEngine(),
                getMachineIndex(), mIndex);
        mVolume = BeatboxSamplerMessage.CHANNEL_VOLUME.query(getEngine(),
                getMachineIndex(), mIndex);
        mIsMute = BeatboxSamplerMessage.CHANNEL_MUTE.query(getEngine(),
                getMachineIndex(), mIndex) == 1f;
        mIsSolo = BeatboxSamplerMessage.CHANNEL_SOLO.query(getEngine(),
                getMachineIndex(), mIndex) == 1f;
    }

    @Override
    public void copy(IMemento memento)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void paste(IMemento memento)
    {
        // TODO Auto-generated method stub

    }
}
