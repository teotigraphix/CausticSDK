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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.tone.components.PatternSequencerComponent;
import com.teotigraphix.caustk.tone.components.SynthComponent;

public abstract class Tone implements IRestore, Serializable {

    private static final long serialVersionUID = 2917863803738244084L;

    private transient ICaustkController controller;

    public void setController(ICaustkController controller) {
        this.controller = controller;

        for (ToneComponent toneComponent : components.values()) {
            toneComponent.setTone(this);
        }
    }

    public ICaustkController getController() {
        return controller;
    }

    private final ToneType toneType;

    public final ToneType getToneType() {
        return toneType;
    }

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
        controller.getRack().getMixerChannel(getIndex()).setMute(muted);
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

    int getComponentCount() {
        return components.size();
    }

    /**
     * Returns the core audio engine interface.
     */
    public ICausticEngine getEngine() {
        return controller;
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

    private Map<Class<? extends ToneComponent>, ToneComponent> components = new HashMap<Class<? extends ToneComponent>, ToneComponent>();

    public void addComponent(Class<? extends ToneComponent> clazz, ToneComponent instance) {
        components.put(clazz, instance);
        instance.setTone(this);
    }

    public <T extends ToneComponent> T getComponent(Class<T> clazz) {
        return clazz.cast(components.get(clazz));
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Tone(ToneType toneType, ICaustkController controller) {
        this.toneType = toneType;
        this.controller = controller;
    }

    //--------------------------------------------------------------------------
    // ISerialize API :: Methods
    //--------------------------------------------------------------------------
    //
    //    @Override
    //    public void sleep() {
    //        components = new HashMap<String, Map<String, String>>();
    //        for (ToneComponent toneComponent : internalComponents.values()) {
    //            HashMap<String, String> map = new HashMap<String, String>();
    //            String className = toneComponent.getClass().getName();
    //            if (toneComponent instanceof ISerialize) {
    //                String data = getController().getSerializeService().toString(toneComponent);
    //                map.put("state", data);
    //            }
    //            components.put(className, map);
    //        }
    //    }
    //
    //    @SuppressWarnings("unchecked")
    //    @Override
    //    public void wakeup(ICaustkController controller) {
    //        // the wakeup() method acts like the Constructor when the instance is deserialized
    //        this.controller = controller;
    //        internalComponents = new HashMap<Class<? extends ToneComponent>, ToneComponent>();
    //        for (Entry<String, Map<String, String>> entry : components.entrySet()) {
    //            String className = entry.getKey();
    //            Map<String, String> component = entry.getValue();
    //            String data = "";
    //            Class<?> cls = null;
    //            try {
    //                cls = Class.forName(className);
    //            } catch (ClassNotFoundException e) {
    //                e.printStackTrace();
    //            }
    //
    //            if (component.containsKey("state")) {
    //                data = component.get("state");
    //            }
    //
    //            ToneComponent instance = (ToneComponent)getController().getSerializeService()
    //                    .fromString(data, cls);
    //            if (instance == null) {
    //                try {
    //                    instance = (ToneComponent)cls.newInstance();
    //                } catch (InstantiationException e) {
    //                    e.printStackTrace();
    //                } catch (IllegalAccessException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //            addComponent((Class<? extends ToneComponent>)cls, instance);
    //        }
    //    }

    @Override
    public void restore() {
        for (ToneComponent toneComponent : components.values()) {
            toneComponent.restore();
        }
    }

}
