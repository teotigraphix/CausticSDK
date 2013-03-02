
package com.teotigraphix.caustic.internal.controller.application;

import java.io.File;

import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.inject.Inject;
import com.teotigraphix.caustic.controller.IApplicationPreferences;
import com.teotigraphix.caustic.song.IProject;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.caustic.song.IWorkspace.OnWorkspaceQuickSaveEvent;

@ContextSingleton
public class ApplicationPreferences implements IApplicationPreferences {

    private static final String TAG = "ApplicationPreferences";

    private static final String QUICK_SAVE_BACKUP_XML = "QuickSaveBackup.xml";

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

    void onWorkspaceQuickSaveHandler(@Observes OnWorkspaceQuickSaveEvent event) {
        Log.d(TAG, "onWorkspaceQuickSaveHandler()");
        IProject project = event.getWorkspace().getProject();
        SharedPreferences settings = event.getWorkspace().getPreferences();

        Editor edit = settings.edit();
        // run the save methods on the Editor
        commitLastProjectFile(project, edit);

        edit.commit();
    }

    private void commitLastProjectFile(IProject project, Editor edit) {
        if (project.getData().isSavedProject()) {
            Log.d(TAG, "commitLastProjectFile() " + project.getFile().getAbsolutePath());
            edit.putString(PREF_LAST_PROJECT_PATH, project.getFile().getAbsolutePath());
        }
    }

    @Override
    public void bootPreferences(SharedPreferences preferences) {
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
