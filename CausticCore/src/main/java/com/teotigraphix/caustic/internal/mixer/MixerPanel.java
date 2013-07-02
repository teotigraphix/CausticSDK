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

package com.teotigraphix.caustic.internal.mixer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.teotigraphix.caustic.core.ICausticEngine;
import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.core.IPersist;
import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.internal.device.Device;
import com.teotigraphix.caustic.internal.utils.MementoUtil;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.mixer.IMixerDelay;
import com.teotigraphix.caustic.mixer.IMixerPanel;
import com.teotigraphix.caustic.mixer.IMixerReverb;
import com.teotigraphix.caustic.osc.MixerMessage;

/**
 * The default implementation of the {@link IMixerPanel} interface.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class MixerPanel extends Device implements IMixerPanel {

    private static final String MASTER = "master";

    private static final String STEREO_WIDTH = "stereo_width";

    private static final String PAN = "pan";

    private static final String REVERB_SEND = "reverb_send";

    private static final String DELAY_SEND = "delay_send";

    private static final String VOLUME = "volume";

    private static final String HIGH = "high";

    private static final String MID = "mid";

    private static final String BASS = "bass";

    private final MixerData mMasterData = new MixerData(this);

    MixerData getMasterData() {
        return mMasterData;
    }

    private final Map<Integer, MixerData> mMixerInfoMap = new HashMap<Integer, MixerData>();

    //--------------------------------------------------------------------------
    //
    // IMixerPanel API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // delay
    //----------------------------------

    private IMixerDelay mDelay;

    @Override
    public IMixerDelay getDelay() {
        return mDelay;
    }

    @Override
    public void setDelay(IMixerDelay value) {
        mDelay = value;
    }

    //----------------------------------
    // reverb
    //----------------------------------

    private IMixerReverb mReverb;

    @Override
    public IMixerReverb getReverb() {
        return mReverb;
    }

    @Override
    public void setReverb(IMixerReverb value) {
        mReverb = value;
    }

    //----------------------------------
    // Master
    //----------------------------------

    @Override
    public float getMasterBass() {
        return mMasterData.bass;
    }

    float getMasterBass(boolean restore) {
        return MixerMessage.EQ_BASS.query(getEngine(), MASTER);
    }

    @Override
    public void setMasterBass(float value) {
        if (mMasterData.bass == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException(BASS, "-1.0..1.0", value);
        mMasterData.bass = value;
        MixerMessage.EQ_BASS.send(getEngine(), MASTER, value);
    }

    @Override
    public float getMasterMid() {
        return mMasterData.mid;
    }

    float getMasterMid(boolean restore) {
        return MixerMessage.EQ_MID.query(getEngine(), MASTER);
    }

    @Override
    public void setMasterMid(float value) {
        if (mMasterData.mid == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException(MID, "-1.0..1.0", value);
        mMasterData.mid = value;
        MixerMessage.EQ_MID.send(getEngine(), MASTER, value);
    }

    @Override
    public float getMasterHigh() {
        return mMasterData.high;
    }

    float getMasterHigh(boolean restore) {
        return MixerMessage.EQ_HIGH.query(getEngine(), MASTER);
    }

    @Override
    public void setMasterHigh(float value) {
        if (mMasterData.high == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException(HIGH, "-1.0..1.0", value);
        mMasterData.high = value;
        MixerMessage.EQ_HIGH.send(getEngine(), MASTER, value);
    }

    @Override
    public float getMasterVolume() {
        return mMasterData.volume;
    }

    float getMasterVolume(boolean restore) {
        return MixerMessage.VOLUME.query(getEngine(), MASTER);
    }

    @Override
    public void setMasterVolume(float value) {
        if (mMasterData.volume == value)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException(VOLUME, "0.0..2.0", value);
        mMasterData.volume = value;
        MixerMessage.VOLUME.send(getEngine(), MASTER, value);
    }

    //----------------------------------
    // bass
    //----------------------------------

    @Override
    public float getBass(int index) {
        MixerData info = getMixerInfo(index);
        if (info == null)
            return Float.MIN_VALUE;
        return info.bass;
    }

    float getBass(int index, boolean restore) {
        return MixerMessage.EQ_BASS.query(getEngine(), index);
    }

    @Override
    public void setBass(int index, float value) {
        MixerData info = getMixerInfo(index);
        if (info == null || info.bass == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException(BASS, "-1.0..1.0", value);
        info.bass = value;
        MixerMessage.EQ_BASS.send(getEngine(), index, value);
    }

    //----------------------------------
    // mid
    //----------------------------------

    @Override
    public float getMid(int index) {
        MixerData info = getMixerInfo(index);
        if (info == null)
            return Float.MIN_VALUE;
        return info.mid;
    }

    float getMid(int index, boolean restore) {
        return MixerMessage.EQ_MID.query(getEngine(), index);
    }

    @Override
    public void setMid(int index, float value) {
        MixerData info = getMixerInfo(index);
        if (info == null || info.mid == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException(MID, "-1.0..1.0", value);
        info.mid = value;
        MixerMessage.EQ_MID.send(getEngine(), index, value);
    }

    //----------------------------------
    // high
    //----------------------------------

    @Override
    public float getHigh(int index) {
        MixerData info = getMixerInfo(index);
        if (info == null)
            return Float.MIN_VALUE;
        return info.high;
    }

    float getHigh(int index, boolean restore) {
        return MixerMessage.EQ_HIGH.query(getEngine(), index);
    }

    @Override
    public void setHigh(int index, float value) {
        MixerData info = getMixerInfo(index);
        if (info == null || info.high == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException(HIGH, "-1.0..1.0", value);
        info.high = value;
        MixerMessage.EQ_HIGH.send(getEngine(), index, value);
    }

    //----------------------------------
    // delay
    //----------------------------------

    @Override
    public float getDelaySend(int index) {
        MixerData info = getMixerInfo(index);
        if (info == null)
            return Float.MIN_VALUE;
        return info.delay;
    }

    float getDelaySend(int index, boolean restore) {
        return MixerMessage.DELAY_SEND.query(getEngine(), index);
    }

    @Override
    public void setDelaySend(int index, float value) {
        MixerData info = getMixerInfo(index);
        if (info == null || info.delay == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(DELAY_SEND, "0.0..1.0", value);
        info.delay = value;
        MixerMessage.DELAY_SEND.send(getEngine(), index, value);
    }

    //----------------------------------
    // reverb
    //----------------------------------

    @Override
    public float getReverbSend(int index) {
        MixerData info = getMixerInfo(index);
        if (info == null)
            return Float.MIN_VALUE;
        return info.reverb;
    }

    float getReverbSend(int index, boolean restore) {
        return MixerMessage.REVERB_SEND.query(getEngine(), index);
    }

    @Override
    public void setReverbSend(int index, float value) {
        MixerData info = getMixerInfo(index);
        if (info == null || info.reverb == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(REVERB_SEND, "0.0..1.0", value);
        info.reverb = value;
        MixerMessage.REVERB_SEND.send(getEngine(), index, value);
    }

    //----------------------------------
    // pan
    //----------------------------------

    @Override
    public float getPan(int index) {
        MixerData info = getMixerInfo(index);
        if (info == null)
            return Float.MIN_VALUE;
        return info.pan;
    }

    float getPan(int index, boolean restore) {
        return MixerMessage.PAN.query(getEngine(), index);
    }

    @Override
    public void setPan(int index, float value) {
        MixerData info = getMixerInfo(index);
        if (info == null || info.pan == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException(PAN, "-1.0..1.0", value);
        info.pan = value;
        MixerMessage.PAN.send(getEngine(), index, value);
    }

    //----------------------------------
    // stereoWidth
    //----------------------------------

    @Override
    public float getStereoWidth(int index) {
        MixerData info = getMixerInfo(index);
        if (info == null)
            return Float.MIN_VALUE;
        return info.stereoWidth;
    }

    float getStereoWidth(int index, boolean restore) {
        return MixerMessage.STEREO_WIDTH.query(getEngine(), index);
    }

    @Override
    public void setStereoWidth(int index, float value) {
        MixerData info = getMixerInfo(index);
        if (info == null || info.stereoWidth == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(STEREO_WIDTH, "0.0..1.0", value);
        info.stereoWidth = value;
        MixerMessage.STEREO_WIDTH.send(getEngine(), index, value);
    }

    //----------------------------------
    // mute
    //----------------------------------

    @Override
    public boolean isMute(int index) {
        MixerData info = getMixerInfo(index);
        if (info == null)
            return false;
        return info.mute;
    }

    boolean isMute(int index, boolean restore) {
        return MixerMessage.MUTE.query(getEngine(), index) != 0f;
    }

    @Override
    public void setMute(int index, boolean muted) {
        MixerData info = getMixerInfo(index);
        if (info == null || info.mute == muted)
            return;
        info.mute = muted;
        MixerMessage.MUTE.send(getEngine(), index, MementoUtil.booleanToInt(muted));
    }

    //----------------------------------
    // solo
    //----------------------------------

    @Override
    public boolean isSolo(int index) {
        MixerData info = getMixerInfo(index);
        if (info == null)
            return false;
        return info.solo;
    }

    boolean isSolo(int index, boolean restore) {
        return MixerMessage.SOLO.query(getEngine(), index) != 0f;
    }

    @Override
    public void setSolo(int index, boolean soloed) {
        MixerData info = getMixerInfo(index);
        if (info == null || info.solo == soloed)
            return;
        info.solo = soloed;
        MixerMessage.SOLO.send(getEngine(), index, MementoUtil.booleanToInt(soloed));
    }

    //----------------------------------
    // volume
    //----------------------------------

    @Override
    public float getVolume(int index) {
        MixerData info = getMixerInfo(index);
        if (info == null)
            return -1;
        return info.volume;
    }

    float getVolume(int index, boolean restore) {
        return MixerMessage.VOLUME.query(getEngine(), index);
    }

    @Override
    public void setVolume(int index, float value) {
        MixerData info = getMixerInfo(index);
        if (info == null || info.volume == value)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException(VOLUME, "0.0..2.0", value);
        info.volume = value;
        MixerMessage.VOLUME.send(getEngine(), index, value);
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public MixerPanel() {
        setId(MixerPanelConstants.DEVICE_ID);
    }

    //--------------------------------------------------------------------------
    //
    // Public :: Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Adds an {@link IMachine} to the mixer panel.
     * <p>
     * The {@link IMachine} must be added to the mixer panel for sends to work
     * correctly.
     * </p>
     * 
     * @param machine The {@link IMachine}.
     */
    public void addMachine(IMachine machine) {
        if (hasMachine(machine))
            return;

        machineAdd(machine);
    }

    /**
     * Removes an {@link IMachine} from the mixer panel.
     * <p>
     * Note: when an {@link IMachine} is removed from the mixer panel, the
     * levels, pan, delay, reverb and stereo will no longer update through the
     * send methods. An {@link IMachine} must be added to the mixer panel
     * explicitly for sends to work.
     * </p>
     * 
     * @param machine The {@link IMachine}.
     */
    public void removeMachine(IMachine machine) {
        if (!hasMachine(machine))
            return;

        machineRemove(machine);
    }

    /**
     * Returns whether the mixer contains the {@link IMachine}.
     * 
     * @param machine The {@link IMachine}.
     */
    public boolean hasMachine(IMachine machine) {
        return mMixerInfoMap.containsKey(machine.getIndex());
    }

    //    @Override
    //    public void onMachineChanged(IMachine machine, MachineChangeKind kind)
    //    {
    //        if (kind == MachineChangeKind.ADDED || kind == MachineChangeKind.LOADED)
    //        {
    //            addMachine(machine);
    //        }
    //        else if (kind == MachineChangeKind.REMOVED)
    //        {
    //            removeMachine(machine);
    //        }
    //    }

    @Override
    public void restore() {
        // super.restore();

        getDelay().restore();
        getReverb().restore();

        mMasterData.restore();

        for (MixerData data : mMixerInfoMap.values()) {
            data.restore();
        }
    }

    //--------------------------------------------------------------------------
    //
    // Overridden Protected :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    protected void initializeEngine(ICausticEngine engine) {
        super.initializeEngine(engine);

        //        setDelay((IMixerDelay) mRack.getFactory().createMixerEffect(this,
        //                MixerEffectType.DELAY));
        //        setReverb((IMixerReverb) mRack.getFactory().createMixerEffect(this,
        //                MixerEffectType.REVERB));
    }

    @Override
    protected IPersist createPersistable() {
        return new MixerPanelState(this);
    }

    //--------------------------------------------------------------------------
    //
    // Protected :: Methods
    //
    //--------------------------------------------------------------------------

    protected void machineRemove(IMachine machine) {
        mMixerInfoMap.remove(machine.getIndex());
    }

    protected void machineAdd(IMachine machine) {
        MixerData info = new MixerData(this, machine);
        mMixerInfoMap.put(machine.getIndex(), info);
    }

    final MixerData _getMixerInfo(IDevice device) {
        return getMixerInfo(device.getIndex());
    }

    final MixerData getMixerInfo(int machineIndex) {
        return mMixerInfoMap.get(machineIndex);
    }

    final boolean hasMixerInfo(IDevice device) {
        return mMixerInfoMap.containsKey(device.getIndex());
    }

    final boolean hasMixerInfo(int machineIndex) {
        return mMixerInfoMap.containsKey(machineIndex);
    }

    final Set<Entry<Integer, MixerData>> getMixerInfoSet() {
        return mMixerInfoMap.entrySet();
    }

    @Override
    public void copyChannel(IMachine machine, IMemento memento) {
        ((MixerPanelState)mPersistable).copyChannel(machine, memento);
    }

    @Override
    public void pasteChannel(IMachine machine, IMemento memento) {
        ((MixerPanelState)mPersistable).pasteChannel(machine, memento);
    }

    public void pasteMasterChannel(IMemento memento) {
        mMasterData.paste(memento);        
    }

}
