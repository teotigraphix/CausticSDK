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
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.core.osc.OSCUtils;
import com.teotigraphix.caustk.core.osc.SynthMessage;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * The {@link PresetComponent} API wraps machine preset read/write operations.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class PresetComponent extends MachineComponent {

    private transient byte[] restoredData;

    /**
     * Returns the original preset data when the {@link #restore()} method was
     * called.
     * <p>
     * Will be <code>null</code> if {@link #restore()} was never called.
     */
    public byte[] getRestoredData() {
        return restoredData;
    }

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private String name;

    private String path;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // name
    //----------------------------------

    /**
     * Returns the preset's name.
     * <p>
     * If this name was restored from the rack, the {@link PresetComponent} will
     * not contain a valid path. If the path is non null, the name will be the
     * file name without extension.
     */
    public String getName() {
        return name;
    }

    /**
     * Queries the native machine for the preset name, <code>null</code> if the
     * machine was not saved with a valid preset.
     * 
     * @see SynthMessage#QUERY_PRESET
     */
    public String queryPreset() {
        return SynthMessage.QUERY_PRESET.queryString(getRack(), machineIndex);
    }

    //----------------------------------
    // path
    //----------------------------------

    /**
     * Returns the relative or absolute path of the preset.
     * <p>
     * If this instance we created during a restore() or update(), this value
     * will be <code>null</code>.
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the preset's save path, and sets it's {@link #getName()}.
     * 
     * @param path The relative or absolute preset path for use when
     *            {@link #export()} is called.
     */
    public void setPath(String path) {
        this.path = path;
        if (path == null)
            return;
        File file = getFile();
        name = FilenameUtils.getBaseName(file.getAbsolutePath());
    }

    /**
     * Returns the raw path File, could be relative or absolute.
     */
    public File getFile() {
        return new File(path);
    }

    /**
     * Returns the full calculated preset file location using the
     * {@link #getPath()}.
     */
    public File getPresetFile() {
        if (path == null)
            throw new IllegalStateException("Preset path null");

        File file = getFile();
        if (file.isAbsolute())
            return file;

        // /path/to/app/caustic/presets/subsynth
        File presetsDirectory = new File(RuntimeUtils.getPresetsDirectory(), getType().getType());

        // throw this error since the path is relative and has to exist for correct
        // saving
        if (!presetsDirectory.exists())
            throw new IllegalStateException("Preset directory does not exist");

        return new File(presetsDirectory, path);
    }

    /**
     * Returns the parent machine's {@link MachineType}.
     * <p>
     * If {@link #getPath()} is not null, uses the file extension, since the
     * node could have been created from code, the current index may not
     * actually point to a real machine natively yet.
     * <p>
     * If index is not null, uses the native rack query.
     */
    public final MachineType getType() {
        if (path != null)
            return MachineType.fromExtension(FilenameUtils.getExtension(path));
        //        if (machineIndex != null)
        return OSCUtils.toMachineType(getRack(), machineIndex);
        //        return null;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public PresetComponent() {
    }

    public PresetComponent(int machineIndex) {
        this.machineIndex = machineIndex;
    }

    public PresetComponent(MachineNode machineNode) {
        this(machineNode.getIndex());
    }

    public PresetComponent(int machineIndex, String path) {
        this(machineIndex);
        setPath(path);
    }

    public PresetComponent(MachineNode machineNode, String path) {
        this(machineNode.getIndex(), path);
    }

    //--------------------------------------------------------------------------
    // Public API Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Loads a preset file into the preset's machine.
     * 
     * @param presetFile The absolute file of the preset.
     * @throws IOException Preset file does not exist
     */
    public void load(File presetFile) throws IOException {
        if (!presetFile.exists())
            throw new IOException("Preset file does not exist:" + presetFile);
        setPath(presetFile.getAbsolutePath());
        SynthMessage.LOAD_PRESET.send(getRack(), machineIndex, path);
    }

    /**
     * Fills the {@link #getRestoredData()} with the current preset data of the
     * native machine located at {@link #getMachineIndex()}.
     */
    public void fill() {
        fillRestoredData();
    }

    /**
     * Fills the {@link #getRestoredData()} with the bytes from the preset File
     * passed.
     * 
     * @param presetFile The preset file to save bytes from.
     * @throws IOException
     */
    public void fill(File presetFile) throws IOException {
        name = FilenameUtils.getBaseName(presetFile.getName());
        restoredData = FileUtils.readFileToByteArray(presetFile);
    }

    /**
     * Exports the preset using the {@link #getPresetFile()} calculation, which
     * involves {@link #getPath()}.
     * 
     * @throws IOException
     * @throws IllegalStateException index of parent machine not set
     */
    public File export() throws IOException {
        if (machineIndex == -1)
            throw new IllegalStateException("index of parent machine not set");
        File presetFile = getPresetFile();
        return exportPreset(presetFile.getParentFile(), getName());
    }

    /**
     * Export the preset byte array to a preset file using the current index's
     * {@link MachineType}.
     * <p>
     * This method does not alter the state of this preset node.
     * 
     * @param destDirectory The destination directory.
     * @param fileName The destination file name without extension.
     * @return The absolute location of the new preset file.
     * @throws IOException Preset file not created, source file exists
     * @throws IllegalStateException Cannot export preset
     */
    public File exportPreset(File destDirectory, String fileName) throws IOException {
        MachineType machineType = OSCUtils.toMachineType(getRack(), machineIndex);
        if (machineType == null)
            throw new IllegalStateException("Cannot export preset");

        File srcFile = RuntimeUtils.getPresetsFile(machineType, fileName);
        if (srcFile.exists())
            throw new IOException("Preset source file already exists with name: '" + fileName + "'");

        File destFile = new File(destDirectory, fileName + "." + machineType.getExtension());
        SynthMessage.SAVE_PRESET.send(getRack(), machineIndex, fileName);

        // now copy if the locations differ and delete source in /presets dir
        if (!srcFile.equals(destFile)) {
            FileUtils.copyFile(srcFile, destFile, true);
            FileUtils.deleteQuietly(srcFile);
        }

        return destFile;
    }

    /**
     * Restores the {@link #getRestoredData()} if not <code>null</code> into the
     * native machine.
     * 
     * @throws CausticException Preset data was not updated
     */
    public void restorePreset() throws CausticException {
        if (restoredData == null)
            return;

        // take the byte data, save it temporarily, load as preset
        final File tempDirectory = RuntimeUtils.getApplicationTempDirectory();

        // IF THERE was a preset name, use ONLY that so it shows up
        // correctly when loaded into caustic
        String presetName = name;
        if (presetName == null)
            presetName = UUID.randomUUID().toString();

        File presetFile = new File(tempDirectory, presetName + "." + getType().getExtension());
        try {
            // save the byte[]s to the temp preset file
            FileUtils.writeByteArrayToFile(presetFile, restoredData);
        } catch (IOException e) {
            getLogger().err("PresetDefinition", "Could not save preset data");
            throw new CausticException("Preset data was not updated "
                    + "correctly, File not created.");
        }

        SynthMessage.LOAD_PRESET.send(getRack(), machineIndex, presetFile.getAbsolutePath());

        // delete the temp preset file
        FileUtils.deleteQuietly(presetFile);
        if (presetFile.exists()) {
            getLogger().log("PresetDefinition", "Temp preset file was not deleted");
        }
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
        try {
            restorePreset();
        } catch (CausticException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void restoreComponents() {
        MachineType type = OSCUtils.toMachineType(getRack(), machineIndex);
        if (type == MachineType.Vocoder)
            return; // Vocoder does not save presets

        // read the preset name from the native rack
        // get the bytes stored in memory of this restore point
        // this way if during this session, the json file and presets need to
        // be saved to the .rack archive, we have the original bytes to save
        // otherwise at the time, the client might just ignore these bytes and
        // use the refreshed bytes of the current machine's preset 
        name = SynthMessage.QUERY_PRESET.queryString(getRack(), machineIndex);
        path = null;

        // store the original bytes
        fillRestoredData();
    }

    @Override
    protected void updateComponents() {
        // if we have a path and its absolute, load it into the machine
        try {
            File presetFile = getPresetFile();
            if (presetFile == null || !presetFile.exists()) // XXX Log Error?
                return;
            SynthMessage.LOAD_PRESET.send(getRack(), presetFile.getAbsolutePath());
        } catch (IllegalStateException e) {
            // TODO: handle exception
        }
    }

    private void fillRestoredData() {
        MachineType type = OSCUtils.toMachineType(getRack(), machineIndex);
        if (type == null)
            throw new IllegalStateException(
                    "Can only restore bytes during existing native rack session");
        // store the original bytes
        String tempName = "__" + UUID.randomUUID().toString().substring(0, 20); // will be deleted
        restoredData = PresetUtils.readPresetBytes(getRack(), machineIndex, type, tempName);
    }

    @Override
    public String toString() {
        return getType() + ":" + getName();
    }
}
