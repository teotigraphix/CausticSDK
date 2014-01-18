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

import com.teotigraphix.caustk.core.osc.ModularMessage;
import com.teotigraphix.caustk.node.machine.patch.MachineComponent;

public abstract class ModularComponentBase extends MachineComponent {

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

    protected int getBay() {
        return bay;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ModularComponentBase() {
    }

    public ModularComponentBase(int bay) {
        super();
        this.bay = bay;
    }

    protected float getValue(String control) {
        return ModularMessage.SET.query(getRack(), getMachineIndex(), getBay(), control);
    }

    protected void setValue(String control, Number value) {
        ModularMessage.SET.send(getRack(), getMachineIndex(), getBay(), control, value);
    }

    public void connect(IModularJack outJack, ModularComponentBase destination,
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
