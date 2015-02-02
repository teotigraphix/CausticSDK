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
 * The master reverb insert node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class MasterReverbNode extends MasterChildNode {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    @Tag(100)
    private float preDelay = 0.02f;

    @Tag(101)
    private float roomSize = 0.75f;

    @Tag(102)
    private float hfDamping = 0.156f;

    @Tag(103)
    private float diffuse = 0.7f;

    @Tag(104)
    private int ditherEchoes = 0;

    @Tag(105)
    private float erGain = 1f;

    @Tag(106)
    private float erDecay = 0.25f;

    @Tag(107)
    private float stereoDelay = 0.5f;

    @Tag(108)
    private float stereoSpread = 0.25f;

    @Tag(100)
    private float wet = 0.25f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // preDelay
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_PRE_DELAY
     */
    public float getPreDelay() {
        return preDelay;
    }

    public float queryPreDelay() {
        return MasterMixerMessage.REVERB_PRE_DELAY.query(getRack());
    }

    /**
     * @param preDelay (0..0.1)
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_PRE_DELAY
     */
    public void setPreDelay(float preDelay) {
        if (preDelay == this.preDelay)
            return;
        if (preDelay < 0f || preDelay > 0.1f)
            throw newRangeException(MasterMixerMessage.REVERB_PRE_DELAY, "0..0.1", preDelay);
        this.preDelay = preDelay;
        MasterMixerMessage.REVERB_PRE_DELAY.send(getRack(), preDelay);
        post(MasterMixerControl.ReverbPreDelay, preDelay);
    }

    //----------------------------------
    // roomSize
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_ROOM_SIZE
     */
    public float getRoomSize() {
        return roomSize;
    }

    public float queryRoomSize() {
        return MasterMixerMessage.REVERB_ROOM_SIZE.query(getRack());
    }

    /**
     * @param roomSize (0..1)
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_ROOM_SIZE
     */
    public void setRoomSize(float roomSize) {
        if (roomSize == this.roomSize)
            return;
        if (roomSize < 0f || roomSize > 1f)
            throw newRangeException(MasterMixerMessage.REVERB_ROOM_SIZE, "0..1", roomSize);
        this.roomSize = roomSize;
        MasterMixerMessage.REVERB_ROOM_SIZE.send(getRack(), roomSize);
        post(MasterMixerControl.ReverbRoomSize, roomSize);
    }

    //----------------------------------
    // hfDamping
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_HF_DAMPING
     */
    public float getHFDamping() {
        return hfDamping;
    }

    public float queryHFDamping() {
        return MasterMixerMessage.REVERB_HF_DAMPING.query(getRack());
    }

    /**
     * @param hfDamping (0..0.8)
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_HF_DAMPING
     */
    public void setHFDamping(float hfDamping) {
        if (hfDamping == this.hfDamping)
            return;
        if (hfDamping < 0f || hfDamping > 0.8f)
            throw newRangeException(MasterMixerMessage.REVERB_HF_DAMPING, "0..0.8", hfDamping);
        this.hfDamping = hfDamping;
        MasterMixerMessage.REVERB_HF_DAMPING.send(getRack(), hfDamping);
        post(MasterMixerControl.ReverbHfDamping, hfDamping);
    }

    //----------------------------------
    // diffuse
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_DIFFUSE
     */
    public float getDiffuse() {
        return diffuse;
    }

    public float queryDiffuse() {
        return MasterMixerMessage.REVERB_DIFFUSE.query(getRack());
    }

    /**
     * @param diffuse (0..0.7)
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_DIFFUSE
     */
    public void setDiffuse(float diffuse) {
        if (diffuse == this.diffuse)
            return;
        if (diffuse < 0f || diffuse > 0.7f)
            throw newRangeException(MasterMixerMessage.REVERB_DIFFUSE, "0..0.7", diffuse);
        this.diffuse = diffuse;
        MasterMixerMessage.REVERB_DIFFUSE.send(getRack(), diffuse);
        post(MasterMixerControl.ReverbDiffuse, diffuse);
    }

    //----------------------------------
    // ditherEchoes
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_DITHER_ECHOS
     */
    public int getDitherEchoes() {
        return ditherEchoes;
    }

    public int queryDitherEchoes() {
        return (int)MasterMixerMessage.REVERB_DITHER_ECHOS.query(getRack());
    }

    /**
     * @param ditherEchoes (0,1)
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_DITHER_ECHOS
     */
    public void setDitherEchoes(int ditherEchoes) {
        if (ditherEchoes == this.ditherEchoes)
            return;
        if (ditherEchoes < 0 || ditherEchoes > 1)
            throw newRangeException(MasterMixerMessage.REVERB_DITHER_ECHOS, "0,1", ditherEchoes);
        this.ditherEchoes = ditherEchoes;
        MasterMixerMessage.REVERB_DITHER_ECHOS.send(getRack(), ditherEchoes);
        post(MasterMixerControl.ReverbDitherEchos, ditherEchoes);
    }

    //----------------------------------
    // erGain
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_ER_GAIN
     */
    public float getERGain() {
        return erGain;
    }

    public float queryERGain() {
        return MasterMixerMessage.REVERB_ER_GAIN.query(getRack());
    }

    /**
     * @param erGain (0..1)
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_ER_GAIN
     */
    public void setERGain(float erGain) {
        if (erGain == this.erGain)
            return;
        if (erGain < 0f || erGain > 1f)
            throw newRangeException(MasterMixerMessage.REVERB_ER_GAIN, "0..1", erGain);
        this.erGain = erGain;
        MasterMixerMessage.REVERB_ER_GAIN.send(getRack(), erGain);
        post(MasterMixerControl.ReverbErGain, erGain);
    }

    //----------------------------------
    // erDecay
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_ER_DECAY
     */
    public float getERDecay() {
        return erDecay;
    }

    public float queryERDecay() {
        return MasterMixerMessage.REVERB_ER_DECAY.query(getRack());
    }

    /**
     * @param erDecay (0..1)
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_ER_DECAY
     */
    public void setERDecay(float erDecay) {
        if (erDecay == this.erDecay)
            return;
        if (erDecay < 0f || erDecay > 1f)
            throw newRangeException("er_decay", "0..1", erDecay);
        this.erDecay = erDecay;
        MasterMixerMessage.REVERB_ER_DECAY.send(getRack(), erDecay);
        post(MasterMixerControl.ReverbErDecay, erDecay);
    }

    //----------------------------------
    // stereoDelay
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_STEREO_DELAY
     */
    public float getStereoDelay() {
        return stereoDelay;
    }

    public float queryStereoDelay() {
        return MasterMixerMessage.REVERB_STEREO_DELAY.query(getRack());
    }

    /**
     * @param stereoDelay (0..1)
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_STEREO_DELAY
     */
    public void setStereoDelay(float stereoDelay) {
        if (stereoDelay == this.stereoDelay)
            return;
        if (stereoDelay < 0f || stereoDelay > 1f)
            throw newRangeException("stereo_delay", "0..1", stereoDelay);
        this.stereoDelay = stereoDelay;
        MasterMixerMessage.REVERB_STEREO_DELAY.send(getRack(), stereoDelay);
        post(MasterMixerControl.ReverbStereoDelay, stereoDelay);
    }

    //----------------------------------
    // stereoSpread
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_STEREO_SPREAD
     */
    public float getStereoSpread() {
        return stereoSpread;
    }

    public float queryStereoSpread() {
        return MasterMixerMessage.REVERB_STEREO_SPREAD.query(getRack());
    }

    /**
     * @param stereoSpread (0..1)
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_STEREO_SPREAD
     */
    public void setStereoSpread(float stereoSpread) {
        if (stereoSpread == this.stereoSpread)
            return;
        if (stereoSpread < 0f || stereoSpread > 1f)
            throw newRangeException(MasterMixerMessage.REVERB_STEREO_SPREAD, "0..1", stereoSpread);
        this.stereoSpread = stereoSpread;
        MasterMixerMessage.REVERB_STEREO_SPREAD.send(getRack(), stereoSpread);
        post(MasterMixerControl.ReverbStereoSpread, stereoSpread);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_WET
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return MasterMixerMessage.REVERB_WET.query(getRack());
    }

    /**
     * @param wet (0..0.5)
     * @see com.teotigraphix.caustk.core.osc.MasterMixerMessage#REVERB_WET
     */
    public void setWet(float wet) {
        if (wet == this.wet)
            return;
        if (wet < 0f || wet > 0.5f)
            throw newRangeException(MasterMixerMessage.REVERB_WET, "0..0.5", wet);
        this.wet = wet;
        MasterMixerMessage.REVERB_WET.send(getRack(), wet);
        post(MasterMixerControl.ReverbWet, wet);
    }

    @Override
    CausticMessage getBypassMessage() {
        return MasterMixerMessage.REVERB_BYPASS;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public MasterReverbNode() {
    }

    public MasterReverbNode(MasterChannel masterNode) {
        super(masterNode);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        MasterMixerMessage.REVERB_DIFFUSE.send(getRack(), diffuse);
        MasterMixerMessage.REVERB_DITHER_ECHOS.send(getRack(), ditherEchoes);
        MasterMixerMessage.REVERB_ER_DECAY.send(getRack(), erDecay);
        MasterMixerMessage.REVERB_ER_GAIN.send(getRack(), erGain);
        MasterMixerMessage.REVERB_HF_DAMPING.send(getRack(), hfDamping);
        MasterMixerMessage.REVERB_PRE_DELAY.send(getRack(), preDelay);
        MasterMixerMessage.REVERB_ROOM_SIZE.send(getRack(), roomSize);
        MasterMixerMessage.REVERB_STEREO_DELAY.send(getRack(), stereoDelay);
        MasterMixerMessage.REVERB_STEREO_SPREAD.send(getRack(), stereoSpread);
        MasterMixerMessage.REVERB_WET.send(getRack(), wet);
    }

    @Override
    protected void restoreComponents() {
        setDiffuse(queryDiffuse());
        setDitherEchoes(queryDitherEchoes());
        setERDecay(queryERDecay());
        setERGain(queryERGain());
        setHFDamping(queryHFDamping());
        setPreDelay(queryPreDelay());
        setRoomSize(queryRoomSize());
        setStereoDelay(queryStereoDelay());
        setStereoSpread(queryStereoSpread());
        setWet(queryWet());
    }

    @Override
    public String toString() {
        return "MasterReverbNode [preDelay=" + preDelay + ", roomSize=" + roomSize + ", hfDamping="
                + hfDamping + ", diffuse=" + diffuse + ", ditherEchoes=" + ditherEchoes
                + ", erGain=" + erGain + ", erDecay=" + erDecay + ", stereoDelay=" + stereoDelay
                + ", stereoSpread=" + stereoSpread + ", wet=" + wet + ", isBypass=" + isBypass()
                + "]";
    }

}
