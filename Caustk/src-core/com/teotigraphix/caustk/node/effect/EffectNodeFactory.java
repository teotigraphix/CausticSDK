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

package com.teotigraphix.caustk.node.effect;

import com.teotigraphix.caustk.core.CaustkFactory;
import com.teotigraphix.caustk.core.factory.CaustkFactoryChildBase;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link EffectNodeFactory} for creating {@link EffectNode}s.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class EffectNodeFactory extends CaustkFactoryChildBase {

    public EffectNodeFactory(CaustkFactory factory) {
        super(factory);
    }

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
}
