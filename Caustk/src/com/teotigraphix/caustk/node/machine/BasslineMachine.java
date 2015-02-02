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
import com.teotigraphix.caustk.node.RackInstance;
import com.teotigraphix.caustk.node.machine.patch.bassline.DistortionComponent;
import com.teotigraphix.caustk.node.machine.patch.bassline.FilterComponent;
import com.teotigraphix.caustk.node.machine.patch.bassline.LFO1Component;
import com.teotigraphix.caustk.node.machine.patch.bassline.Osc1Component;

/**
 * The Caustic <strong>Bassline</strong> OSC decorator.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class BasslineMachine extends Machine {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private FilterComponent filter;

    @Tag(101)
    private Osc1Component osc1;

    @Tag(102)
    private LFO1Component lfo1;

    @Tag(103)
    private DistortionComponent distortion;

    //--------------------------------------------------------------------------
    // Components
    //--------------------------------------------------------------------------

    //----------------------------------
    // filter
    //----------------------------------

    public FilterComponent getFilter() {
        return filter;
    }

    //----------------------------------
    // osc1
    //----------------------------------

    public Osc1Component getOsc1() {
        return osc1;
    }

    //----------------------------------
    // lfo1
    //----------------------------------

    public LFO1Component getLFO1() {
        return lfo1;
    }

    //----------------------------------
    // distortion
    //----------------------------------

    public DistortionComponent getDistortion() {
        return distortion;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public BasslineMachine() {
    }

    public BasslineMachine(RackInstance rackNode, int index, String name) {
        super(rackNode, index, MachineType.Bassline, name);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void intialize() {
        super.intialize();
        filter = new FilterComponent(this);
        osc1 = new Osc1Component(this);
        lfo1 = new LFO1Component(this);
        distortion = new DistortionComponent(this);
    }

    @Override
    protected void createComponents() {
        super.createComponents();
        filter.create();
        osc1.create();
        lfo1.create();
        distortion.create();
    }

    @Override
    protected void updateComponents() {
        super.updateComponents();
        filter.update();
        osc1.update();
        lfo1.update();
        distortion.update();
    }

    @Override
    protected void restorePresetProperties() {
        filter.restore();
        osc1.restore();
        lfo1.restore();
        distortion.restore();
    }
}
