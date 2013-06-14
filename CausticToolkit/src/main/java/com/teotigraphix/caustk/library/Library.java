
package com.teotigraphix.caustk.library;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * The {@link Library} is the main serialized json file that gets saved along
 * with the caustic song file.
 * <p>
 * {@link Library} is a model with add/remove and access to its sub items.
 */
public class Library {
    private transient XStream xStream;

    protected File getPresetsDirectory() {
        return new File(directory, "presets");
    }

    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    //----------------------------------
    // patches
    //----------------------------------

    private Map<UUID, LibraryPatch> patches = new HashMap<UUID, LibraryPatch>();

    public Collection<LibraryPatch> getPatches() {
        return patches.values();
    }

    public void addPatch(LibraryPatch patch) {
        if (patches.containsKey(patch.getId()))
            return;
        patches.put(patch.getId(), patch);
    }

    public void removePatch(LibraryPatch patch) {
        if (!patches.containsKey(patch.getId()))
            return;
        patches.remove(patch.getId());
    }

    //----------------------------------
    // phrases
    //----------------------------------

    private Map<UUID, LibraryPhrase> phrases = new HashMap<UUID, LibraryPhrase>();

    public Collection<LibraryPhrase> getPhrases() {
        return phrases.values();
    }

    public void addPhrase(LibraryPhrase patch) {
        if (phrases.containsKey(patch.getId()))
            return;
        phrases.put(patch.getId(), patch);
    }

    public void removePhrase(LibraryPhrase patch) {
        if (!phrases.containsKey(patch.getId()))
            return;
        phrases.remove(patch.getId());
    }

    //----------------------------------
    // scenes
    //----------------------------------

    private Map<UUID, LibraryScene> scenes = new HashMap<UUID, LibraryScene>();

    public Collection<LibraryScene> getScenes() {
        return scenes.values();
    }

    public void addScene(LibraryScene patch) {
        if (scenes.containsKey(patch.getId()))
            return;
        scenes.put(patch.getId(), patch);
    }

    public void removeScene(LibraryScene patch) {
        if (!scenes.containsKey(patch.getId()))
            return;
        scenes.remove(patch.getId());
    }

    //----------------------------------
    // automations
    //----------------------------------

    @SuppressWarnings("unused")
    private Map<UUID, LibraryAutomation> automations = new HashMap<UUID, LibraryAutomation>();

    private File directory;

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File value) {
        directory = value;
    }

    public Library() {
        xStream = new XStream(new JettisonMappedXmlDriver());
        xStream.setMode(XStream.NO_REFERENCES);
    }

    public void _save() throws IOException {
        String data = xStream.toXML(this);
        File file = new File(directory, "library.ctk");
        FileUtils.writeStringToFile(file, data);
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

}
