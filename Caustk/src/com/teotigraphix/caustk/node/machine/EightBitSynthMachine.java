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
import com.teotigraphix.caustk.node.RackInstance;
import com.teotigraphix.caustk.node.machine.patch.eightbitsynth.EightBitSynthControlsComponent;
import com.teotigraphix.caustk.node.machine.patch.eightbitsynth.ExpressionComponent;

/**
 * The Caustic <strong>8BitSynth</strong> OSC decorator.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class EightBitSynthMachine extends Machine {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private ExpressionComponent expression;

    @Tag(101)
    private EightBitSynthControlsComponent controls;

    //--------------------------------------------------------------------------
    // Components
    //--------------------------------------------------------------------------

    //----------------------------------
    // expression
    //----------------------------------

    public ExpressionComponent getExpression() {
        return expression;
    }

    //----------------------------------
    // controls
    //----------------------------------

    public EightBitSynthControlsComponent getControls() {
        return controls;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public EightBitSynthMachine() {
    }

    public EightBitSynthMachine(RackInstance rackNode, int index, String name) {
        super(rackNode, index, MachineType.EightBitSynth, name);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void intialize() {
        super.intialize();
        expression = new ExpressionComponent(this);
        controls = new EightBitSynthControlsComponent(this);
    }

    @Override
    protected void createComponents() {
        super.createComponents();
        expression.create();
        controls.create();
    }

    @Override
    protected void destroyComponents() {
        super.destroyComponents();
        expression.destroy();
        controls.destroy();
    }

    @Override
    protected void updateComponents() {
        super.updateComponents();
        expression.update();
        controls.update();
    }

    @Override
    protected void restorePresetProperties() {
        expression.restore();
        controls.restore();
    }
}
