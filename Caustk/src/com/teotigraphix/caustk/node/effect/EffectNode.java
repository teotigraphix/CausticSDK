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

import android.media.effect.Effect;

import com.teotigraphix.caustk.core.osc.EffectsRackMessage;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.EffectControl;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.IEffectControl;
import com.teotigraphix.caustk.core.osc.IOSCControl;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.utils.ExceptionUtils;

/**
 * The base effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see EffectNodeChangeEvent
 */
public class EffectNode extends NodeBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private Integer slot;

    private EffectType type;

    private boolean bypass = false;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // index
    //----------------------------------

    /**
     * Returns the owner machine's rack index.
     */
    @Override
    public Integer getIndex() {
        return index;
    }

    //----------------------------------
    // slot
    //----------------------------------

    /**
     * The effect's slot within the effect channel.
     */
    public Integer getSlot() {
        return slot;
    }

    void setSlot(Integer slot) {
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
     * @see EffectControl#Bypass
     */
    public boolean isBypass() {
        return bypass;
    }

    public boolean queryIsBypass() {
        return get(EffectControl.Bypass) == 0f ? false : true;
    }

    /**
     * @param bypass Whether to bypass the effect.
     * @see EffectControl#Bypass
     */
    public void setBypass(boolean bypass) {
        if (bypass == this.bypass)
            return;
        this.bypass = bypass;
        set(EffectControl.Bypass, bypass ? 1 : 0);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public EffectNode() {
    }

    public EffectNode(int slot, int machineIndex) {
        this.slot = slot;
        this.index = machineIndex;
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
        set(EffectControl.Bypass, bypass ? 1 : 0);
    }

    @Override
    protected void restoreComponents() {
        setBypass(queryIsBypass());
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Returns a float value for the {@link IEffectControl} parameter.
     * <p>
     * If the {@link #getRack()} is null, meaning it has not {@link Effect}
     * parent, the method will return {@link Float#NaN}.
     * 
     * @param control The control to query.
     */
    protected final float get(IEffectControl control) {
        return EffectsRackMessage.GET.query(getRack(), index, getSlot(), control.getControl());
    }

    /**
     * Sets a float value using the {@link IEffectControl} parameter on the
     * effect.
     * <p>
     * Will send the OSC message only if the {@link IEffect} has a
     * {@link Effect} parent.
     * 
     * @param control The target control on the effect.
     * @param value The new float value for the control.
     */
    protected final void set(IEffectControl control, float value) {
        EffectsRackMessage.SET.send(getRack(), index, getSlot(), control.getControl(), value);
        post(new EffectNodeChangeEvent(this, control, value));
    }

    /**
     * Sets a int value using the {@link IEffectControl} parameter on the
     * effect.
     * <p>
     * Will send the OSC message only if the {@link IEffect} has a
     * {@link Effect} parent.
     * 
     * @param control The target control on the effect.
     * @param value The new int value for the control.
     */
    protected final void set(IEffectControl control, int value) {
        set(control, (float)value);
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
     * @see EffectNode#set(IEffectControl, float)
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
