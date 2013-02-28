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

package com.teotigraphix.caustic.internal.sequencer;

import com.teotigraphix.caustic.sequencer.IPhrase;
import com.teotigraphix.caustic.sequencer.ITrigger;
import com.teotigraphix.caustic.sequencer.IPhrase.Resolution;
import com.teotigraphix.caustic.sequencer.data.TriggerData;
import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.utils.MementoUtil;

/**
 * The default implementation of the ITrigger interface.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class Trigger implements ITrigger {

    private static final String SPACE = " ";

    private static final String ATT_SELECTED = "selected";

    private static final String ATT_PARAMETERS = "parameters";

    private static final String TAG_DATA = "data";

    //--------------------------------------------------------------------------
    //
    // ITrigger API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // phrase
    //----------------------------------

    private IPhrase mPhrase;

    @Override
    public IPhrase getPhrase() {
        return mPhrase;
    }

    void setPhrase(IPhrase value) {
        mPhrase = value;
    }

    //----------------------------------
    // index
    //----------------------------------

    private int mIndex = -1;

    @Override
    public int getIndex() {
        return mIndex;
    }

    void setIndex(int value) {
        mIndex = value;
    }

    //----------------------------------
    // pitch
    //----------------------------------

    private int mPitch = 60;

    @Override
    public int getPitch() {
        return mPitch;
    }

    void setPitch(int value) {
        mPitch = value;
    }

    //----------------------------------
    // gate
    //----------------------------------

    private float mGate = 1f;

    @Override
    public float getGate() {
        return mGate;
    }

    void setGate(float value) {
        mGate = value;
    }

    //----------------------------------
    // velcoity
    //----------------------------------

    private float mVelcoity = 1.0f;

    @Override
    public float getVelocity() {
        return mVelcoity;
    }

    void setVelocity(float value) {
        mVelcoity = value;
    }

    //----------------------------------
    // flags
    //----------------------------------

    private int mFlags = 0;

    @Override
    public int getFlags() {
        return mFlags;
    }

    void setFlags(int value) {
        mFlags = value;
    }

    //----------------------------------
    // selected
    //----------------------------------

    private boolean mSelected = false;

    @Override
    public boolean getSelected() {
        return mSelected;
    }

    void setSelected(boolean value) {
        mSelected = value;
    }

    //----------------------------------
    // data
    //----------------------------------

    private TriggerData mData;

    @Override
    public TriggerData getData() {
        return mData;
    }

    void setData(TriggerData value) {
        mData = value;
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public Trigger() {
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Properties
    //
    //--------------------------------------------------------------------------

    /*
     * <?xml version="1.0" encoding="UTF-8"?> <trigger selected="1"
     * parameters="3.5 65 0.4 4.0 1"/>
     */

    @Override
    public void copy(IMemento memento) {
        if (mData != null) {
            mData.copy(memento.createChild(TAG_DATA));
        }

        memento.putInteger(ATT_SELECTED, MementoUtil.booleanToInt(getSelected()));

        float start = Resolution.toBeat(mIndex, mPhrase.getResolution());
        float end = start + mGate;

        StringBuffer sb = new StringBuffer();
        sb.append(start);
        sb.append(SPACE);
        sb.append(mPitch);
        sb.append(SPACE);
        sb.append(mVelcoity);
        sb.append(SPACE);
        sb.append(end);
        sb.append(SPACE);
        sb.append(mFlags);

        memento.putString(ATT_PARAMETERS, sb.toString());
    }

    @Override
    public void paste(IMemento memento) {
        if (mData != null) {
            mData.paste(memento.getChild(TAG_DATA));
        }

        String parameters = memento.getString(ATT_PARAMETERS);

        String[] split = parameters.split(SPACE);
        float start = Float.valueOf(split[0]);
        int step = Resolution.toStep(start, mPhrase.getResolution());
        int pitch = Float.valueOf(split[1]).intValue();
        float velocity = Float.valueOf(split[2]);
        float end = Float.valueOf(split[3]);
        float gate = end - start;
        int flags = Float.valueOf(split[4]).intValue();

        setIndex(step);
        setPitch(pitch);
        setGate(gate);
        setVelocity(velocity);
        setFlags(flags);
        setSelected(MementoUtil.intToBoolean(memento.getInteger(ATT_SELECTED)));
    }

    // @Override
    // public String toString() {
    // return "Trigger [mStep=" + mIndex + ", mPitch=" + mPitch + ", mGate="
    // + mGate + ", mVelcoity=" + mVelcoity + ", mSelected="
    // + mSelected + "]";
    // }

    @Override
    public String toString() {
        return "[" + mIndex + "]P:" + mPitch + "S:" + mSelected;
    }
}
