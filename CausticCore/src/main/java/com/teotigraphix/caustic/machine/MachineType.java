////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.machine;

/**
 * IRack machine types.
 */
public enum MachineType {

    /**
     * The subsynth machine.
     */
    SUBSYNTH("subsynth"),

    /**
     * The pcmsynth machine.
     */
    PCMSYNTH("pcmsynth"),

    /**
     * The beatbox machine.
     */
    BEATBOX("beatbox"),

    /**
     * The bassline machine.
     */
    BASSLINE("bassline");

    private final String mValue;

    MachineType(String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }

    public static MachineType fromString(String value) {
        if (value.equals(SUBSYNTH.getValue()))
            return SUBSYNTH;
        else if (value.equals(PCMSYNTH.getValue()))
            return PCMSYNTH;
        else if (value.equals(BEATBOX.getValue()))
            return BEATBOX;
        else if (value.equals(BASSLINE.getValue()))
            return BASSLINE;
        return null;
    }
}
