////////////////////////////////////////////////////////////////////////////////
//Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0 
//
//Unless required by applicable law or agreed to in writing, software 
//distributed under the License is distributed on an "AS IS" BASIS, 
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and 
//limitations under the License
//
//Author: Michael Schmalle, Principal Architect
//mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.node.machine;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.machine.patch.SynthFilterComponent;
import com.teotigraphix.caustk.node.machine.patch.VolumeEnvelopeComponent;
import com.teotigraphix.caustk.node.machine.patch.pcmsynth.LFO1Component;
import com.teotigraphix.caustk.node.machine.patch.pcmsynth.PCMSamplerComponent;
import com.teotigraphix.caustk.node.machine.patch.pcmsynth.PCMTunerComponent;

/**
 * The Caustic <strong>PCMSynth</strong> OSC decorator.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class PCMSynthMachine extends MachineNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private VolumeEnvelopeComponent volumeEnvelope;

    @Tag(101)
    private SynthFilterComponent filter;

    @Tag(102)
    private LFO1Component lfo1;

    @Tag(103)
    private PCMSamplerComponent sampler;

    @Tag(104)
    private PCMTunerComponent tuner;

    //--------------------------------------------------------------------------
    // Components
    //--------------------------------------------------------------------------

    //----------------------------------
    // volumeEnvelope
    //----------------------------------

    public VolumeEnvelopeComponent getVolumeEnvelope() {
        return volumeEnvelope;
    }

    //----------------------------------
    // filter
    //----------------------------------

    public SynthFilterComponent getFilter() {
        return filter;
    }

    //----------------------------------
    // lfo1
    //----------------------------------

    public LFO1Component getLFO1() {
        return lfo1;
    }

    //----------------------------------
    // sampler
    //----------------------------------

    public PCMSamplerComponent getSampler() {
        return sampler;
    }

    //----------------------------------
    // tuner
    //----------------------------------

    public PCMTunerComponent getTuner() {
        return tuner;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public PCMSynthMachine() {
    }

    public PCMSynthMachine(RackNode rackNode, int index, String name) {
        super(rackNode, index, MachineType.PCMSynth, name);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void intialize() {
        super.intialize();
        volumeEnvelope = new VolumeEnvelopeComponent(this);
        filter = new SynthFilterComponent(this);
        lfo1 = new LFO1Component(this);
        sampler = new PCMSamplerComponent(this);
        tuner = new PCMTunerComponent(this);
    }

    @Override
    protected void createComponents() {
        super.createComponents();
        volumeEnvelope.create();
        filter.create();
        lfo1.create();
        sampler.create();
        tuner.create();
    }

    @Override
    protected void updateComponents() {
        super.updateComponents();
        volumeEnvelope.update();
        filter.update();
        lfo1.update();
        sampler.update();
        tuner.update();
    }

    @Override
    protected void restorePresetProperties() {
        volumeEnvelope.restore();
        filter.restore();
        lfo1.restore();
        sampler.restore();
        tuner.restore();
    }
}
