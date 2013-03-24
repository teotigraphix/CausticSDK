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

package com.teotigraphix.caustic.internal.filter;

import com.teotigraphix.caustic.filter.IBasslineOSC1;
import com.teotigraphix.caustic.internal.machine.MachineComponent;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.BasslineOscMessage;
import com.teotigraphix.common.IMemento;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class BasslineOSC1 extends MachineComponent implements IBasslineOSC1
{

    //--------------------------------------------------------------------------
    //
    // IBasslineFilter API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // accent
    //----------------------------------

    private float mAccent = 0.5f;

    @Override
    public float getAccent()
    {
        return mAccent;
    }

    public float getAccent(boolean restore)
    {
        return BasslineOscMessage.ACCENT.query(getEngine(), getMachineIndex());

    }

    @Override
    public void setAccent(float value)
    {
        if (value == mAccent)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(BasslineOscMessage.ACCENT.toString(),
                    "0..1", value);
        mAccent = value;
        BasslineOscMessage.ACCENT.send(getEngine(), getMachineIndex(), mAccent);
    }

    //----------------------------------
    // pulseWidth
    //----------------------------------

    private float mPulseWidth = 0.5f;

    @Override
    public float getPulseWidth()
    {
        return mPulseWidth;
    }

    public float getPulseWidth(boolean restore)
    {
        return BasslineOscMessage.PULSE_WIDTH.query(getEngine(),
                getMachineIndex());
    }

    @Override
    public void setPulseWidth(float value)
    {
        if (value == mPulseWidth)
            return;
        if (value < 0.05f || value > 0.5f)
            throw newRangeException(BasslineOscMessage.PULSE_WIDTH.toString(),
                    "0.05..0.5", value);
        mPulseWidth = value;
        BasslineOscMessage.PULSE_WIDTH.send(getEngine(), getMachineIndex(),
                mPulseWidth);
    }

    //----------------------------------
    // tune
    //----------------------------------

    private int mTune = 0;

    @Override
    public int getTune()
    {
        return mTune;
    }

    public int getTune(boolean restore)
    {
        return (int) BasslineOscMessage.TUNE.query(getEngine(),
                getMachineIndex());
    }

    @Override
    public void setTune(int value)
    {
        if (value == mTune)
            return;
        if (value < -12 || value > 12)
            throw newRangeException(BasslineOscMessage.TUNE.toString(),
                    "-12..12", value);
        mTune = value;
        BasslineOscMessage.TUNE.send(getEngine(), getMachineIndex(), mTune);
    }

    //----------------------------------
    // waveform
    //----------------------------------

    private Waveform mWaveForm = Waveform.SAW;

    @Override
    public Waveform getWaveForm()
    {
        return mWaveForm;
    }

    public Waveform getWaveForm(boolean restore)
    {
        return Waveform.toType(BasslineOscMessage.WAVEFORM.query(getEngine(),
                getMachineIndex()));
    }

    @Override
    public void setWaveForm(Waveform value)
    {
        if (value == mWaveForm)
            return;
        mWaveForm = value;
        BasslineOscMessage.WAVEFORM.send(getEngine(), getMachineIndex(),
                mWaveForm.getValue());
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public BasslineOSC1(IMachine machine)
    {
        super(machine);
    }

    @Override
    public void copy(IMemento memento)
    {
        super.copy(memento);
        memento.putInteger(FilterConstants.ATT_WAVEFORM, getWaveForm()
                .getValue());
        memento.putFloat(FilterConstants.ATT_PULSEWIDTH, getPulseWidth());
        memento.putInteger(FilterConstants.ATT_TUNE, getTune());
        memento.putFloat(FilterConstants.ATT_ACCENT, getAccent());
    }

    @Override
    public void paste(IMemento memento)
    {
        super.paste(memento);
        setWaveForm(Waveform.toType(memento
                .getInteger(FilterConstants.ATT_WAVEFORM)));
        setPulseWidth(memento.getFloat(FilterConstants.ATT_PULSEWIDTH));
        setTune(memento.getInteger(FilterConstants.ATT_TUNE));
        setAccent(memento.getFloat(FilterConstants.ATT_ACCENT));
    }

    @Override
    public void restore()
    {
        super.restore();
        setAccent(getAccent(true));
        setPulseWidth(getPulseWidth(true));
        setTune(getTune(true));
        setWaveForm(getWaveForm(true));
    }
}
