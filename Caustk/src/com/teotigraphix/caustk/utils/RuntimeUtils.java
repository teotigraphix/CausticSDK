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

package com.teotigraphix.caustk.utils;

import com.teotigraphix.caustk.core.MachineType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Runtime file utility methods.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class RuntimeUtils {

    private static final String TEMP = ".temp";

    private static final String FORWARD_SLASH = "/";

    private static final String DOT = ".";

    private static final String CONTENT = "Content";

    private static final String PROJECTS = "Projects";

    private static final String CAUSTIC = "caustic";

    private static final String CAUSTIC_EXTENSION = ".caustic";

    private static final String CAUSTIC_PRESETS = "caustic/presets";

    private static final String CAUSTIC_SAMPLES = "caustic/samples";

    private static final String CAUSTIC_SONGS = "caustic/songs";

    /**
     * This has to be set at Application startup before ANYTHING happens
     * Android; Environment.getExternalStorageDirectory();
     */
    public static String STORAGE_ROOT = null;

    public static String APP_ROOT = null;

    /**
     * /mnt/sdcard/
     */
    public static final File getExternalStorageDirectory() {
        //File file = Environment.getExternalStorageDirectory();
        if (STORAGE_ROOT == null)
            throw new RuntimeException("STORAGE_ROOT is null, set in RuntimeUtils");
        File directory = new File(STORAGE_ROOT);
        return directory;
    }

    public static final File getApplicationDirectory() {
        if (APP_ROOT == null)
            throw new RuntimeException("APP_ROOT is null, set in RuntimeUtils");
        File directory = new File(APP_ROOT);
        return directory;
    }

    /**
     * Returns the <code>/__external__/Application/.temp</code> directory., safe
     * for extracting and saving temp files for the application.
     * <p>
     * Could be deleted at any time.
     */
    public static final File getApplicationTempDirectory() {
        final File directory = getApplicationDirectory(TEMP);
        if (!directory.exists())
            directory.mkdirs();
        return directory;
    }

    /**
     * Returns the <code>/__external__/Application/Content</code> directory.
     * <p>
     * This directory is where all default and factory content is installed.
     */
    public static File getApplicationContentDirectory() {
        return getApplicationDirectory(CONTENT);
    }

    /**
     * Returns the <code>/__external__/Application/Projects</code> directory.
     */
    public static File getApplicationProjectsDirectory() {
        return getApplicationDirectory(PROJECTS);
    }

    public static final File getApplicationDirectory(String path) {
        File directory = new File(getApplicationDirectory(), path);
        return directory;
    }

    /**
     * Returns a File directory that is based off the
     * {@link #getExternalStorageDirectory()} (/mnt/sdcard/) path.
     * 
     * @param path The path to append to the public device directory.
     */
    public static final File getDirectory(String path) {
        File directory = new File(getExternalStorageDirectory(), path);
        return directory;
    }

    /**
     * Returns a File directory based off the application's root directory.
     * 
     * @param applicationDirectory Usually /mnt/sdcard/MyApp
     * @param path The child folder hierarchy.
     */
    public static File getDirectory(File applicationDirectory, String path) {
        File directory = new File(applicationDirectory, path);
        return directory;
    }

    /**
     * Converts and {@link java.io.InputStream} to a String.
     * 
     * @param is The {@link java.io.InputStream} to read into a String.
     * @return THe String read from the stream.
     * @throws java.io.IOException
     */
    public static final String convertStreamToString(InputStream is) throws IOException {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }
        return writer.toString();
    }

    /**
     * Loads a String from the File.
     * 
     * @param file The location of the File to load.
     * @return The String loaded from the File.
     * @throws java.io.IOException
     */
    public static final String loadFile(File file) throws IOException {
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        StringBuffer s = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            s.append(line);
            s.append("\n");
        }
        reader.close();
        return s.toString();
    }

    /**
     * Saves the String data to the File.
     * 
     * @param file The location to save the String.
     * @param data The String data to save.
     * @throws java.io.IOException
     */
    public static final void saveFile(File file, String data) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(data);
        } catch (IOException e) {
            throw e;
        } finally {
            writer.close();
        }
    }

    //--------------------------------------------------------------------------
    //
    // Caustic File Locations
    //
    //--------------------------------------------------------------------------

    /**
     * Returns the <code>/__external__/caustic</code> directory.
     */
    public static File getCausticDirectory() {
        return new File(getExternalStorageDirectory(), CAUSTIC);
    }

    /**
     * Returns the <code>/__external__/caustic/presets</code> directory.
     */
    public static File getPresetsDirectory() {
        return new File(getExternalStorageDirectory(), CAUSTIC_PRESETS);
    }

    /**
     * Returns the <code>/__external__/caustic/presets/[subDirectory]</code>
     * directory.
     */
    public static File getPresetsDirectory(String subDirectory) {
        return new File(getPresetsDirectory(), subDirectory);
    }

    /**
     * Returns a preset file located in the native Caustic app
     * <code>/caustic/presets</code> directory.
     * <p>
     * If a relative path is provided, the prefix preset type directory should
     * not be included, this is added automatically such as
     * <code>subsynth</code> or <code>modular</code>.
     * 
     * @param machineType The {@link com.teotigraphix.caustk.core.MachineType}.
     * @param nameOrRelativePath The preset name or preset name with relative
     *            path without extension. e.g <code>MyPreset</code>,
     *            <code>sub/dir/MyPreset</code>.
     */
    public static File getPresetsFile(MachineType machineType, String nameOrRelativePath) {
        final StringBuilder sb = new StringBuilder();
        sb.append(machineType.getType());
        sb.append(FORWARD_SLASH);
        sb.append(nameOrRelativePath);
        sb.append(DOT);
        sb.append(machineType.getExtension());
        return new File(getPresetsDirectory(), sb.toString());
    }

    /**
     * Returns the <code>/__external__/caustic/samples</code> directory.
     */
    public static File getSamplesDirectory() {
        return new File(getExternalStorageDirectory(), CAUSTIC_SAMPLES);
    }

    public static File getSamplesFile(final String sampleType, final String sampleName) {
        final StringBuilder sb = new StringBuilder();
        sb.append(sampleType);
        sb.append(FORWARD_SLASH);
        sb.append(sampleName);
        sb.append(".wav");
        return new File(getSamplesDirectory(), sb.toString());
    }

    /**
     * Returns the <code>/__external__/caustic/songs</code> directory.
     */
    public static File getSongsDirectory() {
        return new File(getExternalStorageDirectory(), CAUSTIC_SONGS);
    }

    /**
     * Returns a <code>.caustic</code> song located in the
     * {@link #getSongsDirectory()}.
     * 
     * @param songName The song name without the <code>.caustic</code>
     *            extension.
     */
    public static File getSongFile(String songName) {
        return new File(getSongsDirectory(), songName + CAUSTIC_EXTENSION);
    }
}
