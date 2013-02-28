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

package com.teotigraphix.caustic.internal.part;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.machine.IBeatbox;
import com.teotigraphix.caustic.part.IRhythmPart;
import com.teotigraphix.caustic.part.ITone;
import com.teotigraphix.caustic.sampler.IBeatboxSampler;

public class RhythmPart extends Part implements IRhythmPart {

    private IBeatboxSampler mSampler;

    //--------------------------------------------------------------------------
    // 
    //  Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  currentChannel
    //----------------------------------

    private int mCurrentChannel = -1;

    @Override
    public int getCurrentChannel() {
        return mCurrentChannel;
    }

    @Override
    public void setCurrentChannel(int value) {
        int old = mCurrentChannel;
        if (old != -1) {
            setSelected(old, false);
        }
        mCurrentChannel = value;
        if (mCurrentChannel != -1) {
            setSelected(mCurrentChannel, true);
        }
    }

    //----------------------------------
    //  selected
    //----------------------------------

    private boolean mSelected;

    @Override
    public boolean isSelected(int channel) {
        if (mSampler == null)
            return false;
        return mSampler.getChannel(channel).isSelected();
    }

    @Override
    public boolean isSelected() {
        return mSelected;
    }

    @Override
    public void setSelected(int channel, boolean value) {
        if (mSampler == null)
            return;
        if (value == isSelected(channel))
            return;
        mSampler.getChannel(channel).setSelected(value);
    }

    @Override
    public void setSelected(boolean value) {
        if (value == mSelected)
            return;
        mSelected = value;
        super.setSelected(value);
    }

    //----------------------------------
    //  mute
    //----------------------------------

    @Override
    public boolean isMute(int channel) {
        if (mSampler == null)
            return false;
        return mSampler.getChannel(channel).isMute();
    }

    @Override
    public boolean isMute() {
        if (mSampler == null)
            return false;
        //return mSampler.isMute();
        return super.isMute();
    }

    @Override
    public void setMute(int channel, boolean value) {
        if (mSampler == null)
            return;
        if (value == isMute(channel))
            return;
        mSampler.getChannel(channel).setMute(value);
    }

    @Override
    public void setMute(boolean value) {
        if (mSampler == null)
            return;
        // XXX This was wrong, when muting the the Rhythm part we actually
        // want to mute it from the mixer which will maintain the individual
        // mutes, this is for mute/solo combos
        //mSampler.setMute(value);
        super.setMute(value);
    }

    //----------------------------------
    //  solo
    //----------------------------------

    @Override
    public boolean isSolo(int channel) {
        if (mSampler == null)
            return false;
        return mSampler.getChannel(channel).isSolo();
    }

    @Override
    public boolean isSolo() {
        if (mSampler == null)
            return false;
        //return mSampler.isSolo();
        return super.isSolo();
    }

    @Override
    public void setSolo(int channel, boolean value) {
        if (mSampler == null)
            return;
        if (value == isSolo(channel))
            return;
        mSampler.getChannel(channel).setSolo(value);
    }

    @Override
    public void setSolo(boolean value) {
        if (mSampler == null)
            return;
        // mSampler.setSolo(value);
        super.setSolo(value);
    }

    public RhythmPart() {
        super();
    }

    @Override
    protected void toneAdd(ITone tone, int index) throws CausticException {
        super.toneAdd(tone, index);

        if (index > 0) {
            throw new CausticException("IRhythmPart can only have 1 ITone");
        }

        IBeatbox beatbox = (IBeatbox)tone;
        mSampler = beatbox.getSampler();
    }

    @Override
    protected void toneRemove(ITone tone) {
        super.toneRemove(tone);
        mSampler = null;
    }

    @Override
    public void dispose() {
        super.dispose();
        mSampler = null;
    }
}
