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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teotigraphix.gdx.IGdxApplication;

/**
 * The {@link ApplicationComponentRegistery} manages a map of
 * {@link IGdxApplicationComponent}s for the application.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class ApplicationComponentRegistery {

    private final IGdxApplication application;

    private final Map<Class<? extends IGdxApplicationComponent>, IGdxApplicationComponent> map = new HashMap<Class<? extends IGdxApplicationComponent>, IGdxApplicationComponent>();

    private final List<IGdxApplicationComponent> listeners = new ArrayList<IGdxApplicationComponent>();

    private boolean attached = false;

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new model registry.
     */
    public ApplicationComponentRegistery(IGdxApplication application) {
        this.application = application;
    }

    /**
     * Whether the registry contains the class API instance.
     * 
     * @param clazz The model's class API key.
     */
    public boolean containsKey(Class<? extends IGdxApplicationComponent> clazz) {
        return map.containsKey(clazz);
    }

    /**
     * Returns the component registered against the clazz API, <code>null</code>
     * if not found.
     * 
     * @param clazz The component's class API key.
     */
    public <T extends IGdxApplicationComponent> T get(Class<T> clazz) {
        return clazz.cast(map.get(clazz));
    }

    /**
     * Register a model instance against a clazz API key.
     * 
     * @param clazz The model's class API key.
     * @param component The {@link IGdxApplicationComponent} instance.
     * @throws IllegalStateException Class type exists in registry
     * @see IGdxApplicationComponent#onAttach()
     */
    public void put(Class<? extends IGdxApplicationComponent> clazz,
            IGdxApplicationComponent component) {
        if (map.containsKey(clazz))
            throw new IllegalStateException("Class type exists in registry: " + clazz);
        map.put(clazz, component);
        listeners.add(component);
        if (component instanceof GdxModel)
            ((GdxModel)component).setApplication(application);
        if (attached)
            component.onAttach();
    }

    /**
     * Removes a model instance using the clazz API key.
     * 
     * @param clazz The model's class API key.
     * @see IGdxApplicationComponent#onDetach()
     */
    public IGdxApplicationComponent remove(Class<? extends IGdxApplicationComponent> clazz) {
        IGdxApplicationComponent removed = map.remove(clazz);
        if (removed != null) {
            listeners.remove(removed);
            if (removed instanceof GdxModel)
                ((GdxModel)removed).setApplication(null);
            removed.onDetach();
        }
        return removed;
    }

    /**
     * Attach the {@link ApplicationComponentRegistery} after the initial models
     * have been added.
     */
    public void attach() {
        for (IGdxApplicationComponent component : listeners) {
            component.onAttach();
        }
        attached = true;
    }

    /**
     * Detaches the {@link ApplicationComponentRegistery} from the application.
     */
    public void detach() {
        for (IGdxApplicationComponent component : listeners) {
            component.onDetach();
        }
        attached = false;
    }
}
