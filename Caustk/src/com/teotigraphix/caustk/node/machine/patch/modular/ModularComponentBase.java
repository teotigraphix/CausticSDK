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

package com.teotigraphix.caustk.node.machine.patch.modular;

import java.util.ArrayList;
import java.util.Collection;

import com.teotigraphix.caustk.core.osc.ModularMessage;
import com.teotigraphix.caustk.core.osc.ModularMessage.ModularComponentType;
import com.teotigraphix.caustk.node.NodeMetaData;
import com.teotigraphix.caustk.node.machine.MachineChannel;
import com.teotigraphix.caustk.node.machine.Machine;
import com.teotigraphix.caustk.utils.node.ModularUtils;

public abstract class ModularComponentBase extends MachineChannel implements IModularComponent {

    @Override
    public Collection<ModularControl> getControls() {
        return ModularUtils.getControls(getType()).values();
    }

    @Override
    public Collection<ModularControl> getFrontControls() {
        ArrayList<ModularControl> result = new ArrayList<ModularControl>();
        for (ModularControl control : getControls()) {
            if (control.getControl().name().contains("Front_"))
                result.add(control);
        }
        return result;
    }

    @Override
    public Collection<ModularControl> getRearControls() {
        ArrayList<ModularControl> result = new ArrayList<ModularControl>();
        for (ModularControl control : getControls()) {
            if (control.getControl().name().contains("Rear_"))
                result.add(control);
        }
        return result;
    }

    @Override
    public String toString() {
        return "[" + getBay() + "]" + getLabel();
    }

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private int bay;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    /**
     * Returns the number of bays this component requires.
     */
    protected abstract int getNumBays();

    @Override
    public int getBay() {
        return bay;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ModularComponentBase() {
    }

    public ModularComponentBase(Machine machineNode, int bay) {
        super(machineNode);
        this.bay = bay;
        setData(new NodeMetaData(this));
    }

    protected float getValue(String control) {
        return ModularMessage.SET.query(getRack(), getMachineIndex(), getBay(), control);
    }

    protected void setValue(String control, Number value) {
        ModularMessage.SET.send(getRack(), getMachineIndex(), getBay(), control, value);
    }

    @Override
    public ModularComponentType getType() {
        if (this instanceof WaveformGenerator)
            return ModularComponentType.WaveformGenerator;
        else if (this instanceof ResonantLP)
            return ModularComponentType.ResonantLP;
        else if (this instanceof Saturator)
            return ModularComponentType.Saturator;
        else if (this instanceof DecayEnvelope)
            return ModularComponentType.DecayEnvelope;
        return null;
    }

    @Override
    public void connect(IModularJack outJack, IModularComponent destination,
            IModularJack destinationInJack) {
        // /caustic/[machine]/connect [src_bay] [src_jack] [dest_bay] [dest_jack]

        int sourceBay = getBay();
        int sourceJack = outJack.getValue();
        int destinationBay = destination.getBay();
        int destinationJack = destinationInJack.getValue();

        ModularMessage.CONNECT.send(getRack(), getMachineIndex(), sourceBay, sourceJack,
                destinationBay, destinationJack);
    }

    @Override
    protected void createComponents() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void destroyComponents() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void updateComponents() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void restoreComponents() {
        // TODO Auto-generated method stub

    }
}
