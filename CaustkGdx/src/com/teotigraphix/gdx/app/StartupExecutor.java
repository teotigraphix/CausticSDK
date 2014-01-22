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

package com.teotigraphix.gdx.app;

import java.io.File;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.ISoundGenerator;
import com.teotigraphix.caustk.node.CaustkFactory;
import com.teotigraphix.caustk.node.Library;
import com.teotigraphix.caustk.rack.CaustkRuntime;
import com.teotigraphix.caustk.rack.Rack;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.gdx.IGdxApplication;

/**
 * The {@link StartupExecutor} manages and creates the {@link CaustkRuntime}
 * that is responsible for the {@link ISoundGenerator}, {@link Rack},
 * {@link CaustkFactory} and session {@link Library}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class StartupExecutor {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private CaustkRuntime runtime;

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new {@link StartupExecutor} with a platform implementation of
     * the {@link ISoundGenerator}.
     * 
     * @param soundGenerator The platform specific {@link ISoundGenerator}.
     */
    public StartupExecutor(ISoundGenerator soundGenerator) {
        runtime = new CaustkRuntime(soundGenerator);
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Creates the application root and sets {@link RuntimeUtils} session
     * properties.
     * 
     * @param gdxApplication The main application.
     * @throws CausticException the caustic folder does not exist, is caustic
     *             installed?
     * @throws IOException
     * @see {@link RuntimeUtils#STORAGE_ROOT}
     * @see {@link RuntimeUtils#APP_ROOT}
     */
    public CaustkRuntime create(IGdxApplication gdxApplication) throws CausticException,
            IOException {
        File root = new File(Gdx.files.getExternalStoragePath());
        File caustic = new File(root, "caustic");
        if (!caustic.exists()) {
            File newRoot = getContainedDirectory(root, new File("caustic"));
            if (newRoot == null)
                throw new CausticException(
                        "the caustic folder does not exist, is caustic installed?");
            root = newRoot;
        }

        RuntimeUtils.STORAGE_ROOT = root.getAbsolutePath();
        RuntimeUtils.APP_ROOT = new File(root, gdxApplication.getApplicationName())
                .getAbsolutePath();

        return runtime;
    }

    /**
     * Searches for the directory 1 level deep.
     * 
     * @param parent
     * @param directory
     * @throws IOException
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
