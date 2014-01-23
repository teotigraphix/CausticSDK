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

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.core.osc.IOSCControl;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.core.osc.RackMessage.RackControl;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.effect.EffectsChannelNode;
import com.teotigraphix.caustk.node.machine.patch.MachineComponent;
import com.teotigraphix.caustk.node.machine.patch.MixerChannelNode;
import com.teotigraphix.caustk.node.machine.patch.PresetNode;
import com.teotigraphix.caustk.node.machine.patch.SynthComponent;
import com.teotigraphix.caustk.node.machine.patch.VolumeComponent;
import com.teotigraphix.caustk.node.machine.sequencer.PatternSequencerNode;
import com.teotigraphix.caustk.node.machine.sequencer.TrackNode;

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

    private MachineType type;

    private String name;

    private VolumeComponent volume;

    private PresetNode preset;

    private SynthComponent synth;

    private MixerChannelNode mixer;

    private EffectsChannelNode effects;

    private PatternSequencerNode sequencer;

    private TrackNode track;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // type
    //----------------------------------

    /**
     * The machine's {@link MachineType}.
     */
    public MachineType getType() {
        return type;
    }

    //    private void setType(MachineType type) {
    //        this.type = type;
    //    }

    //----------------------------------
    // name
    //----------------------------------

    /**
     * The machine's alphanumeric name within the native rack, 10 character
     * limit.
     */
    public String getName() {
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
    public void setName(String name) {
        if (name.equals(this.name))
            return;
        // XXX Check machine name length 10 char limit
        this.name = name;
        RackMessage.MACHINE_NAME.send(getRack(), index, name);
        post(new MachineNodeNameEvent(this, RackControl.MachineName, name));
    }

    //----------------------------------
    // preset
    //----------------------------------

    /**
     * The machine's preset patch.
     */
    public PresetNode getPreset() {
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
    public MixerChannelNode getMixer() {
        return mixer;
    }

    //----------------------------------
    // effects
    //----------------------------------

    /**
     * The machine's effects channel.
     */
    public EffectsChannelNode getEffects() {
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
    public PatternSequencerNode getSequencer() {
        return sequencer;
    }

    //----------------------------------
    // track
    //----------------------------------

    /**
     * The machine's track that relates to the main track sequencer.
     */
    public TrackNode getTrack() {
        return track;
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
    public MachineNode(int index, MachineType type, String name) {
        this.index = index;
        this.type = type;
        this.name = name;
        intialize();
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void create(int index) throws CausticException {
        if (isNative)
            throw new IllegalStateException("machine already exists native");
        this.index = index;
        volume.setIndex(index);
        preset.setIndex(index);
        synth.setIndex(index);
        mixer.setIndex(index);
        effects.setIndex(index);
        sequencer.setIndex(index);
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
        preset = new PresetNode(this);
        volume = new VolumeComponent(this);
        mixer = new MixerChannelNode(this);
        effects = new EffectsChannelNode(this);
        synth = new SynthComponent(this);
        sequencer = new PatternSequencerNode(this);
        track = new TrackNode(this);
    }

    /**
     * Base event for the {@link MachineNodeEvent}.
     * 
     * @author Michael Schmalle
     * @since 1.0
     */
    public static class MachineNodeEvent extends NodeEvent {
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
}
