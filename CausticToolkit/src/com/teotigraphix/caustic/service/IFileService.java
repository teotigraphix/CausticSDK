////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.service;

import java.io.File;

import android.app.Activity;

import com.teotigraphix.caustic.song.IProject;

/**
 * The {@link IFileService} API allows a universal access into the file system
 * for CausticCore applications.
 * <p>
 * Any application that expects to interface with the {@link IProject} is
 * required to use this API when creating, loading or editing project files.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IFileService {

    /**
     * Returns a File instance for something like
     * <code>/data/data/com.teotigraphix.caustic.test/files</code> using the
     * {@link Activity#getFilesDir()}.
     */
    File getPrivateDirectory();

    /**
     * Returns a File instance for <code>/sdcard/</code>.
     */
    File getPublicDirectory();

    /**
     * Returns a File instance for <code>/sdcard/AppName/</code>.
     */
    File getApplicationDirectory();

    /**
     * Returns a File instance for <code>/sdcard/AppName/libraries</code>.
     */
    File getLibrariesDirectory();

    /**
     * Returns a File instance for
     * <code>/sdcard/AppName/[current_library]/patches</code>.
     */
    File getPatchesDirectory();

    /**
     * Returns a File instance for
     * <code>/sdcard/AppName/[current_library]/patterns</code>.
     */
    File getPatternsDirectory();

    /**
     * Returns a File instance for
     * <code>/sdcard/AppName/[current_library]/presets</code>.
     */
    File getPresetsDirectory();

    /**
     * Returns a File instance for
     * <code>/sdcard/AppName/[current_library]/songs</code>.
     */
    File getSongsDirectory();

    /**
     * Returns a File instance for <code>/sdcard/AppName/projects</code>.
     */
    File getProjectsDirectory();

    /**
     * Returns a project file located in the projects directory.
     * 
     * @param reletivePath The relative path to the project file based on the
     *            projects root directory.
     */
    File getProjectFile(String reletivePath);

}
