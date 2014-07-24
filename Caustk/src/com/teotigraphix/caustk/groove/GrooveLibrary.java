
package com.teotigraphix.caustk.groove;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

public class GrooveLibrary {

    private File rootDirectory;

    private Map<UUID, LibraryProject> projects = new HashMap<UUID, LibraryProject>();

    private Map<UUID, LibraryGroup> groups = new HashMap<UUID, LibraryGroup>();

    private Map<UUID, LibrarySound> sounds = new HashMap<UUID, LibrarySound>();

    private Map<UUID, LibraryInstrument> instruments = new HashMap<UUID, LibraryInstrument>();

    private Map<UUID, LibraryEffect> effects = new HashMap<UUID, LibraryEffect>();

    private Map<UUID, LibrarySample> samples = new HashMap<UUID, LibrarySample>();

    private DefaultContentProvider defaultContentProvider;

    public File getRootDirectory() {
        return rootDirectory;
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

    public GrooveLibrary(File rootDirectory, DefaultContentProvider defaultContentProvider)
            throws IOException {
        this.rootDirectory = rootDirectory;
        this.defaultContentProvider = defaultContentProvider;
        initRootDirectory(rootDirectory);
    }

    private void initRootDirectory(File rootDirectory) throws IOException {
        if (!rootDirectory.exists()) {
            FileUtils.forceMkdir(rootDirectory);
            installDefaultContent(rootDirectory);
        }
    }

    private void installDefaultContent(File rootDirectory) {
        defaultContentProvider.install(this, rootDirectory);
        save();
    }

    //    public void addSourceRoot(File sourceRoot) {
    //    }

    private void save() {
        // save the libraries model to disk
    }

}
