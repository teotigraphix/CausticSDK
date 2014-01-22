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

import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.gdx.IGdxApplication;

/**
 * The {@link ModelRegistry} manages a map of {@link IGdxModel}s for the
 * application.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class ModelRegistry {

    private final IGdxApplication application;

    private final Map<Class<? extends IGdxModel>, IGdxModel> map = new HashMap<Class<? extends IGdxModel>, IGdxModel>();

    private boolean attached = false;

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new model registry.
     */
    public ModelRegistry(IGdxApplication application) {
        this.application = application;
    }

    /**
     * Whether the registry contains the class API instance.
     * 
     * @param clazz The model's class API key.
     */
    public boolean containsKey(Class<? extends IGdxModel> clazz) {
        return map.containsKey(clazz);
    }

    /**
     * Returns the model registered against the clazz API, <code>null</code> if
     * not found.
     * 
     * @param clazz The model's class API key.
     */
    public <T extends IGdxModel> T get(Class<T> clazz) {
        return clazz.cast(map.get(clazz));
    }

    /**
     * Register a model instance against a clazz API key.
     * 
     * @param clazz The model's class API key.
     * @param model The {@link IGdxModel} instance.
     * @throws IllegalStateException Class type exists in registry
     * @see IGdxModel#onAttach()
     */
    public void put(Class<? extends IGdxModel> clazz, IGdxModel model) {
        if (map.containsKey(clazz))
            throw new IllegalStateException("Class type exists in registry: " + clazz);
        map.put(clazz, model);
        ((GdxModel)model).setApplication(application);
        if (attached)
            model.onAttach();
    }

    /**
     * Removes a model instance using the clazz API key.
     * 
     * @param clazz The model's class API key.
     * @see IGdxModel#onDetach()
     */
    public IGdxModel remove(Class<? extends IGdxModel> clazz) {
        IGdxModel removed = map.remove(clazz);
        if (removed != null) {
            ((GdxModel)removed).setApplication(null);
            removed.onDetach();
        }
        return removed;
    }

    /**
     * Attach the {@link ModelRegistry} after the initial models have been
     * added.
     */
    public void attach() {
        for (IGdxModel model : map.values()) {
            model.onAttach();
        }
        attached = true;
    }

    /**
     * Detaches the {@link ModelRegistry} from the application.
     */
    public void detach() {
        for (IGdxModel model : map.values()) {
            model.onDetach();
        }
        attached = false;
    }
}
