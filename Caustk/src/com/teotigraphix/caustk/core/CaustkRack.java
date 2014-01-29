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
import java.util.Collection;

import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.master.MasterDelayNode;
import com.teotigraphix.caustk.node.master.MasterEqualizerNode;
import com.teotigraphix.caustk.node.master.MasterLimiterNode;
import com.teotigraphix.caustk.node.master.MasterReverbNode;
import com.teotigraphix.caustk.node.master.MasterVolumeNode;

/**
 * The {@link CaustkRack} holds the current {@link RackNode} session state.
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
public class CaustkRack extends CaustkEngine {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private final CaustkRuntime runtime;

    private RackNode rackNode;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    /**
     * Returns whether the rack has a valid {@link RackNode} backing state.
     */
    public final boolean isLoaded() {
        return rackNode != null;
    }

    //----------------------------------
    // rackNode
    //----------------------------------

    /**
     * Returns the current {@link RackNode} native rack state.
     * <p>
     * When listening to the rack's eventBus, there is no need to worry about
     * memory leaks with events because the {@link NodeBase} is not a dispatcher
     * or listener. All views would register the subscribers to the rack's
     * eventBus, which is constant throughout the whole application life cycle.
     */
    public final RackNode getRackNode() {
        return rackNode;
    }

    private void setRackNode(RackNode rackNode) {
        if (rackNode == this.rackNode)
            return;
        RackNode oldNode = this.rackNode;
        if (oldNode != null) {
            oldNode.destroy();
        }
        this.rackNode = rackNode;
        RackMessage.BLANKRACK.send(this);
    }

    //--------------------------------------------------------------------------
    // Public RackNode Facade API
    //--------------------------------------------------------------------------

    //----------------------------------
    // RackNode
    //----------------------------------

    public String getName() {
        return rackNode.getName();
    }

    public String getPath() {
        return rackNode.getPath();
    }

    public File getFile() {
        return rackNode.getAbsoluteFile();
    }

    //----------------------------------
    // MasterNode
    //----------------------------------

    public MasterDelayNode getDelay() {
        return rackNode.getMaster().getDelay();
    }

    public MasterReverbNode getReverb() {
        return rackNode.getMaster().getReverb();
    }

    public MasterEqualizerNode getEqualizer() {
        return rackNode.getMaster().getEqualizer();
    }

    public MasterLimiterNode getLimiter() {
        return rackNode.getMaster().getLimiter();
    }

    public MasterVolumeNode getVolume() {
        return rackNode.getMaster().getVolume();
    }

    //----------------------------------
    // MachineNode
    //----------------------------------

    public Collection<? extends MachineNode> getMachines() {
        return rackNode.getMachines();
    }

    public MachineNode getMachine(int machineIndex) {
        return rackNode.getMachine(machineIndex);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    CaustkRack(CaustkRuntime runtime) {
        super(runtime.getSoundGenerator());
        this.runtime = runtime;
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    /**
     * Creates a new {@link RackNode} and returns it.
     * <p>
     * This is the only {@link RackNode} creation method that does NOT assign
     * the node the this rack(which will call {@link RackMessage#BLANKRACK}).
     * <p>
     * This method allows for {@link RackNode}s to be created and initialized
     * before assigning that state to the native rack(this rack).
     */
    public RackNode create() {
        RackNode rackNode = runtime.getFactory().createRack();
        return rackNode;
    }

    /**
     * Creates a new {@link RackNode} and returns it.
     * 
     * @param relativeOrAbsolutePath The relative or absolute location of the
     *            <code>.caustic</code> file. The file is for saving, not
     *            loading with this method. See {@link #create(File)} to restore
     *            a {@link RackNode} from an existing <code>.caustic</code>
     *            file.
     */
    public RackNode create(String relativeOrAbsolutePath) {
        RackNode rackNode = runtime.getFactory().createRack(relativeOrAbsolutePath);
        restore(rackNode);
        return rackNode;
    }

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
    public RackNode create(File file) {
        RackNode rackNode = runtime.getFactory().createRack(file);
        restore(rackNode);
        return rackNode;
    }

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
    public void create(RackNode rackNode) {
        // the native rack is created based on the state of the node graph
        // machines/patterns/effects in the node graph get created through OSC
        setRackNode(rackNode);
        rackNode.create();
    }

    /**
     * Restores a {@link RackNode} state, machines, effects etc.
     * 
     * @param rackNode The {@link RackNode} to restore, this method will fail if
     *            the machines are already native.
     */
    public void restore(RackNode rackNode) {
        // will save the state of the native rack into the node
        // this basically takes a snap shot of the native rack
        setRackNode(rackNode);
        rackNode.restore();
    }

    /**
     * Called when a frame changes in the application, update measure and beat
     * positions.
     * 
     * @param deltaTime The amount of time that has changed since the last
     *            frame.
     */
    public void frameChanged(float deltaTime) {
        getRackNode().getSequencer().frameChanged(deltaTime);
    }
}
