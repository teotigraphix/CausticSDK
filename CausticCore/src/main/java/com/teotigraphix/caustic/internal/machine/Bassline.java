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

package com.teotigraphix.caustic.internal.machine;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.effect.IBasslineDistortionUnit;
import com.teotigraphix.caustic.filter.IBasslineFilter;
import com.teotigraphix.caustic.filter.IBasslineLFO1;
import com.teotigraphix.caustic.filter.IBasslineOSC1;
import com.teotigraphix.caustic.filter.IVolumeComponent;
import com.teotigraphix.caustic.internal.effect.BasslineDistortionUnit;
import com.teotigraphix.caustic.internal.filter.BasslineFilter;
import com.teotigraphix.caustic.internal.filter.BasslineLFO1;
import com.teotigraphix.caustic.internal.filter.BasslineOSC1;
import com.teotigraphix.caustic.internal.filter.VolumeComponent;
import com.teotigraphix.caustic.machine.IBassline;

/**
 * The concrete implementation of the {@link IBassline} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class Bassline extends Synth implements IBassline {

    //--------------------------------------------------------------------------
    //
    // IBassline API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // volume
    //----------------------------------

    private IVolumeComponent mVolume;

    @Override
    public IVolumeComponent getVolume() {
        return mVolume;
    }

    public void setVolume(IVolumeComponent value) {
        mVolume = value;
    }

    //----------------------------------
    // distortion
    //----------------------------------

    private IBasslineDistortionUnit mDistortion;

    @Override
    public IBasslineDistortionUnit getDistortion() {
        return mDistortion;
    }

    public void setDistortion(IBasslineDistortionUnit value) {
        mDistortion = value;
    }

    //----------------------------------
    // lfo1
    //----------------------------------

    private IBasslineLFO1 mLFO1;

    @Override
    public IBasslineLFO1 getLFO1() {
        return mLFO1;
    }

    public void setLFO1(IBasslineLFO1 value) {
        mLFO1 = value;
    }

    //----------------------------------
    // filter
    //----------------------------------

    private IBasslineFilter mFilter;

    @Override
    public IBasslineFilter getFilter() {
        return mFilter;
    }

    public void setFilter(IBasslineFilter value) {
        mFilter = value;
    }

    //----------------------------------
    // osc1
    //----------------------------------

    private IBasslineOSC1 mOsc1;

    @Override
    public IBasslineOSC1 getOsc1() {
        return mOsc1;
    }

    public void setOsc1(IBasslineOSC1 value) {
        mOsc1 = value;
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    public Bassline(String id) {
        super();
        setId(id);
    }

    //--------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
        super.createComponents();
        setSynth(new BasslineSynthComponent(this));
        setVolume(new VolumeComponent(this));
        setDistortion(new BasslineDistortionUnit(this));
        setLFO1(new BasslineLFO1(this));
        setFilter(new BasslineFilter(this));
        setOsc1(new BasslineOSC1(this));
    }

    @Override
    public void copy(IMemento memento) {
        super.copy(memento);
        getOsc1().copy(memento.createChild(MachineConstants.TAG_OSC));
        getVolume().copy(memento.createChild(MachineConstants.TAG_VOLUME));
        getDistortion().copy(memento.createChild(MachineConstants.TAG_DISTORTION));
        getLFO1().copy(memento.createChild(MachineConstants.TAG_LFO));
        getFilter().copy(memento.createChild(MachineConstants.TAG_FILTER));
    }

    @Override
    public void paste(IMemento memento) {
        super.paste(memento);
        getOsc1().paste(memento.getChild(MachineConstants.TAG_OSC));
        getVolume().paste(memento.getChild(MachineConstants.TAG_VOLUME));
        getDistortion().paste(memento.getChild(MachineConstants.TAG_DISTORTION));
        getLFO1().paste(memento.getChild(MachineConstants.TAG_LFO));
        getFilter().paste(memento.getChild(MachineConstants.TAG_FILTER));
    }

    @Override
    public void restore() {
        getSequencer().restore();
        getOsc1().restore();
        getVolume().restore();
        getFilter().restore();
        getLFO1().restore();
        getDistortion().restore();
    }

}
