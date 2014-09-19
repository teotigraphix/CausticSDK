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

package com.teotigraphix.gdx.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.inject.Singleton;
import com.teotigraphix.gdx.app.ApplicationComponent;

/**
 * @author Michael Schmalle
 */
@Singleton
public class FileModel extends ApplicationComponent implements IFileModel {

    private List<String> items = null;

    private List<String> paths = null;

    private File lastDirectory = null;

    private File rootDirectory;

    private File location;

    private FileRequest fileRequest;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // items
    //----------------------------------

    @Override
    public List<String> getItems() {
        return items;
    }

    //----------------------------------
    // paths
    //----------------------------------

    @Override
    public List<String> getPaths() {
        return paths;
    }

    @Override
    public File getLastDirectory() {
        return lastDirectory == null ? location : lastDirectory;
    }

    public void setLastDirectory(File lastDirectory) {
        this.lastDirectory = lastDirectory;
    }

    //----------------------------------
    // rootDirectory
    //----------------------------------

    @Override
    public File getRootDirectory() {
        return rootDirectory;
    }

    @Override
    public void setRootDirectory(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    //----------------------------------
    // location
    //----------------------------------

    @Override
    public File getLocation() {
        return location;
    }

    @Override
    public void setLocation(File location) {
        this.location = location;
        if (location.isDirectory()) {
            lastDirectory = location;
        }
        updateModel();
        getEventBus().post(new OnFileModelEvent(OnFileModelEvent.Location, location));
    }

    //----------------------------------
    // selectFile
    //----------------------------------

    /**
     * Sets the selected File from the chooser.
     */
    @Override
    public void selectFile(File selectedFile) {
        fileRequest.setFile(selectedFile);
        getEventBus().post(new OnFileModelEvent(OnFileModelEvent.Complete, fileRequest));
        fileRequest = null;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public FileModel() {
    }

    private void setFileRequest(FileRequest fileRequest) {
        this.fileRequest = fileRequest;
        setRootDirectory(fileRequest.getRoot());
        setLocation(fileRequest.getLocation());
    }

    @Override
    public void browse(FileRequest fileRequest) {
        setFileRequest(fileRequest);
        getEventBus().post(new OnFileModelEvent(OnFileModelEvent.Request, fileRequest));
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
                for (String filter : fileRequest.getFilter()) {
                    if (file.getName().endsWith(filter))
                        files.add(file);
                }
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
}
