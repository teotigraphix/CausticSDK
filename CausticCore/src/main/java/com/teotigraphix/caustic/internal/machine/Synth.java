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

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.machine.ISynth;
import com.teotigraphix.caustic.machine.ISynthComponent;

/**
 * The default implementation of the ISynth interface.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public abstract class Synth extends Machine implements ISynth
{

    //--------------------------------------------------------------------------
    //
    // ISynth API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // synth
    //----------------------------------

    private ISynthComponent mSynth;

    @Override
    public final ISynthComponent getSynth()
    {
        return mSynth;
    }

    public final void setSynth(ISynthComponent value)
    {
        mSynth = value;
    }

    //--------------------------------------------------------------------------
    //
    // ITone API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // enabled
    //----------------------------------

    private boolean mEnabled = false;

    public final boolean isEnabled()
    {
        return mEnabled;
    }

    public final void setEnabled(boolean value)
    {
        if (value == mEnabled)
            return;
        mEnabled = value;
        // firePropertyChange(TonePropertyKind.ENABLED, mEnabled);
    }

    //----------------------------------
    // muted
    //----------------------------------

    private boolean mMuted = false;

    public boolean isMuted()
    {
        return mMuted;
    }

    public void setMuted(boolean value)
    {
        if (value == mMuted)
            return;
        mMuted = value;
        // firePropertyChange(TonePropertyKind.MUTE, mMuted);
    }

    //----------------------------------
    // selected
    //----------------------------------

    private boolean mSelected = false;

    public boolean getSelected()
    {
        return mSelected;
    }

    public void setSelected(boolean value)
    {
        if (value == mSelected)
            return;
        mSelected = value;
        // firePropertyChange(TonePropertyKind.SELECTED, mSelected);
    }

    //----------------------------------
    // presetBank
    //----------------------------------

    private String mPresetBank;

    public final String getPresetBank()
    {
        return mPresetBank;
    }

    public final void setPresetBank(String value)
    {
        if (value == mPresetBank)
            return;
        mPresetBank = value;
        // firePropertyChange(TonePropertyKind.PRESET_BANK, mPresetBank);
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public Synth()
    {
        super();
    }

    //--------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Creates the synthComponent.
     */
    @Override
    protected void createComponents()
    {
        super.createComponents();
        setSynth(new SynthComponent(this));
    }

    @Override
    public void copy(IMemento memento)
    {
        super.copy(memento);
        mSynth.copy(memento.createChild(MachineConstants.TAG_SYNTH));
    }

    @Override
    public void paste(IMemento memento)
    {
        super.paste(memento);
        mSynth.paste(memento.getChild(MachineConstants.TAG_SYNTH));
    }

    @Override
    public void restore()
    {
        super.restore();
        mSynth.restore();
    }

}
