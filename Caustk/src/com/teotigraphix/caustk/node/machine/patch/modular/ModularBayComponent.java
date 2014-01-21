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
import com.teotigraphix.caustk.core.osc.ModularMessage.ModularComponentType;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.patch.MachineComponent;

/**
 * The modular synth bay component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class ModularBayComponent extends MachineComponent {

    // TODO think about the further impl of ModularBayComponent
    // for now the Modular supports preset loading and dynamic creation but no
    // reserialization of node graph

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public ModularBayComponent() {
    }

    public ModularBayComponent(MachineNode machineNode) {
        super(machineNode);
    }

    /**
     * @param type
     * @param bay
     * @return
     */
    public ModularComponentBase create(ModularComponentType type, int bay) {
        ModularComponentBase component = null;
        int machineIndex = getMachineIndex();
        switch (type) {
            case TwoToOneMixerModulator:
                component = new TwoInputMixer(bay);
                component.setIndex(machineIndex);
                break;
            case ThreeToOneMixer:
                component = new ThreeInputMixer(bay);
                component.setIndex(machineIndex);
                break;
            case SixToOneMixer:
                component = new SixInputMixer(bay);
                component.setIndex(machineIndex);
                break;
            case Oscillator:
                component = new Oscillator(bay);
                component.setIndex(machineIndex);
                break;
            case SubOscillator:
                component = new SubOscillator(bay);
                component.setIndex(machineIndex);
                break;
            case PulseGenerator:
                component = new PulseGenerator(bay);
                component.setIndex(machineIndex);
                break;
            case DADSREnvelope:
                component = new DADSREnvelope(bay);
                component.setIndex(machineIndex);
                break;
            case AREnvelope:
                component = new AREnvelope(bay);
                component.setIndex(machineIndex);
                break;
            case DecayEnvelope:
                component = new DecayEnvelope(bay);
                component.setIndex(machineIndex);
                break;
            case SVFilter:
                component = new SVFilter(bay);
                component.setIndex(machineIndex);
                break;
            case StereoLPF:
                component = new StereoLPF(bay);
                component.setIndex(machineIndex);
                break;
            case FormantFilter:
                component = new FormantFilter(bay);
                component.setIndex(machineIndex);
                break;
            case MiniLFO:
                component = new MiniLFO(bay);
                component.setIndex(machineIndex);
                break;
            case NoiseGenerator:
                component = new NoiseGenerator(bay);
                component.setIndex(machineIndex);
                break;
            case PanModule:
                component = new PanModule(bay);
                component.setIndex(machineIndex);
                break;
            case CrossFade:
                component = new Crossfader(bay);
                component.setIndex(machineIndex);
                break;
            case LagProcessor:
                component = new LagProcessor(bay);
                component.setIndex(machineIndex);
                break;
            case Delay:
                component = new DelayModule(bay);
                component.setIndex(machineIndex);
                break;
            case SampleAndHold:
                component = new SampleAndHold(bay);
                component.setIndex(machineIndex);
                break;
            case CrossOver:
                component = new CrossoverModule(bay);
                component.setIndex(machineIndex);
                break;
            case Saturator:
                component = new Saturator(bay);
                component.setIndex(machineIndex);
                break;
            case FMPair:
                component = new FMPair(bay);
                component.setIndex(machineIndex);
                break;
            case Arpeggiator:
                component = new Arpeggiator(bay);
                component.setIndex(machineIndex);
                break;

            default:
                break;
        }
        ModularMessage.CREATE.send(getRack(), machineIndex, bay, type.getValue());
        return component;
    }

    @Override
    protected void createComponents() {
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void updateComponents() {
    }

    @Override
    protected void restoreComponents() {
    }
}
