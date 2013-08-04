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

package com.teotigraphix.caustk.library;

import java.io.File;
import java.util.UUID;

import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.tone.ToneType;

/**
 * The {@link LibraryPatch} has a unique {@link UUID} and has a corresponding
 * preset file located in the <code>presets</code> directory.
 */
public class LibraryPatch extends LibraryItem {

    //----------------------------------
    // name
    //----------------------------------

    private String name;

    /**
     * The name of the Machine the patch was saved against in the .caustic file.
     */
    public final String getName() {
        return name;
    }

    public final void setName(String value) {
        name = value;
    }

    //----------------------------------
    // toneType
    //----------------------------------

    private ToneType toneType;

    public ToneType getToneType() {
        return toneType;
    }

    public void setToneType(ToneType value) {
        toneType = value;
    }

    //----------------------------------
    // data
    //----------------------------------

    private String data;

    /**
     * NOT IMPLEMENTED YET
     * <p>
     * Returns the String {@link ISerialize} data that was saved from the
     * current tone.
     */
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    //----------------------------------
    // presetFile
    //----------------------------------

    /**
     * Returns the file, not the path of the preset {@link File}.
     * <p>
     * The file extension is correctly created using the {@link #getToneType()}.
     */
    public File getPresetFile() {
        return new File(getId().toString() + "." + toneType.getValue());
    }

    public LibraryPatch() {
    }

    @Override
    public String toString() {
        return getMetadataInfo().getName() + " - " + getMetadataInfo().getTags().toString();
    }
}
