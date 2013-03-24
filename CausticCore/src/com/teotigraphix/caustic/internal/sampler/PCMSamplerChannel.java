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

import com.teotigraphix.caustic.core.CausticError;
import com.teotigraphix.caustic.internal.machine.MachineComponent;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.PCMSamplerMessage;
import com.teotigraphix.caustic.sampler.IPCMSampler;
import com.teotigraphix.caustic.sampler.IPCMSampler.PlayMode;
import com.teotigraphix.caustic.sampler.IPCMSamplerChannel;
import com.teotigraphix.common.IMemento;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class PCMSamplerChannel extends MachineComponent implements
        IPCMSamplerChannel
{

    private static final String ATT_END = "end";

    private static final String ATT_START = "start";

    private static final String ATT_MODE = "mode";

    private static final String ATT_HIGH_KEY = "highKey";

    private static final String ATT_LOW_KEY = "lowKey";

    private static final String ATT_ROOT_KEY = "rootKey";

    private static final String ATT_TUNE = "tune";

    private static final String ATT_LEVEL = "level";

    private static final String ATT_CHANNEL = "channel";

    private static final String ATT_NAME = "name";

    private IPCMSampler mSampler;

    private int mIndex;

    private String mName;

    private float mLevel = 1.0f;

    private int mTune;

    private int mRootKey;

    private int mLowKey;

    private int mHighKey;

    private PlayMode mMode;

    private int mStart;

    private int mEnd;

    @Override
    public boolean hasSample()
    {
        return mName != null;
    }

    @Override
    public final int getIndex()
    {
        return mIndex;
    }

    public final void setIndex(int value)
    {
        mIndex = value;
    }

    @Override
    public final String getName()
    {
        return mName;
    }

    public final void setName(String value)
    {
        mName = value;
    }

    @Override
    public final float getLevel()
    {
        return mLevel;
    }

    @Override
    public final void setLevel(float value)
    {
        if (value == mLevel)
            return;
        mLevel = value;
        PCMSamplerMessage.SAMPLE_LEVEL.send(getEngine(), getMachineIndex(),
                mLevel);
    }

    @Override
    public final int getTune()
    {
        return mTune;
    }

    @Override
    public final void setTune(int value)
    {
        if (value == mTune)
            return;
        mTune = value;
        PCMSamplerMessage.SAMPLE_TUNE.send(getEngine(), getMachineIndex(),
                mTune);
    }

    @Override
    public final int getRootKey()
    {
        return mRootKey;
    }

    @Override
    public final void setRootKey(int value)
    {
        if (value == mRootKey)
            return;
        mRootKey = value;
        PCMSamplerMessage.SAMPLE_ROOTKEY.send(getEngine(), getMachineIndex(),
                mRootKey);
    }

    @Override
    public final int getLowKey()
    {
        return mLowKey;
    }

    @Override
    public final void setLowKey(int value)
    {
        if (value == mLowKey)
            return;
        mLowKey = value;
        PCMSamplerMessage.SAMPLE_LOWKEY.send(getEngine(), getMachineIndex(),
                mLowKey);
    }

    @Override
    public final int getHighKey()
    {
        return mHighKey;
    }

    @Override
    public final void setHighKey(int value)
    {
        if (value == mHighKey)
            return;
        mHighKey = value;
        PCMSamplerMessage.SAMPLE_HIGHKEY.send(getEngine(), getMachineIndex(),
                mHighKey);
    }

    @Override
    public final PlayMode getMode()
    {
        return mMode;
    }

    @Override
    public final void setMode(PlayMode value)
    {
        if (value == mMode)
            return;
        mMode = value;
        PCMSamplerMessage.SAMPLE_MODE.send(getEngine(), getMachineIndex(),
                mMode.getValue());
    }

    @Override
    public final int getStart()
    {
        return mStart;
    }

    @Override
    public final void setStart(int value)
    {
        if (value == mStart)
            return;
        mStart = value;
        PCMSamplerMessage.SAMPLE_START.send(getEngine(), getMachineIndex(),
                mStart);
    }

    @Override
    public final int getEnd()
    {
        return mEnd;
    }

    @Override
    public final void setEnd(int value)
    {
        if (value == mEnd)
            return;
        mEnd = value;
        PCMSamplerMessage.SAMPLE_END.send(getEngine(), getMachineIndex(), mEnd);
    }

    public PCMSamplerChannel(IPCMSampler sampler)
    {
        super((IMachine) sampler.getDevice());
        mSampler = sampler;
    }

    @Override
    public void copy(IMemento memento)
    {
        memento.putString(ATT_NAME, mName);
        memento.putFloat(ATT_CHANNEL, mIndex);
        memento.putFloat(ATT_LEVEL, mLevel);
        memento.putInteger(ATT_TUNE, mTune);
        memento.putInteger(ATT_ROOT_KEY, mRootKey);
        memento.putInteger(ATT_LOW_KEY, mLowKey);
        memento.putInteger(ATT_HIGH_KEY, mHighKey);
        memento.putInteger(ATT_MODE, mMode.getValue());
        memento.putInteger(ATT_START, mStart);
        memento.putInteger(ATT_END, mEnd);
    }

    @Override
    public void paste(IMemento memento)
    {
        int index = memento.getInteger(ATT_CHANNEL);
        if (mIndex != index)
            throw new CausticError("index is not the same");

        setName(memento.getString(ATT_NAME));
        // check index
        setLevel(memento.getFloat(ATT_LEVEL));
        setTune(memento.getInteger(ATT_TUNE));
        setRootKey(memento.getInteger(ATT_ROOT_KEY));
        setLowKey(memento.getInteger(ATT_LOW_KEY));
        setHighKey(memento.getInteger(ATT_HIGH_KEY));
        setMode(PlayMode.toType(memento.getInteger(ATT_MODE)));
        setStart(memento.getInteger(ATT_START));
        setEnd(memento.getInteger(ATT_END));
    }

    @Override
    public void restore()
    {
        mName = mSampler.getSampleName(mIndex);
        if (mName == null)
            return;

        mLevel = PCMSamplerMessage.SAMPLE_LEVEL.query(getEngine(),
                getMachineIndex());
        mTune = (int) PCMSamplerMessage.SAMPLE_TUNE.query(getEngine(),
                getMachineIndex());
        mRootKey = (int) PCMSamplerMessage.SAMPLE_ROOTKEY.query(getEngine(),
                getMachineIndex());
        mLowKey = (int) PCMSamplerMessage.SAMPLE_LOWKEY.query(getEngine(),
                getMachineIndex());
        mHighKey = (int) PCMSamplerMessage.SAMPLE_HIGHKEY.query(getEngine(),
                getMachineIndex());
        mMode = PlayMode.toType((int) PCMSamplerMessage.SAMPLE_MODE.query(
                getEngine(), getMachineIndex()));
        mStart = (int) PCMSamplerMessage.SAMPLE_START.query(getEngine(),
                getMachineIndex());
        mEnd = (int) PCMSamplerMessage.SAMPLE_END.query(getEngine(),
                getMachineIndex());
    }
}
