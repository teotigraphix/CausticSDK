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

package com.teotigraphix.caustk.node.machine.patch.fmsynth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.teotigraphix.caustk.core.osc.FMSynthMessage;
import com.teotigraphix.caustk.core.osc.FMSynthMessage.FMOperatorControl;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The fm operators component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class FMOperatorComponent extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    // TODO the FMOperatorControl API really needs to be added to FMOperatorComponent

    private ArrayList<HashMap<FMOperatorControl, Float>> operators = new ArrayList<HashMap<FMOperatorControl, Float>>();

    // /caustic/[machine_index]/operator [op_index] [command] [value]

    public Float getOperatorValue(int index, FMOperatorControl control) {
        Map<FMOperatorControl, Float> map = operators.get(index);
        return map.get(control);
    }

    public void setOperatorValue(int index, FMOperatorControl control, boolean value) {
        setOperatorValue(index, control, value ? 1f : 0f);
    }

    public float queryOperatorValue(int index, FMOperatorControl control) {
        return FMSynthMessage.QUERY_OPERATOR.send(getRack(), getMachineIndex(), index,
                control.getValue());
    }

    public void setOperatorValue(int index, FMOperatorControl control, float value) {
        Map<FMOperatorControl, Float> map = operators.get(index);
        map.put(control, value);
        FMSynthMessage.OPERATOR
                .send(getRack(), getMachineIndex(), index, control.getValue(), value);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public FMOperatorComponent() {
    }

    public FMOperatorComponent(int machineIndex) {
        super(machineIndex);
    }

    public FMOperatorComponent(MachineNode machineNode) {
        super(machineNode);
        operators.add(new HashMap<FMSynthMessage.FMOperatorControl, Float>());
        operators.add(new HashMap<FMSynthMessage.FMOperatorControl, Float>());
        operators.add(new HashMap<FMSynthMessage.FMOperatorControl, Float>());
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
        for (int i = 0; i < 3; i++) {
            Map<FMOperatorControl, Float> map = operators.get(i);
            for (Entry<FMOperatorControl, Float> entry : map.entrySet()) {
                FMSynthMessage.OPERATOR.send(getRack(), getMachineIndex(), i, entry.getKey()
                        .getValue(), entry.getValue());
            }
        }
    }

    @Override
    protected void restoreComponents() {
        for (int i = 0; i < 3; i++) {
            for (FMOperatorControl control : FMOperatorControl.values()) {
                setOperatorValue(i, control, queryOperatorValue(i, control));
            }
        }
    }
}
