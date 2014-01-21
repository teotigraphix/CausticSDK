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

package com.teotigraphix.caustk.rack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.ISoundGenerator;
import com.teotigraphix.caustk.node.CaustkFactory;
import com.teotigraphix.caustk.node.ICaustkNode;
import com.teotigraphix.caustk.node.Library;

/**
 * The {@link CaustkRuntime} encapsulates the {@link ISoundGenerator} and
 * {@link Rack} creation and initialization.
 * <p>
 * Holds the single {@link Rack} instance for an application session.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class CaustkRuntime {

    private ISoundGenerator soundGenerator;

    private Rack rack;

    private CaustkFactory factory;

    private Library library;

    ISoundGenerator getSoundGenerator() {
        return soundGenerator;
    }

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    /**
     * Returns the session {@link Rack} instance.
     */
    public final Rack getRack() {
        return rack;
    }

    /**
     * Returns the session {@link CaustkFactory} for created {@link ICaustkNode}
     * s.
     */
    public final CaustkFactory getFactory() {
        return factory;
    }

    /**
     * The current library loaded for an application.
     */
    public final Library getLibrary() {
        return library;
    }

    private void setLibrary(Library library) {
        this.library = library;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new runtime with {@link Rack}.
     * 
     * @param soundGenerator The platform sound engine.
     */
    public CaustkRuntime(ISoundGenerator soundGenerator) {
        this.soundGenerator = soundGenerator;
        initialize();
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public Library loadLibrary(File file) throws CausticException, IOException {
        if (file.exists())
            throw new FileNotFoundException("Library does not exist: " + file);

        String json = FileUtils.readFileToString(file);
        library = getFactory().deserialize(json, Library.class);

        return library;
    }

    public Library createLibrary(String relativePath) throws CausticException {
        Library library = getFactory().createLibrary(relativePath);
        //        if (library.exists())
        //            throw new CausticException("Library exists: " + library.getDirectory());
        setLibrary(library);
        library.create();
        return library;
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    private void initialize() {
        factory = new CaustkFactory(this);
        rack = RackProvider.createRack(this);
        RackProvider.setRack(rack);
    }
}
