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

import com.teotigraphix.caustic.filter.IFilter;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.FilterMessage;
import com.teotigraphix.common.IMemento;

/**
 * The default implementation of the {@link IFilter} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class SynthFilter extends FilterComponent implements IFilter
{

    //--------------------------------------------------------------------------
    //
    // IFilter API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // attack
    //----------------------------------

    private float mAttack = 0f;

    @Override
    public float getAttack()
    {
        return mAttack;
    }

    public float getAttack(boolean restore)
    {
        return FilterMessage.FILTER_ATTACK
                .query(getEngine(), getMachineIndex());
    }

    @Override
    public void setAttack(float value)
    {
        if (value == mAttack)
            return;
        if (value < 0 || value > 3.0625f)
            throw newRangeException(FilterMessage.FILTER_ATTACK.toString(),
                    "0..3.0625", value);
        mAttack = value;
        FilterMessage.FILTER_ATTACK.send(getEngine(), getMachineIndex(),
                mAttack);
    }

    //----------------------------------
    // decay
    //----------------------------------

    private float mDecay = 0f;

    @Override
    public float getDecay()
    {
        return mDecay;
    }

    public float getDecay(boolean restore)
    {
        return FilterMessage.FILTER_DECAY.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setDecay(float value)
    {
        if (value == mDecay)
            return;
        if (value < 0 || value > 3.0625f)
            throw newRangeException(FilterMessage.FILTER_DECAY.toString(),
                    "0..3.0625", value);
        mDecay = value;
        FilterMessage.FILTER_DECAY.send(getEngine(), getMachineIndex(), mDecay);
    }

    //----------------------------------
    // release
    //----------------------------------

    private float mRelease = 1.5f;

    @Override
    public float getRelease()
    {
        return mRelease;
    }

    public float getRelease(boolean restore)
    {
        return FilterMessage.FILTER_RELEASE.query(getEngine(),
                getMachineIndex());
    }

    @Override
    public void setRelease(float value)
    {
        if (value == mRelease)
            return;
        if (value < 0 || value > 3.0625f)
            throw newRangeException(FilterMessage.FILTER_RELEASE.toString(),
                    "0..3.0625", value);
        mRelease = value;
        FilterMessage.FILTER_RELEASE.send(getEngine(), getMachineIndex(),
                mRelease);
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
        return FilterMessage.FILTER_SUSTAIN.query(getEngine(),
                getMachineIndex());
    }

    @Override
    public void setSustain(float value)
    {
        if (value == mSustain)
            return;
        if (value < 0 || value > 1.0f)
            throw newRangeException(FilterMessage.FILTER_SUSTAIN.toString(),
                    "0..1.0", value);
        mSustain = value;
        FilterMessage.FILTER_SUSTAIN.send(getEngine(), getMachineIndex(),
                mSustain);
    }

    //----------------------------------
    // track
    //----------------------------------

    private float mTrack = 0f;

    @Override
    public float getTrack()
    {
        return mTrack;
    }

    public float getTrack(boolean restore)
    {
        return FilterMessage.FILTER_KBTRACK.query(getEngine(),
                getMachineIndex());
    }

    @Override
    public void setTrack(float value)
    {
        if (value == mTrack)
            return;
        if (value < 0 || value > 1.0f)
            throw newRangeException(FilterMessage.FILTER_KBTRACK.toString(),
                    "0..1.0", value);
        mTrack = value;
        FilterMessage.FILTER_KBTRACK.send(getEngine(), getMachineIndex(),
                mTrack);
    }

    //----------------------------------
    // type
    //----------------------------------

    private FilterType mFilterType = FilterType.NONE;

    @Override
    public FilterType getType()
    {
        return mFilterType;
    }

    public FilterType getType(boolean restore)
    {
        return FilterType.toType(FilterMessage.FILTER_TYPE.query(getEngine(),
                getMachineIndex()));
    }

    @Override
    public void setType(FilterType value)
    {
        if (value == mFilterType)
            return;
        mFilterType = value;
        FilterMessage.FILTER_TYPE.send(getEngine(), getMachineIndex(),
                mFilterType.getValue());
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public SynthFilter(IMachine machine)
    {
        super(machine);
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
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
        try
        {
            memento.putFloat(FilterConstants.ATT_TRACK, getTrack());
        }
        catch (IllegalArgumentException e)
        {
            // PCMSynth dosn't have tracking
        }
        memento.putInteger(FilterConstants.ATT_TYPE, getType().getValue());
    }

    @Override
    public void paste(IMemento memento)
    {
        super.paste(memento);
        setAttack(memento.getFloat(FilterConstants.ATT_ATTACK));
        setDecay(memento.getFloat(FilterConstants.ATT_DECAY));
        setRelease(memento.getFloat(FilterConstants.ATT_RELEASE));
        setSustain(memento.getFloat(FilterConstants.ATT_SUSTAIN));
        try
        {
            setTrack(memento.getFloat(FilterConstants.ATT_TRACK));
        }
        catch (IllegalArgumentException e)
        {
            // PCMSynth dosn't have tracking
        }

        setType(FilterType.toType(memento.getInteger(FilterConstants.ATT_TYPE)));
    }

    @Override
    public void restore()
    {
        super.restore();

        setAttack(getAttack(true));
        setDecay(getDecay(true));
        setRelease(getRelease(true));
        setSustain(getSustain(true));
        try
        {
            setTrack(getTrack(true));
        }
        catch (IllegalArgumentException e)
        {
            // PCMSynth dosn't have tracking
        }

        setType(getType(true));
    }
}
