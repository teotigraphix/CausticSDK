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
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.core.components.PatternSequencerComponent;
import com.teotigraphix.caustk.core.components.PatternSequencerComponent.Resolution;
import com.teotigraphix.caustk.core.components.SynthComponent;
import com.teotigraphix.caustk.core.components.ToneComponent;
import com.teotigraphix.caustk.library.SoundSourceState;
import com.teotigraphix.caustk.sequencer.SystemSequencer;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.service.ISerializeService;

public abstract class Tone implements ISerialize, IRestore {

    private transient ICaustkController controller;

    @SuppressWarnings("unused")
    private ToneType toneType;

    //    public VolumeComponent getVolume() {
    //        return getComponent(VolumeComponent.class);
    //    }

    public SynthComponent getSynth() {
        return getComponent(SynthComponent.class);
    }

    public PatternSequencerComponent getPatternSequencer() {
        return getComponent(PatternSequencerComponent.class);
    }

    //----------------------------------
    // enabled
    //----------------------------------

    private boolean enabled = false;

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean value) {
        if (value == enabled)
            return;
        enabled = value;
        // firePropertyChange(TonePropertyKind.ENABLED, mEnabled);
    }

    //----------------------------------
    // muted
    //----------------------------------

    private boolean muted = false;

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean value) {
        if (value == muted)
            return;
        muted = value;
        // firePropertyChange(TonePropertyKind.MUTE, mMuted);
    }

    //----------------------------------
    // presetBank
    //----------------------------------

    private String presetBank;

    public final String getPresetBank() {
        return presetBank;
    }

    public final void setPresetBank(String value) {
        if (value == presetBank)
            return;
        presetBank = value;
        // firePropertyChange(TonePropertyKind.PRESET_BANK, mPresetBank);
    }

    //----------------------------------
    // selected
    //----------------------------------

    private boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean value) {
        if (value == selected)
            return;
        selected = value;
        // firePropertyChange(TonePropertyKind.SELECTED, mSelected);
    }

    /**
     * Called from the {@link SystemSequencer} in the triggerOn observer.
     * 
     * @param step
     * @param pitch
     * @param gate
     * @param velocity
     * @param flags
     */
    public void _triggerOn(int step, int pitch, float gate, float velocity, int flags) {
        getComponent(PatternSequencerComponent.class).triggerOn(Resolution.SIXTEENTH, step, pitch,
                gate, velocity, flags);
    }

    /**
     * Called from the {@link SystemSequencer} in the triggerOff observer.
     * 
     * @param step
     * @param pitch
     */
    public void _triggerOff(int step, int pitch) {
        getComponent(PatternSequencerComponent.class).triggerOff(Resolution.SIXTEENTH, step, pitch);
    }

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

    public abstract ToneType getToneType();

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

    //----------------------------------
    // defaultPatchId
    //----------------------------------

    private UUID defaultPatchId;

    /**
     * If loaded from the library as a {@link SoundSourceState} item, will point
     * to the patch that was created when the tone was serialized.
     */
    public UUID getDefaultPatchId() {
        return defaultPatchId;
    }

    public void setDefaultPatchId(UUID value) {
        defaultPatchId = value;
    }

    //--------------------------------------------------------------------------
    // Public Component API
    //--------------------------------------------------------------------------

    private transient Map<Class<? extends ToneComponent>, ToneComponent> internalComponents = new HashMap<Class<? extends ToneComponent>, ToneComponent>();

    private Map<String, Map<String, String>> components = new HashMap<String, Map<String, String>>();

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
        toneType = getToneType();
        components = new HashMap<String, Map<String, String>>();
        for (ToneComponent toneComponent : internalComponents.values()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String className = toneComponent.getClass().getName();
            if (toneComponent instanceof ISerialize) {
                String data = toneComponent.serialize();
                map.put("state", data);
            }
            components.put(className, map);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void wakeup(ICaustkController controller) {
        // the wakeup() method acts like the Constructor when the instance is deserialized
        this.controller = controller;
        internalComponents = new HashMap<Class<? extends ToneComponent>, ToneComponent>();
        for (Entry<String, Map<String, String>> entry : components.entrySet()) {
            String className = entry.getKey();
            Map<String, String> component = entry.getValue();
            String data = "";
            Class<?> cls = null;
            try {
                cls = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (component.containsKey("state")) {
                data = component.get("state");
            }

            ToneComponent instance = (ToneComponent)getController().getSerializeService()
                    .fromString(data, cls);
            if (instance == null) {
                try {
                    instance = (ToneComponent)cls.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            addComponent((Class<? extends ToneComponent>)cls, instance);
        }
    }

    @Override
    public void restore() {
        for (ToneComponent toneComponent : internalComponents.values()) {
            toneComponent.restore();
        }
    }

}
