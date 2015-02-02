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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.FMSynthMessage;
import com.teotigraphix.caustk.core.osc.FMSynthMessage.FMOperatorControl;
import com.teotigraphix.caustk.node.machine.MachineChannel;
import com.teotigraphix.caustk.node.machine.Machine;

/**
 * The fm operators component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class FMOperatorComponent extends MachineChannel {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private HashMap<FMOperatorControl, Float> op1 = new HashMap<FMOperatorControl, Float>();

    @Tag(101)
    private HashMap<FMOperatorControl, Float> op2 = new HashMap<FMOperatorControl, Float>();

    @Tag(102)
    private HashMap<FMOperatorControl, Float> op3 = new HashMap<FMOperatorControl, Float>();

    // /caustic/[machine_index]/operator [op_index] [command] [value]

    public Float getOperatorValue(int index, FMOperatorControl control) {
        Map<FMOperatorControl, Float> map = get(index);
        return map.get(control);
    }

    private Map<FMOperatorControl, Float> get(int index) {
        switch (index) {
            case 0:
                return op1;
            case 1:
                return op2;
            case 2:
                return op3;
        }
        return null;
    }

    public void setOperatorValue(int index, FMOperatorControl control, boolean value) {
        setOperatorValue(index, control, value ? 1f : 0f);
    }

    public float queryOperatorValue(int index, FMOperatorControl control) {
        return FMSynthMessage.QUERY_OPERATOR.send(getRack(), getMachineIndex(), index,
                control.getValue());
    }

    public void setOperatorValue(int index, FMOperatorControl control, float value) {
        Map<FMOperatorControl, Float> map = get(index);
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

    public FMOperatorComponent(Machine machineNode) {
        super(machineNode);
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
            Map<FMOperatorControl, Float> map = get(i);
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
