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

public final class EffectUtils {

    public static IEffect create(EffectType type, int slot, int toneIndex) {
        IEffect effect = null;
        switch (type) {
            case AUTOWAH:
                effect = new AutowahEffect(slot, toneIndex);
                break;

            case BITCRUSHER:
                effect = new BitcrusherEffect(slot, toneIndex);
                break;

            case CHORUS:
                effect = new ChorusEffect(slot, toneIndex);
                break;

            case COMPRESSOR:
                effect = new CompressorEffect(slot, toneIndex);
                break;

            case DISTORTION:
                effect = new DistortionEffect(slot, toneIndex);
                break;

            case FLANGER:
                effect = new FlangerEffect(slot, toneIndex);
                break;

            case PARAMETRICEQ:
                effect = new ParametricEQEffect(slot, toneIndex);
                break;

            case PHASER:
                effect = new PhaserEffect(slot, toneIndex);
                break;
        }
        return effect;
    }

}
