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
import com.teotigraphix.caustk.core.osc.MixerControls;
import com.teotigraphix.caustk.core.osc.OSCControlsMap;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.NodeBaseEvents.NodeEvent;
import com.teotigraphix.caustk.node.machine.Machine;
import com.teotigraphix.caustk.node.machine.MachineChannel;

/**
 * The {@link MixerChannel} manages the machine's main mixer panel controls
 * channel.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see com.teotigraphix.caustk.node.machine.patch.MixerChannel.MixerChannelChangeEvent
 */
public class MixerChannel extends MachineChannel {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float eqBass = 0f;

    @Tag(101)
    private float eqMid = 0f;

    @Tag(102)
    private float eqHigh = 0f;

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
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#EQ_BASS
     */
    public final float getEqBass() {
        return eqBass;
    }

    public float queryEqBass() {
        return MixerChannelMessage.EQ_BASS.query(getRack(), getMachineIndex());
    }

    /**
     * @param bass (-1.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#EQ_BASS
     */
    public final void setEqBass(float bass) {
        if (bass == this.eqBass)
            return;
        if (bass < -1f || bass > 1f)
            throw newRangeException(MixerChannelMessage.EQ_BASS, "-1.0..1.0", bass);
        this.eqBass = bass;
        MixerChannelMessage.EQ_BASS.send(getRack(), getMachineIndex(), bass);
        post(MixerControls.EqBass, bass);
    }

    //----------------------------------
    // mid
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#EQ_MID
     */
    public float getEqMid() {
        return eqMid;
    }

    public float queryEqMid() {
        return MixerChannelMessage.EQ_MID.query(getRack(), getMachineIndex());
    }

    /**
     * @param mid (-1.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#EQ_MID
     */
    public void setEqMid(float mid) {
        if (mid == this.eqMid)
            return;
        if (mid < -1f || mid > 1f)
            throw newRangeException(MixerChannelMessage.EQ_MID, "-1.0..1.0", mid);
        this.eqMid = mid;
        MixerChannelMessage.EQ_MID.send(getRack(), getMachineIndex(), mid);
        post(MixerControls.EqMid, mid);
    }

    //----------------------------------
    // high
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#EQ_HIGH
     */
    public final float getEqHigh() {
        return eqHigh;
    }

    public float queryEqHigh() {
        return MixerChannelMessage.EQ_HIGH.query(getRack(), getMachineIndex());
    }

    /**
     * @param high (-1.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#EQ_HIGH
     */
    public final void setEqHigh(float high) {
        if (high == this.eqHigh)
            return;
        if (high < -1f || high > 1f)
            throw newRangeException(MixerChannelMessage.EQ_HIGH, "-1.0..1.0", high);
        this.eqHigh = high;
        MixerChannelMessage.EQ_HIGH.send(getRack(), getMachineIndex(), high);
        post(MixerControls.EqHigh, high);
    }

    //----------------------------------
    // delaySend
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#DELAY_SEND
     */
    public final float getDelaySend() {
        return delaySend;
    }

    public float queryDelaySend() {
        return MixerChannelMessage.DELAY_SEND.query(getRack(), getMachineIndex());
    }

    /**
     * @param vdelaySendalue (0.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#DELAY_SEND
     */
    public void setDelaySend(float delaySend) {
        if (delaySend == this.delaySend)
            return;
        if (delaySend < 0f || delaySend > 1f)
            throw newRangeException(MixerChannelMessage.DELAY_SEND, "0.0..1.0", delaySend);
        this.delaySend = delaySend;
        MixerChannelMessage.DELAY_SEND.send(getRack(), getMachineIndex(), delaySend);
        post(MixerControls.DelaySend, delaySend);
    }

    //----------------------------------
    // reverbSend
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#REVERB_SEND
     */
    public final float getReverbSend() {
        return reverbSend;
    }

    public float queryReverbSend() {
        return MixerChannelMessage.REVERB_SEND.query(getRack(), getMachineIndex());
    }

    /**
     * @param reverbSend (0.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#REVERB_SEND
     */
    public void setReverbSend(float reverbSend) {
        if (reverbSend == this.reverbSend)
            return;
        if (reverbSend < 0f || reverbSend > 1f)
            throw newRangeException(MixerChannelMessage.REVERB_SEND, "0.0..1.0", reverbSend);
        this.reverbSend = reverbSend;
        MixerChannelMessage.REVERB_SEND.send(getRack(), getMachineIndex(), reverbSend);
        post(MixerControls.ReverbSend, reverbSend);
    }

    //----------------------------------
    // pan
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#PAN
     */
    public final float getPan() {
        return pan;
    }

    public float queryPan() {
        return MixerChannelMessage.PAN.query(getRack(), getMachineIndex());
    }

    /**
     * @param pan (-1.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#PAN
     */
    public void setPan(float pan) {
        if (pan == this.pan)
            return;
        if (pan < -1f || pan > 1f)
            throw newRangeException(MixerChannelMessage.PAN, "-1.0..1.0", pan);
        this.pan = pan;
        MixerChannelMessage.PAN.send(getRack(), getMachineIndex(), pan);
        post(MixerControls.Pan, pan);
    }

    //----------------------------------
    // stereoWidth
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#STEREO_WIDTH
     */
    public final float getStereoWidth() {
        return stereoWidth;
    }

    public float queryStereoWidth() {
        return MixerChannelMessage.STEREO_WIDTH.query(getRack(), getMachineIndex());
    }

    /**
     * @param stereoWidth (-1.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#STEREO_WIDTH
     */
    public void setStereoWidth(float stereoWidth) {
        if (stereoWidth == this.stereoWidth)
            return;
        if (stereoWidth < -1f || stereoWidth > 1f)
            throw newRangeException(MixerChannelMessage.STEREO_WIDTH, "-1.0..1.0", stereoWidth);
        this.stereoWidth = stereoWidth;
        MixerChannelMessage.STEREO_WIDTH.send(getRack(), getMachineIndex(), stereoWidth);
        post(MixerControls.StereoWidth, stereoWidth);
    }

    //----------------------------------
    // mute
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#MUTE
     */
    public final boolean isMute() {
        return mute;
    }

    public boolean queryMute() {
        return MixerChannelMessage.MUTE.query(getRack(), getMachineIndex()) != 0f;
    }

    public void setMute(float mute) {
        setMute(mute == 0f ? false : true);
    }

    /**
     * @param mute (true, false)
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#MUTE
     */
    public void setMute(boolean mute) {
        //        if (mute == this.mute)
        //            return;
        this.mute = mute;
        MixerChannelMessage.MUTE.send(getRack(), getMachineIndex(), mute ? 1 : 0);
        post(MixerControls.Mute, mute ? 1 : 0);
    }

    //----------------------------------
    // solo
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#SOLO
     */
    public final boolean isSolo() {
        return solo;
    }

    public boolean querySolo() {
        return MixerChannelMessage.SOLO.query(getRack(), getMachineIndex()) != 0f;
    }

    public void setSolo(float solo) {
        setSolo(solo == 0f ? false : true);
    }

    /**
     * @param solo (true, false)
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#SOLO
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
            post(MixerControls.Solo, solo ? 1 : 0);

            //if (solo) {
            post(new OnRackSoloRefresh(this));
            //}
        }
    }

    //----------------------------------
    // volume
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#VOLUME
     */
    public final float getVolume() {
        return volume;
    }

    public float queryVolume() {
        return MixerChannelMessage.VOLUME.query(getRack(), getMachineIndex());
    }

    /**
     * @param volume (0.0..2.0)
     * @see com.teotigraphix.caustk.core.osc.MixerChannelMessage#VOLUME
     */
    public void setVolume(float volume) {
        if (volume == this.volume)
            return;
        if (volume < 0f || volume > 2f)
            throw newRangeException("volume", "0.0..2.0", volume);
        this.volume = volume;
        MixerChannelMessage.VOLUME.send(getRack(), getMachineIndex(), volume);
        post(MixerControls.Volume, volume);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public MixerChannel() {
    }

    public MixerChannel(Machine machineNode) {
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

    /**
     * Updates the current mixer channel with the source, sends native OSC
     * messages to the core.
     * 
     * @param mixer the source mixer to copy values from.
     */
    public void update(MixerChannel mixer) {
        setEqBass(mixer.getEqBass());
        setEqMid(mixer.getEqMid());
        setEqHigh(mixer.getEqHigh());
        setReverbSend(mixer.getReverbSend());
        setDelaySend(mixer.getDelaySend());
        setStereoWidth(mixer.getStereoWidth());
        setPan(mixer.getPan());
        setMute(mixer.isMute());
        setSolo(mixer.isSolo());
        setVolume(mixer.getVolume());
    }

    @Override
    protected void updateComponents() {
        int machineIndex = getMachineIndex();
        MixerChannelMessage.EQ_BASS.send(getRack(), machineIndex, getEqBass());
        MixerChannelMessage.EQ_MID.send(getRack(), machineIndex, getEqMid());
        MixerChannelMessage.EQ_HIGH.send(getRack(), machineIndex, getEqHigh());
        MixerChannelMessage.REVERB_SEND.send(getRack(), machineIndex, getReverbSend());
        MixerChannelMessage.DELAY_SEND.send(getRack(), machineIndex, getDelaySend());
        MixerChannelMessage.STEREO_WIDTH.send(getRack(), machineIndex, getStereoWidth());

        MixerChannelMessage.PAN.send(getRack(), machineIndex, getPan());
        MixerChannelMessage.VOLUME.send(getRack(), machineIndex, getVolume());

        MixerChannelMessage.MUTE.send(getRack(), machineIndex, mute ? 1 : 0);
        MixerChannelMessage.SOLO.send(getRack(), machineIndex, solo ? 1 : 0);
    }

    public void invoke(MixerControls control, float value) {
        OSCControlsMap.setValue(this, control, value);
    }

    @Override
    protected void restoreComponents() {
        setEqBass(queryEqBass());
        setEqMid(queryEqMid());
        setEqHigh(queryEqHigh());
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
        result = prime * result + Float.floatToIntBits(eqBass);
        result = prime * result + Float.floatToIntBits(delaySend);
        result = prime * result + Float.floatToIntBits(eqHigh);
        result = prime * result + Float.floatToIntBits(eqMid);
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
        if (Float.floatToIntBits(eqBass) != Float.floatToIntBits(other.eqBass))
            return false;
        if (Float.floatToIntBits(delaySend) != Float.floatToIntBits(other.delaySend))
            return false;
        if (Float.floatToIntBits(eqHigh) != Float.floatToIntBits(other.eqHigh))
            return false;
        if (Float.floatToIntBits(eqMid) != Float.floatToIntBits(other.eqMid))
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

    protected void post(MixerControls control, float value) {
        post(new MixerChannelChangeEvent(this, control, value));
    }

    /**
     * Base event for the {@link MixerChannel}.
     * 
     * @author Michael Schmalle
     * @since 1.0
     */
    public static class MixerChannelNodeEvent extends NodeEvent {
        public MixerChannelNodeEvent(NodeBase target, MixerControls message) {
            super(target, message);
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see MixerChannel
     */
    public static class MixerChannelChangeEvent extends NodeEvent {
        private float value;

        public float getValue() {
            return value;
        }

        public MixerChannelChangeEvent(NodeBase target, MixerControls control, float value) {
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
