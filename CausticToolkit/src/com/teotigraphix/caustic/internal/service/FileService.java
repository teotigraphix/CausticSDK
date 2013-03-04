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

package com.teotigraphix.caustic.internal.service;

import java.io.File;

import android.app.Application;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.activity.IApplicationConfiguration;
import com.teotigraphix.caustic.service.IFileService;
import com.teotigraphix.common.utils.RuntimeUtils;

/**
 * Default implementation of the {@link IFileService} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
@Singleton
public class FileService implements IFileService {

    private static final String SONGS = "songs";

    private static final String PROJECTS = "projects";

    private static final String PRESETS = "presets";

    private static final String PATTERNS = "patterns";

    private static final String PATCHES = "patches";

    private static final String LIBRARIES = "libraries";

    private File mPrivateDirectory;

    private File mPublicDirectory;

    private File mApplicationDirectory;

    private File mLibrariesDirectory;

    private File mProjectsDirectory;

    private File mPatchesDirectory;

    private File mPatternsDirectory;

    private File mPresetsDirectory;

    private File mSongsDirectory;

    @Override
    public File getPublicDirectory() {
        return mPublicDirectory;
    }

    @Override
    public File getApplicationDirectory() {
        return mApplicationDirectory;
    }

    @Override
    public File getLibrariesDirectory() {
        return mLibrariesDirectory;
    }

    @Override
    public File getProjectsDirectory() {
        return mProjectsDirectory;
    }

    @Override
    public File getSongsDirectory() {
        return mSongsDirectory;
    }

    @Override
    public File getPatchesDirectory() {
        return mPatchesDirectory;
    }

    @Override
    public File getPresetsDirectory() {
        return mPresetsDirectory;
    }

    @Override
    public File getPatternsDirectory() {
        return mPatternsDirectory;
    }

    @Override
    public File getPrivateDirectory() {
        return mPrivateDirectory;
    }

    @Inject
    public FileService(Application application, IApplicationConfiguration applicationConfig) {
        Log.e("FileService", applicationConfig.getApplicationName());
        mPrivateDirectory = application.getFilesDir();

        mPublicDirectory = RuntimeUtils.getExternalStorageDirectory();

        mApplicationDirectory = RuntimeUtils.getDirectory(applicationConfig.getApplicationName());
        if (!mApplicationDirectory.exists())
            mApplicationDirectory.mkdirs();

        mLibrariesDirectory = RuntimeUtils.getDirectory(mApplicationDirectory, LIBRARIES);
        if (!mLibrariesDirectory.exists())
            mLibrariesDirectory.mkdirs();

        mProjectsDirectory = RuntimeUtils.getDirectory(mApplicationDirectory, PROJECTS);
        if (!mProjectsDirectory.exists())
            mProjectsDirectory.mkdirs();
    }

    @Override
    public File getProjectFile(String reletivePath) {
        return new File(mProjectsDirectory, reletivePath);
    }

    //    /**
    //     * This handler gets fired before the complete event, any clients that
    //     * actually do work with the preset data will act on the complete event not
    //     * the load.
    //     * 
    //     * @param event
    //     */
    //    void onPresetLoadComplete(@Observes OnPresetLoad event) {
    //        File rootDirectory = new File(mLibrariesDirectory, event.getLibraryName());
    //
    //        if (event.getKind().equals(PresetLibraryKind.FULL)) {
    //            updatePresetDirectory(rootDirectory);
    //            updatePatchDirectory(rootDirectory);
    //            updatePatternDirectory(rootDirectory);
    //            updateSongDirectory(rootDirectory);
    //        } else if (event.getKind().equals(PresetLibraryKind.PATCH)) {
    //            updatePatchDirectory(rootDirectory);
    //        } else if (event.getKind().equals(PresetLibraryKind.PATTERN)) {
    //            updatePatternDirectory(rootDirectory);
    //        } else if (event.getKind().equals(PresetLibraryKind.SONG)) {
    //            updateSongDirectory(rootDirectory);
    //        } else if (event.getKind().equals(PresetLibraryKind.PRESET)) {
    //            updatePresetDirectory(rootDirectory);
    //        }
    //    }
    @SuppressWarnings("unused")
    private void updatePresetDirectory(File root) {
        mPresetsDirectory = RuntimeUtils.getDirectory(root, PRESETS);
    }

    @SuppressWarnings("unused")
    private void updatePatchDirectory(File root) {
        mPatchesDirectory = RuntimeUtils.getDirectory(root, PATCHES);
    }

    @SuppressWarnings("unused")
    private void updatePatternDirectory(File root) {
        mPatternsDirectory = RuntimeUtils.getDirectory(root, PATTERNS);
    }

    @SuppressWarnings("unused")
    private void updateSongDirectory(File root) {
        mSongsDirectory = RuntimeUtils.getDirectory(root, SONGS);
    }

}
