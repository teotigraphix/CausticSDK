
package com.teotigraphix.caustk.groove;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

public class GrooveLibrary {

    private File contentDirectory;

    private Map<UUID, LibraryProject> projects = new HashMap<UUID, LibraryProject>();

    private Map<UUID, LibraryGroup> groups = new HashMap<UUID, LibraryGroup>();

    private Map<UUID, LibrarySound> sounds = new HashMap<UUID, LibrarySound>();

    private Map<UUID, LibraryInstrument> instruments = new HashMap<UUID, LibraryInstrument>();

    private Map<UUID, LibraryEffect> effects = new HashMap<UUID, LibraryEffect>();

    private Map<UUID, LibrarySample> samples = new HashMap<UUID, LibrarySample>();

    private FactoryProductProvider defaultContentProvider;

    /**
     * The location of the application's Content folder.
     * <p>
     * All factory content is installed here.
     */
    public File getContentDirectory() {
        return contentDirectory;
    }

    public Collection<LibraryProject> getProjects() {
        return projects.values();
    }

    public Collection<LibraryGroup> getGroups() {
        return groups.values();
    }

    public Collection<LibrarySound> getSounds() {
        return sounds.values();
    }

    public Collection<LibraryInstrument> getInstruments() {
        return instruments.values();
    }

    public Collection<LibraryEffect> getEffects() {
        return effects.values();
    }

    public Collection<LibrarySample> getSamples() {
        return samples.values();
    }

    public GrooveLibrary(File contentDirectory, FactoryProductProvider defaultContentProvider)
            throws IOException {
        this.contentDirectory = contentDirectory;
        this.defaultContentProvider = defaultContentProvider;
        initContentDirectory(contentDirectory);
    }

    private void initContentDirectory(File contentDirectory) throws IOException {
        if (!contentDirectory.exists()) {
            FileUtils.forceMkdir(contentDirectory);
            installDefaultContent(contentDirectory);
        }
    }

    private void installDefaultContent(File contentDirectory) {
        defaultContentProvider.install(this, contentDirectory);
        save();
    }

    //    public void addSourceRoot(File sourceRoot) {
    //    }

    private void save() {
        // save the libraries model to disk
    }

}
