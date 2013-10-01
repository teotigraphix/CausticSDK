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

package com.teotigraphix.caustk.tone.components.pcmsynth;

import com.teotigraphix.caustk.core.osc.PCMSynthMessage;
import com.teotigraphix.caustk.tone.ToneComponent;
import com.teotigraphix.caustk.tone.components.pcmsynth.PCMSamplerComponent.PlayMode;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class PCMSamplerChannel extends ToneComponent {

    private static final long serialVersionUID = -8709245253837063500L;

    private transient PCMSamplerComponent sampler;

    private int mIndex;

    private String name;

    private float level = 1.0f;

    private int tune;

    private int rootKey;

    private int lowKey;

    private int highKey;

    private PlayMode mode;

    private int start;

    private int end;

    public boolean hasSample() {
        return name != null;
    }

    public final int getIndex() {
        return mIndex;
    }

    public final void setIndex(int value) {
        mIndex = value;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String value) {
        name = value;
    }

    public final float getLevel() {
        return level;
    }

    public final void setLevel(float value) {
        if (value == level)
            return;
        level = value;
        PCMSynthMessage.SAMPLE_LEVEL.send(getEngine(), getToneIndex(), level);
    }

    public final int getTune() {
        return tune;
    }

    public final void setTune(int value) {
        if (value == tune)
            return;
        tune = value;
        PCMSynthMessage.SAMPLE_TUNE.send(getEngine(), getToneIndex(), tune);
    }

    public final int getRootKey() {
        return rootKey;
    }

    public final void setRootKey(int value) {
        if (value == rootKey)
            return;
        rootKey = value;
        PCMSynthMessage.SAMPLE_ROOTKEY.send(getEngine(), getToneIndex(), rootKey);
    }

    public final int getLowKey() {
        return lowKey;
    }

    public final void setLowKey(int value) {
        if (value == lowKey)
            return;
        lowKey = value;
        PCMSynthMessage.SAMPLE_LOWKEY.send(getEngine(), getToneIndex(), lowKey);
    }

    public final int getHighKey() {
        return highKey;
    }

    public final void setHighKey(int value) {
        if (value == highKey)
            return;
        highKey = value;
        PCMSynthMessage.SAMPLE_HIGHKEY.send(getEngine(), getToneIndex(), highKey);
    }

    public final PlayMode getMode() {
        return mode;
    }

    public final void setMode(PlayMode value) {
        if (value == mode)
            return;
        mode = value;
        PCMSynthMessage.SAMPLE_MODE.send(getEngine(), getToneIndex(), mode.getValue());
    }

    public final int getStart() {
        return start;
    }

    public final void setStart(int value) {
        if (value == start)
            return;
        start = value;
        PCMSynthMessage.SAMPLE_START.send(getEngine(), getToneIndex(), start);
    }

    public final int getEnd() {
        return end;
    }

    public final void setEnd(int value) {
        if (value == end)
            return;
        end = value;
        PCMSynthMessage.SAMPLE_END.send(getEngine(), getToneIndex(), end);
    }

    public PCMSamplerChannel(PCMSamplerComponent sampler) {
        this.sampler = sampler;
    }

    @Override
    public void restore() {
        name = sampler.getSampleName(mIndex);
        if (name == null)
            return;

        level = PCMSynthMessage.SAMPLE_LEVEL.query(getEngine(), getToneIndex());
        tune = (int)PCMSynthMessage.SAMPLE_TUNE.query(getEngine(), getToneIndex());
        rootKey = (int)PCMSynthMessage.SAMPLE_ROOTKEY.query(getEngine(), getToneIndex());
        lowKey = (int)PCMSynthMessage.SAMPLE_LOWKEY.query(getEngine(), getToneIndex());
        highKey = (int)PCMSynthMessage.SAMPLE_HIGHKEY.query(getEngine(), getToneIndex());
        mode = PlayMode.toType((int)PCMSynthMessage.SAMPLE_MODE.query(getEngine(), getToneIndex()));
        start = (int)PCMSynthMessage.SAMPLE_START.query(getEngine(), getToneIndex());
        end = (int)PCMSynthMessage.SAMPLE_END.query(getEngine(), getToneIndex());
    }
}
