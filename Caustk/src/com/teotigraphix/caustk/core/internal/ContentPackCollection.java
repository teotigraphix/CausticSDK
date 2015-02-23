////////////////////////////////////////////////////////////////////////////////
// Copyright 2015 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.core.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.teotigraphix.caustk.node.machine.MachineType;

public class ContentPackCollection {

    private String name;

    private File rootDirectory;

    private Map<MachineType, Collection<File>> presetFiles = new HashMap<MachineType, Collection<File>>();

    // <beatbox <Kit1, Collection[.wav, .wav]>>
    private Map<MachineType, Map<String, Collection<File>>> kitSampleFiles = new HashMap<MachineType, Map<String, Collection<File>>>();

    private Map<MachineType, Map<String, Collection<File>>> kitMidiFiles = new HashMap<MachineType, Map<String, Collection<File>>>();

    private Collection<String> kitNames = new ArrayList<String>();

    private Collection<File> kitSongs = new ArrayList<File>();

    public String getName() {
        return name;
    }

    public File getRootDirectory() {
        return rootDirectory;
    }

    public Collection<File> getPresets(MachineType machineType) {
        return presetFiles.get(machineType);
    }

    public Collection<File> getKitSamples(MachineType machineType, String kitName) {
        if (!kitSampleFiles.containsKey(machineType))
            return null;

        return kitSampleFiles.get(machineType).get(kitName);
    }

    public Collection<File> getKitMidi(MachineType machineType, String kitName) {
        if (!kitMidiFiles.containsKey(machineType))
            return null;

        return kitMidiFiles.get(machineType).get(kitName);
    }

    public Collection<String> getKitNames() {
        return kitNames;
    }

    public Collection<File> getKitSongs() {
        return kitSongs;
    }

    public ContentPackCollection(File rootDirectory, String name) {
        this.rootDirectory = rootDirectory;
        this.name = name;
    }

    public void scan() {
        File presetDirectory = new File(rootDirectory, "presets");
        File samplesDirectory = new File(rootDirectory, "samples");
        File songsDirectory = new File(rootDirectory, "songs");

        scanPresets(presetDirectory);
        scanSamples(samplesDirectory);
        scanSongs(songsDirectory);
    }

    private void scanSongs(File songsDirectory) {
        if (songsDirectory.exists()) {
            Collection<File> files = FileUtils.listFiles(songsDirectory, new String[] {
                "caustic"
            }, true);
            kitSongs.addAll(files);
        }
    }

    private void scanSamples(File samplesDirectory) {
        for (MachineType type : MachineType.values()) {
            // root/presets/beatbox/tg-trance1
            final File directory = new File(samplesDirectory, type.getType() + File.separator
                    + name);
            if (directory.exists()) {
                Collection<File> files = FileUtils.listFilesAndDirs(directory, new IOFileFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return true;
                    }

                    @Override
                    public boolean accept(File file) { // beatbox\tg-trance1\Kit1
                        return file.getParentFile().equals(directory);
                    }
                }, new IOFileFilter() {

                    @Override
                    public boolean accept(File dir, String name) {
                        return true;
                    }

                    @Override
                    public boolean accept(File file) {
                        return file.getParentFile().equals(directory);
                    }
                });
                for (File kitDirectory : files) {
                    if (kitDirectory.isDirectory()) {
                        String kitName = kitDirectory.getName();
                        if (kitName.equals(name))
                            continue;

                        kitNames.add(kitName);

                        Collection<File> samples = FileUtils.listFiles(kitDirectory, new String[] {
                            "wav"
                        }, false);
                        Map<String, Collection<File>> map = kitSampleFiles.get(type);
                        if (map == null) {
                            map = new HashMap<String, Collection<File>>();
                            kitSampleFiles.put(type, map);
                        }
                        map.put(kitName, samples);

                        File kitMidiDirectory = new File(kitDirectory, "midi");
                        if (kitMidiDirectory.exists()) {
                            Collection<File> midis = FileUtils.listFiles(kitMidiDirectory,
                                    new String[] {
                                        "mid"
                                    }, false);
                            Map<String, Collection<File>> amap = kitMidiFiles.get(type);
                            if (amap == null) {
                                amap = new HashMap<String, Collection<File>>();
                                kitMidiFiles.put(type, amap);
                            }
                            amap.put(kitName, midis);

                        }
                    }
                }
            }
        }
    }

    private void scanPresets(File presetDirectory) {
        for (MachineType type : MachineType.values()) {
            // root/presets/beatbox/tg-trance1
            File directory = new File(presetDirectory, type.getType() + File.separator + name);
            if (directory.exists()) {
                Collection<File> files = FileUtils.listFiles(directory, new String[] {
                    type.getExtension()
                }, true);
                presetFiles.put(type, files);
            }
        }
    }

}
