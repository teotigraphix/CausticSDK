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

package com.teotigraphix.caustic.internal.filter;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.filter.IVolumeEnvelope;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.VolumeMessage;

/**
 * The default implementation of the {@link IVolumeEnvelope} api.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class VolumeEnvelope extends VolumeComponent implements IVolumeEnvelope
{

    //--------------------------------------------------------------------------
    //
    // IVolumeEnvelope API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // attack
    //----------------------------------

    private float mAttack = 0.0f;

    @Override
    public float getAttack()
    {
        return mAttack;
    }

    public float getAttack(boolean restore)
    {
        return VolumeMessage.VOLUME_ATTACK
                .query(getEngine(), getMachineIndex());
    }

    @Override
    public void setAttack(float value)
    {
        if (value == mAttack)
            return;
        if (value < 0 || value > 3.0625f)
            throw newRangeException(VolumeMessage.VOLUME_ATTACK.toString(),
                    "0..3.0625", value);
        mAttack = value;
        VolumeMessage.VOLUME_ATTACK.send(getEngine(), getMachineIndex(),
                mAttack);
    }

    //----------------------------------
    // decay
    //----------------------------------

    private float mDecay = 0.0f;

    @Override
    public float getDecay()
    {
        return mDecay;
    }

    public float getDecay(boolean restore)
    {
        return VolumeMessage.VOLUME_DECAY.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setDecay(float value)
    {
        if (value == mDecay)
            return;
        if (value < 0 || value > 3.0625f)
            throw newRangeException(VolumeMessage.VOLUME_DECAY.toString(),
                    "0..3.0625", value);
        mDecay = value;
        VolumeMessage.VOLUME_DECAY.send(getEngine(), getMachineIndex(), mDecay);
    }

    //----------------------------------
    // sustain
    //----------------------------------

    private float mSustain = 1.0f;

    @Override
    public float getSustain()
    {
        return mSustain;
    }

    public float getSustain(boolean restore)
    {
        return VolumeMessage.VOLUME_SUSTAIN.query(getEngine(),
                getMachineIndex());
    }

    @Override
    public void setSustain(float value)
    {
        if (value == mSustain)
            return;
        if (value < 0 || value > 1.0f)
            throw newRangeException(VolumeMessage.VOLUME_SUSTAIN.toString(),
                    "0..1.0", value);
        mSustain = value;
        VolumeMessage.VOLUME_SUSTAIN.send(getEngine(), getMachineIndex(),
                mSustain);
    }

    //----------------------------------
    // release
    //----------------------------------

    private float mRelease = 0.0f;

    @Override
    public float getRelease()
    {
        return mRelease;
    }

    public float getRelease(boolean restore)
    {
        return VolumeMessage.VOLUME_RELEASE.query(getEngine(),
                getMachineIndex());
    }

    @Override
    public void setRelease(float value)
    {
        if (value == mRelease)
            return;
        if (value < 0 || value > 3.0625f)
            throw newRangeException(VolumeMessage.VOLUME_RELEASE.toString(),
                    "0..3.0625", value);
        mRelease = value;
        VolumeMessage.VOLUME_RELEASE.send(getEngine(), getMachineIndex(),
                mRelease);
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public VolumeEnvelope(IMachine machine)
    {
        super(machine);
    }

    //--------------------------------------------------------------------------
    //
    // IPersist API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento)
    {
        super.copy(memento);
        memento.putFloat(FilterConstants.ATT_ATTACK, getAttack());
        memento.putFloat(FilterConstants.ATT_DECAY, getDecay());
        memento.putFloat(FilterConstants.ATT_RELEASE, getRelease());
        memento.putFloat(FilterConstants.ATT_SUSTAIN, getSustain());
    }

    @Override
    public void paste(IMemento memento)
    {
        super.paste(memento);
        setAttack(memento.getFloat(FilterConstants.ATT_ATTACK));
        setDecay(memento.getFloat(FilterConstants.ATT_DECAY));
        setRelease(memento.getFloat(FilterConstants.ATT_RELEASE));
        setSustain(memento.getFloat(FilterConstants.ATT_SUSTAIN));
    }

    @Override
    public void restore()
    {
        super.restore();
        setAttack(getAttack(true));
        setDecay(getDecay(true));
        setRelease(getRelease(true));
        setSustain(getSustain(true));
    }
}
