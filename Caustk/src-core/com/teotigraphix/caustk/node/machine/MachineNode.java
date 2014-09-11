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
import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.core.osc.IOSCControl;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.core.osc.RackMessage.RackControl;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.NodeBaseEvents.NodeEvent;
import com.teotigraphix.caustk.node.NodeMetaData;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.effect.EffectsChannel;
import com.teotigraphix.caustk.node.machine.patch.MixerChannel;
import com.teotigraphix.caustk.node.machine.patch.PresetComponent;
import com.teotigraphix.caustk.node.machine.patch.SynthComponent;
import com.teotigraphix.caustk.node.machine.patch.VolumeComponent;
import com.teotigraphix.caustk.node.machine.sequencer.ClipComponent;
import com.teotigraphix.caustk.node.machine.sequencer.PatternSequencerComponent;
import com.teotigraphix.caustk.node.machine.sequencer.TrackComponent;

/**
 * The base node for all {@link MachineNode} subclasses.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see MachineType
 * @see RackNode#createMachine(int, MachineType, String)
 */
public abstract class MachineNode extends NodeBase {

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
    private RackNode rackNode;

    @Tag(51)
    private int index = -1;

    @Tag(52)
    private MachineType type;

    @Tag(53)
    private String name;

    @Tag(54)
    private int channelIndex;

    @Tag(55)
    private VolumeComponent volume;

    @Tag(56)
    private PresetComponent preset;

    @Tag(57)
    private SynthComponent synth;

    @Tag(58)
    private PatternSequencerComponent sequencer;

    @Tag(59)
    private MixerChannel mixer;

    @Tag(60)
    private EffectsChannel effects;

    @Tag(61)
    private TrackComponent track;

    @Tag(62)
    private ClipComponent clips;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // rackNode
    //----------------------------------

    public final RackNode getRackNode() {
        return rackNode;
    }

    public void setRackNode(RackNode rackNode) {
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
     * The machine's {@link MachineType}.
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
     * @see RackMessage#MACHINE_NAME
     * @see MachineNodeNameEvent
     * @see RackControl#MachineName
     */
    public final void setName(String name) {
        if (name.equals(this.name))
            return;
        // XXX Check machine name length 10 char limit
        this.name = name;
        RackMessage.MACHINE_NAME.send(getRack(), index, name);
        post(new MachineNodeNameEvent(this, RackControl.MachineName, name));
    }

    //----------------------------------
    // channelIndex
    //----------------------------------

    /**
     * The channel index within the arranger.
     */
    public Integer getChannelIndex() {
        return channelIndex;
    }

    /**
     * Sets the layout channel index within the arranger.
     * 
     * @param channelIndex The layout channel index.
     * @see MachineNodeChannelIndexEvent
     */
    public void setChannelIndex(Integer channelIndex) {
        if (channelIndex == this.channelIndex)
            return;
        this.channelIndex = channelIndex;
    }

    //----------------------------------
    // preset
    //----------------------------------

    /**
     * The machine's preset patch.
     */
    public PresetComponent getPreset() {
        return preset;
    }

    //----------------------------------
    // volume
    //----------------------------------

    /**
     * The machine's volume out component.
     */
    public VolumeComponent getVolume() {
        return volume;
    }

    protected void setVolume(VolumeComponent volume) {
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
    public EffectsChannel getEffects() {
        return effects;
    }

    //----------------------------------
    // synth
    //----------------------------------

    /**
     * The machine's synth to play notes and set polyphony.
     */
    public SynthComponent getSynth() {
        return synth;
    }

    //----------------------------------
    // sequencer
    //----------------------------------

    /**
     * The machine's pattern sequencer.
     */
    public PatternSequencerComponent getSequencer() {
        return sequencer;
    }

    //----------------------------------
    // track
    //----------------------------------

    /**
     * The machine's track sequencer.
     */
    public TrackComponent getTrack() {
        return track;
    }

    //----------------------------------
    // clips
    //----------------------------------

    /**
     * The machine's clips sequencer.
     */
    public ClipComponent getClips() {
        return clips;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public MachineNode() {
    }

    /**
     * Creates a new {@link MachineNode} with machine index, type and name.
     * <p>
     * All sub components are created but the machine is not added to the native
     * rack, see {@link RackNode#createMachine(int, MachineType, String)}.
     * 
     * @param index The machine index in the native rack.
     * @param type The {@link MachineType}.
     * @param name The 10 character alphanumeric machine name.
     */
    public MachineNode(RackNode rackNode, int index, MachineType type, String name) {
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
        effects.create();
        sequencer.create();
        track.create();
        clips.create();
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
        effects.destroy();
        sequencer.destroy();
        track.destroy();
        clips.destroy();
    }

    @Override
    protected void restoreComponents() {
        volume.restore();
        preset.restore();
        synth.restore();
        mixer.restore();
        effects.restore();
        restorePresetProperties();
        sequencer.restore();
        track.restore();
        clips.restore();
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
        effects.update();
        sequencer.update();
        track.update();
        clips.update();
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
     * Place any component with a call to {@link MachineComponent#restore()} in
     * this method that's state is bound to the machine's patch sound.
     */
    protected abstract void restorePresetProperties();

    /**
     * Initializes the machine's composites.
     */
    protected void intialize() {
        preset = new PresetComponent(this, null);
        volume = new VolumeComponent(this);
        mixer = new MixerChannel(this);
        effects = new EffectsChannel(this);
        synth = new SynthComponent(this);
        sequencer = new PatternSequencerComponent(this);
        track = new TrackComponent(this);
        clips = new ClipComponent(this);
    }

    public void updateMixer(MixerChannel mixer) {
        this.mixer = mixer;
        this.mixer.update();
    }

    public void updateSequencer(PatternSequencerComponent sequencer) {
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
     * Base event for the {@link MachineNodeEvent}.
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
     * @see MachineNode#setName(String)
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
     * @see MachineNode#setChannelIndex(Integer)
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
