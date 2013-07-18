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

package com.teotigraphix.caustk.core.components.pcmsynth;

import com.teotigraphix.caustic.osc.PCMSamplerMessage;
import com.teotigraphix.caustk.core.components.ToneComponent;
import com.teotigraphix.caustk.core.components.pcmsynth.PCMSamplerComponent.PlayMode;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class PCMSamplerChannel extends ToneComponent {

    private PCMSamplerComponent mSampler;

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

    public boolean hasSample() {
        return mName != null;
    }

    public final int getIndex() {
        return mIndex;
    }

    public final void setIndex(int value) {
        mIndex = value;
    }

    public final String getName() {
        return mName;
    }

    public final void setName(String value) {
        mName = value;
    }

    public final float getLevel() {
        return mLevel;
    }

    public final void setLevel(float value) {
        if (value == mLevel)
            return;
        mLevel = value;
        PCMSamplerMessage.SAMPLE_LEVEL.send(getEngine(), getToneIndex(), mLevel);
    }

    public final int getTune() {
        return mTune;
    }

    public final void setTune(int value) {
        if (value == mTune)
            return;
        mTune = value;
        PCMSamplerMessage.SAMPLE_TUNE.send(getEngine(), getToneIndex(), mTune);
    }

    public final int getRootKey() {
        return mRootKey;
    }

    public final void setRootKey(int value) {
        if (value == mRootKey)
            return;
        mRootKey = value;
        PCMSamplerMessage.SAMPLE_ROOTKEY.send(getEngine(), getToneIndex(), mRootKey);
    }

    public final int getLowKey() {
        return mLowKey;
    }

    public final void setLowKey(int value) {
        if (value == mLowKey)
            return;
        mLowKey = value;
        PCMSamplerMessage.SAMPLE_LOWKEY.send(getEngine(), getToneIndex(), mLowKey);
    }

    public final int getHighKey() {
        return mHighKey;
    }

    public final void setHighKey(int value) {
        if (value == mHighKey)
            return;
        mHighKey = value;
        PCMSamplerMessage.SAMPLE_HIGHKEY.send(getEngine(), getToneIndex(), mHighKey);
    }

    public final PlayMode getMode() {
        return mMode;
    }

    public final void setMode(PlayMode value) {
        if (value == mMode)
            return;
        mMode = value;
        PCMSamplerMessage.SAMPLE_MODE.send(getEngine(), getToneIndex(), mMode.getValue());
    }

    public final int getStart() {
        return mStart;
    }

    public final void setStart(int value) {
        if (value == mStart)
            return;
        mStart = value;
        PCMSamplerMessage.SAMPLE_START.send(getEngine(), getToneIndex(), mStart);
    }

    public final int getEnd() {
        return mEnd;
    }

    public final void setEnd(int value) {
        if (value == mEnd)
            return;
        mEnd = value;
        PCMSamplerMessage.SAMPLE_END.send(getEngine(), getToneIndex(), mEnd);
    }

    public PCMSamplerChannel(PCMSamplerComponent sampler) {
        mSampler = sampler;
    }

    public void restore() {
        mName = mSampler.getSampleName(mIndex);
        if (mName == null)
            return;

        mLevel = PCMSamplerMessage.SAMPLE_LEVEL.query(getEngine(), getToneIndex());
        mTune = (int)PCMSamplerMessage.SAMPLE_TUNE.query(getEngine(), getToneIndex());
        mRootKey = (int)PCMSamplerMessage.SAMPLE_ROOTKEY.query(getEngine(), getToneIndex());
        mLowKey = (int)PCMSamplerMessage.SAMPLE_LOWKEY.query(getEngine(), getToneIndex());
        mHighKey = (int)PCMSamplerMessage.SAMPLE_HIGHKEY.query(getEngine(), getToneIndex());
        mMode = PlayMode.toType((int)PCMSamplerMessage.SAMPLE_MODE.query(getEngine(),
                getToneIndex()));
        mStart = (int)PCMSamplerMessage.SAMPLE_START.query(getEngine(), getToneIndex());
        mEnd = (int)PCMSamplerMessage.SAMPLE_END.query(getEngine(), getToneIndex());
    }
}
