////////////////////////////////////////////////////////////////////////////////
//Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0 
//
//Unless required by applicable law or agreed to in writing, software 
//distributed under the License is distributed on an "AS IS" BASIS, 
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and 
//limitations under the License
//
//Author: Michael Schmalle, Principal Architect
//mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.node.machine.patch;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.MixerChannelMessage;
import com.teotigraphix.caustk.core.osc.MixerChannelMessage.MixerChannelControl;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.NodeBaseEvents.NodeEvent;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link MixerChannel} manages the machine's main mixer panel controls
 * channel.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see MixerChannelNodeChangeEvent
 */
public class MixerChannel extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float bass = 0f;

    @Tag(101)
    private float mid = 0f;

    @Tag(102)
    private float high = 0f;

    @Tag(103)
    private float delaySend = 0f;

    @Tag(104)
    private float reverbSend = 0f;

    @Tag(105)
    private float pan = 0f;

    @Tag(106)
    private float stereoWidth = 0f;

    @Tag(107)
    private boolean mute = false;

    @Tag(108)
    private boolean solo = false;

    @Tag(109)
    private float volume = 1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // bass
    //----------------------------------

    /**
     * @see MixerChannelMessage#EQ_BASS
     */
    public final float getBass() {
        return bass;
    }

    public float queryBass() {
        return MixerChannelMessage.EQ_BASS.query(getRack(), getMachineIndex());
    }

    /**
     * @param bass (-1.0..1.0)
     * @see MixerChannelMessage#EQ_BASS
     */
    public final void setBass(float bass) {
        if (bass == this.bass)
            return;
        if (bass < -1f || bass > 1f)
            throw newRangeException(MixerChannelMessage.EQ_BASS, "-1.0..1.0", bass);
        this.bass = bass;
        MixerChannelMessage.EQ_BASS.send(getRack(), getMachineIndex(), bass);
        post(MixerChannelControl.Bass, bass);
    }

    //----------------------------------
    // mid
    //----------------------------------

    /**
     * @see MixerChannelMessage#EQ_MID
     */
    public float getMid() {
        return mid;
    }

    public float queryMid() {
        return MixerChannelMessage.EQ_MID.query(getRack(), getMachineIndex());
    }

    /**
     * @param mid (-1.0..1.0)
     * @see MixerChannelMessage#EQ_MID
     */
    public void setMid(float mid) {
        if (mid == this.mid)
            return;
        if (mid < -1f || mid > 1f)
            throw newRangeException(MixerChannelMessage.EQ_MID, "-1.0..1.0", mid);
        this.mid = mid;
        MixerChannelMessage.EQ_MID.send(getRack(), getMachineIndex(), mid);
        post(MixerChannelControl.Mid, mid);
    }

    //----------------------------------
    // high
    //----------------------------------

    /**
     * @see MixerChannelMessage#EQ_HIGH
     */
    public final float getHigh() {
        return high;
    }

    public float queryHigh() {
        return MixerChannelMessage.EQ_HIGH.query(getRack(), getMachineIndex());
    }

    /**
     * @param high (-1.0..1.0)
     * @see MixerChannelMessage#EQ_HIGH
     */
    public final void setHigh(float high) {
        if (high == this.high)
            return;
        if (high < -1f || high > 1f)
            throw newRangeException(MixerChannelMessage.EQ_HIGH, "-1.0..1.0", high);
        this.high = high;
        MixerChannelMessage.EQ_HIGH.send(getRack(), getMachineIndex(), high);
        post(MixerChannelControl.High, high);
    }

    //----------------------------------
    // delaySend
    //----------------------------------

    /**
     * @see MixerChannelMessage#DELAY_SEND
     */
    public final float getDelaySend() {
        return delaySend;
    }

    public float queryDelaySend() {
        return MixerChannelMessage.DELAY_SEND.query(getRack(), getMachineIndex());
    }

    /**
     * @param vdelaySendalue (0.0..1.0)
     * @see MixerChannelMessage#DELAY_SEND
     */
    public void setDelaySend(float delaySend) {
        if (delaySend == this.delaySend)
            return;
        if (delaySend < 0f || delaySend > 1f)
            throw newRangeException(MixerChannelMessage.DELAY_SEND, "0.0..1.0", delaySend);
        this.delaySend = delaySend;
        MixerChannelMessage.DELAY_SEND.send(getRack(), getMachineIndex(), delaySend);
        post(MixerChannelControl.DelaySend, delaySend);
    }

    //----------------------------------
    // reverbSend
    //----------------------------------

    /**
     * @see MixerChannelMessage#REVERB_SEND
     */
    public final float getReverbSend() {
        return reverbSend;
    }

    public float queryReverbSend() {
        return MixerChannelMessage.REVERB_SEND.query(getRack(), getMachineIndex());
    }

    /**
     * @param reverbSend (0.0..1.0)
     * @see MixerChannelMessage#REVERB_SEND
     */
    public void setReverbSend(float reverbSend) {
        if (reverbSend == this.reverbSend)
            return;
        if (reverbSend < 0f || reverbSend > 1f)
            throw newRangeException(MixerChannelMessage.REVERB_SEND, "0.0..1.0", reverbSend);
        this.reverbSend = reverbSend;
        MixerChannelMessage.REVERB_SEND.send(getRack(), getMachineIndex(), reverbSend);
        post(MixerChannelControl.ReverbSend, reverbSend);
    }

    //----------------------------------
    // pan
    //----------------------------------

    /**
     * @see MixerChannelMessage#PAN
     */
    public final float getPan() {
        return pan;
    }

    public float queryPan() {
        return MixerChannelMessage.PAN.query(getRack(), getMachineIndex());
    }

    /**
     * @param pan (-1.0..1.0)
     * @see MixerChannelMessage#PAN
     */
    public void setPan(float pan) {
        if (pan == this.pan)
            return;
        if (pan < -1f || pan > 1f)
            throw newRangeException(MixerChannelMessage.PAN, "-1.0..1.0", pan);
        this.pan = pan;
        MixerChannelMessage.PAN.send(getRack(), getMachineIndex(), pan);
        post(MixerChannelControl.Pan, pan);
    }

    //----------------------------------
    // stereoWidth
    //----------------------------------

    /**
     * @see MixerChannelMessage#STEREO_WIDTH
     */
    public final float getStereoWidth() {
        return stereoWidth;
    }

    public float queryStereoWidth() {
        return MixerChannelMessage.STEREO_WIDTH.query(getRack(), getMachineIndex());
    }

    /**
     * @param stereoWidth (-1.0..1.0)
     * @see MixerChannelMessage#STEREO_WIDTH
     */
    public void setStereoWidth(float stereoWidth) {
        if (stereoWidth == this.stereoWidth)
            return;
        if (stereoWidth < -1f || stereoWidth > 1f)
            throw newRangeException(MixerChannelMessage.STEREO_WIDTH, "-1.0..1.0", stereoWidth);
        this.stereoWidth = stereoWidth;
        MixerChannelMessage.STEREO_WIDTH.send(getRack(), getMachineIndex(), stereoWidth);
        post(MixerChannelControl.StereoWidth, stereoWidth);
    }

    //----------------------------------
    // mute
    //----------------------------------

    /**
     * @see MixerChannelMessage#MUTE
     */
    public final boolean isMute() {
        return mute;
    }

    public boolean queryMute() {
        return MixerChannelMessage.MUTE.query(getRack(), getMachineIndex()) != 0f;
    }

    /**
     * @param mute (true, false)
     * @see MixerChannelMessage#MUTE
     */
    public void setMute(boolean mute) {
        //        if (mute == this.mute)
        //            return;
        this.mute = mute;
        MixerChannelMessage.MUTE.send(getRack(), getMachineIndex(), mute ? 1 : 0);
        post(MixerChannelControl.Mute, mute ? 1 : 0);
    }

    //----------------------------------
    // solo
    //----------------------------------

    /**
     * @see MixerChannelMessage#SOLO
     */
    public final boolean isSolo() {
        return solo;
    }

    public boolean querySolo() {
        return MixerChannelMessage.SOLO.query(getRack(), getMachineIndex()) != 0f;
    }

    /**
     * @param solo (true, false)
     * @see MixerChannelMessage#SOLO
     */
    public void setSolo(boolean solo) {
        setSolo(solo, true);
    }

    public void setSolo(boolean solo, boolean send) {
        //        if (solo == this.solo)
        //            return;
        this.solo = solo;

        if (send) {
            MixerChannelMessage.SOLO.send(getRack(), getMachineIndex(), solo ? 1 : 0);
            post(MixerChannelControl.Solo, solo ? 1 : 0);

            if (solo) {
                post(new OnRackSoloRefresh(this));
            }
        }
    }

    //----------------------------------
    // volume
    //----------------------------------

    /**
     * @see MixerChannelMessage#VOLUME
     */
    public final float getVolume() {
        return volume;
    }

    public float queryVolume() {
        return MixerChannelMessage.VOLUME.query(getRack(), getMachineIndex());
    }

    /**
     * @param volume (0.0..2.0)
     * @see MixerChannelMessage#VOLUME
     */
    public void setVolume(float volume) {
        if (volume == this.volume)
            return;
        if (volume < 0f || volume > 2f)
            throw newRangeException("volume", "0.0..2.0", volume);
        this.volume = volume;
        MixerChannelMessage.VOLUME.send(getRack(), getMachineIndex(), volume);
        post(MixerChannelControl.Volume, volume);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public MixerChannel() {
    }

    public MixerChannel(MachineNode machineNode) {
        super(machineNode);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void updateComponents() {
        int machineIndex = getMachineIndex();
        MixerChannelMessage.EQ_BASS.send(getRack(), machineIndex, getBass());
        MixerChannelMessage.EQ_MID.send(getRack(), machineIndex, getMid());
        MixerChannelMessage.EQ_HIGH.send(getRack(), machineIndex, getHigh());
        MixerChannelMessage.REVERB_SEND.send(getRack(), machineIndex, getReverbSend());
        MixerChannelMessage.DELAY_SEND.send(getRack(), machineIndex, getDelaySend());
        MixerChannelMessage.STEREO_WIDTH.send(getRack(), machineIndex, getStereoWidth());

        MixerChannelMessage.PAN.send(getRack(), machineIndex, getPan());
        MixerChannelMessage.VOLUME.send(getRack(), machineIndex, getVolume());

        MixerChannelMessage.MUTE.send(getRack(), machineIndex, mute ? 1 : 0);
        MixerChannelMessage.SOLO.send(getRack(), machineIndex, solo ? 1 : 0);
    }

    @Override
    protected void restoreComponents() {
        setBass(queryBass());
        setMid(queryMid());
        setHigh(queryHigh());
        setReverbSend(queryReverbSend());
        setDelaySend(queryDelaySend());
        setStereoWidth(queryStereoWidth());
        setPan(queryPan());
        setMute(queryMute());
        setSolo(querySolo());
        setVolume(queryVolume());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(bass);
        result = prime * result + Float.floatToIntBits(delaySend);
        result = prime * result + Float.floatToIntBits(high);
        result = prime * result + Float.floatToIntBits(mid);
        result = prime * result + (mute ? 1231 : 1237);
        result = prime * result + Float.floatToIntBits(pan);
        result = prime * result + Float.floatToIntBits(reverbSend);
        result = prime * result + (solo ? 1231 : 1237);
        result = prime * result + Float.floatToIntBits(stereoWidth);
        result = prime * result + Float.floatToIntBits(volume);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MixerChannel other = (MixerChannel)obj;
        if (Float.floatToIntBits(bass) != Float.floatToIntBits(other.bass))
            return false;
        if (Float.floatToIntBits(delaySend) != Float.floatToIntBits(other.delaySend))
            return false;
        if (Float.floatToIntBits(high) != Float.floatToIntBits(other.high))
            return false;
        if (Float.floatToIntBits(mid) != Float.floatToIntBits(other.mid))
            return false;
        if (mute != other.mute)
            return false;
        if (Float.floatToIntBits(pan) != Float.floatToIntBits(other.pan))
            return false;
        if (Float.floatToIntBits(reverbSend) != Float.floatToIntBits(other.reverbSend))
            return false;
        if (solo != other.solo)
            return false;
        if (Float.floatToIntBits(stereoWidth) != Float.floatToIntBits(other.stereoWidth))
            return false;
        if (Float.floatToIntBits(volume) != Float.floatToIntBits(other.volume))
            return false;
        return true;
    }

    //--------------------------------------------------------------------------
    // Events
    //--------------------------------------------------------------------------

    protected void post(MixerChannelControl control, float value) {
        post(new MixerChannelNodeChangeEvent(this, control, value));
    }

    /**
     * Base event for the {@link MixerChannel}.
     * 
     * @author Michael Schmalle
     * @since 1.0
     */
    public static class MixerChannelNodeEvent extends NodeEvent {
        public MixerChannelNodeEvent(NodeBase target, MixerChannelControl message) {
            super(target, message);
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see MixerChannel
     */
    public static class MixerChannelNodeChangeEvent extends NodeEvent {
        private float value;

        public float getValue() {
            return value;
        }

        public MixerChannelNodeChangeEvent(NodeBase target, MixerChannelControl control, float value) {
            super(target, control);
            this.value = value;
        }
    }

    public static class OnRackSoloRefresh extends NodeEvent {

        /**
         * The soloed mixer channel soloed.
         */
        public MixerChannel getMixerChannel() {
            return (MixerChannel)super.getTarget();
        }

        public OnRackSoloRefresh(MixerChannel mixerChannel) {
            super(mixerChannel);
        }
    }
}
