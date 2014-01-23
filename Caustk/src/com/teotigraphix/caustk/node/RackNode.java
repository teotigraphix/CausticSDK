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

package com.teotigraphix.caustk.node;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.core.osc.OSCUtils;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.master.MasterNode;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * The root node of a rack state.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class RackNode extends NodeBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private String path;

    private MasterNode master;

    private Map<Integer, MachineNode> machines = new HashMap<Integer, MachineNode>();

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // path
    //----------------------------------

    /**
     * Returns the relative or absolute path to the <code>.caustic</code> file.
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the <code>.caustic</code> file path.
     * <p>
     * If the path is not absolute, the {@link RuntimeUtils#getSongFile(String)}
     * will be used to calculate the location of the <code>.caustic</code> file
     * within the <code>caustic/songs</code> directory.
     * 
     * @param path The absolute path with <code>.caustic</code> extension.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Returns the base name of the {@link #getPath()}.
     * 
     * @throws IllegalStateException if {@link #getPath()} is null
     */
    public String getName() {
        if (path == null)
            throw new IllegalStateException("path of Rack cannot be null");
        return FilenameUtils.getBaseName(path);
    }

    /**
     * Returns the absolute {@link File} location of the <code>.caustic</code>
     * file.
     * 
     * @throws IllegalStateException if {@link #getPath()} is null
     */
    public File getAbsoluteFile() {
        if (path == null)
            throw new IllegalStateException("path of Rack cannot be null");
        File file = new File(path);
        if (file.isAbsolute())
            return file;
        return new File(RuntimeUtils.getSongsDirectory(), path);
    }

    //----------------------------------
    // master
    //----------------------------------

    /**
     * Returns the {@link MasterNode} of the rack state.
     */
    public MasterNode getMaster() {
        return master;
    }

    //----------------------------------
    // machines
    //----------------------------------

    /**
     * Returns an unmodifiable collection of {@link MachineNode}s.
     */
    public final Collection<? extends MachineNode> getMachines() {
        return Collections.unmodifiableCollection(machines.values());
    }

    /**
     * Returns whether the node graph contains a machine at index.
     * 
     * @param index The machine index.
     */
    public final boolean containsMachine(int index) {
        return machines.containsKey(index);
    }

    /**
     * Returns a {@link MachineNode} at the specified index, or
     * <code>null</code> if not found.
     * 
     * @param machineIndex The machine index.
     */
    @SuppressWarnings("unchecked")
    public final <T extends MachineNode> T getMachine(int machineIndex) {
        return (T)machines.get(machineIndex);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    RackNode() {
        master = new MasterNode();
        machines = new HashMap<Integer, MachineNode>();
    }

    /**
     * Create a new {@link RackNode} with a <code>.caustic</code> file path.
     * 
     * @param path The path can be relative or absolute and must include the
     *            <code>.caustic</code> extension.
     */
    RackNode(String path) {
        this();
        this.path = path;
    }

    /**
     * Create a new {@link RackNode} with a <code>.caustic</code> file.
     * 
     * @param file The file can be relative or absolute and must include the
     *            <code>.caustic</code> extension in it's name.
     */
    RackNode(File file) {
        this(file.getAbsolutePath());
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Creates and adds a machine to this node graph an native rack.
     * 
     * @param index The machine index.
     * @param type The machine type.
     * @param name The machine name.
     * @return A {@link MachineNode} that has been created with
     *         {@link MachineNode#create()}.
     * @see MachineNode#isNative()
     * @throws CausticException machine exists in rack for index
     */
    @SuppressWarnings("unchecked")
    public <T extends MachineNode> T createMachine(int index, MachineType type, String name)
            throws CausticException {
        if (machines.containsKey(index))
            throw new CausticException("machine exists in rack for index: " + index);
        MachineNode machineNode = addMachine(index, type, name);
        createMachine(machineNode);
        return (T)machineNode;
    }

    /**
     * Creates and adds a {@link MachineNode} from a value object.
     * <p>
     * Adds to the {@link #getMachines()} collection and calls
     * {@link MachineNode#create()} to create subcomponents(preset, sequencer
     * etc.).
     * 
     * @param machineNode The {@link MachineNode} value object.
     */
    MachineNode createMachine(MachineNode machineNode) {
        addMachine(machineNode);
        machineNode.create();
        return machineNode;
    }

    /**
     * Destroys and removes a machine from this node graph.
     * 
     * @param index The machine index.
     * @return A {@link MachineNode} that has been destroyed with
     *         {@link MachineNode#destroy()}.
     */
    @SuppressWarnings("unchecked")
    public <T extends MachineNode> T destroyMachine(int index) {
        MachineNode machineNode = removeMachine(index);
        machineNode.destroy();
        return (T)machineNode;
    }

    /**
     * Adds a {@link MachineNode} to the node graph.
     * <p>
     * This method does not call {@link MachineNode#create()}.
     * 
     * @param index The machine index.
     * @param type The {@link MachineType}.
     * @param name The 10 character machine name.
     * @return A new {@link MachineNode}.
     * @see MachineNode#isNative()
     */
    MachineNode addMachine(int index, MachineType type, String name) {
        MachineNode machineNode = getFactory().createMachine(index, type, name);
        addMachine(machineNode);
        return machineNode;
    }

    MachineNode addMachine(MachineNode machineNode) {
        machines.put(machineNode.getIndex(), machineNode);
        return machineNode;
    }

    /**
     * Removes a {@link MachineNode} from the node graph.
     * 
     * @param index The machine index.
     * @return A removed {@link MachineNode}.
     */
    MachineNode removeMachine(int index) {
        MachineNode machineNode = machines.remove(index);
        if (machineNode == null)
            return machineNode;
        return machineNode;
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * Creates and updates native machines using the node's current state.
     */
    @Override
    protected void createComponents() {
        master.create();
        // the MachineNodes must already exist in the state
        for (MachineNode machineNode : machines.values()) {
            machineNode.create();
        }
    }

    @Override
    protected void destroyComponents() {
        // called from CaustkRack.setRackNode() when this node becomes
        // the old node and no longer represents the native rack state
        master.destroy();
        for (MachineNode machineNode : machines.values()) {
            machineNode.destroy();
        }
    }

    @Override
    protected void updateComponents() {
        master.update();
        for (MachineNode machineNode : machines.values()) {
            // calls RackMessage.CREATE
            machineNode.update();
        }
    }

    @Override
    protected void restoreComponents() {
        if (path != null) {
            // restore is special here, we clean the native rack
            RackMessage.BLANKRACK.send(getRack());

            // load the song, the push the native model into the node graph
            RackMessage.LOAD_SONG.send(getRack(), getAbsoluteFile().getAbsolutePath());
        }

        master.restore();
        // machines already created, must use addMachine() through the query
        for (int i = 0; i < 14; i++) {
            String name = OSCUtils.toMachineName(getRack(), i);
            if (name == null || name.equals(""))
                continue;
            MachineType type = OSCUtils.toMachineType(getRack(), i);
            addMachine(i, type, name);
        }
        // push native values into MachineNodes
        for (MachineNode machineNode : machines.values()) {
            machineNode.restore();
        }
    }
}
