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
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.google.common.io.Files;
import com.teotigraphix.caustk.core.CausticError;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.OSCUtils;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.core.osc.RackMessage.RackControl;
import com.teotigraphix.caustk.node.NodeBaseEvents.NodeEvent;
import com.teotigraphix.caustk.node.machine.Machine;
import com.teotigraphix.caustk.node.machine.MachineType;
import com.teotigraphix.caustk.node.machine.patch.MixerChannel.OnRackSoloRefresh;
import com.teotigraphix.caustk.node.master.MasterChannel;
import com.teotigraphix.caustk.node.sequencer.MasterSequencerChannel;
import com.teotigraphix.caustk.utils.core.RuntimeUtils;

/**
 * The root node of a rack state.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class RackInstance extends NodeBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(50)
    private String path;

    @Tag(51)
    private MasterChannel master;

    @Tag(52)
    private Map<Integer, Machine> machines = new HashMap<Integer, Machine>();

    @Tag(53)
    private MasterSequencerChannel sequencer;

    @Tag(54)
    private int selectedIndex = 0;

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
     * If the path is not absolute, the
     * {@link com.teotigraphix.caustk.utils.core.RuntimeUtils#getSongFile(String)}
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
     * Returns the absolute {@link java.io.File} location of the
     * <code>.caustic</code> file.
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
     * Returns the {@link com.teotigraphix.caustk.node.master.MasterChannel} of
     * the rack state.
     */
    public MasterChannel getMaster() {
        return master;
    }

    //----------------------------------
    // machines
    //----------------------------------

    /**
     * Returns an unmodifiable collection of
     * {@link com.teotigraphix.caustk.node.machine.Machine}s.
     */
    public final Collection<? extends Machine> getMachines() {
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
     * Returns a {@link com.teotigraphix.caustk.node.machine.Machine} at the
     * specified index, or <code>null</code> if not found.
     * 
     * @param machineIndex The machine index.
     */
    @SuppressWarnings("unchecked")
    public final <T extends Machine> T getMachine(int machineIndex) {
        return (T)machines.get(machineIndex);
    }

    //----------------------------------
    // sequencer
    //----------------------------------

    /**
     * The rack's main pattern/song sequencer with output transport panel.
     */
    public MasterSequencerChannel getSequencer() {
        return sequencer;
    }

    //----------------------------------
    // sequencer
    //----------------------------------

    /**
     * Returns the selected {@link com.teotigraphix.caustk.node.machine.Machine}
     * .
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Sets the selected {@link com.teotigraphix.caustk.node.machine.Machine}.
     * 
     * @param selectedIndex The new selected machine index.
     * @see com.teotigraphix.caustk.node.RackInstance.RackNodeSelectionEvent
     */
    public void setSelectedIndex(int selectedIndex) {
        if (this.selectedIndex == selectedIndex)
            return;

        Machine lastMachineNode = getSelectedMachine();
        this.selectedIndex = selectedIndex;
        if (lastMachineNode != null)
            lastMachineNode.setSelected(false);
        Machine selectedMachine = getSelectedMachine();
        if (selectedMachine != null)
            selectedMachine.setSelected(true);

        post(new RackNodeSelectionEvent(this, getSelectedMachine(), lastMachineNode));
    }

    /**
     * Sets the selected {@link com.teotigraphix.caustk.node.machine.Machine}.
     * 
     * @param machineNode The new selected
     *            {@link com.teotigraphix.caustk.node.machine.Machine}
     * @throws com.teotigraphix.caustk.core.CausticError
     */
    public void setSelectedIndex(Machine machineNode) {
        if (!containsMachine(machineNode.getIndex()))
            throw new CausticError("Machine does not exist; " + machineNode);
        setSelectedIndex(machineNode.getIndex());
    }

    /**
     * Returns the selected {@link com.teotigraphix.caustk.node.machine.Machine}
     * , if no selection, returns <code>null</code>.
     */
    public Machine getSelectedMachine() {
        return getMachine(selectedIndex);
    }

    public boolean hasSelected() {
        return getMachine(selectedIndex) != null;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public RackInstance() {
        master = new MasterChannel(this);
        machines = new HashMap<Integer, Machine>();
        sequencer = new MasterSequencerChannel(this);
    }

    /**
     * Create a new {@link RackInstance} with a <code>.caustic</code> file path.
     * 
     * @param path The path can be relative or absolute and must include the
     *            <code>.caustic</code> extension.
     */
    public RackInstance(String path) {
        this();
        this.path = path;
    }

    /**
     * Create a new {@link RackInstance} with a <code>.caustic</code> file.
     * 
     * @param file The file can be relative or absolute and must include the
     *            <code>.caustic</code> extension in it's name.
     */
    public RackInstance(File file) {
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
     * @return A {@link com.teotigraphix.caustk.node.machine.Machine} that has
     *         been created with
     *         {@link com.teotigraphix.caustk.node.machine.Machine#create()} .
     * @see com.teotigraphix.caustk.node.machine.Machine#isNative()
     * @throws com.teotigraphix.caustk.core.CausticException machine exists in
     *             rack for index
     * @see com.teotigraphix.caustk.node.RackInstance.RackNodeCreateEvent
     */
    @SuppressWarnings("unchecked")
    public <T extends Machine> T createMachine(int index, MachineType type, String name)
            throws CausticException {
        if (machines.containsKey(index))
            throw new CausticException("machine exists in rack for index: " + index);
        Machine machineNode = addMachine(index, type, name);
        createMachine(machineNode);
        post(new RackNodeCreateEvent(this, RackControl.Create, machineNode));
        return (T)machineNode;
    }

    /**
     * Creates and adds a {@link com.teotigraphix.caustk.node.machine.Machine}
     * from a value object.
     * <p>
     * Adds to the {@link #getMachines()} collection and calls
     * {@link com.teotigraphix.caustk.node.machine.Machine#create()} to create
     * subcomponents(preset, sequencer etc.).
     * 
     * @param machineNode The
     *            {@link com.teotigraphix.caustk.node.machine.Machine} value
     *            object.
     */
    Machine createMachine(Machine machineNode) {
        addMachine(machineNode);
        machineNode.create();
        return machineNode;
    }

    /**
     * Destroys and removes a machine from this node graph.
     * 
     * @param index The machine index.
     * @return A {@link com.teotigraphix.caustk.node.machine.Machine} that has
     *         been destroyed with
     *         {@link com.teotigraphix.caustk.node.machine.Machine#destroy()} .
     * @see com.teotigraphix.caustk.node.RackInstance.RackNodDestroyEvent
     */
    @SuppressWarnings("unchecked")
    public <T extends Machine> T destroyMachine(int index) {
        Machine machineNode = removeMachine(index);
        machineNode.destroy();
        if (selectedIndex == index) {
            for (int i = index; i >= 0; i--) {
                if (machines.containsKey(i))
                    break;
            }
            setSelectedIndex(Math.max(index, 0));
        }
        post(new RackNodDestroyEvent(this, RackControl.Remove, machineNode));
        return (T)machineNode;
    }

    /**
     * Adds a {@link com.teotigraphix.caustk.node.machine.Machine} to the node
     * graph.
     * <p>
     * This method does not call
     * {@link com.teotigraphix.caustk.node.machine.Machine#create()}.
     * 
     * @param index The machine index.
     * @param type The {@link com.teotigraphix.caustk.node.machine.MachineType}.
     * @param name The 10 character machine name.
     * @return A new {@link com.teotigraphix.caustk.node.machine.Machine}.
     * @see com.teotigraphix.caustk.node.machine.Machine#isNative()
     */
    Machine addMachine(int index, MachineType type, String name) {
        Machine machineNode = getFactory().getNodeFactory().createMachine(this, index, type, name);
        addMachine(machineNode);
        return machineNode;
    }

    Machine addMachine(Machine machineNode) {
        machines.put(machineNode.getIndex(), machineNode);
        return machineNode;
    }

    /**
     * Removes a {@link com.teotigraphix.caustk.node.machine.Machine} from the
     * node graph.
     * 
     * @param index The machine index.
     * @return A removed {@link com.teotigraphix.caustk.node.machine.Machine}.
     */
    Machine removeMachine(int index) {
        Machine machineNode = machines.remove(index);
        if (machineNode == null)
            return machineNode;
        return machineNode;
    }

    public void loadSong(File causticFile) throws IOException {
        if (!causticFile.exists())
            throw new IOException(".caustic File not found: " + causticFile);

        RackMessage.LOAD_SONG.send(getRack(), causticFile.getAbsolutePath());
    }

    public File saveSong(String name) {
        RackMessage.SAVE_SONG.send(getRack(), name);
        return RuntimeUtils.getSongFile(name);
    }

    public File saveSongAs(File file) throws IOException {
        File song = saveSong(file.getName().replace(".caustic", ""));
        if (!file.getParentFile().equals(song.getParentFile())
        /*&& file.getName().equals(song.getName())*/) {
            Files.move(song, file.getParentFile());
        }
        return file;
    }

    public void setMute(int machineIndex, boolean selected) {
        for (Machine machineNode : getMachines()) {
            machineNode.getMixer().setSolo(false, false);
        }
        getMachine(machineIndex).getMixer().setMute(selected);
        post(new OnRackSoloRefresh(null));
    }

    public void setSolo(int machineIndex, boolean selected) {
        if (selected) {
            for (Machine machineNode : getMachines()) {
                if (machineNode.getIndex() != machineIndex) {
                    machineNode.getMixer().setMute(true);
                    machineNode.getMixer().setSolo(false, true);
                }
            }
            getMachine(machineIndex).getMixer().setMute(false);
            getMachine(machineIndex).getMixer().setSolo(true, true);
        } else {
            for (Machine machineNode : getMachines()) {
                machineNode.getMixer().setMute(false);
                machineNode.getMixer().setSolo(false);
            }
        }
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
        for (Machine machineNode : machines.values()) {
            machineNode.create();
        }

        sequencer.create();
    }

    @Override
    protected void destroyComponents() {
        // called from CaustkRack.setRackNode() when this node becomes
        // the old node and no longer represents the native rack state
        master.destroy();

        for (Machine machineNode : machines.values()) {
            machineNode.destroy();
        }

        sequencer.destroy();
    }

    @Override
    protected void updateComponents() {
        master.update();

        for (Machine machineNode : machines.values()) {
            // calls RackMessage.CREATE
            machineNode.update();
        }

        sequencer.update();
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
        for (Machine machineNode : machines.values()) {
            machineNode.restore();
        }

        sequencer.restore();
    }

    //--------------------------------------------------------------------------
    // Events
    //--------------------------------------------------------------------------

    /**
     * Base event for the {@link RackInstance}.
     * 
     * @author Michael Schmalle
     * @since 1.0
     */
    public static class RackNodeEvent extends NodeEvent {
        private Machine machineNode;

        public Machine getMachineNode() {
            return machineNode;
        }

        public RackNodeEvent(NodeBase target, RackControl control, Machine machineNode) {
            super(target, control);
            this.machineNode = machineNode;
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see RackInstance#createMachine(int,
     *      com.teotigraphix.caustk.node.machine.MachineType, String)
     */
    public static class RackNodeCreateEvent extends RackNodeEvent {
        public RackNodeCreateEvent(NodeBase target, RackControl control, Machine machineNode) {
            super(target, control, machineNode);
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see RackInstance#destroyMachine(int)
     */
    public static class RackNodDestroyEvent extends RackNodeEvent {
        public RackNodDestroyEvent(NodeBase target, RackControl control, Machine machineNode) {
            super(target, control, machineNode);
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see RackInstance#setSelectedIndex(int)
     */
    public static class RackNodeSelectionEvent extends RackNodeEvent {
        private Machine lastMachineNode;

        public Machine getLastMachineNode() {
            return lastMachineNode;
        }

        public RackNodeSelectionEvent(NodeBase target, Machine machineNode, Machine lastMachineNode) {
            super(target, RackControl.SelectionChange, machineNode);
            this.lastMachineNode = lastMachineNode;
        }
    }
}
