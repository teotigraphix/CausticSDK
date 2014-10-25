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

package com.teotigraphix.caustk.core.factory;

import com.teotigraphix.caustk.core.CaustkFactory;
import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.effect.AutoWahEffect;
import com.teotigraphix.caustk.node.effect.BitcrusherEffect;
import com.teotigraphix.caustk.node.effect.CabinetSimulatorEffect;
import com.teotigraphix.caustk.node.effect.ChorusEffect;
import com.teotigraphix.caustk.node.effect.CombFilterEffect;
import com.teotigraphix.caustk.node.effect.CompressorEffect;
import com.teotigraphix.caustk.node.effect.DelayEffect;
import com.teotigraphix.caustk.node.effect.DistortionEffect;
import com.teotigraphix.caustk.node.effect.EffectNode;
import com.teotigraphix.caustk.node.effect.EffectType;
import com.teotigraphix.caustk.node.effect.EffectChannel;
import com.teotigraphix.caustk.node.effect.FlangerEffect;
import com.teotigraphix.caustk.node.effect.LimiterEffect;
import com.teotigraphix.caustk.node.effect.MultiFilterEffect;
import com.teotigraphix.caustk.node.effect.ParametricEQEffect;
import com.teotigraphix.caustk.node.effect.PhaserEffect;
import com.teotigraphix.caustk.node.effect.ReverbEffect;
import com.teotigraphix.caustk.node.effect.StaticFlangerEffect;
import com.teotigraphix.caustk.node.effect.VinylSimulatorEffect;
import com.teotigraphix.caustk.node.machine.BasslineMachine;
import com.teotigraphix.caustk.node.machine.BeatBoxMachine;
import com.teotigraphix.caustk.node.machine.EightBitSynthMachine;
import com.teotigraphix.caustk.node.machine.FMSynthMachine;
import com.teotigraphix.caustk.node.machine.KSSynthMachine;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.ModularMachine;
import com.teotigraphix.caustk.node.machine.OrganMachine;
import com.teotigraphix.caustk.node.machine.PCMSynthMachine;
import com.teotigraphix.caustk.node.machine.PadSynthMachine;
import com.teotigraphix.caustk.node.machine.SubSynthMachine;
import com.teotigraphix.caustk.node.machine.VocoderMachine;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class NodeFactory extends CaustkFactoryChildBase {

    public NodeFactory(CaustkFactory factory) {
        super(factory);
    }

    //----------------------------------
    // Effect
    //----------------------------------

    /**
     * Creates an {@link EffectNode} subclass using the {@link EffectType}.
     * 
     * @param machineNode The machine for the new effect.
     * @param slot The effect slot within the {@link EffectChannel}.
     * @param effectType The {@link EffectType} of the effect to be created.
     * @return A new {@link EffectNode}, has not been added to an
     *         {@link EffectChannel}.
     */
    @SuppressWarnings("unchecked")
    public <T extends EffectNode> T createEffect(MachineNode machineNode, int slot,
            EffectType effectType) {
        EffectNode effect = null;
        switch (effectType) {
            case Autowah:
                effect = new AutoWahEffect(machineNode, slot);
                break;
            case Bitcrusher:
                effect = new BitcrusherEffect(machineNode, slot);
                break;
            case CabinetSimulator:
                effect = new CabinetSimulatorEffect(machineNode, slot);
                break;
            case Chorus:
                effect = new ChorusEffect(machineNode, slot);
                break;
            case CombFilter:
                effect = new CombFilterEffect(machineNode, slot);
                break;
            case Compressor:
                effect = new CompressorEffect(machineNode, slot);
                break;
            case Delay:
                effect = new DelayEffect(machineNode, slot);
                break;
            case Distortion:
                effect = new DistortionEffect(machineNode, slot);
                break;
            case Flanger:
                effect = new FlangerEffect(machineNode, slot);
                break;
            case Limiter:
                effect = new LimiterEffect(machineNode, slot);
                break;
            case MultiFilter:
                effect = new MultiFilterEffect(machineNode, slot);
                break;
            case ParametricEQ:
                effect = new ParametricEQEffect(machineNode, slot);
                break;
            case Phaser:
                effect = new PhaserEffect(machineNode, slot);
                break;
            case Reverb:
                effect = new ReverbEffect(machineNode, slot);
                break;
            case StaticFlanger:
                effect = new StaticFlangerEffect(machineNode, slot);
                break;
            case VinylSimulator:
                effect = new VinylSimulatorEffect(machineNode, slot);
                break;
        }
        return (T)effect;
    }

    //----------------------------------
    // Machine
    //----------------------------------

    /**
     * Creates a {@link MachineNode} subclass using the {@link MachineType}.
     * 
     * @param index The machine index lost in the native rack.
     * @param type The {@link MachineType} of machine to create.
     * @param name The machine name (10 character alpha numeric).
     * @return A new {@link MachineNode}, added to the native rack.
     */
    @SuppressWarnings("unchecked")
    public <T extends MachineNode> T createMachine(RackNode rackNode, int index, MachineType type,
            String name) {
        MachineNode machineNode = null;
        switch (type) {
            case SubSynth:
                machineNode = new SubSynthMachine(rackNode, index, name);
                break;
            case Bassline:
                machineNode = new BasslineMachine(rackNode, index, name);
                break;
            case BeatBox:
                machineNode = new BeatBoxMachine(rackNode, index, name);
                break;
            case EightBitSynth:
                machineNode = new EightBitSynthMachine(rackNode, index, name);
                break;
            case FMSynth:
                machineNode = new FMSynthMachine(rackNode, index, name);
                break;
            case Modular:
                machineNode = new ModularMachine(rackNode, index, name);
                break;
            case Organ:
                machineNode = new OrganMachine(rackNode, index, name);
                break;
            case PCMSynth:
                machineNode = new PCMSynthMachine(rackNode, index, name);
                break;
            case PadSynth:
                machineNode = new PadSynthMachine(rackNode, index, name);
                break;
            case Vocoder:
                machineNode = new VocoderMachine(rackNode, index, name);
                break;
            case KSSynth:
                machineNode = new KSSynthMachine(rackNode, index, name);
                break;
        }
        return (T)machineNode;
    }
}
