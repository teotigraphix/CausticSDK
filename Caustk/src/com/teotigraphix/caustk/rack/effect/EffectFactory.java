////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.rack.effect;

import com.teotigraphix.caustk.rack.IEffect;

/**
 * @author Michael Schmalle
 */
public final class EffectFactory {

    /**
     * Creates an empty {@link IEffect} instance.
     * 
     * @param type The type of effect to create.
     */
    public static IEffect create(EffectType type) {
        return create(type, -1, -1);
    }

    /**
     * @param type
     * @param slot
     * @param toneIndex
     */
    public static IEffect create(EffectType type, int slot, int toneIndex) {
        IEffect effect = null;
        switch (type) {
            case Autowah:
                effect = new AutowahEffect(slot, toneIndex);
                break;

            case Bitcrusher:
                effect = new BitcrusherEffect(slot, toneIndex);
                break;

            case Chorus:
                effect = new ChorusEffect(slot, toneIndex);
                break;

            case Compressor:
                effect = new CompressorEffect(slot, toneIndex);
                break;

            case Distortion:
                effect = new DistortionEffect(slot, toneIndex);
                break;

            case Flanger:
                effect = new FlangerEffect(slot, toneIndex);
                break;

            case ParametricEQ:
                effect = new ParametricEQEffect(slot, toneIndex);
                break;

            case Phaser:
                effect = new PhaserEffect(slot, toneIndex);
                break;
        }
        return effect;
    }
}
