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

package com.teotigraphix.caustk.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.node.CaustkFactory;
import com.teotigraphix.caustk.node.ICaustkNode;
import com.teotigraphix.caustk.node.Library;

/**
 * The {@link CaustkRuntime} encapsulates the {@link ISoundGenerator} and
 * {@link CaustkRack} creation and initialization.
 * <p>
 * Holds the single {@link CaustkRack} instance for an application session.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class CaustkRuntime implements ICaustkRuntime {

    private static CaustkRuntime instance;

    private ISoundGenerator soundGenerator;

    private CaustkLogger logger;

    private CaustkRack rack;

    private CaustkFactory factory;

    private Library library;

    ISoundGenerator getSoundGenerator() {
        return soundGenerator;
    }

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    /**
     * Returns the session {@link CaustkLogger} instance.
     */
    @Override
    public final ICaustkLogger getLogger() {
        return logger;
    }

    /**
     * Returns the session {@link CaustkRack} instance.
     */
    @Override
    public final CaustkRack getRack() {
        return rack;
    }

    /**
     * Returns the session {@link CaustkFactory} for created {@link ICaustkNode}
     * s.
     */
    @Override
    public final CaustkFactory getFactory() {
        return factory;
    }

    /**
     * The current library loaded for an application.
     */
    @Override
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
     * Creates a new runtime with {@link CaustkRack}.
     * 
     * @param soundGenerator The platform sound engine.
     */
    private CaustkRuntime(ISoundGenerator soundGenerator) {
        this.soundGenerator = soundGenerator;
        initialize();
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public Library loadLibrary(File file) throws CausticException, IOException {
        if (file.exists())
            throw new FileNotFoundException("Library does not exist: " + file);

        String json = FileUtils.readFileToString(file);
        library = getFactory().deserialize(json, Library.class);

        return library;
    }

    @Override
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
        System.out.println("CaustkRuntime : Create CaustkLogger");
        logger = new CaustkLogger();
        System.out.println("CaustkRuntime : Create CaustkFactory");
        factory = new CaustkFactory(this);
        System.out.println("CaustkRuntime : Create CaustkRack");
        rack = new CaustkRack(this);
    }

    //--------------------------------------------------------------------------
    // Public Static :: Methods
    //--------------------------------------------------------------------------

    /**
     * Creates the single instance of the runtime, the method must only be
     * called at application startup.
     * 
     * @param soundGenerator The platform sound generator.
     * @return The single instance of the CaustkRuntime
     */
    public static CaustkRuntime createInstance(ISoundGenerator soundGenerator) {
        if (instance != null)
            return instance;
        //        if (instance != null)
        //            throw new IllegalStateException(
        //                    "CaustkRuntime.createInstance() must be called only once");
        instance = new CaustkRuntime(soundGenerator);
        return instance;
    }

    /**
     * Returns the single instance of the {@link CaustkRuntime}.
     * <p>
     * {@link #createInstance(ISoundGenerator)} must be called before access to
     * this method.
     */
    public static CaustkRuntime getInstance() {
        if (instance == null)
            throw new IllegalStateException("CaustkRuntime.createInstance() must be called");
        return instance;
    }
}
