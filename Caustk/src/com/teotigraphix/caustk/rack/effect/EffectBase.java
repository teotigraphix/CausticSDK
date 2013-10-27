////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.rack.effect;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.EffectRackMessage;
import com.teotigraphix.caustk.machine.CaustkLibraryFactory;
import com.teotigraphix.caustk.rack.IEffect;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.utils.ExceptionUtils;

/**
 * @author Michael Schmalle
 */
public abstract class EffectBase implements IEffect {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private IRack rack;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private EffectType type;

    @Tag(1)
    private int toneIndex;

    @Tag(2)
    private int slot;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // type
    //----------------------------------

    @Override
    public final EffectType getType() {
        return type;
    }

    //----------------------------------
    // slot
    //----------------------------------

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public void setSlot(int value) {
        slot = value;
    }

    //----------------------------------
    // toneIndex
    //----------------------------------

    @Override
    public int getToneIndex() {
        return toneIndex;
    }

    @Override
    public void setToneIndex(int value) {
        toneIndex = value;
    }

    //--------------------------------------------------------------------------
    // IRackAware API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // rack
    //----------------------------------

    @Override
    public IRack getRack() {
        return rack;
    }

    @Override
    public void setRack(IRack rack) {
        this.rack = rack;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    EffectBase() {
    }

    /**
     * @see EffectFactory#create(EffectType, int, int)
     */
    public EffectBase(EffectType type, int slot, int toneIndex) {
        this.type = type;
        this.slot = slot;
        this.toneIndex = toneIndex;
    }

    //--------------------------------------------------------------------------
    // IRackSerializer API
    //--------------------------------------------------------------------------

    @Override
    public void load(CaustkLibraryFactory factory) {
    }

    @Override
    public void restore() {
    }

    @Override
    public void update() {
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Returns a float value for the {@link IEffectControl} parameter.
     * 
     * @param control The control to query.
     */
    protected final float get(IEffectControl control) {
        return EffectRackMessage.GET.query(rack, getToneIndex(), getSlot(), control.getControl());
    }

    /**
     * Sets a float value using the {@link IEffectControl} parameter on the
     * effect.
     * 
     * @param control The target control on the effect.
     * @param value The new float value for the control.
     */
    protected final void set(IEffectControl control, float value) {
        EffectRackMessage.SET.send(rack, getToneIndex(), getSlot(), control.getControl(), value);
    }

    /**
     * Sets a int value using the {@link IEffectControl} parameter on the
     * effect.
     * 
     * @param control The target control on the effect.
     * @param value The new int value for the control.
     */
    protected final void set(IEffectControl control, int value) {
        EffectRackMessage.SET.send(rack, getToneIndex(), getSlot(), control.getControl(), value);
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
}
