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

/**
 * The {@link EffectNodeFactory} for creating {@link EffectNode}s.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class EffectNodeFactory {

    @SuppressWarnings("unchecked")
    public <T extends EffectNode> T create(int machineIndex, int slot, EffectType effectType) {
        EffectNode effect = null;
        switch (effectType) {
            case Autowah:
                effect = new AutoWahEffect(slot, machineIndex);
                break;
            case Bitcrusher:
                effect = new BitcrusherEffect(slot, machineIndex);
                break;
            case CabinetSimulator:
                effect = new CabinetSimulatorEffect(slot, machineIndex);
                break;
            case Chorus:
                effect = new ChorusEffect(slot, machineIndex);
                break;
            case CombFilter:
                effect = new CombFilterEffect(slot, machineIndex);
                break;
            case Compressor:
                effect = new CompressorEffect(slot, machineIndex);
                break;
            case Delay:
                effect = new DelayEffect(slot, machineIndex);
                break;
            case Distortion:
                effect = new DistortionEffect(slot, machineIndex);
                break;
            case Flanger:
                effect = new FlangerEffect(slot, machineIndex);
                break;
            case Limiter:
                effect = new LimiterEffect(slot, machineIndex);
                break;
            case MultiFilter:
                effect = new MultiFilterEffect(slot, machineIndex);
                break;
            case ParametricEQ:
                effect = new ParametricEQEffect(slot, machineIndex);
                break;
            case Phaser:
                effect = new PhaserEffect(slot, machineIndex);
                break;
            case Reverb:
                effect = new ReverbEffect(slot, machineIndex);
                break;
            case StaticFlanger:
                effect = new StaticFlangerEffect(slot, machineIndex);
                break;
            case VinylSimulator:
                effect = new VinylSimulatorEffect(slot, machineIndex);
                break;
        }
        return (T)effect;
    }
}
