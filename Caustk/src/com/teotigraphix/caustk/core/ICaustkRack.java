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

package com.teotigraphix.caustk.core;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.groove.LibraryGroup;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.master.MasterDelayNode;
import com.teotigraphix.caustk.node.master.MasterEqualizerNode;
import com.teotigraphix.caustk.node.master.MasterLimiterNode;
import com.teotigraphix.caustk.node.master.MasterReverbNode;
import com.teotigraphix.caustk.node.master.MasterVolumeNode;
import com.teotigraphix.caustk.node.sequencer.SequencerNode;

/**
 * The {@link ICaustkRack} holds the current {@link RackNode} session state.
 * <p>
 * All events dispatched through a {@link NodeBase} uses the rack's
 * {@link #getEventBus()}.
 * <p>
 * The rack is really just a Facade over the {@link RackNode} public API for
 * ease of access.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public interface ICaustkRack extends ISoundGenerator, IRackEventBus {

    /**
     * Returns whether the rack has a valid {@link RackNode} backing state.
     */
    boolean isLoaded();

    /**
     * Returns the current {@link RackNode} native rack state.
     * <p>
     * When listening to the rack's eventBus, there is no need to worry about
     * memory leaks with events because the {@link NodeBase} is not a dispatcher
     * or listener. All views would register the subscribers to the rack's
     * eventBus, which is constant throughout the whole application life cycle.
     */
    RackNode getRackNode();

    String getName();

    String getPath();

    File getFile();

    MasterDelayNode getDelay();

    MasterReverbNode getReverb();

    MasterEqualizerNode getEqualizer();

    MasterLimiterNode getLimiter();

    MasterVolumeNode getVolume();

    Collection<? extends MachineNode> getMachines();

    MachineNode getMachine(int machineIndex);

    SequencerNode getSequencer();

    /**
     * Creates a new {@link RackNode} and returns it.
     * <p>
     * This is the only {@link RackNode} creation method that does NOT assign
     * the node the this rack(which will call {@link RackMessage#BLANKRACK}).
     * <p>
     * This method allows for {@link RackNode}s to be created and initialized
     * before assigning that state to the native rack(this rack).
     */
    RackNode create();

    /**
     * Creates a new {@link RackNode} and returns it.
     * 
     * @param relativeOrAbsolutePath The relative or absolute location of the
     *            <code>.caustic</code> file. The file is for saving, not
     *            loading with this method. See {@link #create(File)} to restore
     *            a {@link RackNode} from an existing <code>.caustic</code>
     *            file.
     */
    RackNode create(String relativeOrAbsolutePath);

    /**
     * Creates a new {@link RackNode} that wraps an existing
     * <code>.caustic</code> file and returns it.
     * <p>
     * After the {@link RackNode} is created, the native rack is cleared with
     * {@link RackMessage#BLANKRACK} and the {@link RackNode#restore()} method
     * is called which restores the internal state of the {@link RackNode} with
     * the state that was loaded into the native rack by the rack node's
     * <code>.caustic</code> file.
     * 
     * @param file The <code>.caustic</code> file that will be used to restore
     *            the {@link RackNode}'s state.
     */
    RackNode create(File file);

    /**
     * Takes the state of the {@link RackNode} and applies it to the
     * {@link CaustkRack} by creating machines and updating all native rack
     * state based on the node graph.
     * <p>
     * The {@link CaustkRack} is reset, native rack cleared and all machines are
     * created through OSC and updated through OSC in the node graph.
     * 
     * @param rackNode The new rack node state.
     */
    void create(RackNode rackNode);

    /**
     * @param libraryGroup
     * @return
     * @throws CausticException
     * @throws IOException
     */
    RackNode create(LibraryGroup libraryGroup) throws CausticException, IOException;

    /**
     * @param libraryGroup
     * @param importPreset
     * @param importEffects
     * @param importPatterns
     * @param importMixer
     * @return
     * @throws CausticException
     * @throws IOException
     */
    RackNode create(LibraryGroup libraryGroup, boolean importPreset, boolean importEffects,
            boolean importPatterns, boolean importMixer) throws CausticException, IOException;

    /**
     * Sets up the {@link RackNode} by setting the instance on the
     * {@link CaustkRack}.
     * 
     * @param rackNode A blank rack node.
     */
    void setup(RackNode rackNode);

    /**
     * Restores a {@link RackNode} state, machines, effects etc.
     * 
     * @param rackNode The {@link RackNode} to restore, this method will fail if
     *            the machines are already native.
     */
    void restore(RackNode rackNode);

    /**
     * Called when a frame changes in the application, update measure and beat
     * positions.
     * 
     * @param deltaTime The amount of time that has changed since the last
     *            frame.
     */
    void frameChanged(float deltaTime);

}
