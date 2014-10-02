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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.CausticMessage;
import com.teotigraphix.caustk.core.osc.MasterMixerMessage;
import com.teotigraphix.caustk.core.osc.MasterMixerMessage.MasterMixerControl;

/**
 * The master equalizer insert node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class MasterEqualizerNode extends MasterChildNode {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    @Tag(100)
    private float bass = 1.1f;

    @Tag(101)
    private float bassMidFreq = 0.5f;

    @Tag(102)
    private float mid = 1f;

    @Tag(103)
    private float midHighFreq = 0.5f;

    @Tag(104)
    private float high = 1.1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // bass
    //----------------------------------

    /**
     * @see MasterMixerMessage#EQ_BASS
     */
    public float getBass() {
        return bass;
    }

    public float queryBass() {
        return MasterMixerMessage.EQ_BASS.query(getRack());
    }

    /**
     * @param bass (0..2)
     * @see MasterMixerMessage#EQ_BASS
     */
    public void setBass(float bass) {
        if (bass == this.bass)
            return;
        if (bass < 0f || bass > 2f)
            throw newRangeException(MasterMixerMessage.EQ_BASS, "0..2", bass);
        this.bass = bass;
        MasterMixerMessage.EQ_BASS.send(getRack(), bass);
        post(MasterMixerControl.EqBass, bass);
    }

    //----------------------------------
    // bassMidFreq
    //----------------------------------

    /**
     * @see MasterMixerMessage#EQ_BASSMID_FREQ
     */
    public float getBassMidFreq() {
        return bassMidFreq;
    }

    public float queryBassMidFreq() {
        return MasterMixerMessage.EQ_BASSMID_FREQ.query(getRack());
    }

    /**
     * @param bassMidFreq (0..1)
     * @see MasterMixerMessage#EQ_BASSMID_FREQ
     */
    public void setBassMidFreq(float bassMidFreq) {
        if (bassMidFreq == this.bassMidFreq)
            return;
        if (bassMidFreq < 0f || bassMidFreq > 1f)
            throw newRangeException(MasterMixerMessage.EQ_BASSMID_FREQ, "0..1", bassMidFreq);
        this.bassMidFreq = bassMidFreq;
        MasterMixerMessage.EQ_BASSMID_FREQ.send(getRack(), bassMidFreq);
        post(MasterMixerControl.EqBassMidFreq, bassMidFreq);
    }

    //----------------------------------
    // mid
    //----------------------------------

    /**
     * @see MasterMixerMessage#EQ_MID
     */
    public float getMid() {
        return mid;
    }

    public float queryMid() {
        return MasterMixerMessage.EQ_MID.query(getRack());
    }

    /**
     * @param mid (0..2)
     * @see MasterMixerMessage#EQ_MID
     */
    public void setMid(float mid) {
        if (mid == this.mid)
            return;
        if (mid < 0f || mid > 2f)
            throw newRangeException(MasterMixerMessage.EQ_MID, "0..2", mid);
        this.mid = mid;
        MasterMixerMessage.EQ_MID.send(getRack(), mid);
        post(MasterMixerControl.EqMid, mid);
    }

    //----------------------------------
    // midHighFreq
    //----------------------------------

    /**
     * @see MasterMixerMessage#EQ_MIDHIGH_FREQ
     */
    public float getMidHighFreq() {
        return midHighFreq;
    }

    public float queryMidHighFreq() {
        return MasterMixerMessage.EQ_MIDHIGH_FREQ.query(getRack());
    }

    /**
     * @param midHighFreq (0..1)
     * @see MasterMixerMessage#EQ_MIDHIGH_FREQ
     */
    public void setMidHighFreq(float midHighFreq) {
        if (midHighFreq == this.midHighFreq)
            return;
        if (midHighFreq < 0f || midHighFreq > 1f)
            throw newRangeException(MasterMixerMessage.EQ_MIDHIGH_FREQ, "0..1", midHighFreq);
        this.midHighFreq = midHighFreq;
        MasterMixerMessage.EQ_MIDHIGH_FREQ.send(getRack(), midHighFreq);
        post(MasterMixerControl.EqMidHighFreq, midHighFreq);
    }

    //----------------------------------
    // high
    //----------------------------------

    /**
     * @see MasterMixerMessage#EQ_HIGH
     */
    public float getHigh() {
        return high;
    }

    public float queryHigh() {
        return MasterMixerMessage.EQ_HIGH.query(getRack());
    }

    /**
     * @param high (0..2)
     * @see MasterMixerMessage#EQ_HIGH
     */
    public void setHigh(float high) {
        if (high == this.high)
            return;
        if (high < 0f || high > 2f)
            throw newRangeException(MasterMixerMessage.EQ_HIGH, "0..2", high);
        this.high = high;
        MasterMixerMessage.EQ_HIGH.send(getRack(), high);
        post(MasterMixerControl.EqHigh, high);
    }

    @Override
    CausticMessage getBypassMessage() {
        return MasterMixerMessage.EQ_BYPASS;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public MasterEqualizerNode() {
    }

    public MasterEqualizerNode(MasterNode masterNode) {
        super(masterNode);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        MasterMixerMessage.EQ_BASS.send(getRack(), bass);
        MasterMixerMessage.EQ_BASSMID_FREQ.send(getRack(), bassMidFreq);
        MasterMixerMessage.EQ_HIGH.send(getRack(), high);
        MasterMixerMessage.EQ_MID.send(getRack(), mid);
        MasterMixerMessage.EQ_MIDHIGH_FREQ.send(getRack(), midHighFreq);
    }

    @Override
    protected void restoreComponents() {
        setBass(queryBass());
        setBassMidFreq(queryBassMidFreq());
        setHigh(queryHigh());
        setMid(queryMid());
        setMidHighFreq(queryMidHighFreq());
    }

    @Override
    public String toString() {
        return "MasterEqualizerNode [bass=" + bass + ", bassMidFreq=" + bassMidFreq + ", mid="
                + mid + ", midHighFreq=" + midHighFreq + ", high=" + high + ", isBypass="
                + isBypass() + "]";
    }

}
