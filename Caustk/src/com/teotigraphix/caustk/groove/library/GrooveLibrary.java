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

package com.teotigraphix.caustk.groove.library;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
