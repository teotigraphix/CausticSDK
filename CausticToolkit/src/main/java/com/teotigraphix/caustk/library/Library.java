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

package com.teotigraphix.caustk.library;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.tone.Tone;

public class Library implements ISerialize {

    protected File getPresetsDirectory() {
        return new File(directory, "presets");
    }

    public String getName() {
        return getDirectory().getName();
    }

    //----------------------------------
    // metadataInfo
    //----------------------------------

    private MetadataInfo metadataInfo;

    public final MetadataInfo getMetadataInfo() {
        return metadataInfo;
    }

    public final void setMetadataInfo(MetadataInfo value) {
        metadataInfo = value;
    }

    //----------------------------------
    // id
    //----------------------------------

    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID value) {
        id = value;
    }

    //----------------------------------
    // directory
    //----------------------------------

    private File directory;

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File value) {
        directory = value;
    }

    //----------------------------------
    // patterns
    //----------------------------------

    private List<LibraryPattern> patterns = new ArrayList<LibraryPattern>();

    public List<LibraryPattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<LibraryPattern> value) {
        patterns = value;
    }

    public void addPattern(LibraryPattern pattern) {
        if (patterns.contains(pattern))
            return;
        patterns.add(pattern);
        pattern.setLibrary(this);
    }

    public void removePattern(LibraryPattern pattern) {
        patterns.remove(pattern);
    }

    //----------------------------------
    // patches
    //----------------------------------

    private List<LibraryPatch> patches = new ArrayList<LibraryPatch>();

    public List<LibraryPatch> getPatches() {
        return patches;
    }

    public void setPatches(List<LibraryPatch> value) {
        patches = value;
    }

    public void addPatch(LibraryPatch patch) {
        if (patches.contains(patch))
            return;
        patches.add(patch);
        patch.setLibrary(this);
    }

    public void removePatch(LibraryPatch patch) {
        patches.remove(patch);
    }

    //----------------------------------
    // phrases
    //----------------------------------

    private List<LibraryPhrase> phrases = new ArrayList<LibraryPhrase>();

    public List<LibraryPhrase> getPhrases() {
        return phrases;
    }

    public void setPhrases(List<LibraryPhrase> value) {
        phrases = value;
    }

    public void addPhrase(LibraryPhrase phrase) {
        if (phrases.contains(phrase))
            return;
        phrases.add(phrase);
        phrase.setLibrary(this);
    }

    public void removePhrase(LibraryPhrase phrase) {
        phrases.remove(phrase);
    }

    //----------------------------------
    // scenes
    //----------------------------------

    private List<LibraryScene> scenes = new ArrayList<LibraryScene>();

    public List<LibraryScene> getScenes() {
        return scenes;
    }

    public void setScenes(List<LibraryScene> value) {
        scenes = value;
    }

    public void addScene(LibraryScene scene) {
        if (scenes.contains(scene))
            return;
        scenes.add(scene);
        scene.setLibrary(this);
    }

    public void removeScene(LibraryScene scene) {
        scenes.remove(scene);
    }

    public Library() {
    }

    public LibraryScene findSceneById(UUID id) {
        for (LibraryScene item : scenes) {
            if (item.getId().equals(id))
                return item;
        }
        return null;
    }

    public LibraryPatch findPatchById(UUID uuid) {
        for (LibraryPatch item : patches) {
            if (item.getId().equals(uuid))
                return item;
        }
        return null;
    }

    public LibraryPhrase findPhraseById(UUID uuid) {
        for (LibraryPhrase item : phrases) {
            if (item.getId().equals(uuid))
                return item;
        }
        return null;
    }

    public List<LibraryScene> findScenesByTag(String tag) {
        List<LibraryScene> result = new ArrayList<LibraryScene>();
        for (LibraryScene item : getScenes()) {
            if (item.hasTag(tag)) {
                result.add(item);
            }
        }
        return result;
    }

    public List<LibraryPatch> findPatchesByTagStartsWith(String search) {
        List<LibraryPatch> result = new ArrayList<LibraryPatch>();
        for (LibraryPatch item : getPatches()) {
            if (item.hasTagStartsWith(search)) {
                result.add(item);
            }
        }
        return result;
    }

    public List<LibraryPatch> findPatchByTag(String tag) {
        List<LibraryPatch> result = new ArrayList<LibraryPatch>();
        for (LibraryPatch item : getPatches()) {
            if (item.hasTag(tag)) {
                result.add(item);
            }
        }
        return result;
    }

    public List<LibraryPhrase> findPhrasesByTag(String tag) {
        List<LibraryPhrase> result = new ArrayList<LibraryPhrase>();
        for (LibraryPhrase item : getPhrases()) {
            if (item.hasTag(tag)) {
                result.add(item);
            }
        }
        return result;
    }

    public List<LibraryPhrase> findPhrasesByTagStartsWith(String search) {
        List<LibraryPhrase> result = new ArrayList<LibraryPhrase>();
        for (LibraryPhrase item : getPhrases()) {
            if (item.hasTagStartsWith(search)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Returns all {@link LibraryPhrase}s in the {@link Library} for the tone's
     * {@link MachineType}.
     * 
     * @param tone The tone used to search.
     * @return A {@link List} of {@link LibraryPhrase}s that are of the same
     *         {@link MachineType} as the tone.
     */
    public List<LibraryPhrase> findPhrasesForTone(Tone tone) {
        String type = tone.getToneType().getValue();
        return findPhrasesByTag(type);
    }

    /**
     * Returns a {@link File} with the correct absolute path of the preset in
     * the <code>/MyLibrary/presets</code> directory.
     * 
     * @param preset The file name of the preset file.
     */
    public File getPresetFile(File preset) {
        return new File(getPresetsDirectory(), preset.getName());
    }

    public LibraryScene getDefaultScene() {
        LibraryScene libraryScene = findScenesByTag("DefaultScene").get(0);
        return libraryScene;
    }

    /**
     * Creates the sub directories of the library on creation.
     */
    public void mkdirs() throws IOException {
        getPresetsDirectory().mkdir();
    }

    public void delete() throws IOException {
        FileUtils.deleteDirectory(directory);
        if (directory.exists())
            throw new IOException("Library " + directory.getAbsolutePath() + " was not deleted.");
    }

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
        for (LibraryScene item : scenes) {
            item.setLibrary(this);
        }
        for (LibraryPhrase item : phrases) {
            item.setLibrary(this);
        }
        for (LibraryPatch item : patches) {
            item.setLibrary(this);
        }
        for (LibraryPattern item : patterns) {
            item.setLibrary(this);
        }
    }
}
