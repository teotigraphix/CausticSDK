
package com.teotigraphix.gdx.app;

import java.io.File;

import com.badlogic.gdx.Preferences;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * A proxy to the {@link ApplicationModel}s {@link Preferences} providing a
 * public API.
 */
public class ApplicationPreferences {

    private static final String ATT_ROOT_DIRECTORY = "root-directory";

    private Preferences preferences;

    // ApplicationModel creates
    public ApplicationPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    //----------------------------------
    // ATT_ROOT_DIRECTORY
    //----------------------------------

    /**
     * Returns the {@link #getRootDirectoryPath()} as a File.
     */
    public File getRootDirectoryFile() {
        return new File(getRootDirectoryPath());
    }

    /**
     * Returns the {@link RuntimeUtils#getCausticDirectory()} or the
     * root-directory found within the {@link #preferences}.
     */
    public String getRootDirectoryPath() {
        return preferences.getString(ATT_ROOT_DIRECTORY, RuntimeUtils.getCausticDirectory()
                .getAbsolutePath());
    }

    /**
     * Sets the system dependent absolute directory path for the root-directory.
     * 
     * @param absolutePath The absolute directory path.
     */
    public void setRootDirectoryPath(String absolutePath) {
        preferences.putString(ATT_ROOT_DIRECTORY, absolutePath);
    }

}
