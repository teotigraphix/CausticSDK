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

package com.teotigraphix.caustic.internal.controller.application;

import java.io.File;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.inject.Inject;
import com.teotigraphix.caustic.activity.IApplicationPreferences;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.song.IProject;
import com.teotigraphix.caustic.song.IWorkspace;

public class ApplicationPreferences implements IApplicationPreferences {

    private static final String TAG = "ApplicationPreferences";

    private static String QUICK_SAVE_BACKUP_XML = "QuickSaveBackup.xml";

    private static final String UNTITLED_XML = "Untitled.xml";

    private static final String PREF_LAST_PROJECT_PATH = "lastProjectPath";

    @Inject
    IWorkspace workspace;

    private File mQuickSaveFile;

    private File mLastProjectFile;

    @Override
    public File getLastProjectFile() {
        return mLastProjectFile;
    }

    void setLastProjectFile(File value) {
        Log.d(TAG, "setLastProjectFile() " + value.getAbsolutePath());
        mLastProjectFile = value;
    }

    @Override
    public File getQuickSaveFile() {
        return mQuickSaveFile;
    }

    private void setQuickSaveFile(File value) {
        mQuickSaveFile = value;
    }

    public ApplicationPreferences() {
    }

    @Override
    public void quickSave() {
        Log.d(TAG, "quickSave()");

        SharedPreferences settings = workspace.getSharedPreferences();
        Editor edit = settings.edit();

        // XXX create an event that all clients of the EventManager can listen
        // to, this will allow them to saves their personal preferences
        // into the SharedPreferences instance
        commitPreferences(edit);

        edit.commit();
    }

    private void commitPreferences(Editor edit) {
        IProject project = workspace.getProject();
        // only save the last project path if the project has been explicitly
        // saved to disk
        if (project.getData().isSavedProject()) {
            Log.d(TAG, "commitLastProjectFile() " + project.getFile().getAbsolutePath());
            edit.putString(PREF_LAST_PROJECT_PATH, project.getFile().getAbsolutePath());
        }

        // quick save the project snapshot
        File quickSave = getQuickSaveFile();
        try {
            project.saveQuick(quickSave);
        } catch (CausticException e) {
            Log.e(TAG, "project.saveQuick(quickSave)", e);
        }

        // dispatch event for all other listeners to save to
    }

    @Override
    public void bootPreferences(SharedPreferences preferences) {
        // called from runtime.boot()

        Log.d(TAG, "bootPreferences()");
        // we load the last project here from preference, set it on the pref instance
        setupLastProjectFile(preferences);
        setupQuickSaveProject(preferences);
    }

    private void setupLastProjectFile(SharedPreferences preferences) {
        File file = null;
        String absolutePath = preferences.getString(PREF_LAST_PROJECT_PATH, null);
        if (absolutePath != null) {
            file = new File(absolutePath);
        } else {
            file = workspace.getFileService().getProjectFile(UNTITLED_XML);
        }
        setLastProjectFile(file);
    }

    private void setupQuickSaveProject(SharedPreferences preferences) {
        File file = workspace.getFileService().getApplicationDirectory();
        File quickSaveFile = new File(file, QUICK_SAVE_BACKUP_XML);
        setQuickSaveFile(quickSaveFile);
    }

}
