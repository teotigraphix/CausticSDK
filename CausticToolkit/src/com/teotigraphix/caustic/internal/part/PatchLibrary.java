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

package com.teotigraphix.caustic.internal.part;

import java.io.File;
import java.net.URI;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.part.IPart;
import com.teotigraphix.caustic.part.IPatch;
import com.teotigraphix.caustic.part.IPatchLibrary;
import com.teotigraphix.caustic.part.IPresetPatch;
import com.teotigraphix.caustic.song.IPreset;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.common.IMemento;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class PatchLibrary extends PresetLibrary implements IPatchLibrary {

    public PatchLibrary(IWorkspace workspace) {
        super(workspace);
        tagChildren = "patches";
        tagChild = "patch";
    }

    @Override
    protected IPreset createPreset(IMemento memento) {
        IPatch patch = new Patch();
        if (memento != null) {
            // need to save this becasue the ITone/IPart is not linked
            // up to the patch yet
            patch.setMemento(memento);
        }
        return patch;
    }

    @Override
    public IPatch createDefaultPatch(MachineType type) {
        // the default patch just gets created and returned.
        // there is nothing added to the library for the default
        DefaultPatch patch = new DefaultPatch();
        return patch;
    }

    @Override
    public void loadPreset(IPart part, File file) throws CausticException {
        PresetPatch patch = new PresetPatch(file);
        patch.setId(file.getAbsolutePath());
        patch.setIndex(NO_INDEX);
        patch.setPresetBank(PRESETS_BANK);
        patch.setName(file.getName());
        patch.setRack(getWorkspace().getRack());
        ((Part)part).setPatch(patch);
        patch.load();
    }

    @Override
    public void savePreset(IPart part) throws CausticException {
        IPatch patch = part.getPatch();
        if (patch instanceof IPresetPatch) {
            ((IPresetPatch)patch).save();
        } else {
            throw new CausticException("Part does not contain a IPresetPatch");
        }
    }

    @Override
    public void savePreset(IPart part, File file) throws CausticException {
        IPatch patch = part.getPatch();
        if (patch instanceof IPresetPatch) {
            ((PresetPatch)patch).setFile(file);
            savePreset(part);
        } else {
            throw new CausticException("Part does not contain a IPresetPatch");
        }
    }

    /**
     * The default Patch for new machines.
     */
    class DefaultPatch extends Patch {
        public DefaultPatch() {
            super();
            setIndex(NO_INDEX);
            setId(DEFAULT_BANK);
            setPresetBank(DEFAULT_BANK);
        }
    }

    /**
     * The patch for loading/saving preset files into a machine.
     * <p>
     * Down the road try experimenting with loading {@link URI} http requests
     * from the server or something.
     * </p>
     */
    class PresetPatch extends Patch implements IPresetPatch {

        private File mFile;

        @Override
        public File getFile() {
            return mFile;
        }

        void setFile(File value) {
            mFile = value;
        }

        /**
         * Creates a proxied Caustic preset file.
         * 
         * @param file The File pointing to the absolute location of the preset
         *            on the device.
         */
        public PresetPatch(File file) {
            setFile(file);
        }

        @Override
        public void save() throws CausticException {
            if (mFile == null)
                throw new CausticException("File is null or doesn't exist");

            IMachine machine = getMachine();
            String name = mFile.getName().replace("." + machine.getType().toString().toLowerCase(),
                    "");
            // TODO eventually you could use the save preset here, then MOVE
            // the new preset file to the actual location of mFile
            machine.savePreset(name);
        }

        @Override
        public void load() throws CausticException {
            if (mFile == null || !mFile.exists())
                throw new CausticException("File is null or doesn't exist");

            IMachine machine = getMachine();
            machine.loadPreset(mFile.getAbsolutePath());
            machine.restore();
        }

        @Override
        public void copy(IMemento memento) {
        }

        @Override
        public void paste(IMemento memento) {
        }
    }

}
