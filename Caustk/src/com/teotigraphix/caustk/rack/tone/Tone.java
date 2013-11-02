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

package com.teotigraphix.caustk.rack.tone;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.machine.Machine;
import com.teotigraphix.caustk.machine.MixerPreset;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent;
import com.teotigraphix.caustk.rack.tone.components.SynthComponent;

/**
 * The base class for all tone's that wrap a native Caustic machine.
 * 
 * @author Michael Schmalle
 */
public abstract class Tone implements IRestore {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private Machine machine;

    @Tag(1)
    private Map<Class<? extends ToneComponent>, ToneComponent> components = new HashMap<Class<? extends ToneComponent>, ToneComponent>();

    @Tag(2)
    private UUID id;

    @Tag(3)
    private String name = "";

    @Tag(4)
    private ToneType toneType;

    @Tag(5)
    private int index;

    @Tag(6)
    private boolean enabled = false;

    @Tag(7)
    private boolean muted = false;

    @Tag(8)
    private String presetBank;

    @Tag(9)
    private boolean selected = false;

    @Tag(10)
    private UUID defaultPatchId;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    public final ToneType getToneType() {
        return toneType;
    }

    public MixerPreset getMixer() {
        return machine.getMixer();
    }

    public SynthComponent getSynth() {
        return getComponent(SynthComponent.class);
    }

    public PatternSequencerComponent getPatternSequencer() {
        return getComponent(PatternSequencerComponent.class);
    }

    //----------------------------------
    // enabled
    //----------------------------------

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

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean value) {
        if (value == muted)
            return;
        muted = value;
        // firePropertyChange(TonePropertyKind.MUTE, mMuted);
        getMixer().setMute(muted);
    }

    //----------------------------------
    // presetBank
    //----------------------------------

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
    public final ICausticEngine getEngine() {
        return machine.getRackSet().getRack();
    }

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // id
    //----------------------------------

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

    /**
     * The name loaded into/from the core rack.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the native machine name in the caustic rack.
     * 
     * @param restore Retrieve the native machine name.
     */
    public final String getName(boolean restore) {
        return RackMessage.QUERY_MACHINE_NAME.queryString(getEngine(), index);
    }

    /**
     * Sets the new name of the tone, will send the
     * {@link RackMessage#MACHINE_NAME} message to the core.
     * 
     * @param value The new name of the tone, 10 character limit, cannot be
     *            <code>null</code>.
     */
    public final void setName(String value) {
        if (value.equals(name))
            return;
        setNameInternal(value);
        RackMessage.MACHINE_NAME.send(getEngine(), index, name);
    }

    void setNameInternal(String value) {
        name = value;
    }

    //----------------------------------
    // defaultPatchId
    //----------------------------------

    /**
     * If loaded from the library as a {@link ToneDescriptor} item, will point
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

    /**
     * Adds a {@link ToneComponent} to the tone's component map and sets the
     * component's tone reference.
     * 
     * @param clazz The component API class.
     * @param instance The component instance.
     */
    void addComponent(Class<? extends ToneComponent> clazz, ToneComponent instance) {
        components.put(clazz, instance);
        instance.setTone(this);
    }

    /**
     * Returns a {@link ToneComponent} by class type.
     * 
     * @param clazz The component API class.
     */
    public <T extends ToneComponent> T getComponent(Class<T> clazz) {
        return clazz.cast(components.get(clazz));
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    Tone() {
    }

    public Tone(Machine machine, ToneType toneType) {
        this.machine = machine;
        this.toneType = toneType;
    }

    //--------------------------------------------------------------------------
    // IRestore API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void restore() {
        setNameInternal(getName(true));
        for (ToneComponent toneComponent : components.values()) {
            toneComponent.restore();
        }
    }
}
