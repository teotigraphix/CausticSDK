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

package com.teotigraphix.caustk.node.effect;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.EffectControls;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage;
import com.teotigraphix.caustk.core.osc.IEffectControl;
import com.teotigraphix.caustk.core.osc.IOSCControl;
import com.teotigraphix.caustk.core.osc.OSCControlsMap;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.NodeBaseEvents.NodeEvent;
import com.teotigraphix.caustk.node.NodeMetaData;
import com.teotigraphix.caustk.node.machine.MachineChannel;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.utils.core.ExceptionUtils;

/**
 * The base effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see com.teotigraphix.caustk.node.effect.EffectNode.EffectNodeChangeEvent
 */
public abstract class EffectNode extends MachineChannel {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private int slot;

    @Tag(101)
    private EffectType type;

    @Tag(102)
    private boolean bypass = false;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // slot
    //----------------------------------

    /**
     * The effect's slot within the effect channel.
     */
    public int getSlot() {
        return slot;
    }

    void setSlot(int slot) {
        this.slot = slot;
    }

    //----------------------------------
    // type
    //----------------------------------

    /**
     * The {@link EffectType} of the effect.
     */
    public EffectType getType() {
        return type;
    }

    void setType(EffectType type) {
        this.type = type;
    }

    //----------------------------------
    // bypass
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Global_Bypass
     */
    public boolean isBypass() {
        return bypass;
    }

    public boolean queryIsBypass() {
        return get(EffectControls.Global_Bypass) == 0f ? false : true;
    }

    public void setBypass(float bypass) {
        setBypass(bypass == 0f ? false : true);
    }

    /**
     * @param bypass Whether to bypass the effect.
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Global_Bypass
     */
    public void setBypass(boolean bypass) {
        if (bypass == this.bypass)
            return;
        this.bypass = bypass;
        set(EffectControls.Global_Bypass, bypass ? 1 : 0);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public EffectNode() {
    }

    public EffectNode(MachineNode machineNode, int slot) {
        super(machineNode);
        this.slot = slot;
        setData(new NodeMetaData(this));
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void updateComponents() {
        set(EffectControls.Global_Bypass, bypass ? 1 : 0);
    }

    @Override
    protected void restoreComponents() {
        setBypass(queryIsBypass());
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Returns a float value for the
     * {@link com.teotigraphix.caustk.core.osc.IEffectControl} parameter.
     * <p>
     * If the {@link #getRack()} is null, meaning it has not
     * {@link android.media.effect.Effect} parent, the method will return
     * {@link Float#NaN}.
     * 
     * @param control The control to query.
     */
    protected final float get(IEffectControl control) {
        return EffectsRackMessage.GET.query(getRack(), getMachineIndex(), getSlot(),
                control.getControl());
    }

    /**
     * Sets a float value using the
     * {@link com.teotigraphix.caustk.core.osc.IEffectControl} parameter on the
     * effect.
     * <p>
     * Will send the OSC message only if the {@link IEffect} has a
     * {@link android.media.effect.Effect} parent.
     * 
     * @param control The target control on the effect.
     * @param value The new float value for the control.
     */
    protected final void set(IEffectControl control, float value) {
        EffectsRackMessage.SET.send(getRack(), getMachineIndex(), getSlot(), control.getControl(),
                value);
        post(new EffectNodeChangeEvent(this, control, value));
    }

    /**
     * Sets a int value using the
     * {@link com.teotigraphix.caustk.core.osc.IEffectControl} parameter on the
     * effect.
     * <p>
     * Will send the OSC message only if the {@link IEffect} has a
     * {@link android.media.effect.Effect} parent.
     * 
     * @param control The target control on the effect.
     * @param value The new int value for the control.
     */
    protected final void set(IEffectControl control, int value) {
        set(control, (float)value);
    }

    /**
     * Invokes the {@link com.teotigraphix.caustk.core.osc.IEffectControl}'s
     * setter.
     * 
     * @param control The control.
     * @param value the valid value for the property.
     */
    public void invoke(IEffectControl control, float value) {
        OSCControlsMap.setValue(this, control, value);
    }

    /**
     * Returns a new {@link IllegalArgumentException} for an error in OSC range.
     * 
     * @param control The OSC control involved.
     * @param range The accepted range.
     * @param value The value that is throwing the range exception.
     * @return A new {@link IllegalArgumentException}.
     */
    protected final RuntimeException newRangeException(IEffectControl control, String range,
            Object value) {
        return ExceptionUtils.newRangeException(control.getControl(), range, value);
    }

    //--------------------------------------------------------------------------
    // Events
    //--------------------------------------------------------------------------

    /**
     * Events dispatched from the {@link EffectNode}.
     * 
     * @author Michael Schmalle
     * @since 1.0
     */
    public static abstract class EffectNodeEvent extends NodeEvent {
        public EffectNodeEvent(NodeBase target) {
            super(target);
        }

        public EffectNodeEvent(NodeBase target, IOSCControl control) {
            super(target, control);
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see EffectNode#set(com.teotigraphix.caustk.core.osc.IEffectControl,
     *      float)
     */
    public static class EffectNodeChangeEvent extends EffectNodeEvent {

        private float value;

        public float getValue() {
            return value;
        }

        public EffectNodeChangeEvent(NodeBase target, IEffectControl control, float value) {
            super(target, control);
            this.value = value;
        }
    }
}
