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

package com.teotigraphix.gdx.app;

import com.google.common.eventbus.EventBus;
import com.teotigraphix.gdx.IGdxApplication;

/**
 * The {@link GdxModel} is the base class for all application models held within
 * the {@link ApplicationComponentRegistery}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public abstract class GdxModel implements IGdxModel {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private EventBus eventBus;

    private IGdxApplication application;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // application
    //----------------------------------

    @Override
    public IGdxApplication getApplication() {
        return application;
    }

    protected final <T extends IGdxModel> T get(Class<T> clazz) {
        return application.get(clazz);
    }

    void setApplication(IGdxApplication application) {
        this.application = application;
    }

    //----------------------------------
    // eventBus
    //----------------------------------

    /**
     * The model's local {@link EventBus}.
     */
    @Override
    public final EventBus getEventBus() {
        return eventBus;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new mediator.
     */
    public GdxModel() {
        eventBus = new EventBus();
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public abstract void onAttach();

    @Override
    public abstract void onDetach();
}
