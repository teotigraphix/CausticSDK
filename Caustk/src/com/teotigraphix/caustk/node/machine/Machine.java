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

package com.teotigraphix.caustk.node.machine;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.IOSCControl;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.core.osc.RackMessage.RackControl;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.NodeBaseEvents.NodeEvent;
import com.teotigraphix.caustk.node.NodeMetaData;
import com.teotigraphix.caustk.node.RackInstance;
import com.teotigraphix.caustk.node.effect.EffectChannel;
import com.teotigraphix.caustk.node.machine.patch.MixerChannel;
import com.teotigraphix.caustk.node.machine.patch.PresetChannel;
import com.teotigraphix.caustk.node.machine.patch.SynthChannel;
import com.teotigraphix.caustk.node.machine.patch.VolumeChannel;
import com.teotigraphix.caustk.node.machine.sequencer.ClipChannel;
import com.teotigraphix.caustk.node.machine.sequencer.SequencerChannel;
import com.teotigraphix.caustk.node.machine.sequencer.TrackChannel;

/**
 * The base node for all {@link Machine} subclasses.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see com.teotigraphix.caustk.node.machine.MachineType
 * @see RackInstance#createMachine(int, com.teotigraphix.caustk.node.machine.MachineType,
 *      String)
 */
public abstract class Machine extends NodeBase {

    private transient boolean isNative;

    /**
     * Returns whether this machine has been created in the native rack(true) or
     * just exists in the node graph(false).
     */
    public boolean isNative() {
        return isNative;
    }

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(50)
    private RackInstance rackNode;

    @Tag(51)
    private int index = -1;

    @Tag(52)
    private MachineType type;

    @Tag(53)
    private String name;

    @Tag(54)
    private int channelIndex;

    @Tag(55)
    private VolumeChannel volume;

    @Tag(56)
    private PresetChannel preset;

    @Tag(57)
    private SynthChannel synth;

    @Tag(58)
    private SequencerChannel sequencer;

    @Tag(59)
    private MixerChannel mixer;

    @Tag(60)
    private EffectChannel effect;

    @Tag(61)
    private TrackChannel track;

    @Tag(62)
    private ClipChannel clip;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // rackNode
    //----------------------------------

    public final RackInstance getRackNode() {
        return rackNode;
    }

    public void setRackNode(RackInstance rackNode) {
        this.rackNode = rackNode;
    }

    //----------------------------------
    // index
    //----------------------------------

    /**
     * Returns the machine index.
     */
    public final int getIndex() {
        return index;
    }

    //----------------------------------
    // type
    //----------------------------------

    /**
     * The machine's {@link com.teotigraphix.caustk.node.machine.MachineType}.
     */
    public final MachineType getType() {
        return type;
    }

    //----------------------------------
    // name
    //----------------------------------

    /**
     * The machine's alphanumeric name within the native rack, 10 character
     * limit.
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets the machines name and updates the native rack.
     * 
     * @param name The new 10 character name.
     * @see com.teotigraphix.caustk.core.osc.RackMessage#MACHINE_NAME
     * @see com.teotigraphix.caustk.node.machine.Machine.MachineNodeNameEvent
     * @see com.teotigraphix.caustk.core.osc.RackMessage.RackControl#MachineName
     */
    public final void setName(String name) {
        if (name.equals(this.name))
            return;

        if (name.length() > 10)
            name = name.substring(0, 9);

        this.name = name;

        RackMessage.MACHINE_NAME.send(getRack(), index, name);
        post(new MachineNodeNameEvent(this, RackControl.MachineName, name));
    }

    //    //----------------------------------
    //    // channelIndex
    //    //----------------------------------
    //
    //    /**
    //     * The channel index within the arranger.
    //     */
    //    public Integer getChannelIndex() {
    //        return channelIndex;
    //    }
    //
    //    /**
    //     * Sets the layout channel index within the arranger.
    //     * 
    //     * @param channelIndex The layout channel index.
    //     * @see MachineNodeChannelIndexEvent
    //     */
    //    public void setChannelIndex(Integer channelIndex) {
    //        if (channelIndex == this.channelIndex)
    //            return;
    //        this.channelIndex = channelIndex;
    //    }

    //----------------------------------
    // preset
    //----------------------------------

    /**
     * The machine's preset patch.
     */
    public PresetChannel getPreset() {
        return preset;
    }

    //----------------------------------
    // volume
    //----------------------------------

    /**
     * The machine's volume out component.
     */
    public VolumeChannel getVolume() {
        return volume;
    }

    protected void setVolume(VolumeChannel volume) {
        this.volume = volume;
    }

    //----------------------------------
    // mixer
    //----------------------------------

    /**
     * The machine's mixer channel.
     */
    public MixerChannel getMixer() {
        return mixer;
    }

    //----------------------------------
    // effects
    //----------------------------------

    /**
     * The machine's effects channel.
     */
    public EffectChannel getEffect() {
        return effect;
    }

    //----------------------------------
    // synth
    //----------------------------------

    /**
     * The machine's synth to play notes and set polyphony.
     */
    public SynthChannel getSynth() {
        return synth;
    }

    //----------------------------------
    // sequencer
    //----------------------------------

    /**
     * The machine's pattern sequencer.
     */
    public SequencerChannel getSequencer() {
        return sequencer;
    }

    //----------------------------------
    // track
    //----------------------------------

    /**
     * The machine's track sequencer.
     */
    public TrackChannel getTrack() {
        return track;
    }

    //----------------------------------
    // clips
    //----------------------------------

    /**
     * The machine's clips sequencer.
     */
    public ClipChannel getClips() {
        return clip;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public Machine() {
    }

    /**
     * Creates a new {@link Machine} with machine index, type and name.
     * <p>
     * All sub components are created but the machine is not added to the native
     * rack, see
     * {@link RackInstance#createMachine(int, com.teotigraphix.caustk.node.machine.MachineType, String)}.
     * 
     * @param index The machine index in the native rack.
     * @param type The {@link com.teotigraphix.caustk.node.machine.MachineType}.
     * @param name The 10 character alphanumeric machine name.
     */
    public Machine(RackInstance rackNode, int index, MachineType type, String name) {
        this.rackNode = rackNode;
        this.index = index;
        this.type = type;
        this.name = name;
        setData(new NodeMetaData(this));
        intialize();
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void create(int index) throws CausticException {
        if (isNative)
            throw new IllegalStateException("machine already exists native");
        this.index = index;
        create();
    }

    /**
     * Refreshes all composites that patch data relies on.
     * 
     * @see NodeBase#restore()
     */
    public void onPresetChanged() {
        restorePresetProperties();
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
        if (isNative)
            throw new IllegalStateException("Machine is native");
        RackMessage.CREATE.send(getRack(), type.getType(), name, index);
        isNative = true;
        volume.create();
        preset.create();
        synth.create();
        mixer.create();
        effect.create();
        sequencer.create();
        track.create();
        clip.create();
    }

    @Override
    protected void destroyComponents() {
        if (!isNative)
            //            throw new IllegalStateException("Machine is not native");
            RackMessage.REMOVE.send(getRack(), index);
        isNative = false;
        volume.destroy();
        preset.destroy();
        synth.destroy();
        mixer.destroy();
        effect.destroy();
        sequencer.destroy();
        track.destroy();
        clip.destroy();
    }

    @Override
    protected void restoreComponents() {
        volume.restore();
        preset.restore();
        synth.restore();
        mixer.restore();
        effect.restore();
        restorePresetProperties();
        sequencer.restore();
        track.restore();
        clip.restore();
    }

    @Override
    protected void updateComponents() {
        // the RackNode calls us, and its our job to create
        RackMessage.CREATE.send(getRack(), type.getType(), name, index);
        isNative = true;
        volume.update();
        preset.update();
        synth.update();
        mixer.update();
        effect.update();
        sequencer.update();
        track.update();
        clip.update();
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Called by {@link #restore()} or {@link #onPresetChanged()}.
     * <p>
     * Will restore all composites components with native values, will not
     * update sequencer, mixer etc.
     * <p>
     * Place any component with a call to {@link MachineChannel#restore()} in
     * this method that's state is bound to the machine's patch sound.
     */
    protected abstract void restorePresetProperties();

    /**
     * Initializes the machine's composites.
     */
    protected void intialize() {
        preset = new PresetChannel(this, null);
        volume = new VolumeChannel(this);
        mixer = new MixerChannel(this);
        effect = new EffectChannel(this);
        synth = new SynthChannel(this);
        sequencer = new SequencerChannel(this);
        track = new TrackChannel(this);
        clip = new ClipChannel(this);
    }

    public void updateMixer(MixerChannel mixer) {
        this.mixer = mixer;
        this.mixer.update();
    }

    public void updateSequencer(SequencerChannel sequencer) {
        this.sequencer = sequencer;
        this.sequencer.update();
    }

    @Override
    public String toString() {
        return name + ":" + type;
    }

    //--------------------------------------------------------------------------
    // Events
    //--------------------------------------------------------------------------

    /**
     * Base event for the
     * {@link com.teotigraphix.caustk.node.machine.Machine.MachineNodeEvent}
     * .
     * 
     * @author Michael Schmalle
     * @since 1.0
     */
    public static class MachineNodeEvent extends NodeEvent {
        public MachineNodeEvent(NodeBase target) {
            super(target);
        }

        public MachineNodeEvent(NodeBase target, IOSCControl control) {
            super(target, control);
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see Machine#setName(String)
     */
    public static class MachineNodeNameEvent extends MachineNodeEvent {
        private String name;

        public String getName() {
            return name;
        }

        public MachineNodeNameEvent(NodeBase target, RackControl control, String name) {
            super(target, control);
            this.name = name;
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see Machine#setChannelIndex(Integer)
     */
    public static class MachineNodeChannelIndexEvent extends MachineNodeEvent {
        private int channelIndex;

        public int getChannelIndex() {
            return channelIndex;
        }

        public MachineNodeChannelIndexEvent(NodeBase target, int channelIndex) {
            super(target);
            this.channelIndex = channelIndex;
        }
    }

}
