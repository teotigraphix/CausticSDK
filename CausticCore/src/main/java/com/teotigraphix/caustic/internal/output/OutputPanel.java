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

package com.teotigraphix.caustic.internal.output;

import com.teotigraphix.caustic.internal.device.Device;
import com.teotigraphix.caustic.osc.OutputPanelMessage;
import com.teotigraphix.caustic.output.IOutputPanel;

/**
 * The default implementation of the IOutputPanel interface.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class OutputPanel extends Device implements IOutputPanel
{

    private static final int STOP = 0;

    private static final int PLAY = 1;

    //private static final String ATT_MODE = "mode";

    //private static final String ATT_BPM = "bpm";

    private boolean mIsPlaying = false;

    int mMinBPM = 60;

    int mMaxBPM = 250;

    //--------------------------------------------------------------------------
    //
    // IOutputPanel API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // bmp
    //----------------------------------

    private float mBPM = 120.0f;

    @Override
    public float getBPM()
    {
        return mBPM;
    }

    public float getBPM(boolean restore)
    {
        return OutputPanelMessage.BPM.query(getEngine());
    }

    @Override
    public void setBPM(float value)
    {
        if (value == mBPM)
            return;
        if (value < 60.0f || value > 250.0f)
            throw newRangeException("bpm", "60.0..250.0", value);
        mBPM = value;
        OutputPanelMessage.BPM.send(getEngine(), mBPM);
    }

    //----------------------------------
    // mode
    //----------------------------------

    private Mode mMode = Mode.PATTERN;

    @Override
    public Mode getMode()
    {
        return mMode;
    }

    public Mode getMode(boolean restore)
    {
        return Mode.toType(OutputPanelMessage.MODE.query(getEngine()));
    }

    @Override
    public void setMode(Mode value)
    {
        mMode = value;
        OutputPanelMessage.MODE.send(getEngine(), mMode.getValue());
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    public OutputPanel()
    {
        super();
        setId(OutputPanelConstants.DEVICE_ID);
    }

    //--------------------------------------------------------------------------
    //
    // IOutputPanel API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public boolean isPlaying()
    {
        return mIsPlaying;
    }

    @Override
    public void play()
    {
        if (mIsPlaying)
            return;

        mIsPlaying = true;
        OutputPanelMessage.PLAY.send(getEngine(), PLAY);
    }

    @Override
    public void stop()
    {
        mIsPlaying = false;
        OutputPanelMessage.PLAY.send(getEngine(), STOP);
    }

    @Override
    public void restore()
    {
        setBPM(getBPM(true));
        setMode(getMode(true));
    }
}
