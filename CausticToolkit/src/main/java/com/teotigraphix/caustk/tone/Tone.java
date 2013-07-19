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

package com.teotigraphix.caustk.tone;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.teotigraphix.caustk.application.IDeviceFactory;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.components.ToneComponent;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.service.ISerializeService;

public class Tone implements ISerialize {

    private transient ICaustkController controller;

    int getComponentCount() {
        return internalComponents.size();
    }

    /**
     * Returns the core audio engine interface.
     */
    public ICausticEngine getEngine() {
        return controller;
    }

    /**
     * Returns the factory that creates all sub components of the audio system.
     */
    public IDeviceFactory getFactory() {
        return controller.getDeviceFactory();
    }

    public ICaustkController getController() {
        return controller;
    }

    private ToneType toneType;

    public final ToneType getToneType() {
        return toneType;
    }

    public final void setToneType(ToneType value) {
        toneType = value;
    }

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // id
    //----------------------------------

    private UUID id;

    /**
     * The tone's unique id within a session.
     * <p>
     * This id is assigned at creation of the tone or set when deserialized from
     * the sleep state (which will be it's original id when created).
     */
    public final UUID getId() {
        return id;
    }

    public final void setId(UUID value) {
        id = value;
    }

    //----------------------------------
    // index
    //----------------------------------

    private int index;

    /**
     * The index location of the tone loaded into/from the core rack.
     */
    public final int getIndex() {
        return index;
    }

    public final void setIndex(int value) {
        index = value;
    }

    //----------------------------------
    // name
    //----------------------------------

    private String name;

    /**
     * The name loaded into/from the core rack.
     */
    public final String getName() {
        return name;
    }

    public final void setName(String value) {
        name = value;
    }

    //--------------------------------------------------------------------------
    // Public Component API
    //--------------------------------------------------------------------------

    private transient Map<Class<? extends ToneComponent>, ToneComponent> internalComponents = new HashMap<Class<? extends ToneComponent>, ToneComponent>();

    private Map<String, String> components = new HashMap<String, String>();

    public void addComponent(Class<? extends ToneComponent> clazz, ToneComponent instance) {
        internalComponents.put(clazz, instance);
        instance.setTone(this);
    }

    public <T extends ToneComponent> T getComponent(Class<T> clazz) {
        return clazz.cast(internalComponents.get(clazz));
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Tone(ICaustkController controller) {
        this.controller = controller;
    }

    /**
     * Serializes the entire instance minus transient properties.
     * <p>
     * Returns the serialized state based on the current implementation of
     * {@link ISerializeService}.
     */
    public String serialize() {
        return controller.getSerializeService().toPrettyString(this);
    }

    //--------------------------------------------------------------------------
    // ISerialize API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void sleep() {
        components = new HashMap<String, String>();
        for (ToneComponent toneComponent : internalComponents.values()) {
            String data = toneComponent.serialize();
            String className = toneComponent.getClass().getName();
            components.put(className, data);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void wakeup(ICaustkController controller) {
        // the wakeup() method acts like the Constructor when the instance is deserialized
        this.controller = controller;
        internalComponents = new HashMap<Class<? extends ToneComponent>, ToneComponent>();
        for (Entry<String, String> entry : components.entrySet()) {
            String className = entry.getKey();
            String data = entry.getValue();
            Class<?> cls = null;
            try {
                cls = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            ToneComponent component = (ToneComponent)getController().getSerializeService()
                    .fromString(data, cls);
            addComponent((Class<? extends ToneComponent>)cls, component);
        }
    }

}
