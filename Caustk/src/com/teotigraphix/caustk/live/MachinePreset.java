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

package com.teotigraphix.caustk.live;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.controller.IRackSerializer;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.SynthMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.tone.RackTone;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/*
 * This instance is disposable in that a CaustkPhrase can easily create a 
 * new MachinePresetFile and replace it's existing preset file with the
 * new instance.
 */

/**
 * The {@link MachinePreset} represents a {@link Patch}'s preset bytes and holds
 * the name of the preset originally saved from the native machine.
 * 
 * @author Michael Schmalle
 */
public class MachinePreset implements IRackSerializer {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private Patch patch;

    @Tag(1)
    private String name;

    @Tag(2)
    private byte[] data;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    public IRack getRack() {
        return patch.getMachine().getRack();
    }

    //----------------------------------
    // patch
    //----------------------------------

    /**
     * Returns the id of the {@link Patch} that created the preset.
     */
    public Patch getPatch() {
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

    MachinePreset(String name, Patch patch) {
        this.name = name;
        this.patch = patch;
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

    @Override
    public void create(ICaustkApplicationContext context) throws CausticException {
        // if creating this rack from the factory and it is not attached
        // to a Machine, just return as there is no Machine to create preset bytes from
        if (getPatch().getMachine() == null)
            return;

        populatePresetBytes();
    }

    @Override
    public void update(ICaustkApplicationContext context) {
        if (name == null && data == null) {
            // this preset has not been saved, so there is no .preset file
            // to load into the machine.
            patch.getMachine().getRack().getLogger()
                    .warn("MachinePreset", "No update() preset data for " + patch.getMachine());
            return;
        }

        // take the byte data, save it temporarily, load as preset
        File temp = new File(RuntimeUtils.getApplicationDirectory(), ".temp");
        temp.mkdirs();

        File presetFile = null;
        String presetName = constructPresetName(true);

        try {
            presetFile = save(temp, presetName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!presetFile.exists())
            throw new IllegalStateException("Preset data was not updated "
                    + "correctly, File not created.");

        SynthMessage.LOAD_PRESET.send(getRack(), patch.getMachine().getMachineIndex(),
                presetFile.getAbsolutePath());

        // delete the temp preset file
        FileUtils.deleteQuietly(presetFile);
    }

    @Override
    public void load(ICaustkApplicationContext context) {
        populatePresetBytes(); // XXX ????
    }

    /**
     * Restores the {@link #getData()} bytes with the {@link RackTone}'s preset
     * file as currently loaded in the rack.
     * <p>
     * The {@link #getPatch()}'s {@link Machine} and {@link RackTone} must be
     * non <code>null</code> for the method to not throw an error.
     * 
     * @throws IOException
     */
    @Override
    public void restore() {
        Machine machine = getPatch().getMachine();
        if (machine == null)
            throw new IllegalStateException(
                    "CaustkMachine cannot be null when trying to update preset file");

        RackTone rackTone = machine.getRackTone();
        if (rackTone == null)
            throw new IllegalStateException(
                    "CaustkMachine Tone cannot be null when trying to update preset file");

        restore(rackTone);
    }

    @Override
    public void disconnect() {
    }

    /**
     * Updates the {@link #getData()} bytes with the {@link RackTone}'s preset
     * file as currently loaded in the rack.
     * <p>
     * The {@link RackTone#getToneType()} must match the
     * {@link Patch#getToneType()} for the method to be successful.
     * 
     * @param rackTone The {@link RackTone} to use when updating the preset
     *            bytes.
     * @throws IOException
     */
    // XXX throw exception?
    public void restore(RackTone rackTone) {
        if (rackTone.getMachineType() != getPatch().getMachineType())
            throw new IllegalStateException("Tone's type does not match the LivePhrase's type");

        // save the temp preset file to get its bytes
        String presetName = constructPresetName(false);
        try {
            rackTone.getSynth().savePreset(presetName);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void load(File targetDirectory, RackTone rackTone) throws IOException {
        if (!targetDirectory.exists())
            throw new IOException("Directory does no exist for preset load: " + targetDirectory);
        // something like MyPreset-12sd1323-123qadsd12-12312qwed.subsynth
        String presetName = constructPresetName(true);
        File savedFile = new File(targetDirectory, presetName);
        rackTone.getSynth().loadPreset(savedFile.getAbsolutePath());
        if (!savedFile.exists())
            throw new IOException("Error saving preset file to: " + savedFile);
    }

    private String constructPresetName(boolean addExtension) {
        String result = patch.getInfo().getId().toString();
        // append the original prest name if it had one
        if (name != null && !name.equals("")) {
            name = name.replace(" ", "_");
            result = name + "-" + result;
        }
        // add the correct extension for ToneType
        if (addExtension)
            result = result + "." + toPresetExtension(this);
        return result;
    }

    private void populatePresetBytes() {
        Machine machine = getPatch().getMachine();
        if (machine == null)
            throw new IllegalStateException("CaustkMachine cannot be null calling load()");

        // save the temp preset file to get its bytes
        String presetName = constructPresetName(false);
        SynthMessage.SAVE_PRESET.send(machine.getRack(), machine.getMachineIndex(), presetName);

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

    private static File toPresetFile(MachineType machineType, String presetName) {
        return RuntimeUtils.getCausticPresetsFile(machineType, presetName);
    }

    private static String toPresetExtension(MachinePreset file) {
        return file.getPatch().getMachineType().getExtension();
    }

    public void onLoad() {
    }

    public void onSave() {
        // we are getting saved from the outside, so refresh the bytes preset data
        // so the most current state snapshot of our Patch gets saved
        populatePresetBytes();
    }

}
