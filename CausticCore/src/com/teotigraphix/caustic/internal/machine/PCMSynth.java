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

import com.teotigraphix.caustic.filter.IFilter;
import com.teotigraphix.caustic.filter.IPCMSynthLFO1;
import com.teotigraphix.caustic.filter.IPitchTuner;
import com.teotigraphix.caustic.filter.IVolumeEnvelope;
import com.teotigraphix.caustic.internal.filter.PCMSynthLFO1;
import com.teotigraphix.caustic.internal.filter.PitchTuner;
import com.teotigraphix.caustic.internal.filter.SynthFilter;
import com.teotigraphix.caustic.internal.filter.VolumeEnvelope;
import com.teotigraphix.caustic.internal.sampler.PCMSampler;
import com.teotigraphix.caustic.internal.sequencer.PatternSequencer;
import com.teotigraphix.caustic.machine.IPCMSynth;
import com.teotigraphix.caustic.sampler.IPCMSampler;
import com.teotigraphix.common.IMemento;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class PCMSynth extends Synth implements IPCMSynth
{

    /**
     * The device name prefix.
     */
    public static final String DEVICE_ID = "pcmsynth";

    //--------------------------------------------------------------------------
    //
    // IPCMSynth API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // volume
    //----------------------------------

    private IVolumeEnvelope mVolume;

    @Override
    public IVolumeEnvelope getVolume()
    {
        return mVolume;
    }

    public void setVolume(IVolumeEnvelope value)
    {
        mVolume = value;
    }

    //----------------------------------
    // filter
    //----------------------------------

    private IFilter mFilter;

    @Override
    public IFilter getFilter()
    {
        return mFilter;
    }

    public void setFilter(IFilter value)
    {
        mFilter = value;
    }

    //----------------------------------
    // lfo1
    //----------------------------------

    private IPCMSynthLFO1 mLFO1;

    @Override
    public IPCMSynthLFO1 getLFO1()
    {
        return mLFO1;
    }

    public void setLFO1(IPCMSynthLFO1 value)
    {
        mLFO1 = value;
    }

    //----------------------------------
    // pitch
    //----------------------------------

    private IPitchTuner mPitch;

    @Override
    public IPitchTuner getPitch()
    {
        return mPitch;
    }

    public void setPitch(IPitchTuner value)
    {
        mPitch = value;
    }

    //----------------------------------
    // pitch
    //----------------------------------

    private IPCMSampler mSampler;

    @Override
    public IPCMSampler getSampler()
    {
        return mSampler;
    }

    public void setSampler(IPCMSampler value)
    {
        mSampler = value;
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public PCMSynth(String id)
    {
        super();
        setId(id);
    }

    //--------------------------------------------------------------------------
    //
    // Overridden Public :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento)
    {
        super.copy(memento);
        getVolume().copy(memento.createChild(MachineConstants.TAG_VOLUME));
        getFilter().copy(memento.createChild(MachineConstants.TAG_FILTER));
        getLFO1().copy(memento.createChild(MachineConstants.TAG_LFO1));
        getPitch().copy(memento.createChild(MachineConstants.TAG_PITCH));
        getSampler().copy(memento.createChild(MachineConstants.TAG_SAMPLER));
    }

    @Override
    public void paste(IMemento memento)
    {
        super.paste(memento);
        getVolume().paste(memento.getChild(MachineConstants.TAG_VOLUME));
        getFilter().paste(memento.getChild(MachineConstants.TAG_FILTER));
        getLFO1().paste(memento.getChild(MachineConstants.TAG_LFO1));
        getPitch().paste(memento.getChild(MachineConstants.TAG_PITCH));
        getSampler().paste(memento.createChild(MachineConstants.TAG_SAMPLER));
    }

    @Override
    public void restore()
    {
        super.restore();
        getVolume().restore();
        getFilter().restore();
        getLFO1().restore();
        getPitch().restore();
        getSampler().restore();
    }

    @Override
    public void dispose()
    {
        super.dispose();
    }

    //--------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents()
    {
        super.createComponents();

        setSequencer(new PatternSequencer(this));
        setVolume(new VolumeEnvelope(this));
        setFilter(new SynthFilter(this));
        setLFO1(new PCMSynthLFO1(this));
        setPitch(new PitchTuner(this));
        setSampler(new PCMSampler(this));
    }

}
