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
 * @author Michael Schmalle
 * @since 1.0
 */
public enum EffectType {

    Delay(0),

    Reverb(1),

    Distortion(2),

    Compressor(3),

    Bitcrusher(4),

    Flanger(5),

    Phaser(6),

    Chorus(7),

    Autowah(8),

    ParametricEQ(9),

    Limiter(10),

    VinylSimulator(11),

    CombFilter(12),

    // MasterDelay(13), // Not Insert

    CabinetSimulator(14),

    // TranceGate(15), // Not Insert

    StaticFlanger(16),

    MultiFilter(17);

    private int value;

    EffectType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static EffectType fromInt(Integer type) {
        for (EffectType result : values()) {
            if (result.getValue() == type)
                return result;
        }
        return null;
    }
}
