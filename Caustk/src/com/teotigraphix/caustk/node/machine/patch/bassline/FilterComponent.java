////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.node.machine.patch.bassline;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.FilterMessage;
import com.teotigraphix.caustk.node.machine.BasslineMachine;
import com.teotigraphix.caustk.node.machine.MachineChannel;
import com.teotigraphix.caustk.node.machine.Machine;

/**
 * The bassline filter component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see BasslineMachine#getFilter()
 */
public class FilterComponent extends MachineChannel {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float decay = 0f;

    @Tag(101)
    private float envMod = 0.99f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // decay
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_DECAY
     */
    public float getDecay() {
        return decay;
    }

    public float queryDecay() {
        return FilterMessage.FILTER_DECAY.query(getRack(), getMachineIndex());
    }

    /**
     * @param decay (0.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_DECAY
     */
    public void setDecay(float decay) {
        if (decay == this.decay)
            return;
        if (decay < 0f || decay > 1f)
            throw newRangeException(FilterMessage.FILTER_DECAY, "0..1.0", decay);
        this.decay = decay;
        FilterMessage.FILTER_DECAY.send(getRack(), getMachineIndex(), decay);
    }

    //----------------------------------
    // envMod
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_ENVMOD
     */
    public float getEnvMod() {
        return envMod;
    }

    public float queryEnvMod() {
        return FilterMessage.FILTER_ENVMOD.query(getRack(), getMachineIndex());
    }

    /**
     * @param envMod (0.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_DECAY
     */
    public void setEnvMod(float envMod) {
        if (envMod == this.envMod)
            return;
        if (envMod < 0f || envMod > 1f)
            throw newRangeException(FilterMessage.FILTER_ENVMOD, "0..1", envMod);
        this.envMod = envMod;
        FilterMessage.FILTER_ENVMOD.send(getRack(), getMachineIndex(), envMod);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public FilterComponent() {
    }

    public FilterComponent(Machine machine) {
        super(machine);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void updateComponents() {
        FilterMessage.FILTER_DECAY.send(getRack(), getMachineIndex(), decay);
        FilterMessage.FILTER_ENVMOD.send(getRack(), getMachineIndex(), envMod);
    }

    @Override
    protected void restoreComponents() {
        setDecay(queryDecay());
        setEnvMod(queryEnvMod());
    }
}
