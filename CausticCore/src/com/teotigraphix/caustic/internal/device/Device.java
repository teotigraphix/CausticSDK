////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.internal.device;

import com.teotigraphix.caustic.core.ICausticEngine;
import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.internal.util.ExceptionUtils;
import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.IPersist;

/**
 * The default implementation of the {@link IDevice} interface.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public abstract class Device implements IDevice {

    //--------------------------------------------------------------------------
    //
    // Protected :: Variables
    //
    //--------------------------------------------------------------------------

    protected IPersist mPersistable;

    //--------------------------------------------------------------------------
    //
    // ICausticEngineAware API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // engine
    //----------------------------------

    private ICausticEngine mEngine;

    @Override
    public final ICausticEngine getEngine() {
        return mEngine;
    }

    public final void setEngine(ICausticEngine engine) {
        if (mEngine != null) {
            disposeEngine(mEngine);
        }
        mEngine = engine;
        initializeEngine(mEngine);
    }

    //--------------------------------------------------------------------------
    //
    // IDevice API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // index
    //----------------------------------

    private int mIndex;

    @Override
    public final int getIndex() {
        return mIndex;
    }

    public final void setIndex(int value) {
        mIndex = value;
    }

    //----------------------------------
    // id
    //----------------------------------

    private String mId;

    @Override
    public final String getId() {
        return mId;
    }

    public final void setId(String value) {
        mId = value;
    }

    //----------------------------------
    // name
    //----------------------------------

    private String mName;

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setName(String value) {
        mName = value;
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public Device() {
    }

    //--------------------------------------------------------------------------
    //
    // Implicit IPersist API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        mPersistable.copy(memento);
    }

    @Override
    public void paste(IMemento memento) {
        mPersistable.paste(memento);
    }

    //--------------------------------------------------------------------------
    //
    // IRestore API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void restore() {
    }

    //--------------------------------------------------------------------------
    //
    // IDispose API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void dispose() {
        disposeEngine(mEngine);
    }

    //--------------------------------------------------------------------------
    //
    // Protected :: Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Initializes the device using the current engine.
     * <p>
     * Subclasses will usually call creation methods or setup dealing with there
     * specific state from this method.
     * </p>
     * 
     * @see #createPersistable()
     */
    protected void initializeEngine(ICausticEngine engine) {
        mPersistable = createPersistable();
    }

    /**
     * Called when an engine instance is being switched out.
     * <p>
     * Sub components can use this call to remove listeners etc, from the old of
     * the engine.
     * </p>
     * 
     * @param engine The old engine to be disposed.
     */
    protected void disposeEngine(ICausticEngine engine) {
        mPersistable = null;
    }

    /**
     * Subclasses return their persistable to save and restore state with.
     */
    protected IPersist createPersistable() {
        return new IPersist() {
            @Override
            public void copy(IMemento memento) {
            }

            @Override
            public void paste(IMemento memento) {
            }
        };
    }

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
