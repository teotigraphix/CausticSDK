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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.controller.IRackSerializer;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.utils.ExceptionUtils;

/**
 * The {@link RackToneComponent} is a sub component held on a {@link RackTone}.
 * <p>
 * Using the {@link RackTone#getComponent(Class)} will return the component that
 * was registered under the class type API passed, null if the component dosn't
 * exist.
 * 
 * @author Michael Schmalle
 */
public abstract class RackToneComponent implements IRackSerializer {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private RackTone rackTone;

    //----------------------------------
    // tone
    //----------------------------------

    public RackTone getTone() {
        return rackTone;
    }

    public void setTone(RackTone value) {
        rackTone = value;
    }

    protected final int getToneIndex() {
        return rackTone.getMachineIndex();
    }

    protected final ICausticEngine getEngine() {
        return rackTone.getEngine();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public RackToneComponent() {
    }

    @Override
    public final void create(ICaustkApplicationContext context) throws CausticException {
        createComponents();
    }

    @Override
    public final void load(ICaustkApplicationContext context) throws CausticException {
        loadComponents();
        restore();
    }

    @Override
    public final void update(ICaustkApplicationContext context) {
        updateComponents();
    }

    @Override
    public final void restore() {
        restoreComponents();
    }

    @Override
    public void disconnect() {
    }

    protected void createComponents() {
    }

    /**
     * {@link #restore()} will be called on the component after this method is
     * called.
     * <p>
     * Most components will just implement {@link #restore()}, and the load
     * algorithm will mean the same as restore. If a component uses composite
     * components, they will need to be created.
     */
    protected void loadComponents() {
    }

    /**
     * Components will use raw {@link RackMessage}s to update the native core
     * with existing instance state.
     * <p>
     * <strong>NOTE:</strong> This method is implemented in about 1/2 of the
     * components, for now using presets is the only option since we cannot yet
     * have access to the .wav samples in the machines from the core. Once .wav
     * data is available, this implementation will be finished.
     */
    // TODO Once .wav samples are available in the native core, finish update() impl
    /*abstract*/protected void updateComponents() {
    }

    /**
     * Components will use raw {@link RackMessage}s query values to update the
     * existing instance state.
     */
    protected abstract void restoreComponents();

    /**
     * Returns a new {@link IllegalArgumentException} for an error in OSC range.
     * 
     * @param control The OSC control involved.
     * @param range The accepted range.
     * @param value The value that is throwing the range exception.
     * @return A new {@link IllegalArgumentException}.
     */
    protected final RuntimeException newRangeException(String control, String range, Object value) {
        return ExceptionUtils.newRangeException(control, range, value);
    }
}
