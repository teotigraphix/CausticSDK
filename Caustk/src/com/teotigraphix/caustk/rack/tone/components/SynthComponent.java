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

package com.teotigraphix.caustk.rack.tone.components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.SynthMessage;
import com.teotigraphix.caustk.live.MachineType;
import com.teotigraphix.caustk.rack.tone.BasslineTone;
import com.teotigraphix.caustk.rack.tone.BeatboxTone;
import com.teotigraphix.caustk.rack.tone.ModularTone;
import com.teotigraphix.caustk.rack.tone.RackToneComponent;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public class SynthComponent extends RackToneComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private int polyphony = 4;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    private transient String absolutePresetPath;

    public String getPresetPath() {
        return absolutePresetPath;
    }

    //--------------------------------------------------------------------------
    // API :: Properties
    //--------------------------------------------------------------------------

    public String getPresetName() {
        return SynthMessage.QUERY_PRESET.queryString(getEngine(), getTone().getMachineIndex());
    }

    //----------------------------------
    // polyphony
    //----------------------------------

    public int getPolyphony() {
        return polyphony;
    }

    int getPolyphony(boolean restore) {
        return (int)SynthMessage.POLYPHONY.query(getEngine(), getToneIndex());
    }

    public void setPolyphony(int value) {
        if (getTone() instanceof BasslineTone || getTone() instanceof ModularTone) {
            polyphony = 1;
            return;
        }
        if (getTone() instanceof BeatboxTone) {
            polyphony = 8;
            return;
        }
        if (value == polyphony)
            return;
        // 0 is lagato in SubSynth
        if (value < 0 || value > 16)
            throw newRangeException(SynthMessage.POLYPHONY.toString(), "0..16", value);
        polyphony = value;
        SynthMessage.POLYPHONY.send(getEngine(), getToneIndex(), polyphony);
    }

    public SynthComponent() {
    }

    //--------------------------------------------------------------------------
    //
    // API :: Methods
    //
    //--------------------------------------------------------------------------

    public void loadPreset(File file) throws IOException {
        if (!file.exists())
            throw new FileNotFoundException("Preset file does not exist:" + file);

        absolutePresetPath = file.getAbsolutePath();
        SynthMessage.LOAD_PRESET.send(getEngine(), getToneIndex(), absolutePresetPath);
    }

    public void loadPreset(String path) throws IOException {
        loadPreset(new File(path));
    }

    /**
     * Saves a machine preset to the <code>caustic/presets/[machineType]</code>
     * directory in the specific machine type sub directory.
     * 
     * @param name The simple name of the preset.
     * @return A File handle of the new preset File location in the
     *         <code>caustic/presets/[machineType]</code> directory.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public File savePreset(String name) throws IOException {
        SynthMessage.SAVE_PRESET.send(getEngine(), getToneIndex(), name);
        MachineType machineType = MachineType.fromString(getTone().getMachineType().getType());
        File file = RuntimeUtils.getCausticPresetsFile(machineType, name);
        if (!file.exists())
            throw new FileNotFoundException("Preset file was not saved:" + file);
        return file;
    }

    /**
     * Saves a machine preset to the destination directory.
     * 
     * @param name The simple name of the preset.
     * @param destinationDirectory The directory to copy preset file to.
     * @return A File handle of the new copied preset File location.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public File savePresetAs(String name, File destinationDirectory) throws IOException {
        File localFile = savePreset(name);
        FileUtils.copyFileToDirectory(localFile, destinationDirectory);
        File newFile = new File(destinationDirectory, localFile.getName());
        if (!newFile.exists())
            throw new FileNotFoundException("Preset file was not copied:" + newFile);
        return newFile;
    }

    public void noteOn(int pitch) {
        noteOn(pitch, 1f);
    }

    public void noteOn(int pitch, float velocity) {
        SynthMessage.NOTE.send(getEngine(), getToneIndex(), pitch, 1, velocity);
    }

    public void noteOff(int pitch) {
        SynthMessage.NOTE.send(getEngine(), getToneIndex(), pitch, 0);
    }

    public void notePreview(int pitch, boolean oneshot) {
        SynthMessage.NOTE_PREVIEW.send(getEngine(), getToneIndex(), pitch, oneshot);
    }

    @Override
    public void restore() {
        setPolyphony(getPolyphony(true));
    }
}
