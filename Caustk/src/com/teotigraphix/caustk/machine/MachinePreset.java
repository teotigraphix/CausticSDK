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

package com.teotigraphix.caustk.machine;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.rack.tone.Tone;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/*
 * This instance is disposable in that a CaustkPhrase can easily create a 
 * new MachinePresetFile and replace it's existing preset file with the
 * new instance.
 */

/**
 * The {@link MachinePreset} represents a {@link CaustkPatch}'s preset bytes and
 * holds the name of the preset originally saved from the native machine.
 * 
 * @author Michael Schmalle
 */
public class MachinePreset implements IRestore {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private CaustkPatch patch;

    @Tag(1)
    private String name;

    @Tag(2)
    private byte[] data;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // patch
    //----------------------------------

    /**
     * Returns the id of the {@link CaustkPatch} that created the preset.
     */
    public CaustkPatch getPatch() {
        return patch;
    }

    //----------------------------------
    // name
    //----------------------------------

    /**
     * Returns the name of the preset when serialized from the native machine.
     * <p>
     * May be <code>null</code> if no preset was assigned when the original
     * <code>.caustic</code> song file was created.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the calculated preset file name using the {@link #getName()} and
     * {@link #getPatch()}'s {@link UUID}.
     * 
     * @param addExtension Whether to add the preset extension to the filename.
     */
    public String getFileName(boolean addExtension) {
        return constructPresetName(addExtension);
    }

    //----------------------------------
    // data
    //----------------------------------

    /**
     * Returns the byte array of data for the serialized preset file.
     */
    public byte[] getData() {
        return data;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    MachinePreset() {
    }

    MachinePreset(String name, CaustkPatch caustkPatch) {
        this.name = name;
        this.patch = caustkPatch;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Saves the {@link #getData()} bytes to the target directory using a
     * constructed preset name as the file name in the target directory.
     * 
     * @param targetDirectory The directory to save the preset file.
     * @return The saved preset file.
     * @throws IOException
     */
    public File save(File targetDirectory) throws IOException {
        final String presetName = constructPresetName(true);
        return save(targetDirectory, presetName);
    }

    /**
     * Saves the {@link #getData()} bytes to the target directory using the
     * preset name as the file name in the target directory.
     * 
     * @param targetDirectory The directory to save the preset file.
     * @return The saved preset file.
     * @throws IOException
     */
    public File save(File targetDirectory, String presetName) throws IOException {
        if (!targetDirectory.exists())
            throw new IOException("Directory does no exist for preset save: " + targetDirectory);
        File savedFile = new File(targetDirectory, presetName);
        FileUtils.writeByteArrayToFile(savedFile, data);
        if (!savedFile.exists())
            throw new IOException("Error saving preset file to: " + savedFile);
        return savedFile;
    }

    /**
     * Restores the {@link #getData()} bytes with the {@link Tone}'s preset file
     * as currently loaded in the rack.
     * <p>
     * The {@link #getPatch()}'s {@link CaustkMachine} and {@link Tone} must be
     * non <code>null</code> for the method to not throw an error.
     * 
     * @throws IOException
     */
    @Override
    public void restore() {
        CaustkMachine machine = getPatch().getMachine();
        if (machine == null)
            throw new IllegalStateException(
                    "CaustkMachine cannot be null when trying to update preset file");

        Tone tone = machine.getTone();
        if (tone == null)
            throw new IllegalStateException(
                    "CaustkMachine Tone cannot be null when trying to update preset file");

        update(tone);
    }

    /**
     * Updates the {@link #getData()} bytes with the {@link Tone}'s preset file
     * as currently loaded in the rack.
     * <p>
     * The {@link Tone#getToneType()} must match the
     * {@link CaustkPatch#getToneType()} for the method to be successful.
     * 
     * @param tone The {@link Tone} to use when updating the preset bytes.
     * @throws IOException
     */
    public void update(Tone tone) {
        if (!tone.getToneType().getValue().equals(getPatch().getMachineType().getType()))
            throw new IllegalStateException("Tone's type does not match the LivePhrase's type");

        // save the temp preset file to get its bytes
        String presetName = constructPresetName(false);
        tone.getSynth().savePreset(presetName);

        // get the preset file from the caustic presets directory
        File presetFile = toPresetFile(getPatch().getMachineType(), presetName);
        if (!presetFile.exists())
            throw new RuntimeException("Error saving preset file to: " + presetFile);

        // read into the data byte array
        try {
            data = FileUtils.readFileToByteArray(presetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // delete the temp preset file
        FileUtils.deleteQuietly(presetFile);
    }

    public void load(File targetDirectory, Tone tone) throws IOException {
        if (!targetDirectory.exists())
            throw new IOException("Directory does no exist for preset load: " + targetDirectory);
        // something like MyPreset-12sd1323-123qadsd12-12312qwed.subsynth
        String presetName = constructPresetName(true);
        File savedFile = new File(targetDirectory, presetName);
        tone.getSynth().loadPreset(savedFile.getAbsolutePath());
        if (!savedFile.exists())
            throw new IOException("Error saving preset file to: " + savedFile);
    }

    private String constructPresetName(boolean addExtension) {
        String result = patch.getId().toString();
        // append the original prest name if it had one
        if (name != null && !name.equals(""))
            result = name + "-" + result;
        // add the correct extension for ToneType
        if (addExtension)
            result = result + "." + toPresetExtension(this);
        return result;
    }

    private static File toPresetFile(MachineType machineType, String presetName) {
        return RuntimeUtils.getCausticPresetsFile(machineType, presetName);
    }

    private static String toPresetExtension(MachinePreset file) {
        return file.getPatch().getMachineType().getExtension();
    }
}
