
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
     * Called from {@link IWorkspace#startAndRun()} boot().
     * <p>
     * The method responsible for calling; {@link IApplicationRuntime#boot()}
     * implementation of the application.
     * 
     * @param preferences The {@link IWorkspace#getPreferences()} instance.
     */
    void bootPreferences(SharedPreferences preferences);

}
