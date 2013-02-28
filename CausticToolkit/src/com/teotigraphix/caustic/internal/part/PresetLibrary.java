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
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.util.Log;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.internal.part.LibraryManifest.LibraryManifestEntry;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.part.ILibraryManifest;
import com.teotigraphix.caustic.part.IPresetLibrary;
import com.teotigraphix.caustic.song.IPreset;
import com.teotigraphix.caustic.song.IProject;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.IPersist;
import com.teotigraphix.common.internal.XMLMemento;
import com.teotigraphix.common.utils.RuntimeUtils;

/**
 * The base implementation for an XML manifest library used for loading tone,
 * patch, pattern and song library items.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public abstract class PresetLibrary implements IPresetLibrary {

    protected static final String ATT_ID = "id";

    protected static final int NO_INDEX = -1;

    static final String DEFAULT_BANK = "DEFAULT";

    static final String PRESETS_BANK = "PRESETS";

    private final Map<String, List<IPreset>> mMap = new HashMap<String, List<IPreset>>();

    // <bankName, Map<presetId, PresetInfo>>
    private Map<String, Map<String, PresetInfo>> mPreloadMap = new HashMap<String, Map<String, PresetInfo>>();

    // <bankName, Map<presetId, IPreset>>
    private Map<String, Map<String, IPreset>> mMainMap = new HashMap<String, Map<String, IPreset>>();

    private Map<String, ILibraryManifest> mManifestMap = new TreeMap<String, ILibraryManifest>();

    String tagChildren;

    String tagChild;

    private IWorkspace mWorkspace;

    IWorkspace getWorkspace() {
        return mWorkspace;
    }

    IProject getProject() {
        return mWorkspace.getProject();
    }

    //--------------------------------------------------------------------------
    // 
    //  Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  map
    //----------------------------------

    final Map<String, List<IPreset>> getPresetMap() {
        return mMap;
    }

    final List<IPreset> getBank(String bankName) {
        return mMap.get(bankName);
    }

    @Override
    public Iterator<IPreset> iterator(String bankName) {
        if (mMap.size() == 0)
            return null;
        return mMap.get(bankName).iterator();
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructor
    // 
    //--------------------------------------------------------------------------

    public PresetLibrary(IWorkspace workspace) {
        mWorkspace = workspace;
    }

    //--------------------------------------------------------------------------
    // 
    //  IPresetLibrary API :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    public void addManifest(ILibraryManifest manifest) throws CausticException {
        if (hasManifest(manifest.getName()))
            throw new CausticException(manifest.getName() + " has already been added");

        try {
            mManifestMap.put(manifest.getName(), manifest);
            preLoadItems(manifest);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasManifest(String name) {
        return mMap.containsKey(name);
    }

    @Override
    public Collection<LibraryManifestEntry> getEntries(String bankName) {
        ILibraryManifest manifest = mManifestMap.get(bankName);
        if (manifest == null)
            return new ArrayList<LibraryManifestEntry>();
        return manifest.getEntries();
    }

    @Override
    public IPreset findItemById(String bankName, String presetId) {
        // no preset bank manifests have been added
        if (mMap.isEmpty())
            return null;

        // no preset bank by that name
        List<IPreset> presets = mMap.get(bankName);
        if (presets == null)
            return null;

        // search for the preset in the preset bank
        for (IPreset preset : presets) {
            if (preset.getId().equals(presetId)) {
                return preset;
            }
        }

        return null;
    }

    @Override
    public IPreset getItemById(String bankName, String presetId) {
        // this method will create a preset and put it in the main map
        // once a preset is created, the findItemById() will return
        // the same instance
        if (mPreloadMap.isEmpty())
            return null;

        // no preset bank by that name
        Map<String, PresetInfo> presets = mPreloadMap.get(bankName);
        if (presets == null)
            return null;

        // no preset by that id in the existing bank
        PresetInfo info = presets.get(presetId);
        if (info == null)
            return null;

        // create a real preset and add it to the map
        // we have to load the file to get the memnto
        File file = info.getFile();
        if (!file.exists()) {
            Log.e("PresetLibrary", "Could not load the [" + bankName + ":" + presetId + "]");
            return null;
        }
        IMemento memento = null;
        try {
            memento = RuntimeUtils.loadMemento(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // TODO this dosn't feel right
        }

        IPreset preset = createPreset(memento);

        // check to see if the bank map exists in the main map
        Map<String, IPreset> subMap = mMainMap.get(bankName);
        if (subMap == null) {
            subMap = new TreeMap<String, IPreset>();
            mMainMap.put(bankName, subMap);
        }

        subMap.put(presetId, preset);

        return preset;
    }

    @Override
    public IPreset findItemAt(String bankName, int index) {
        // no preset bank manifests have been added
        if (mMap.isEmpty())
            return null;

        // no preset bank by that name
        List<IPreset> presets = mMap.get(bankName);
        if (presets == null)
            return null;

        for (IPreset preset : presets) {
            if (preset.getIndex() == index)
                return preset;
        }

        return null;
    }

    @Override
    public LibraryManifest copyBank(LibraryManifest manifest, String name) throws CausticException {
        // TODO Auto-generated method stub
        return null;
    }

    //--------------------------------------------------------------------------
    // 
    //  Protected :: Methods
    // 
    //--------------------------------------------------------------------------

    public static class PresetInfo {

        private String mName;

        private String mPresetBank;

        private String mId;

        private MachineType mType;

        private File mFile;

        public final MachineType getType() {
            return mType;
        }

        public File getFile() {
            return mFile;
        }

        public final String getName() {
            return mName;
        }

        public final String getPresetBank() {
            return mPresetBank;
        }

        public final String getId() {
            return mId;
        }

        PresetInfo(File file, IMemento memento) {
            mFile = file;
            mId = memento.getId();
            mPresetBank = memento.getString("presetBank");
            mType = MachineType.fromString(memento.getString("type"));
            mName = memento.getString("name");
        }
    }

    /**
     * This method will preload the library items using it's manifest XML file.
     * The library uses lazy loading to create the real preset when a get method
     * is called. The find methods only return a handle and not the full preset.
     * 
     * @param manifest The libraries manifest.
     * @throws IOException
     */
    protected void preLoadItems(ILibraryManifest manifest) throws IOException {
        String bankName = manifest.getName();

        Map<String, PresetInfo> map = new TreeMap<String, PresetInfo>();
        mPreloadMap.put(bankName, map);

        File manifestFile = manifest.getFile();

        IMemento memento = RuntimeUtils.loadMemento(manifestFile);

        IMemento[] patches = memento.getChildren(tagChild);
        for (IMemento patch : patches) {
            String id = patch.getString("id");
            File file = new File(manifestFile.getParentFile(), id + ".xml");
            PresetInfo info = new PresetInfo(file, patch);
            map.put(info.getId(), info);
        }
    }

    protected void loadItems(ILibraryManifest manifest) {
        String bankName = manifest.getName();

        IMemento memento = createRootMemento(manifest);
        IMemento childMemento = memento.getChild(tagChildren);

        IMemento[] children = childMemento.getChildren(tagChild);
        for (IMemento child : children) {
            initializePreset(child, bankName);
        }
    }

    protected void initializePreset(IMemento child, String bankName) {
        IPreset preset = createPreset(child);
        presetAdd(preset, child.getString(ATT_ID), bankName);
        presetLoad(preset, child);
    }

    protected void presetAdd(IPreset preset, String presetId, String bankName) {
        List<IPreset> presets = mMap.get(bankName);
        if (presets == null) {
            presets = new ArrayList<IPreset>();
            mMap.put(bankName, presets);
        }

        // set the id and bank
        preset.setId(presetId);
        preset.setPresetBank(bankName);

        presets.add(preset);

        firePresetAdded(preset);
    }

    protected void presetLoad(IPreset preset, IMemento child) {
        // this is a pre load, it doesn't load the machine state
        // the machine is not created until the addSoundSource() is called
        // on the ISoundSourceLibrary
        if (preset instanceof IPersist)
            ((IPersist)preset).paste(child);
    }

    private IMemento createRootMemento(ILibraryManifest manifest) {
        String xml = manifest.getData();
        if (xml == null)
            return null;
        Reader reader = new StringReader(xml);
        IMemento memento = XMLMemento.createReadRoot(reader);
        return memento;
    }

    @Override
    public void loadBankState(IMemento memento) {
        // TODO Auto-generated method stub

    }

    @Override
    public IMemento saveBankState(String bankName) {
        // TODO Auto-generated method stub
        return null;
    }

    //--------------------------------------------------------------------------
    // 
    //  Abstract Protected :: Methods
    // 
    //--------------------------------------------------------------------------

    /**
     * Creates a preset based on the subclasses implementation.
     * 
     * @param memento The memento to initialize the preset with.
     * @return A concrete preset implementation.
     */
    abstract protected IPreset createPreset(IMemento memento);

    private final List<PresetLibraryListener> mListeners = new ArrayList<PresetLibraryListener>();

    @Override
    public void addPresetLibraryListener(PresetLibraryListener listener) {
        if (mListeners.contains(listener))
            return;
        mListeners.add(listener);
    }

    @Override
    public void removePresetLibraryListener(PresetLibraryListener listener) {
        if (!mListeners.contains(listener))
            return;
        mListeners.remove(listener);
    }

    protected void firePresetAdded(IPreset preset) {
        for (PresetLibraryListener listener : mListeners) {
            listener.onPresetAdded(preset);
        }
    }
}
