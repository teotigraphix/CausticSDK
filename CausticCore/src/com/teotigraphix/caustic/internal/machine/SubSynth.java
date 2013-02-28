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

package com.teotigraphix.caustic.internal.machine;

import com.teotigraphix.caustic.filter.IFilter;
import com.teotigraphix.caustic.filter.ISubSynthLFO1;
import com.teotigraphix.caustic.filter.ISubSynthLFO2;
import com.teotigraphix.caustic.filter.ISubSynthOsc1;
import com.teotigraphix.caustic.filter.ISubSynthOsc2;
import com.teotigraphix.caustic.filter.IVolumeEnvelope;
import com.teotigraphix.caustic.internal.filter.SubSynthLFO1;
import com.teotigraphix.caustic.internal.filter.SubSynthLFO2;
import com.teotigraphix.caustic.internal.filter.SubSynthOsc1;
import com.teotigraphix.caustic.internal.filter.SubSynthOsc2;
import com.teotigraphix.caustic.internal.filter.SynthFilter;
import com.teotigraphix.caustic.internal.filter.VolumeEnvelope;
import com.teotigraphix.caustic.machine.ISubSynth;
import com.teotigraphix.common.IMemento;

/**
 * The default implementation of the {@link ISubSynth} interface.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class SubSynth extends Synth implements ISubSynth {

    //--------------------------------------------------------------------------
    //
    // ISubSynth API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // volume
    //----------------------------------

    private IVolumeEnvelope mVolume;

    @Override
    public IVolumeEnvelope getVolume() {
        return mVolume;
    }

    public void setVolume(IVolumeEnvelope value) {
        mVolume = value;
    }

    //----------------------------------
    // filter
    //----------------------------------

    private IFilter mFilter;

    @Override
    public IFilter getFilter() {
        return mFilter;
    }

    public void setFilter(IFilter value) {
        mFilter = value;
    }

    //----------------------------------
    // osc1
    //----------------------------------

    private ISubSynthOsc1 mOsc1;

    @Override
    public ISubSynthOsc1 getOsc1() {
        return mOsc1;
    }

    public void setOsc1(ISubSynthOsc1 value) {
        mOsc1 = value;
    }

    //----------------------------------
    // osc2
    //----------------------------------

    private ISubSynthOsc2 mOsc2;

    @Override
    public ISubSynthOsc2 getOsc2() {
        return mOsc2;
    }

    public void setOsc2(ISubSynthOsc2 value) {
        mOsc2 = value;
    }

    //----------------------------------
    // lfo1
    //----------------------------------

    private ISubSynthLFO1 mLFO1;

    @Override
    public ISubSynthLFO1 getLFO1() {
        return mLFO1;
    }

    public void setLFO1(ISubSynthLFO1 value) {
        mLFO1 = value;
    }

    //----------------------------------
    // lfo2
    //----------------------------------

    private ISubSynthLFO2 mLFO2;

    @Override
    public ISubSynthLFO2 getLFO2() {
        return mLFO2;
    }

    public void setLFO2(ISubSynthLFO2 value) {
        mLFO2 = value;
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public SubSynth(String id) {
        super();
        setId(id);
    }

    //--------------------------------------------------------------------------
    //
    // Overridden Public :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        super.copy(memento);
        getVolume().copy(memento.createChild(MachineConstants.TAG_VOLUME));
        getFilter().copy(memento.createChild(MachineConstants.TAG_FILTER));
        getOsc1().copy(memento.createChild(MachineConstants.TAG_OSC1));
        getOsc2().copy(memento.createChild(MachineConstants.TAG_OSC2));
        getLFO1().copy(memento.createChild(MachineConstants.TAG_LFO1));
        getLFO2().copy(memento.createChild(MachineConstants.TAG_LFO2));
    }

    @Override
    public void paste(IMemento memento) {
        super.paste(memento);
        getVolume().paste(memento.getChild(MachineConstants.TAG_VOLUME));
        getFilter().paste(memento.getChild(MachineConstants.TAG_FILTER));
        getOsc1().paste(memento.getChild(MachineConstants.TAG_OSC1));
        getOsc2().paste(memento.getChild(MachineConstants.TAG_OSC2));
        getLFO1().paste(memento.getChild(MachineConstants.TAG_LFO1));
        getLFO2().paste(memento.getChild(MachineConstants.TAG_LFO2));
    }

    @Override
    public void restore() {
        super.restore();
        getVolume().restore();
        getFilter().restore();
        getOsc1().restore();
        getOsc2().restore();
        getLFO1().restore();
        getLFO2().restore();
    }

    //--------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
        super.createComponents();

        setVolume(new VolumeEnvelope(this));
        setFilter(new SynthFilter(this));
        setOsc1(new SubSynthOsc1(this));
        setOsc2(new SubSynthOsc2(this));
        setLFO1(new SubSynthLFO1(this));
        setLFO2(new SubSynthLFO2(this));
    }

    @Override
    public void dispose() {
        super.dispose();

        setVolume(null);
        setFilter(null);
        setOsc1(null);
        setOsc2(null);
        setLFO1(null);
        setLFO2(null);
    }
}
