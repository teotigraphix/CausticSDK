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

package com.teotigraphix.caustk.tone;

public enum ToneType {

    /**
     * The subsynth machine.
     */
    SubSynth("subsynth"),

    /**
     * The pcmsynth machine.
     */
    PCMSynth("pcmsynth"),

    /**
     * The beatbox machine.
     */
    Beatbox("beatbox"),

    /**
     * The bassline machine.
     */
    Bassline("bassline"),

    /**
     * The padsynth machine.
     */
    PadSynth("padsynth"),

    /**
     * The organ machine.
     */
    Organ("organ"),

    /**
     * The vocoder machine.
     */
    Vocoder("vocoder"),

    /**
     * The 8bitsynth machine.
     */
    EightBitSynth("8bitsynth"),

    /**
     * The modular machine.
     */
    Modular("modular"),

    /**
     * The fmsynth machine.
     */
    FMSynth("fmsynth");

    private final String value;

    ToneType(String type) {
        value = type;
    }

    /**
     * Returns the String value of the ToneType.
     * <p>
     * This value is used with the core rack to create native machine instances.
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns a enum matched with the {@link #getValue()}.
     * 
     * @param type The String type to match.
     */
    public static ToneType fromString(String type) {
        for (ToneType toneType : values()) {
            if (toneType.getValue().equals(type))
                return toneType;
        }
        return null;
    }

}
