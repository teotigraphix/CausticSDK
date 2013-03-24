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

package com.teotigraphix.caustic.internal.machine;

import com.teotigraphix.caustic.core.ICausticEngine;
import com.teotigraphix.caustic.internal.device.Device;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.osc.MachineMessage;
import com.teotigraphix.caustic.rack.IRackFactory;
import com.teotigraphix.caustic.sequencer.IPatternSequencer;
import com.teotigraphix.common.IMemento;

/**
 * The default implementation of the {@link IMachine} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public abstract class Machine extends Device implements IMachine
{

    private String mAbsolutePresetPath;

    //--------------------------------------------------------------------------
    //
    // IMachine API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // factory
    //----------------------------------

    private IRackFactory mFactory;

    @Override
    public IRackFactory getFactory()
    {
        return mFactory;
    }

    @Override
    public void setFactory(IRackFactory value)
    {
        mFactory = value;
    }

    //----------------------------------
    // sequencer
    //----------------------------------

    private IPatternSequencer mSequencer;

    @Override
    public final IPatternSequencer getSequencer()
    {
        return mSequencer;
    }

    public final void setSequencer(IPatternSequencer value)
    {
        mSequencer = value;
    }

    //--------------------------------------------------------------------------
    //
    // IPreset API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // type
    //----------------------------------

    private MachineType mType;

    @Override
    public final MachineType getType()
    {
        return mType;
    }

    public final void setType(MachineType type)
    {
        mType = type;
    }

    //----------------------------------
    // presetName
    //----------------------------------

    @Override
    public String getPresetName()
    {
        return MachineMessage.QUERY_PRESET.queryString(getEngine(), getIndex());
    }

    @Override
    public String getPresetPath()
    {
        return mAbsolutePresetPath;
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public Machine()
    {
        super();
    }

    //--------------------------------------------------------------------------
    //
    // IMachine API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void loadPreset(String path)
    {
        mAbsolutePresetPath = path;
        MachineMessage.LOAD_PRESET.send(getEngine(), getIndex(), path);
    }

    @Override
    public void savePreset(String name)
    {
        // calculate the preset path
        MachineMessage.SAVE_PRESET.send(getEngine(), getIndex(), name);
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento)
    {
        memento.putString(MachineConstants.ATT_ID, getId());
        memento.putString(MachineConstants.ATT_NAME, getName());
        memento.putInteger(MachineConstants.ATT_INDEX, getIndex());
        memento.putString(MachineConstants.ATT_TYPE, getType().getValue());
        String path = getPresetPath();
        if (path != null)
            path = "";
        memento.putString(MachineConstants.ATT_PRESET_PATH, path);
        // a client will copy the sequencer on a required basis
        // most mementos will not contain sequencer data, only the Pattern
        // does right now, of course any combination can be used by the client
        // mSequencer.copy(memento.createChild(MachineConstants.TAG_SEQUENCER));
    }

    @Override
    public void paste(IMemento memento)
    {
        // mSequencer.paste(memento.getChild(MachineConstants.TAG_SEQUENCER));
        setName(memento.getString(MachineConstants.ATT_NAME));
        mAbsolutePresetPath = memento
                .getString(MachineConstants.ATT_PRESET_PATH);
    }

    //--------------------------------------------------------------------------
    //
    // IRestore API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void restore()
    {
        mSequencer.restore();
    }

    //--------------------------------------------------------------------------
    //
    // IDispose API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void dispose()
    {
        super.dispose();
        if (mSequencer != null)
            mSequencer.dispose();
        setSequencer(null);
    }

    //--------------------------------------------------------------------------
    //
    // Overridden Public :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public String toString()
    {
        return "Machine [" + getIndex() + "]" + getId() + ":" + getType();
    }

    //--------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Calls createComponents().
     * 
     * @see #createComponents()
     */
    @Override
    protected void initializeEngine(ICausticEngine engine)
    {
        super.initializeEngine(engine);
        createComponents();
    }

    //--------------------------------------------------------------------------
    //
    // Protected :: Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Creates the machine's internal components.
     * 
     * @see #setSequencer(IPatternSequencer)
     */
    protected void createComponents()
    {
        setSequencer(mFactory.createPatternSequencer(this));
    }
}
