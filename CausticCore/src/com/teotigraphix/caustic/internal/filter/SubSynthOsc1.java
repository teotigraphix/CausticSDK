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

import com.teotigraphix.caustic.filter.ISubSynthOsc1;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.SubSynthOscMessage;
import com.teotigraphix.common.IMemento;

/**
 * The default implementation of the {@link ISubSynthOsc1} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class SubSynthOsc1 extends OscillatorComponent implements ISubSynthOsc1
{

    //--------------------------------------------------------------------------
    //
    // ISubSynthOsc1 API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // bend
    //----------------------------------

    private float mBend = 0.0f;

    @Override
    public float getBend()
    {
        return mBend;
    }

    public float getBend(boolean restore)
    {
        return SubSynthOscMessage.OSC_BEND
                .query(getEngine(), getMachineIndex());
    }

    @Override
    public void setBend(float value)
    {
        if (value == mBend)
            return;
        if (value < 0 || value > 1f)
            throw newRangeException(SubSynthOscMessage.OSC_BEND.toString(),
                    "0..1", value);
        mBend = value;
        SubSynthOscMessage.OSC_BEND.send(getEngine(), getMachineIndex(), mBend);
    }

    //----------------------------------
    // fm
    //----------------------------------

    private float mFM = 0.0f;

    @Override
    public float getFM()
    {
        return mFM;
    }

    public float getFM(boolean restore)
    {
        return SubSynthOscMessage.OSC1_FM.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setFM(float value)
    {
        if (value == mFM)
            return;
        if (value < 0 || value > 1f)
            throw newRangeException(SubSynthOscMessage.OSC1_FM.toString(),
                    "0..1", value);
        mFM = value;
        SubSynthOscMessage.OSC1_FM.send(getEngine(), getMachineIndex(), mFM);
    }

    //----------------------------------
    // mix
    //----------------------------------

    private float mMix = 0.5f;

    @Override
    public float getMix()
    {
        return mMix;
    }

    public float getMix(boolean restore)
    {
        return SubSynthOscMessage.OSC_MIX.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setMix(float value)
    {
        if (value == mMix)
            return;
        if (value < 0 || value > 1.0f)
            throw newRangeException(SubSynthOscMessage.OSC_MIX.toString(),
                    "0..1", value);
        mMix = value;
        SubSynthOscMessage.OSC_MIX.send(getEngine(), getMachineIndex(), mMix);
    }

    //----------------------------------
    // waveform
    //----------------------------------

    private Waveform mWaveform = Waveform.SINE;

    @Override
    public Waveform getWaveform()
    {
        return mWaveform;
    }

    public Waveform getWaveform(boolean restore)
    {
        return Waveform.toType(SubSynthOscMessage.OSC1_WAVEFORM.query(
                getEngine(), getMachineIndex()));
    }

    @Override
    public void setWaveform(Waveform value)
    {
        if (value == mWaveform)
            return;
        mWaveform = value;
        SubSynthOscMessage.OSC1_WAVEFORM.send(getEngine(), getMachineIndex(),
                mWaveform.getValue());
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public SubSynthOsc1(IMachine machine)
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
        memento.putFloat(FilterConstants.ATT_BEND, getBend());
        memento.putFloat(FilterConstants.ATT_FM, getFM());
        memento.putFloat(FilterConstants.ATT_MIX, getMix());
        memento.putInteger(FilterConstants.ATT_WAVEFORM, getWaveform()
                .getValue());
    }

    @Override
    public void paste(IMemento memento)
    {
        super.paste(memento);
        setBend(memento.getFloat(FilterConstants.ATT_BEND));
        setFM(memento.getFloat(FilterConstants.ATT_FM));
        setMix(memento.getFloat(FilterConstants.ATT_MIX));
        setWaveform(Waveform.toType(memento
                .getInteger(FilterConstants.ATT_WAVEFORM)));
    }

    @Override
    public void restore()
    {
        super.restore();
        setBend(getBend(true));
        setFM(getFM(true));
        setMix(getMix(true));
        setWaveform(getWaveform(true));
    }
}
