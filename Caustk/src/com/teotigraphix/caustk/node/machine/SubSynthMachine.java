////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.node.machine;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.machine.patch.SynthFilterComponent;
import com.teotigraphix.caustk.node.machine.patch.VolumeEnvelopeComponent;
import com.teotigraphix.caustk.node.machine.patch.subsynth.LFO1Component;
import com.teotigraphix.caustk.node.machine.patch.subsynth.LFO2Component;
import com.teotigraphix.caustk.node.machine.patch.subsynth.Osc1Component;
import com.teotigraphix.caustk.node.machine.patch.subsynth.Osc2Component;

/**
 * The Caustic <strong>SubSynth</strong> OSC decorator.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class SubSynthMachine extends MachineNode {

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
    private LFO2Component lfo2;

    @Tag(104)
    private Osc1Component osc1;

    @Tag(105)
    private Osc2Component osc2;

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
    // lfo2
    //----------------------------------

    public LFO2Component getLFO2() {
        return lfo2;
    }

    //----------------------------------
    // osc1
    //----------------------------------

    public Osc1Component getOsc1() {
        return osc1;
    }

    //----------------------------------
    // osc2
    //----------------------------------

    public Osc2Component getOsc2() {
        return osc2;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public SubSynthMachine() {
    }

    public SubSynthMachine(RackNode rackNode, int index, String name) {
        super(rackNode, index, MachineType.SubSynth, name);
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
        lfo2 = new LFO2Component(this);
        osc1 = new Osc1Component(this);
        osc2 = new Osc2Component(this);
    }

    @Override
    protected void createComponents() {
        super.createComponents();
        volumeEnvelope.create();
        filter.create();
        lfo1.create();
        lfo2.create();
        osc1.create();
        osc2.create();
    }

    @Override
    protected void updateComponents() {
        super.updateComponents();
        volumeEnvelope.update();
        filter.update();
        lfo1.update();
        lfo2.update();
        osc1.update();
        osc2.update();
    }

    @Override
    protected void restorePresetProperties() {
        volumeEnvelope.restore();
        filter.restore();
        lfo1.restore();
        lfo2.restore();
        osc1.restore();
        osc2.restore();
    }
}
