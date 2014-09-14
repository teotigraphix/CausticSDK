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
