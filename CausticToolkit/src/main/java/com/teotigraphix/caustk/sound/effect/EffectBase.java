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

package com.teotigraphix.caustk.sound.effect;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.osc.EffectRackMessage;
import com.teotigraphix.caustk.sound.IEffect;
import com.teotigraphix.caustk.utils.ExceptionUtils;

public abstract class EffectBase implements IEffect {

    private transient ICaustkController controller;

    //----------------------------------
    // type
    //----------------------------------

    private EffectType type;

    public final EffectType getType() {
        return type;
    }

    //----------------------------------
    // toneIndex
    //----------------------------------

    private int toneIndex;

    public int getToneIndex() {
        return toneIndex;
    }

    //----------------------------------
    // slot
    //----------------------------------

    private int slot;

    public int getSlot() {
        return slot;
    }

    protected void setSlot(int value) {
        slot = value;
    }

    public EffectBase(EffectType type, int slot, int toneIndex) {
        this.type = type;
        this.slot = slot;
        this.toneIndex = toneIndex;
    }

    /**
     * /caustic/effects_rack/[machine_index]/[slot]/[param]
     * 
     * @param control
     * @return
     */
    protected final float get(IEffectControl control) {
        return EffectRackMessage.GET.query(controller, getToneIndex(), getSlot(),
                control.getControl());
    }

    /**
     * /caustic/effects_rack/[machine_index]/[slot]/[param] [value]
     * 
     * @param control
     * @param value
     */
    protected final void set(IEffectControl control, float value) {
        EffectRackMessage.SET.send(controller, getToneIndex(), getSlot(), control.getControl(),
                value);
    }

    protected final void set(IEffectControl control, int value) {
        EffectRackMessage.SET.send(controller, getToneIndex(), getSlot(), control.getControl(),
                value);
    }

    @Override
    public void restore() {
    }

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
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
