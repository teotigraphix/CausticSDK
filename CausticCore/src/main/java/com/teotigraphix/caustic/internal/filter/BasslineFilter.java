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

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.filter.IBasslineFilter;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.FilterMessage;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class BasslineFilter extends FilterComponent implements IBasslineFilter {

    //--------------------------------------------------------------------------
    //
    // IBasslineFilter API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // decay
    //----------------------------------

    private float mDecay = 0f;

    @Override
    public float getDecay() {
        return mDecay;
    }

    public float getDecay(boolean restore) {
        return FilterMessage.FILTER_DECAY.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setDecay(float value) {
        if (value == mDecay)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("filter_decay", "0..1", value);
        mDecay = value;
        FilterMessage.FILTER_DECAY.send(getEngine(), getMachineIndex(), mDecay);
    }

    //----------------------------------
    // envMod
    //----------------------------------

    private float mEnvMod = 0.99f;

    @Override
    public float getEnvMod() {
        return mEnvMod;
    }

    public float getEnvMod(boolean restore) {
        return FilterMessage.FILTER_ENVMOD.query(getEngine(), getMachineIndex());
    }

    @Override
    public void setEnvMod(float value) {
        if (value == mEnvMod)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("filter_envmod", "0..1", value);
        mEnvMod = value;
        FilterMessage.FILTER_ENVMOD.send(getEngine(), getMachineIndex(), mEnvMod);
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public BasslineFilter(IMachine machine) {
        super(machine);
        mCutoff = 0.4f;
        mResonance = 0.99f;
    }

    @Override
    public void copy(IMemento memento) {
        super.copy(memento);
        memento.putFloat(FilterConstants.ATT_ENVMOD, getEnvMod());
        memento.putFloat(FilterConstants.ATT_DECAY, getDecay());
    }

    @Override
    public void paste(IMemento memento) {
        super.paste(memento);
        setEnvMod(memento.getFloat(FilterConstants.ATT_ENVMOD));
        setDecay(memento.getFloat(FilterConstants.ATT_DECAY));
    }

    @Override
    public void restore() {
        super.restore();
        setDecay(getDecay(true));
        setEnvMod(getEnvMod(true));
    }
}
