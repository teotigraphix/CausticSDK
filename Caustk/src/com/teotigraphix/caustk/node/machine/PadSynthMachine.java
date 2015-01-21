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
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.machine.patch.padsynth.HarmonicsComponent;
import com.teotigraphix.caustk.node.machine.patch.padsynth.LFO1Component;
import com.teotigraphix.caustk.node.machine.patch.padsynth.LFO2Component;
import com.teotigraphix.caustk.node.machine.patch.padsynth.MorphComponent;
import com.teotigraphix.caustk.node.machine.patch.padsynth.VolumeEnvelopeComponent;

/**
 * The Caustic <strong>PadSynth</strong> OSC decorator.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class PadSynthMachine extends MachineNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private VolumeEnvelopeComponent volumeEnvelope;

    @Tag(101)
    private HarmonicsComponent harmonics;

    @Tag(102)
    private LFO1Component lfo1;

    @Tag(103)
    private LFO2Component lfo2;

    @Tag(104)
    private MorphComponent morph;

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
    // harmonics
    //----------------------------------

    public HarmonicsComponent getHarmonics() {
        return harmonics;
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
    // morph
    //----------------------------------

    public MorphComponent getMorph() {
        return morph;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public PadSynthMachine() {
    }

    public PadSynthMachine(RackNode rackNode, int index, String name) {
        super(rackNode, index, MachineType.PadSynth, name);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void intialize() {
        super.intialize();
        volumeEnvelope = new VolumeEnvelopeComponent(this);
        harmonics = new HarmonicsComponent(this);
        lfo1 = new LFO1Component(this);
        lfo2 = new LFO2Component(this);
        morph = new MorphComponent(this);
    }

    @Override
    protected void updateComponents() {
        super.updateComponents();
        volumeEnvelope.update();
        harmonics.update();
        lfo1.update();
        lfo2.update();
        morph.update();
    }

    @Override
    protected void restorePresetProperties() {
        volumeEnvelope.restore();
        harmonics.restore();
        lfo1.restore();
        lfo2.restore();
        morph.restore();
    }
}
