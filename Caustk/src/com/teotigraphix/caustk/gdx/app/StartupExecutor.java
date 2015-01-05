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

package com.teotigraphix.caustk.gdx.app;

import java.io.File;
import java.io.IOException;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.core.ICaustkRuntime;
import com.teotigraphix.caustk.core.ISoundGenerator;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * The {@link StartupExecutor} manages and creates the
 * {@link com.teotigraphix.caustk.core.CaustkRuntime} that is responsible for
 * the {@link com.teotigraphix.caustk.core.ISoundGenerator}, {@link Rack},
 * {@link com.teotigraphix.caustk.core.CaustkFactory} and session
 * {@link com.sun.jna.Library}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class StartupExecutor {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private ICaustkRuntime runtime;

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new {@link StartupExecutor} with a platform implementation of
     * the {@link com.teotigraphix.caustk.core.ISoundGenerator}.
     * 
     * @param application The main caustk application.
     * @param soundGenerator The platform specific
     *            {@link com.teotigraphix.caustk.core.ISoundGenerator}.
     */
    public StartupExecutor(ICaustkApplication application, ISoundGenerator soundGenerator) {
        System.out.println("StartupExecutor : >>> Create CaustkRuntime");
        runtime = CaustkRuntime.createInstance(application, soundGenerator);
        System.out.println("StartupExecutor : <<< Create CaustkRuntime");
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Creates the application root and sets
     * {@link com.teotigraphix.caustk.utils.RuntimeUtils} session properties.
     * 
     * @param application The main application.
     * @throws com.teotigraphix.caustk.core.CausticException the caustic folder
     *             does not exist, is caustic installed?
     * @throws java.io.IOException
     * @see {@link com.teotigraphix.caustk.utils.RuntimeUtils#STORAGE_ROOT}
     * @see {@link com.teotigraphix.caustk.utils.RuntimeUtils#APP_ROOT}
     */
    public ICaustkRuntime create(ICaustkApplication application, File storageRoot,
            File applicationRoot) throws CausticException, IOException {
        RuntimeUtils.STORAGE_ROOT = storageRoot.getAbsolutePath();
        RuntimeUtils.APP_ROOT = applicationRoot.getAbsolutePath();
        return runtime;
    }

    /**
     * Searches for the directory 1 level deep.
     * 
     * @param parent
     * @param directory
     * @throws java.io.IOException
     */
    public static File getContainedDirectory(File parent, File directory) throws IOException {
        File[] files = parent.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                File[] search = file.listFiles();
                if (search != null) {
                    for (File child : search) {
                        if (child.isDirectory() && child.getName().equals(directory.getName()))
                            return child.getParentFile();
                    }
                }
            }
        }
        return null;
    }
}
