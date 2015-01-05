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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.ModularMessage;
import com.teotigraphix.caustk.core.osc.ModularMessage.ModularComponentType;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;

import java.util.HashMap;
import java.util.Map;

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

    @Tag(100)
    // <bay, IModularComponent>
    private Map<Integer, IModularComponent> components = new HashMap<Integer, IModularComponent>();

    public Map<Integer, IModularComponent> getComponents() {
        return components;
    }

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
        MachineNode machineNode = getMachineNode();
        int machineIndex = getMachineIndex();
        switch (type) {

            case Empty:
                break;

            case AREnvelope:
                component = new AREnvelope(machineNode, bay);
                break;

            case Arpeggiator:
                component = new Arpeggiator(machineNode, bay);
                break;

            case CrossFade:
                component = new Crossfader(machineNode, bay);
                break;

            case CrossOver:
                component = new CrossoverModule(machineNode, bay);
                break;

            case DADSREnvelope:
                component = new DADSREnvelope(machineNode, bay);
                break;

            case DecayEnvelope:
                component = new DecayEnvelope(machineNode, bay);
                break;

            case Delay:
                component = new DelayModule(machineNode, bay);
                break;

            case FMPair:
                component = new FMPair(machineNode, bay);
                break;

            case FormantFilter:
                component = new FormantFilter(machineNode, bay);
                break;

            case LagProcessor:
                component = new LagProcessor(machineNode, bay);
                break;

            case MiniLFO:
                component = new MiniLFO(machineNode, bay);
                break;

            case NoiseGenerator:
                component = new NoiseGenerator(machineNode, bay);
                break;

            case PanModule:
                component = new PanModule(machineNode, bay);
                break;

            case PulseGenerator:
                component = new PulseGenerator(machineNode, bay);
                break;

            case ResonantLP:
                component = new ResonantLP(machineNode, bay);
                break;

            case SampleAndHold:
                component = new SampleAndHold(machineNode, bay);
                break;

            case Saturator:
                component = new Saturator(machineNode, bay);
                break;

            case SixToOneMixer:
                component = new SixInputMixer(machineNode, bay);
                break;

            case SubOscillator:
                component = new SubOscillator(machineNode, bay);
                break;

            case SVFilter:
                component = new SVFilter(machineNode, bay);
                break;

            case ThreeToOneMixer:
                component = new ThreeInputMixer(machineNode, bay);
                break;

            case TwoToOneMixerModulator:
                component = new TwoInputMixer(machineNode, bay);
                break;

            case WaveformGenerator:
                component = new WaveformGenerator(machineNode, bay);
                break;
        }

        ModularMessage.CREATE.send(getRack(), machineIndex, bay, type.getValue());
        // TODO Modular check bays during component creation
        components.put(bay, component);
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
        for (int i = 0; i < 16; i++) {
            int component = (int)ModularMessage.TYPE.send(getRack(), 0, i);
            if (component != 0) {
                ModularComponentType type = ModularComponentType.fromInt(component);
                if (type == null) {
                    System.err.println("Modular Type null: " + this);
                    continue;
                }
                ModularComponentBase comp = create(type, i);
                components.put(i, comp);
                if (comp.getNumBays() > 1)
                    i++;
            }
        }
        for (IModularComponent component : components.values()) {
            component.restore();
        }
    }
}
