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

public class MasterDelay extends MasterComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private int steps = 2;

    @Tag(101)
    private int loop = 2;

    @Tag(102)
    private int time = 8;

    @Tag(103)
    private int sync = 1;

    @Tag(104)
    private float feedback = 0.5f;

    @Tag(105)
    private int feedbackFirst = 0;

    @Tag(106)
    private float damping = 0f;

    @Tag(107)
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
        return (int)MasterMixerMessage.DELAY_STEPS.query(rack);
    }

    public void setSteps(int value) {
        if (steps == value)
            return;
        if (value < 1 || value > 5)
            throw newRangeException("steps", "1,2,3,4,5", value);
        steps = value;
        MasterMixerMessage.DELAY_STEPS.send(rack, value);
    }

    //----------------------------------
    // loop
    //----------------------------------

    public int getLoop() {
        return steps;
    }

    int getLoop(boolean restore) {
        return (int)MasterMixerMessage.DELAY_LOOP.query(rack);
    }

    public void setLoop(int value) {
        if (loop == value)
            return;
        if (value < 0 || value > 1)
            throw newRangeException("loop", "0,1", value);
        loop = value;
        MasterMixerMessage.DELAY_LOOP.send(rack, value);
    }

    //----------------------------------
    // time
    //----------------------------------

    public int getTime() {
        return time;
    }

    int getTime(boolean restore) {
        return (int)MasterMixerMessage.DELAY_TIME.query(rack);
    }

    public void setTime(int value) {
        if (time == value)
            return;
        if (value < 1 || value > 12)
            throw newRangeException("time", "1..12", value);
        time = value;
        MasterMixerMessage.DELAY_TIME.send(rack, value);
    }

    //----------------------------------
    // sync
    //----------------------------------

    public int getSync() {
        return sync;
    }

    int getSync(boolean restore) {
        return (int)MasterMixerMessage.DELAY_SYNC.query(rack);
    }

    public void setSync(int value) {
        if (sync == value)
            return;
        if (value < 0 || value > 1)
            throw newRangeException("sync", "0,1", value);
        sync = value;
        MasterMixerMessage.DELAY_SYNC.send(rack, value);
    }

    //----------------------------------
    // feedback
    //----------------------------------

    public float getFeedback() {
        return feedback;
    }

    float getFeedback(boolean restore) {
        return MasterMixerMessage.DELAY_FEEDBACK.query(rack);
    }

    public void setFeedback(float value) {
        if (feedback == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("feedback", "0..1", value);
        feedback = value;
        MasterMixerMessage.DELAY_FEEDBACK.send(rack, value);
    }

    //----------------------------------
    // feedbackFirst
    //----------------------------------

    public int getFeedbackFirst() {
        return feedbackFirst;
    }

    int getFeedbackFirst(boolean restore) {
        return (int)MasterMixerMessage.DELAY_FEEDBACK_FIRST.query(rack);
    }

    public void setFeedbackFirst(int value) {
        if (feedbackFirst == value)
            return;
        if (value < 0 || value > 1)
            throw newRangeException("feedback_first", "0,1", value);
        feedbackFirst = value;
        MasterMixerMessage.DELAY_FEEDBACK_FIRST.send(rack, value);
    }

    //----------------------------------
    // damping
    //----------------------------------

    public float getDamping() {
        return damping;
    }

    float getDamping(boolean restore) {
        return MasterMixerMessage.DELAY_DAMPING.query(rack);
    }

    public void setDamping(float value) {
        if (damping == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("damping", "0..1", value);
        damping = value;
        MasterMixerMessage.DELAY_DAMPING.send(rack, value);
    }

    //----------------------------------
    // wet
    //----------------------------------

    public float getWet() {
        return wet;
    }

    float getWet(boolean restore) {
        return MasterMixerMessage.DELAY_WET.query(rack);
    }

    public void setWet(float value) {
        if (wet == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("wet", "0..1", value);
        wet = value;
        MasterMixerMessage.DELAY_WET.send(rack, value);
    }

    //----------------------------------
    // pan
    //----------------------------------

    public float getPan(int step) {
        return (int)MasterMixerMessage.DELAY_PAN.send(rack, step);
    }

    // XXX todo impl panMap
    float getPan(boolean restore) {
        return (int)MasterMixerMessage.DELAY_PAN.send(rack, 0);
    }

    public void setPan(int step, float value) {
        //if (pan == value)
        //return;
        if (value < -1f || value > 40f)
            throw newRangeException("pan", "-1..1", value);
        //pan = value;
        MasterMixerMessage.DELAY_PAN.send(rack, step, value);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MasterDelay() {
        bypassMessage = MasterMixerMessage.DELAY_BYPASS;
    }

    //--------------------------------------------------------------------------
    // IRackSerializer API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void restore() {
        super.restore();
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
    public void update() {
        super.update();
        MasterMixerMessage.DELAY_DAMPING.send(rack, damping);
        MasterMixerMessage.DELAY_FEEDBACK.send(rack, feedback);
        MasterMixerMessage.DELAY_FEEDBACK_FIRST.send(rack, feedbackFirst);
        MasterMixerMessage.DELAY_LOOP.send(rack, loop);
        MasterMixerMessage.DELAY_STEPS.send(rack, steps);
        MasterMixerMessage.DELAY_SYNC.send(rack, sync);
        MasterMixerMessage.DELAY_TIME.send(rack, time);
        MasterMixerMessage.DELAY_WET.send(rack, wet);
    }
}
