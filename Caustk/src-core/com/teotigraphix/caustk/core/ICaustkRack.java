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

import com.google.common.eventbus.EventBus;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.master.MasterDelayNode;
import com.teotigraphix.caustk.node.master.MasterEqualizerNode;
import com.teotigraphix.caustk.node.master.MasterLimiterNode;
import com.teotigraphix.caustk.node.master.MasterReverbNode;
import com.teotigraphix.caustk.node.master.MasterVolumeNode;
import com.teotigraphix.caustk.node.sequencer.SequencerNode;
import com.teotigraphix.gdx.app.ICaustkApplication;
import com.teotigraphix.gdx.app.Project;

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
public interface ICaustkRack extends ISoundGenerator {

    /**
     * Returns whether the rack has a valid {@link RackNode} backing state.
     */
    boolean isLoaded();

    EventBus getEventBus();

    ICaustkApplication getApplication();

    ICaustkSerializer getSerializer();

    <T extends Project> T setProject(File file, Class<T> type) throws IOException;

    void setProject(Project project) throws IOException;

    Project getProject();

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

    boolean contains(int machineIndex);

    Collection<? extends MachineNode> machines();

    MachineNode get(int machineIndex);

    SequencerNode getSequencer();

    /**
     * Fills a {@link RackNode} using the .caustic file loaded an restore() on
     * the node structure.
     * <p>
     * The {@link RackNode} will be populated, the original rack node and state
     * will be restored to the rack after the method returns.
     * 
     * @param file The absolute location of the .caustic file to load.
     * @throws IOException
     */
    RackNode fill(File file) throws IOException;

    /**
     * @param group
     * @throws CausticException
     * @throws IOException
     */
    void fill(LibraryGroup group) throws CausticException, IOException;

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
    RackNode fill(LibraryGroup libraryGroup, boolean importPreset, boolean importEffects,
            boolean importPatterns, boolean importMixer) throws CausticException, IOException;

    /**
     * Restores a {@link RackNode} state, machines, effects etc.
     * 
     * @param rackNode The {@link RackNode} to restore, this method will fail if
     *            the machines are already native.
     */
    void restore(RackNode rackNode);

    /**
     * Updates the native rack with the {@link RackNode}'s internal state by
     * sending OSC messages to the core.
     * 
     * @param rackNode The {@link RackNode} to use as the state to initialize
     *            the native rack.
     */
    void update(RackNode rackNode);

    /**
     * Called when a frame changes in the application, update measure and beat
     * positions.
     * 
     * @param deltaTime The amount of time that has changed since the last
     *            frame.
     */
    void frameChanged(float deltaTime);
}
