////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.node.master;

import java.util.HashMap;
import java.util.Map.Entry;

import com.teotigraphix.caustk.core.osc.CausticMessage;
import com.teotigraphix.caustk.core.osc.MasterMixerMessage;
import com.teotigraphix.caustk.core.osc.MasterMixerMessage.MasterMixerControl;

/**
 * The master delay insert node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class MasterDelayNode extends MasterChildNode {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private int steps = 2;

    private int loop = 2;

    private int time = 8;

    private int sync = 1;

    private float feedback = 0.5f;

    private int feedbackFirst = 0;

    private float damping = 0f;

    private float wet = 0.5f;

    private HashMap<Integer, Float> panSteps = new HashMap<Integer, Float>();

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // steps
    //----------------------------------

    /**
     * @see MasterMixerMessage#DELAY_STEPS
     */
    public int getSteps() {
        return steps;
    }

    public int querySteps() {
        return (int)MasterMixerMessage.DELAY_STEPS.query(getRack());
    }

    /**
     * @param steps (1,2,3,4,5)
     * @see MasterMixerMessage#DELAY_STEPS
     */
    public void setSteps(int steps) {
        if (steps == this.steps)
            return;
        if (steps < 1 || steps > 5)
            throw newRangeException(MasterMixerMessage.DELAY_STEPS, "1,2,3,4,5", steps);
        this.steps = steps;
        MasterMixerMessage.DELAY_STEPS.send(getRack(), steps);
        post(MasterMixerControl.DelaySteps, steps);
    }

    //----------------------------------
    // loop
    //----------------------------------

    /**
     * @see MasterMixerMessage#DELAY_LOOP
     */
    public int getLoop() {
        return loop;
    }

    public int queryLoop() {
        return (int)MasterMixerMessage.DELAY_LOOP.query(getRack());
    }

    /**
     * @param loop (0,1).
     * @see MasterMixerMessage#DELAY_LOOP
     */
    public void setLoop(int loop) {
        if (loop == this.loop)
            return;
        if (loop < 0 || loop > 1)
            throw newRangeException(MasterMixerMessage.DELAY_LOOP, "0,1", loop);
        this.loop = loop;
        MasterMixerMessage.DELAY_LOOP.send(getRack(), loop);
        post(MasterMixerControl.DelayLoop, loop);
    }

    //----------------------------------
    // time
    //----------------------------------

    /**
     * @see MasterMixerMessage#DELAY_TIME
     */
    public int getTime() {
        return time;
    }

    public int queryTime() {
        return (int)MasterMixerMessage.DELAY_TIME.query(getRack());
    }

    /**
     * @param time (1..12).
     * @see MasterMixerMessage#DELAY_TIME
     */
    public void setTime(int time) {
        if (time == this.time)
            return;
        if (time < 1 || time > 12)
            throw newRangeException(MasterMixerMessage.DELAY_TIME, "1..12", time);
        this.time = time;
        MasterMixerMessage.DELAY_TIME.send(getRack(), time);
        post(MasterMixerControl.DelayTime, time);
    }

    //----------------------------------
    // sync
    //----------------------------------

    /**
     * @see MasterMixerMessage#DELAY_SYNC
     */
    public int getSync() {
        return sync;
    }

    public int querySync() {
        return (int)MasterMixerMessage.DELAY_SYNC.query(getRack());
    }

    /**
     * @param sync (0,1).
     * @see MasterMixerMessage#DELAY_SYNC
     */
    public void setSync(int sync) {
        if (sync == this.sync)
            return;
        if (sync < 0 || sync > 1)
            throw newRangeException(MasterMixerMessage.DELAY_SYNC, "0,1", sync);
        this.sync = sync;
        MasterMixerMessage.DELAY_SYNC.send(getRack(), sync);
        post(MasterMixerControl.DelaySync, sync);
    }

    //----------------------------------
    // feedback
    //----------------------------------

    /**
     * @see MasterMixerMessage#DELAY_FEEDBACK
     */
    public float getFeedback() {
        return feedback;
    }

    public float queryFeedback() {
        return MasterMixerMessage.DELAY_FEEDBACK.query(getRack());
    }

    /**
     * @param feedback (0..1).
     * @see MasterMixerMessage#DELAY_FEEDBACK
     */
    public void setFeedback(float feedback) {
        if (feedback == this.feedback)
            return;
        if (feedback < 0f || feedback > 1f)
            throw newRangeException(MasterMixerMessage.DELAY_FEEDBACK, "0..1", feedback);
        this.feedback = feedback;
        MasterMixerMessage.DELAY_FEEDBACK.send(getRack(), feedback);
        post(MasterMixerControl.DelayFeedback, feedback);
    }

    //----------------------------------
    // feedbackFirst
    //----------------------------------

    /**
     * @see MasterMixerMessage#DELAY_FEEDBACK_FIRST
     */
    public int getFeedbackFirst() {
        return feedbackFirst;
    }

    public int queryFeedbackFirst() {
        return (int)MasterMixerMessage.DELAY_FEEDBACK_FIRST.query(getRack());
    }

    /**
     * @param feedbackFirst (0,1).
     * @see MasterMixerMessage#DELAY_FEEDBACK_FIRST
     */
    public void setFeedbackFirst(int feedbackFirst) {
        if (feedbackFirst == this.feedbackFirst)
            return;
        if (feedbackFirst < 0 || feedbackFirst > 1)
            throw newRangeException(MasterMixerMessage.DELAY_FEEDBACK_FIRST, "0,1", feedbackFirst);
        this.feedbackFirst = feedbackFirst;
        MasterMixerMessage.DELAY_FEEDBACK_FIRST.send(getRack(), feedbackFirst);
        post(MasterMixerControl.DelayFeedbackFirst, feedbackFirst);
    }

    //----------------------------------
    // damping
    //----------------------------------

    /**
     * @see MasterMixerMessage#DELAY_DAMPING
     */
    public float getDamping() {
        return damping;
    }

    public float queryDamping() {
        return MasterMixerMessage.DELAY_DAMPING.query(getRack());
    }

    /**
     * @param damping (0..1).
     * @see MasterMixerMessage#DELAY_DAMPING
     */
    public void setDamping(float damping) {
        if (damping == this.damping)
            return;
        if (damping < 0f || damping > 1f)
            throw newRangeException(MasterMixerMessage.DELAY_DAMPING, "0..1", damping);
        this.damping = damping;
        MasterMixerMessage.DELAY_DAMPING.send(getRack(), damping);
        post(MasterMixerControl.DelayDamping, damping);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see MasterMixerMessage#DELAY_WET
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return MasterMixerMessage.DELAY_WET.query(getRack());
    }

    /**
     * @param wet (0..1).
     * @see MasterMixerMessage#DELAY_WET
     */
    public void setWet(float wet) {
        if (wet == this.wet)
            return;
        if (wet < 0f || wet > 1f)
            throw newRangeException(MasterMixerMessage.DELAY_WET, "0..1", wet);
        this.wet = wet;
        MasterMixerMessage.DELAY_WET.send(getRack(), wet);
        post(MasterMixerControl.DelayWet, wet);
    }

    //----------------------------------
    // pan
    //----------------------------------

    /**
     * @param step The pan step (0..4).
     * @see MasterMixerMessage#DELAY_PAN
     */
    public float getPan(int step) {
        return panSteps.get(step);
    }

    public float queryPan(int step) {
        return (int)MasterMixerMessage.QUERY_DELAY_PAN.send(getRack(), step);
    }

    /**
     * @param step (0,1,2,3,4).
     * @param pan (-1..1)
     * @see MasterMixerMessage#DELAY_PAN
     */
    public void setPan(int step, float pan) {
        Float ovalue = panSteps.get(step);
        if (ovalue != null && pan == ovalue)
            return;
        if (pan < -1f || pan > 1f)
            throw newRangeException(MasterMixerMessage.DELAY_PAN, "-1..1", pan);
        panSteps.put(step, pan);
        MasterMixerMessage.DELAY_PAN.send(getRack(), step, pan);
        post(MasterMixerControl.DelayPan, pan);
    }

    @Override
    CausticMessage getBypassMessage() {
        return MasterMixerMessage.DELAY_BYPASS;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public MasterDelayNode() {
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        MasterMixerMessage.DELAY_DAMPING.send(getRack(), damping);
        MasterMixerMessage.DELAY_FEEDBACK.send(getRack(), feedback);
        MasterMixerMessage.DELAY_FEEDBACK_FIRST.send(getRack(), feedbackFirst);
        MasterMixerMessage.DELAY_LOOP.send(getRack(), loop);
        MasterMixerMessage.DELAY_STEPS.send(getRack(), steps);
        MasterMixerMessage.DELAY_SYNC.send(getRack(), sync);
        MasterMixerMessage.DELAY_TIME.send(getRack(), time);
        MasterMixerMessage.DELAY_WET.send(getRack(), wet);
        for (Entry<Integer, Float> entry : panSteps.entrySet()) {
            MasterMixerMessage.DELAY_PAN.send(getRack(), entry.getKey(), entry.getValue());
        }
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setDamping(queryDamping());
        setFeedback(queryFeedback());
        setFeedbackFirst(queryFeedbackFirst());
        setLoop(queryLoop());
        for (int i = 0; i < 5; i++) {
            setPan(i, queryPan(i));
        }
        setSteps(querySteps());
        setSync(querySync());
        setTime(queryTime());
        setWet(queryWet());
    }
}
