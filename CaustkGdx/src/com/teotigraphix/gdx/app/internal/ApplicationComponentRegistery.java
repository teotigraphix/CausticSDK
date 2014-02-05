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

package com.teotigraphix.gdx.app.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teotigraphix.gdx.app.Model;
import com.teotigraphix.gdx.app.IApplication;
import com.teotigraphix.gdx.app.IApplicationComponent;


/**
 * The {@link ApplicationComponentRegistery} manages a map of
 * {@link IApplicationComponent}s for the application.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class ApplicationComponentRegistery {

    private final IApplication application;

    private final Map<Class<? extends IApplicationComponent>, IApplicationComponent> map = new HashMap<Class<? extends IApplicationComponent>, IApplicationComponent>();

    private final List<IApplicationComponent> listeners = new ArrayList<IApplicationComponent>();

    private boolean attached = false;

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new model registry.
     */
    public ApplicationComponentRegistery(IApplication application) {
        this.application = application;
    }

    /**
     * Whether the registry contains the class API instance.
     * 
     * @param clazz The model's class API key.
     */
    public boolean containsKey(Class<? extends IApplicationComponent> clazz) {
        return map.containsKey(clazz);
    }

    /**
     * Returns the component registered against the clazz API, <code>null</code>
     * if not found.
     * 
     * @param clazz The component's class API key.
     */
    public <T extends IApplicationComponent> T get(Class<T> clazz) {
        return clazz.cast(map.get(clazz));
    }

    /**
     * Register a model instance against a clazz API key.
     * 
     * @param clazz The model's class API key.
     * @param component The {@link IApplicationComponent} instance.
     * @throws IllegalStateException Class type exists in registry
     * @see IApplicationComponent#onAwake()
     */
    public void put(Class<? extends IApplicationComponent> clazz,
            IApplicationComponent component) {
        if (map.containsKey(clazz))
            throw new IllegalStateException("Class type exists in registry: " + clazz);
        map.put(clazz, component);
        listeners.add(component);
        if (component instanceof Model)
            ((Model)component).setApplication(application);
        if (attached)
            component.onAwake();
    }

    /**
     * Removes a model instance using the clazz API key.
     * 
     * @param clazz The model's class API key.
     * @see IApplicationComponent#onDestroy()
     */
    public IApplicationComponent remove(Class<? extends IApplicationComponent> clazz) {
        IApplicationComponent removed = map.remove(clazz);
        if (removed != null) {
            listeners.remove(removed);
            if (removed instanceof Model)
                ((Model)removed).setApplication(null);
            removed.onDestroy();
        }
        return removed;
    }

    /**
     * Attach the {@link ApplicationComponentRegistery} after the initial models
     * have been added.
     */
    public void awake() {
        for (IApplicationComponent component : listeners) {
            component.onAwake();
        }
        attached = true;
    }

    /**
     * Detaches the {@link ApplicationComponentRegistery} from the application.
     */
    public void destroy() {
        for (IApplicationComponent component : listeners) {
            component.onDestroy();
        }
        attached = false;
    }
}
