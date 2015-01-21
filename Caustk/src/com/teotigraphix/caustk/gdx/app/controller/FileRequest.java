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

package com.teotigraphix.caustk.gdx.app.controller;

import java.io.File;

import com.badlogic.gdx.math.Rectangle;

public class FileRequest {

    private Object id;

    private File root;

    private File location;

    private String[] filter;

    private Rectangle rectangle;

    private File file;

    private String title;

    private Object data;

    public Object getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public File getRoot() {
        return root;
    }

    public File getLocation() {
        return location;
    }

    public String[] getFilter() {
        return filter;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Extra data needed for the operation (per call unique).
     */
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public FileRequest(Object id, String title, File root, File location, String[] filter,
            Rectangle rectangle) {
        this.id = id;
        this.title = title;
        this.root = root;
        this.location = location;
        this.filter = filter;
        this.rectangle = rectangle;
    }
}
