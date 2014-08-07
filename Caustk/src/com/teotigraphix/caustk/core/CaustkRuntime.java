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

    private static final String TAG = "CaustkRuntime";

    private static CaustkRuntime instance;

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private ISoundGenerator soundGenerator;

    private CaustkLogger logger;

    private CaustkRack rack;

    private CaustkFactory factory;

    //--------------------------------------------------------------------------
    // Internal :: Properties
    //--------------------------------------------------------------------------

    ISoundGenerator getSoundGenerator() {
        return soundGenerator;
    }

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    @Override
    public final ICaustkLogger getLogger() {
        return logger;
    }

    @Override
    public final CaustkRack getRack() {
        return rack;
    }

    @Override
    public final CaustkFactory getFactory() {
        return factory;
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
    // Private :: Methods
    //--------------------------------------------------------------------------

    private void initialize() {
        logger = new CaustkLogger();
        logger.log(TAG, "Create CaustkLogger");
        logger.log(TAG, "Create CaustkFactory");
        factory = new CaustkFactory(this);
        logger.log(TAG, "Create CaustkRack");
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
        if (instance == null)
            instance = new CaustkRuntime(soundGenerator);
        return instance;
    }

    /**
     * Returns the single instance of the {@link ICaustkRuntime}.
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
