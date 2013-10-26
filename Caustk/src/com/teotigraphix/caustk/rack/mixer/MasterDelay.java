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

package com.teotigraphix.caustk.rack.mixer;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.MasterMixerMessage;
import com.teotigraphix.caustk.rack.IRack;

public class MasterDelay extends MasterComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private int steps = 2;

    @Tag(110)
    private int loop = 2;

    @Tag(120)
    private int time = 8;

    @Tag(130)
    private int sync = 1;

    @Tag(140)
    private float feedback = 0.5f;

    @Tag(150)
    private int feedbackFirst = 0;

    @Tag(160)
    private float damping = 0f;

    @Tag(170)
    private float wet = 0.5f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // steps
    //----------------------------------

    public int getSteps() {
        return steps;
    }

    int getSteps(boolean restore) {
        return (int)MasterMixerMessage.DELAY_STEPS.query(getEngine());
    }

    public void setSteps(int value) {
        if (steps == value)
            return;
        if (value < 1 || value > 5)
            throw newRangeException("steps", "1,2,3,4,5", value);
        steps = value;
        MasterMixerMessage.DELAY_STEPS.send(getEngine(), value);
    }

    //----------------------------------
    // loop
    //----------------------------------

    public int getLoop() {
        return steps;
    }

    int getLoop(boolean restore) {
        return (int)MasterMixerMessage.DELAY_LOOP.query(getEngine());
    }

    public void setLoop(int value) {
        if (loop == value)
            return;
        if (value < 0 || value > 1)
            throw newRangeException("loop", "0,1", value);
        loop = value;
        MasterMixerMessage.DELAY_LOOP.send(getEngine(), value);
    }

    //----------------------------------
    // time
    //----------------------------------

    public int getTime() {
        return time;
    }

    int getTime(boolean restore) {
        return (int)MasterMixerMessage.DELAY_TIME.query(getEngine());
    }

    public void setTime(int value) {
        if (time == value)
            return;
        if (value < 1 || value > 12)
            throw newRangeException("time", "1..12", value);
        time = value;
        MasterMixerMessage.DELAY_TIME.send(getEngine(), value);
    }

    //----------------------------------
    // sync
    //----------------------------------

    public int getSync() {
        return sync;
    }

    int getSync(boolean restore) {
        return (int)MasterMixerMessage.DELAY_SYNC.query(getEngine());
    }

    public void setSync(int value) {
        if (sync == value)
            return;
        if (value < 0 || value > 1)
            throw newRangeException("sync", "0,1", value);
        sync = value;
        MasterMixerMessage.DELAY_SYNC.send(getEngine(), value);
    }

    //----------------------------------
    // feedback
    //----------------------------------

    public float getFeedback() {
        return feedback;
    }

    float getFeedback(boolean restore) {
        return MasterMixerMessage.DELAY_FEEDBACK.query(getEngine());
    }

    public void setFeedback(float value) {
        if (feedback == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("feedback", "0..1", value);
        feedback = value;
        MasterMixerMessage.DELAY_FEEDBACK.send(getEngine(), value);
    }

    //----------------------------------
    // feedbackFirst
    //----------------------------------

    public int getFeedbackFirst() {
        return feedbackFirst;
    }

    int getFeedbackFirst(boolean restore) {
        return (int)MasterMixerMessage.DELAY_FEEDBACK_FIRST.query(getEngine());
    }

    public void setFeedbackFirst(int value) {
        if (feedbackFirst == value)
            return;
        if (value < 0 || value > 1)
            throw newRangeException("feedback_first", "0,1", value);
        feedbackFirst = value;
        MasterMixerMessage.DELAY_FEEDBACK_FIRST.send(getEngine(), value);
    }

    //----------------------------------
    // damping
    //----------------------------------

    public float getDamping() {
        return damping;
    }

    float getDamping(boolean restore) {
        return MasterMixerMessage.DELAY_DAMPING.query(getEngine());
    }

    public void setDamping(float value) {
        if (damping == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("damping", "0..1", value);
        damping = value;
        MasterMixerMessage.DELAY_DAMPING.send(getEngine(), value);
    }

    //----------------------------------
    // wet
    //----------------------------------

    public float getWet() {
        return wet;
    }

    float getWet(boolean restore) {
        return MasterMixerMessage.DELAY_WET.query(getEngine());
    }

    public void setWet(float value) {
        if (wet == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("wet", "0..1", value);
        wet = value;
        MasterMixerMessage.DELAY_WET.send(getEngine(), value);
    }

    //----------------------------------
    // pan
    //----------------------------------

    public float getPan(int step) {
        return (int)MasterMixerMessage.DELAY_PAN.send(getEngine(), step);
    }

    // XXX todo impl panMap
    float getPan(boolean restore) {
        return (int)MasterMixerMessage.DELAY_PAN.send(getEngine(), 0);
    }

    public void setPan(int step, float value) {
        //if (pan == value)
        //return;
        if (value < -1f || value > 40f)
            throw newRangeException("pan", "-1..1", value);
        //pan = value;
        MasterMixerMessage.DELAY_PAN.send(getEngine(), step, value);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MasterDelay() {
        bypassMessage = MasterMixerMessage.DELAY_BYPASS;
    }

    public MasterDelay(IRack rack) {
        super(rack);
        bypassMessage = MasterMixerMessage.DELAY_BYPASS;
    }

    @Override
    public void restore(IRack rack) {
        super.restore(rack);
        setDamping(getDamping(true));
        setFeedback(getFeedback(true));
        setFeedbackFirst(getFeedbackFirst(true));
        setLoop(getLoop(true));
        //setPan(getPan(true));
        setSteps(getSteps(true));
        setSync(getSync(true));
        setTime(getTime(true));
        setWet(getWet(true));
    }

    @Override
    public void update(IRack rack) {
        super.update(rack);
        MasterMixerMessage.DELAY_DAMPING.send(getEngine(), damping);
        MasterMixerMessage.DELAY_FEEDBACK.send(getEngine(), feedback);
        MasterMixerMessage.DELAY_FEEDBACK_FIRST.send(getEngine(), feedbackFirst);
        MasterMixerMessage.DELAY_LOOP.send(getEngine(), loop);
        MasterMixerMessage.DELAY_STEPS.send(getEngine(), steps);
        MasterMixerMessage.DELAY_SYNC.send(getEngine(), sync);
        MasterMixerMessage.DELAY_TIME.send(getEngine(), time);
        MasterMixerMessage.DELAY_WET.send(getEngine(), wet);
    }
}
