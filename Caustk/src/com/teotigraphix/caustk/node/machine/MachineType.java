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

package com.teotigraphix.caustk.node.machine;

import java.io.File;

import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * The type enum for the native Caustic machines.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public enum MachineType {

    /**
     * The subsynth machine.
     */
    SubSynth("subsynth", "subsynth"),

    /**
     * The pcmsynth machine.
     */
    PCMSynth("pcmsynth", "pcmsynth"),

    /**
     * The beatbox machine.
     */
    BeatBox("beatbox", "beatbox"),

    /**
     * The bassline machine.
     */
    Bassline("bassline", "bassline"),

    /**
     * The padsynth machine.
     */
    PadSynth("padsynth", "padsynth"),

    /**
     * The organ machine.
     */
    Organ("organ", "organ"),

    /**
     * The vocoder machine.
     */
    Vocoder("vocoder", "vocoder"),

    /**
     * The 8bitsynth machine.
     */
    EightBitSynth("8bitsynth", "8bitsynth"),

    /**
     * The modular machine.
     */
    Modular("modular", "modularsynth"),

    /**
     * The fmsynth machine.
     */
    FMSynth("fmsynth", "fmsynth"),

    /**
     * The kssynth machine.
     */
    KSSynth("kssynth", "kssynth");

    //----------------------------------
    // type
    //----------------------------------

    private final String type;

    /**
     * Returns the String value of the ToneType.
     * <p>
     * This value is used with the core rack to create native machine instances.
     */
    public String getType() {
        return type;
    }

    //----------------------------------
    // extension
    //----------------------------------

    private final String extension;

    /**
     * Returns the machine's preset extension name.
     */
    public String getExtension() {
        return extension;
    }

    MachineType(String type, String extension) {
        this.type = type;
        this.extension = extension;
    }

    /**
     * Returns a preset in the native caustic app presets directory.
     * 
     * @param nameOrRelativePath The preset name or relative path.
     * @see com.teotigraphix.caustk.utils.RuntimeUtils#getPresetsFile(MachineType,
     *      String)
     */
    public File getNativePresetFile(String nameOrRelativePath) {
        return RuntimeUtils.getPresetsFile(this, nameOrRelativePath);
    }

    /**
     * Returns a enum matched with the {@link #getValue()}.
     * 
     * @param type The String type to match.
     */
    public static MachineType fromString(String type) {
        for (MachineType machineType : values()) {
            if (machineType.getType().equals(type))
                return machineType;
        }
        return null;
    }

    public static MachineType fromExtension(String extension) {
        for (MachineType machineType : values()) {
            if (machineType.getExtension().equals(extension))
                return machineType;
        }
        return null;
    }
}
