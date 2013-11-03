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

package com.teotigraphix.caustk.rack.tone;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.IRackContext;
import com.teotigraphix.caustk.controller.IRackSerializer;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.live.Machine;
import com.teotigraphix.caustk.live.MachineType;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.tone.components.MixerChannel;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent;
import com.teotigraphix.caustk.rack.tone.components.SynthComponent;

/**
 * The base class for all tone's that wrap a native Caustic machine.
 * 
 * @author Michael Schmalle
 */
public abstract class RackTone implements IRackSerializer {

    private transient IRack rack;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private Machine machine;

    @Tag(1)
    private int machineIndex;

    @Tag(2)
    private MachineType machineType;

    @Tag(3)
    private String machineName;

    @Tag(4)
    private Map<Class<? extends RackToneComponent>, RackToneComponent> components = new HashMap<Class<? extends RackToneComponent>, RackToneComponent>();

    /**
     * Returns the core audio engine interface.
     */
    public final ICausticEngine getEngine() {
        return rack;
    }

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // machine
    //----------------------------------

    public Machine getMachine() {
        return machine;
    }

    //----------------------------------
    // machineIndex
    //----------------------------------

    /**
     * The index location of the tone loaded into/from the core rack.
     */
    public final int getMachineIndex() {
        return machineIndex;
    }

    //----------------------------------
    // machineType
    //----------------------------------

    public final MachineType getMachineType() {
        return machineType;
    }

    //----------------------------------
    // machineName
    //----------------------------------

    /**
     * The name loaded into/from the core rack.
     */
    public final String getMachineName() {
        return machineName;
    }

    /**
     * Returns the native machine name in the caustic rack.
     * 
     * @param restore Retrieve the native machine name.
     */
    public final String getMachineName(boolean restore) {
        return RackMessage.QUERY_MACHINE_NAME.queryString(getEngine(), machineIndex);
    }

    /**
     * Sets the new name of the tone, will send the
     * {@link RackMessage#MACHINE_NAME} message to the core.
     * 
     * @param value The new name of the tone, 10 character limit, cannot be
     *            <code>null</code>.
     */
    public final void setMachineName(String value) {
        setMachineName(value, false);
    }

    public void setMachineName(String value, boolean noUpdate) {
        if (value.equals(machineName))
            return;
        machineName = value;
        if (!noUpdate)
            RackMessage.MACHINE_NAME.send(getEngine(), machineIndex, machineName);
    }

    //--------------------------------------------------------------------------
    // Public Component API
    //--------------------------------------------------------------------------

    public int getComponentCount() {
        return components.size();
    }

    /**
     * Adds a {@link RackToneComponent} to the tone's component map and sets the
     * component's tone reference.
     * 
     * @param clazz The component API class.
     * @param instance The component instance.
     */
    void addComponent(Class<? extends RackToneComponent> clazz, RackToneComponent instance) {
        components.put(clazz, instance);
        instance.setTone(this);
    }

    /**
     * Returns a {@link RackToneComponent} by class type.
     * 
     * @param clazz The component API class.
     */
    public <T extends RackToneComponent> T getComponent(Class<T> clazz) {
        return clazz.cast(components.get(clazz));
    }

    public MixerChannel getMixer() {
        return getComponent(MixerChannel.class);
    }

    public SynthComponent getSynth() {
        return getComponent(SynthComponent.class);
    }

    public PatternSequencerComponent getPatternSequencer() {
        return getComponent(PatternSequencerComponent.class);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    RackTone() {
    }

    RackTone(Machine machine, MachineType machineType, String machineName, int index) {
        this.machine = machine;
        this.machineType = machineType;
        this.machineName = machineName;
        this.machineIndex = index;
        this.rack = machine.getRackSet().getRack();
    }

    //--------------------------------------------------------------------------
    // IRackSerializer API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public abstract void create();

    @Override
    public void load(IRackContext context) throws CausticException {
        rack = context.getRack();
    }

    @Override
    public void update(IRackContext context) {
        rack = context.getRack();
    }

    @Override
    public void restore() {
        setMachineName(getMachineName(true), true);
        for (RackToneComponent component : components.values()) {
            component.restore();
        }
    }
}
