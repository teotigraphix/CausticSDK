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

package com.teotigraphix.caustic.meta.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;

/**
 * @author Michael Schmalle
 */
public class BrowserModel {

    @SuppressWarnings("unused")
    private Activity activity;

    private List<String> items = null;

    private List<String> paths = null;

    private File rootDirectory;

    private File location;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // items
    //----------------------------------

    public List<String> getItems() {
        return items;
    }

    //----------------------------------
    // paths
    //----------------------------------

    public List<String> getPaths() {
        return paths;
    }

    //----------------------------------
    // rootDirectory
    //----------------------------------

    public File getRootDirectory() {
        return rootDirectory;
    }

    public void setRootDirectory(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    //----------------------------------
    // location
    //----------------------------------

    public File getLocation() {
        return location;
    }

    public void setLocation(File location) {
        this.location = location;
        updateModel();
        listener.onLocationChange(location);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public BrowserModel(Activity activity) {
        this.activity = activity;
    }

    private void updateModel() {
        items = new ArrayList<String>();
        paths = new ArrayList<String>();

        File directory = new File(location.getAbsolutePath());
        File[] allFiles = directory.listFiles();

        final String rootPath = rootDirectory.getAbsolutePath();
        if (!location.equals(rootDirectory)) {
            items.add(rootPath);
            paths.add(rootPath);
            items.add("../");
            paths.add(directory.getParent());
        }

        List<File> directories = new ArrayList<File>();
        List<File> files = new ArrayList<File>();

        for (int i = 0; i < allFiles.length; i++) {
            File file = allFiles[i];
            if (file.isDirectory()) {
                directories.add(file);
            } else {
                if (file.getName().endsWith(".caustic"))
                    files.add(file);
            }
        }

        Collections.sort(directories);
        Collections.sort(files);

        for (File file : directories) {
            paths.add(file.getPath());
            items.add(file.getName() + "/");
        }

        for (File file : files) {
            paths.add(file.getPath());
            items.add(file.getName());
        }
    }

    //--------------------------------------------------------------------------
    // Event API
    //--------------------------------------------------------------------------

    private OnBrowserModelListener listener;

    public void setOnBrowserModelListener(OnBrowserModelListener l) {
        listener = l;
    }

    public static interface OnBrowserModelListener {
        void onLocationChange(File location);
    }
}
