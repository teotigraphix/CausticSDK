
package com.teotigraphix.caustic.controller;

import java.io.File;

import android.content.SharedPreferences;

import com.teotigraphix.caustic.activity.IApplicationRuntime;
import com.teotigraphix.caustic.song.IWorkspace;

public interface IApplicationPreferences {

    File getLastProjectFile();

    File getQuickSaveFile();

    //List<PartConfig> getConfiguration(String name);

    /**
     * Saves a snapshot of the project for backup while recording the
     * preferences to the {@link SharedPreferences} of the project.
     * <p>
     * Any preferences that are to be persisted through a users session will be
     * saved during this call to the preferences editor.
     * <p>
     * XXX put a see for the event dispatched
     */
    void quickSave();

    /**
     * Called from {@link IWorkspace#startAndRun()} boot().
     * <p>
     * The method responsible for calling; {@link IApplicationRuntime#boot()}
     * implementation of the application.
     * 
     * @param preferences The {@link IWorkspace#getPreferences()} instance.
     */
    void bootPreferences(SharedPreferences preferences);

}
