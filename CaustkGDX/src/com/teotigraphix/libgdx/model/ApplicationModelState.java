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

package com.teotigraphix.libgdx.model;

import java.io.Serializable;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.libgdx.application.CausticSongFile;

/**
 * The serialized binary file root.
 * <p>
 * Each application creates a specific model used within it's
 * {@link ICaustkModel} implementations.
 */
public abstract class ApplicationModelState implements Serializable {

    private static final long serialVersionUID = 7409599794451779182L;

    //----------------------------------
    // controller
    //----------------------------------

    private transient ICaustkController controller;

    public ICaustkController getController() {
        return controller;
    }

    public void setController(ICaustkController controller) {
        this.controller = controller;
    }

    //----------------------------------
    // id
    //----------------------------------

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //----------------------------------
    // songFile
    //----------------------------------

    private CausticSongFile songFile;

    public CausticSongFile getSongFile() {
        return songFile;
    }

    public void setSongFile(CausticSongFile value) {
        songFile = value;
    }

    public ApplicationModelState() {
    }

    public void save() {
    }

}
