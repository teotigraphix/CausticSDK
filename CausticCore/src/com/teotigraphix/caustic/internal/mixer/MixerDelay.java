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

import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.internal.effect.EffectComponent;
import com.teotigraphix.caustic.internal.effect.EffectConstants;
import com.teotigraphix.caustic.mixer.IMixerDelay;
import com.teotigraphix.caustic.osc.MixerMessage;
import com.teotigraphix.common.IMemento;

/**
 * The default implementation of the {@link IMixerDelay} interface.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class MixerDelay extends EffectComponent implements IMixerDelay {

    //--------------------------------------------------------------------------
    //
    // IDelayEffect API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // time
    //----------------------------------

    private int mTime = 7;

    @Override
    public int getTime() {
        return mTime;
    }

    int getTime(boolean restore) {
        return (int)MixerMessage.DELAY_UNIT_TIME.query(getEngine());
    }

    @Override
    public void setTime(int value) {
        if (value == mTime)
            return;
        if (value < 1 || value > 9)
            throw newRangeException(CONTROL_TIME, "1..9", value);
        mTime = value;
        MixerMessage.DELAY_UNIT_TIME.send(getEngine(), mTime);
    }

    //----------------------------------
    // feedback
    //----------------------------------

    private float mFeedback = 0.5f;

    @Override
    public float getFeedback() {
        return mFeedback;
    }

    float getFeedback(boolean restore) {
        return MixerMessage.DELAY_UNIT_FEEDBACK.query(getEngine());
    }

    @Override
    public void setFeedback(float value) {
        if (value == mFeedback)
            return;
        if (value < 0f || value > 5f)
            throw newRangeException(CONTROL_FEEDBACK, "0..5", value);
        mFeedback = value;
        MixerMessage.DELAY_UNIT_FEEDBACK.send(getEngine(), mFeedback);
    }

    //----------------------------------
    // stereo
    //----------------------------------

    private boolean mStereo = false;

    @Override
    public boolean isStereo() {
        return mStereo;
    }

    boolean isStereo(boolean restore) {
        return MixerMessage.DELAY_UNIT_STEREO.query(getEngine()) == 1f;
    }

    @Override
    public void setStereo(boolean value) {
        if (value == mStereo)
            return;
        mStereo = value;
        MixerMessage.DELAY_UNIT_STEREO.send(getEngine(), mStereo ? 1 : 0);
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public MixerDelay(IDevice device) {
        super(device);
        setName(EffectConstants.TAG_DELAY);
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        memento.putFloat(EffectConstants.ATT_FEEDBACK, getFeedback());
        memento.putInteger(EffectConstants.ATT_STEREO, isStereo() ? 1 : 0);
        memento.putInteger(EffectConstants.ATT_TIME, getTime());
    }

    @Override
    public void paste(IMemento memento) {
        setFeedback(memento.getFloat(EffectConstants.ATT_FEEDBACK));
        setStereo(memento.getInteger(EffectConstants.ATT_STEREO) == 1);
        setTime(memento.getInteger(EffectConstants.ATT_TIME));
    }

    @Override
    public void restore() {
        super.restore();
        setFeedback(getFeedback(true));
        setStereo(isStereo(true));
        setTime(getTime(true));
    }
}
