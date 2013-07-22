
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
        // TODO Auto-generated method stub

    }

    @Override
    public void wakeup(ICaustkController controller) {

    }
}
