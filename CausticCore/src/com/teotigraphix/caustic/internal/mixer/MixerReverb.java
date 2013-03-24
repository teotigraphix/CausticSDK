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
import com.teotigraphix.caustic.mixer.IMixerReverb;
import com.teotigraphix.caustic.osc.MixerMessage;
import com.teotigraphix.common.IMemento;

/**
 * The default implementation of the {@link IMixerReverb} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class MixerReverb extends EffectComponent implements IMixerReverb
{

    //--------------------------------------------------------------------------
    //
    // IReverbEffect API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // room
    //----------------------------------

    private float mRoom = 0.75f;

    @Override
    public float getRoom()
    {
        return mRoom;
    }

    float getRoom(boolean restore)
    {
        return MixerMessage.REVERB_UNIT_ROOM.query(getEngine());
    }

    @Override
    public void setRoom(float value)
    {
        if (value == mRoom)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(CONTROL_ROOM, "0..1", value);
        mRoom = value;
        MixerMessage.REVERB_UNIT_ROOM.send(getEngine(), mRoom);
    }

    //----------------------------------
    // damping
    //----------------------------------

    private float mDamping = 0.2f;

    @Override
    public float getDamping()
    {
        return mDamping;
    }

    float getDamping(boolean restore)
    {
        return MixerMessage.REVERB_UNIT_DAMPING.query(getEngine());
    }

    @Override
    public void setDamping(float value)
    {
        if (value == mDamping)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(CONTROL_DAMPING, "0..1", value);
        mDamping = value;
        MixerMessage.REVERB_UNIT_DAMPING.send(getEngine(), mDamping);
    }

    //----------------------------------
    // stereo
    //----------------------------------

    private boolean mStereo = false;

    @Override
    public boolean isStereo()
    {
        return mStereo;
    }

    boolean isStereo(boolean restore)
    {
        return MixerMessage.REVERB_UNIT_STEREO.query(getEngine()) == 1f;
    }

    @Override
    public void setStereo(boolean value)
    {
        if (value == mStereo)
            return;
        mStereo = value;
        MixerMessage.REVERB_UNIT_STEREO.send(getEngine(), mStereo ? 1 : 0);
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public MixerReverb(IDevice device)
    {
        super(device);
        setName(EffectConstants.TAG_REVERB);
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento)
    {
        memento.putFloat(EffectConstants.ATT_DAMPING, getDamping());
        memento.putFloat(EffectConstants.ATT_ROOM, getRoom());
        memento.putInteger(EffectConstants.ATT_STEREO, isStereo() ? 1 : 0);
    }

    @Override
    public void paste(IMemento memento)
    {
        setDamping(memento.getFloat(EffectConstants.ATT_DAMPING));
        setRoom(memento.getFloat(EffectConstants.ATT_ROOM));
        setStereo(memento.getInteger(EffectConstants.ATT_STEREO) == 1);
    }

    @Override
    public void restore()
    {
        super.restore();
        setDamping(getDamping(true));
        setRoom(getRoom(true));
        setStereo(isStereo(true));
    }
}
