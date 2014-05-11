////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.node.machine.patch;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.CaustkRack;
import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.core.osc.SynthMessage;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * {@link PresetComponent} utilities.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public final class PresetUtils {

    public static byte[] readPresetBytes(CaustkRack caustkRack, int machineIndex, MachineType machineType,
            String tempPresetName) {
        SynthMessage.SAVE_PRESET.send(caustkRack, machineIndex, tempPresetName);

        // get the preset file from the caustic presets directory
        File presetFile = PresetUtils.toPresetFile(machineType, tempPresetName);
        if (!presetFile.exists())
            throw new RuntimeException("Error saving preset file to: " + presetFile);

        // read into the data byte array
        byte[] data = null;
        try {
            data = FileUtils.readFileToByteArray(presetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // delete the temp preset file
        FileUtils.deleteQuietly(presetFile);
        return data;
    }

    public static File toPresetFile(MachineType machineType, String presetName) {
        return RuntimeUtils.getPresetsFile(machineType, presetName);
    }
}
