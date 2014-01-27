////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.node.machine.patch.beatbox;

import com.teotigraphix.caustk.core.osc.BeatboxMessage;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link WavSamplerComponent} channel.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see WavSamplerComponent#getChannel(int)
 */
public class WavSamplerChannel extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private int samplerChannel;

    private String name;

    private boolean selected;

    private boolean mute;

    private boolean solo;

    private float tune;

    private float punch;

    private float decay;

    private float pan;

    private float volume;

    private int muteGroups;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    /**
     * Whether this channel contains a sample, using {@link #getName()} as the
     * test for <code>null</code>.
     */
    public boolean hasSample() {
        return name != null;
    }

    //----------------------------------
    // samplerChannel
    //----------------------------------

    /**
     * The sample channel.
     */
    public final int getSamplerChannel() {
        return samplerChannel;
    }

    //----------------------------------
    // name
    //----------------------------------

    /**
     * The sample name.
     */
    public String getName() {
        return name;
    }

    public String queryName() {
        return BeatboxMessage.QUERY_CHANNEL_SAMPLE_NAME.queryString(getRack(), getMachineIndex(),
                samplerChannel);
    }

    /**
     * Sets the sample's name.
     * 
     * @param value The name of the sample.
     */
    public void setName(String value) {
        name = value;
    }

    //----------------------------------
    // selected
    //----------------------------------

    /**
     * Whether this channel is the selected channel in a beatbox.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets this channel as the selected beatbox channel.
     * 
     * @param value Whether selected.
     */
    public void setSelected(boolean value) {
        selected = value;
    }

    //----------------------------------
    // mute
    //----------------------------------

    /**
     * @see BeatboxMessage#CHANNEL_MUTE
     */
    public boolean isMute() {
        return mute;
    }

    public boolean queryMute() {
        return BeatboxMessage.CHANNEL_MUTE.query(getRack(), getMachineIndex(), samplerChannel) == 0f ? false
                : true;
    }

    /**
     * @param mute (true|false)
     * @see BeatboxMessage#CHANNEL_MUTE
     */
    public void setMute(boolean mute) {
        if (mute == this.mute)
            return;
        this.mute = mute;
        BeatboxMessage.CHANNEL_MUTE
                .send(getRack(), getMachineIndex(), samplerChannel, mute ? 1 : 0);
    }

    //----------------------------------
    // muteGroups
    //----------------------------------

    // TODO Need to return to once I understand more  BeatboxMessage#CHANNEL_MUTE_GROUPS

    /**
     * @see BeatboxMessage#CHANNEL_MUTE_GROUPS
     */
    public int getMuteGroups() {
        return (int)BeatboxMessage.CHANNEL_MUTE_GROUPS.query(getRack(), getMachineIndex(),
                samplerChannel);
    }

    public int queryMuteGroups() {
        return (int)BeatboxMessage.CHANNEL_MUTE_GROUPS.query(getRack(), getMachineIndex(),
                samplerChannel);
    }

    /**
     * @param muteGroups (true|false)
     * @see BeatboxMessage#CHANNEL_MUTE_GROUPS
     */
    public void getMuteGroups(int muteGroups) {
        if (muteGroups == this.muteGroups)
            return;
        this.muteGroups = muteGroups;
        BeatboxMessage.CHANNEL_MUTE_GROUPS.send(getRack(), getMachineIndex(), samplerChannel,
                muteGroups);
    }

    //----------------------------------
    // solo
    //----------------------------------

    /**
     * @see BeatboxMessage#CHANNEL_SOLO
     */
    public boolean isSolo() {
        return solo;
    }

    public boolean querySolo() {
        return BeatboxMessage.CHANNEL_SOLO.query(getRack(), getMachineIndex(), samplerChannel) == 0f ? false
                : true;
    }

    /**
     * @param solo (true|false)
     * @see BeatboxMessage#CHANNEL_SOLO
     */
    public void setSolo(boolean solo) {
        if (solo == this.solo)
            return;
        this.solo = solo;
        BeatboxMessage.CHANNEL_SOLO
                .send(getRack(), getMachineIndex(), samplerChannel, solo ? 1 : 0);
    }

    //----------------------------------
    // tune
    //----------------------------------

    /**
     * @see BeatboxMessage#CHANNEL_TUNE
     */
    public float getTune() {
        return tune;
    }

    public float queryTune() {
        return BeatboxMessage.CHANNEL_TUNE.query(getRack(), getMachineIndex(), samplerChannel);
    }

    /**
     * @param tune (-6.0..6.0)
     * @see BeatboxMessage#CHANNEL_TUNE
     */
    public void setTune(float tune) {
        if (tune == this.tune)
            return;
        if (tune < -6f || tune > 6f)
            throw newRangeException(BeatboxMessage.CHANNEL_TUNE, "-6.0..6.0", tune);
        this.tune = tune;
        BeatboxMessage.CHANNEL_TUNE.send(getRack(), getMachineIndex(), samplerChannel, tune);
    }

    //----------------------------------
    // punch
    //----------------------------------

    /**
     * @see BeatboxMessage#CHANNEL_PUNCH
     */
    public float getPunch() {
        return punch;
    }

    public float queryPunch() {
        return BeatboxMessage.CHANNEL_PUNCH.query(getRack(), getMachineIndex(), samplerChannel);
    }

    /**
     * @param tune (0.0..1.0)
     * @see BeatboxMessage#CHANNEL_PUNCH
     */
    public void setPunch(float punch) {
        if (punch == this.punch)
            return;
        if (punch < 0f || punch > 1f)
            throw newRangeException(BeatboxMessage.CHANNEL_PUNCH, "0.0..1.0", punch);
        this.punch = punch;
        BeatboxMessage.CHANNEL_PUNCH.send(getRack(), getMachineIndex(), samplerChannel, punch);
    }

    //----------------------------------
    // decay
    //----------------------------------

    /**
     * @see BeatboxMessage#CHANNEL_DECAY
     */
    public float getDecay() {
        return decay;
    }

    public float queryDecay() {
        return BeatboxMessage.CHANNEL_DECAY.query(getRack(), getMachineIndex(), samplerChannel);
    }

    /**
     * @param decay (0.0..1.0)
     * @see BeatboxMessage#CHANNEL_DECAY
     */
    public void setDecay(float decay) {
        if (decay == this.decay)
            return;
        if (decay < 0f || decay > 1f)
            throw newRangeException(BeatboxMessage.CHANNEL_DECAY, "0.0..1.0", decay);
        this.decay = decay;
        BeatboxMessage.CHANNEL_DECAY.send(getRack(), getMachineIndex(), samplerChannel, decay);
    }

    //----------------------------------
    // pan
    //----------------------------------

    /**
     * @see BeatboxMessage#CHANNEL_PAN
     */
    public float getPan() {
        return pan;
    }

    public float queryPan() {
        return BeatboxMessage.CHANNEL_PAN.query(getRack(), getMachineIndex(), samplerChannel);
    }

    /**
     * @param pan (-1.0..1.0)
     * @see BeatboxMessage#CHANNEL_PAN
     */
    public void setPan(float pan) {
        if (pan == this.pan)
            return;
        this.pan = pan;
        if (pan < -1f || pan > 1f)
            throw newRangeException(BeatboxMessage.CHANNEL_PAN, "-1.0..1.0", pan);
        BeatboxMessage.CHANNEL_PAN.send(getRack(), getMachineIndex(), samplerChannel, pan);
    }

    //----------------------------------
    // volume
    //----------------------------------

    /**
     * @see BeatboxMessage#CHANNEL_VOLUME
     */
    public float getVolume() {
        return volume;
    }

    public float queryVolume() {
        return BeatboxMessage.CHANNEL_VOLUME.query(getRack(), getMachineIndex(), samplerChannel);
    }

    /**
     * @param volume (0.0..2.0)
     * @see BeatboxMessage#CHANNEL_VOLUME
     */
    public void setVolume(float volume) {
        if (volume == this.volume)
            return;
        if (volume < 0f || volume > 2f)
            throw newRangeException(BeatboxMessage.CHANNEL_VOLUME, "0.0..2.0", volume);
        this.volume = volume;
        BeatboxMessage.CHANNEL_VOLUME.send(getRack(), getMachineIndex(), samplerChannel, volume);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public WavSamplerChannel() {
    }

    public WavSamplerChannel(MachineNode machineNode, int samplerChannel) {
        super(machineNode);
        this.samplerChannel = samplerChannel;
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
        BeatboxMessage.CHANNEL_MUTE
                .send(getRack(), getMachineIndex(), samplerChannel, mute ? 1 : 0);
        BeatboxMessage.CHANNEL_MUTE_GROUPS.send(getRack(), getMachineIndex(), samplerChannel,
                muteGroups);
        BeatboxMessage.CHANNEL_SOLO
                .send(getRack(), getMachineIndex(), samplerChannel, solo ? 1 : 0);
        BeatboxMessage.CHANNEL_TUNE.send(getRack(), getMachineIndex(), samplerChannel, tune);
        BeatboxMessage.CHANNEL_PUNCH.send(getRack(), getMachineIndex(), samplerChannel, punch);
        BeatboxMessage.CHANNEL_DECAY.send(getRack(), getMachineIndex(), samplerChannel, decay);
        BeatboxMessage.CHANNEL_PAN.send(getRack(), getMachineIndex(), samplerChannel, pan);
        BeatboxMessage.CHANNEL_VOLUME.send(getRack(), getMachineIndex(), samplerChannel, volume);
    }

    @Override
    protected void restoreComponents() {
        setName(queryName());
        setDecay(queryDecay());
        setPan(queryPan());
        setPunch(queryPunch());
        setTune(queryTune());
        setVolume(queryVolume());
        setMute(queryMute());
        setSolo(querySolo());
    }
}
