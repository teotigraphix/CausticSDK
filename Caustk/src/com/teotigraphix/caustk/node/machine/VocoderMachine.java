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

import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.node.machine.patch.PresetNode;
import com.teotigraphix.caustk.node.machine.patch.vocoder.ModulatorControls;
import com.teotigraphix.caustk.node.machine.patch.vocoder.VocoderModulator;

/**
 * The Caustic <strong>Vocoder</strong> OSC decorator.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class VocoderMachine extends MachineNode {

    @Override
    public PresetNode getPreset() {
        throw new IllegalStateException("Vocoder does not use presets");
    }

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private VocoderModulator modulator;

    private ModulatorControls controls;

    //--------------------------------------------------------------------------
    // Components
    //--------------------------------------------------------------------------

    //----------------------------------
    // modulator
    //----------------------------------

    public VocoderModulator getModulator() {
        return modulator;
    }

    //----------------------------------
    // controls
    //----------------------------------

    public ModulatorControls getControls() {
        return controls;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public VocoderMachine() {
    }

    public VocoderMachine(int index, String name) {
        super(index, MachineType.Vocoder, name);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void intialize() {
        super.intialize();
        modulator = new VocoderModulator(this);
        controls = new ModulatorControls(this);
    }

    @Override
    protected void createComponents() {
        super.createComponents();
        modulator.create();
        controls.create();
    }

    @Override
    protected void destroyComponents() {
        super.destroyComponents();
        modulator.destroy();
        controls.destroy();
    }

    @Override
    protected void updateComponents() {
        super.updateComponents();
        modulator.update();
        controls.update();
    }

    @Override
    protected void restorePresetProperties() {
        modulator.restore();
        controls.restore();
    }
}
