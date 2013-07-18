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

import com.teotigraphix.caustic.sequencer.IStepPhrase;
import com.teotigraphix.caustic.sequencer.ITrigger;
import com.teotigraphix.caustic.sequencer.data.TriggerData;

/**
 * The default implementation of the ITrigger interface.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class Trigger implements ITrigger {
    //--------------------------------------------------------------------------
    //
    // ITrigger API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // phrase
    //----------------------------------

    private IStepPhrase stepPhrase;

    @Override
    public IStepPhrase getStepPhrase() {
        return stepPhrase;
    }

    void setPhrase(IStepPhrase value) {
        stepPhrase = value;
    }

    //----------------------------------
    // index
    //----------------------------------

    private int index = -1;

    @Override
    public int getIndex() {
        return index;
    }

    void setIndex(int value) {
        index = value;
    }

    //----------------------------------
    // pitch
    //----------------------------------

    private int pitch = 60;

    @Override
    public int getPitch() {
        return pitch;
    }

    void setPitch(int value) {
        pitch = value;
    }

    //----------------------------------
    // gate
    //----------------------------------

    private float gate = 0.25f; // 16th note

    @Override
    public float getGate() {
        return gate;
    }

    void setGate(float value) {
        gate = value;
    }

    //----------------------------------
    // velcoity
    //----------------------------------

    private float velocity = 0.75f;

    @Override
    public float getVelocity() {
        return velocity;
    }

    void setVelocity(float value) {
        velocity = value;
    }

    //----------------------------------
    // flags
    //----------------------------------

    private int flags = 0;

    @Override
    public int getFlags() {
        return flags;
    }

    void setFlags(int value) {
        flags = value;
        accent = ((flags & 1) == 1);
        slide = ((flags & 2) == 2);
    }

    //----------------------------------
    // selected
    //----------------------------------

    private boolean selected = false;

    @Override
    public boolean isSelected() {
        return selected;
    }

    void setSelected(boolean value) {
        selected = value;
    }

    //----------------------------------
    // selected
    //----------------------------------

    private boolean accent = false;

    @Override
    public void setAccent(boolean value) {
        accent = value;
    }

    @Override
    public boolean isAccent() {
        return accent;
    }

    //----------------------------------
    // selected
    //----------------------------------

    private boolean slide = false;

    @Override
    public void setSlide(boolean value) {
        slide = value;
    }

    @Override
    public boolean isSlide() {
        return slide;
    }

    //----------------------------------
    // data
    //----------------------------------

    private TriggerData data;

    @Override
    public TriggerData getData() {
        return data;
    }

    void setData(TriggerData value) {
        data = value;
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

    @Override
    public String toString() {
        return "[Trigger=(" + pitch + ":" + selected + ":" + gate + ")]";
    }
}
