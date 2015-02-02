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

package com.teotigraphix.caustk.node.machine.patch.eightbitsynth;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.EightBitSynthMessage;
import com.teotigraphix.caustk.node.machine.EightBitSynthMachine;
import com.teotigraphix.caustk.node.machine.MachineChannel;
import com.teotigraphix.caustk.node.machine.Machine;
import com.teotigraphix.caustk.node.machine.PadSynthMachine;

/**
 * The {@link EightBitSynthMachine} expression component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see PadSynthMachine#getLFO1()
 */
public class ExpressionComponent extends MachineChannel {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private String[] expressions = new String[2];

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // expression
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EightBitSynthMessage#EXPRESSION
     */
    public String getExpression(int index) {
        return expressions[index];
    }

    public String queryExpression(int index) {
        return EightBitSynthMessage.QUERY_EXPRESSION.queryString(getRack(), getMachineIndex(),
                index);
    }

    /**
     * @param index (0,1)
     * @param expression The String expression.
     * @see com.teotigraphix.caustk.core.osc.EightBitSynthMessage#EXPRESSION
     */
    public void setExpression(int index, String expression) {
        expressions[index] = expression;
        EightBitSynthMessage.EXPRESSION.send(getRack(), getMachineIndex(), index, expression);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public ExpressionComponent() {
    }

    public ExpressionComponent(Machine machineNode) {
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
        EightBitSynthMessage.EXPRESSION.send(getRack(), getMachineIndex(), 0, expressions[0]);
        EightBitSynthMessage.EXPRESSION.send(getRack(), getMachineIndex(), 1, expressions[1]);
    }

    @Override
    protected void restoreComponents() {
        String expression1 = queryExpression(0);
        String expression2 = queryExpression(1);
        if (expression1 != null)
            setExpression(0, expression1);
        if (expression2 != null)
            setExpression(1, expression2);
    }
}
