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

package com.teotigraphix.caustic.internal.effect;

import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.effect.IBasslineDistortionUnit;
import com.teotigraphix.caustic.osc.BasslineDistortionMessage;
import com.teotigraphix.common.IMemento;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class BasslineDistortionUnit extends EffectComponent implements IBasslineDistortionUnit {

    //--------------------------------------------------------------------------
    //
    // IBasslineDistortionUnit API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // amount
    //----------------------------------

    private float mAmount = 15f;

    @Override
    public float getAmount() {
        return mAmount;
    }

    public float getAmount(boolean restore) {
        return BasslineDistortionMessage.DISTORTION_AMOUNT.query(getEngine(), getDeviceIndex());
    }

    @Override
    public void setAmount(float value) {
        if (value == mAmount)
            return;
        if (value < 0f || value > 20f)
            throw newRangeException(BasslineDistortionMessage.DISTORTION_AMOUNT.toString(),
                    "0..20", value);
        mAmount = value;
        BasslineDistortionMessage.DISTORTION_AMOUNT.send(getEngine(), getDeviceIndex(), mAmount);
    }

    //----------------------------------
    // postGain
    //----------------------------------

    private float mPostGain = 0.2f;

    @Override
    public float getPostGain() {
        return mPostGain;
    }

    public float getPostGain(boolean restore) {
        return BasslineDistortionMessage.DISTORTION_POSTGAIN.query(getEngine(), getDeviceIndex());
    }

    @Override
    public void setPostGain(float value) {
        if (value == mPostGain)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(BasslineDistortionMessage.DISTORTION_POSTGAIN.toString(),
                    "0..1", value);
        mPostGain = value;
        BasslineDistortionMessage.DISTORTION_POSTGAIN
                .send(getEngine(), getDeviceIndex(), mPostGain);
    }

    //----------------------------------
    // preGain
    //----------------------------------

    private float mPreGain = 4.05f;

    @Override
    public float getPreGain() {
        return mPreGain;
    }

    public float getPreGain(boolean restore) {
        return BasslineDistortionMessage.DISTORTION_PREGAIN.query(getEngine(), getDeviceIndex());
    }

    @Override
    public void setPreGain(float value) {
        if (value == mPreGain)
            return;
        if (value < 0f || value > 5f)
            throw newRangeException(BasslineDistortionMessage.DISTORTION_PREGAIN.toString(),
                    "0..5", value);
        mPreGain = value;
        BasslineDistortionMessage.DISTORTION_PREGAIN.send(getEngine(), getDeviceIndex(), mPreGain);
    }

    //----------------------------------
    // program
    //----------------------------------

    private Program mProgram = Program.OFF;

    @Override
    public Program getProgram() {
        return mProgram;
    }

    public Program getProgram(boolean restore) {
        return Program.toType(BasslineDistortionMessage.DISTORTION_PROGRAM.query(getEngine(),
                getDeviceIndex()));
    }

    @Override
    public void setProgram(Program value) {
        if (value == mProgram)
            return;
        mProgram = value;
        BasslineDistortionMessage.DISTORTION_PROGRAM.send(getEngine(), getDeviceIndex(),
                mProgram.getValue());
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public BasslineDistortionUnit(IDevice device) {
        super(device);
        setName(device.getId());
    }

    @Override
    public void copy(IMemento memento) {
        memento.putInteger(EffectConstants.ATT_PROGRAM, mProgram.getValue());
        memento.putFloat(EffectConstants.ATT_PREGAIN, mPreGain);
        memento.putFloat(EffectConstants.ATT_AMOUNT, mAmount);
        memento.putFloat(EffectConstants.ATT_POSTGAIN, mPostGain);
    }

    @Override
    public void paste(IMemento memento) {
        setProgram(Program.toType(memento.getInteger(EffectConstants.ATT_PROGRAM)));
        setPreGain(memento.getFloat(EffectConstants.ATT_PREGAIN));
        setAmount(memento.getFloat(EffectConstants.ATT_AMOUNT));
        setPostGain(memento.getFloat(EffectConstants.ATT_POSTGAIN));
    }

    @Override
    public void restore() {
        super.restore();
        setAmount(getAmount(true));
        setPostGain(getPostGain(true));
        setPreGain(getPreGain(true));
        setProgram(getProgram(true));
    }
}
