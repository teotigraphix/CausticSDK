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

package com.teotigraphix.caustic.rack;

import java.util.Map;

import org.androidtransfuse.event.EventManager;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.core.IRestore;
import com.teotigraphix.caustic.device.IDevice;
import com.teotigraphix.caustic.device.IDeviceFactory;
import com.teotigraphix.caustic.effect.IEffectsRack;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.machine.MachineException;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.mixer.IMixerPanel;
import com.teotigraphix.caustic.output.IOutputPanel;
import com.teotigraphix.caustic.sequencer.ISequencer;

/**
 * The {@link IRack} interface is the top level machine management device.
 * <p>
 * All CausticCore devices such as the mixer, sequencer, outputpanel,
 * effectpanel and machines are accessed and created through this interface.
 * <p>
 * The rack manages the {@link IMachine} map whenever a machine is created or
 * destroyed. The rack also dispatches events when updates occur through the
 * {@link IRackSignals} interface.
 * <p>
 * The full rack state can be loaded and restored through the
 * {@link IPersistable} methods.
 * <p>
 * Using the {@link #loadSong(String)} api, a full .caustic song file can be
 * loaded into the rack. Prior to loading the new song, the rack is completely
 * reinitialized.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IRack extends IDevice, IRestore {
    EventManager getDispatcher();

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    /**
     * Whether the {@link #restore()} method has been called on the current
     * {@link #getSong()} instance and the full rack state has been restored.
     */
    boolean isRestored();

    /**
     * Returns the factory that is used to create all top level devices and
     * effects.
     */
    IDeviceFactory getFactory();

    /**
     * Returns the single instance of the {@link IMixerPanel} found in the rack.
     */
    IMixerPanel getMixerPanel();

    /**
     * Returns the single instance of the {@link IEffectsRack} found in the
     * rack.
     */
    IEffectsRack getEffectsRack();

    /**
     * Returns the single instance of the {@link ISequencer} found in the rack.
     */
    ISequencer getSequencer();

    /**
     * Returns the single instance of the {@link IOutputPanel} found in the
     * rack.
     */
    IOutputPanel getOutputPanel();

    //----------------------------------
    // numTracks
    //----------------------------------

    /**
     * The number of allowed machine tracks created in the rack.
     */
    int getNumTracks();

    /**
     * Returns a map in index order containing all tracks the rack has
     * containing a Machine or null in each track.
     * <p>
     * If a machine exists at <code>0, 2, 4, 5</code>, the track map would look
     * like <code>IMachine, null, IMachine, null, IMachine, IMachine</code>.
     * </p>
     */
    Map<Integer, IMachine> getTrackMap();

    /**
     * Returns a map in index order ONLY containing tracks that hold existing
     * machines.
     * <p>
     * This is useful for quickly enumerating existing machines.
     * </p>
     */
    Map<Integer, IMachine> getMachineMap();

    /**
     * Returns a list of existing machine names.
     */
    Map<Integer, String> getMachineNames();

    /**
     * Returns the number of machines in the rack, this is not the same as
     * {@link #getNumTracks()}.
     */
    int getNumMachines();

    //--------------------------------------------------------------------------
    //
    // Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Loads a .caustic song into memory and initializes the machine by creating
     * and loading default state, effects and patterns.
     * 
     * @param path The full path to the .caustic song file.
     * @see {@link RackSongListener#onSongPreloaded()}
     * @see {@link RackSongListener#onSongLoaded()}
     * @throws CausticException
     */
    void loadSong(String path) throws CausticException;

    /**
     * Saves a .caustic song onto as a *.caustic file.
     * <p>
     * Note: The file will be saved in the /sdcard/caustic/songs directory.
     * </p>
     * 
     * @param name The name of the .caustic song file.
     * @see {@link RackSongListener#onSongSaved()}
     * @throws CausticException
     */
    void saveSong(String name) throws CausticException;

    /**
     * Destroys all machines and removes them from all rack devices.
     * <p>
     * Will destroy the current {@link IRackSong} instance and dispose of it.
     * 
     * @throws MachineException
     */
    void destroySong() throws MachineException;

    /**
     * Creates and returns a new machine at the index specified by type.
     * <p>
     * Note: This method suppresses OSC message sent to the core which means
     * it's only to be used in conjunction with {@link #loadSong(String)}. If
     * used elsewhere, the machines will never be created in the core.
     * </p>
     * <p>
     * The String type is of {@link MachineType}.
     * </p>
     * 
     * @param index The index slot to create the machine at.
     * @param name The non-unique name of the machine.
     * @param type The {@link MachineType} to be created.
     * @throws CausticException An exception happened when creating the machine.
     */
    IMachine addMachineAt(int index, String name, MachineType type) throws CausticException;

    /**
     * Creates an {@link IMachine} and places it at the next available index.
     * <p>
     * The {@link #createSong(String, String)} method must be called before a
     * machine is added to the rack or else an exception will be thrown
     * regarding an illegal state within the rack.
     * 
     * @param name The non-unique name of the machine.
     * @param type The {@link MachineType} to be created.
     * @throws CausticException Machine creation or illegal state exception.
     */
    IMachine addMachine(String name, MachineType type) throws CausticException;

    /**
     * Removes a machine from the rack if it exists.
     * 
     * @param machine The machine id assigned during creation of the machine.
     * @return The machine removed, <code>null</code> if not found.
     * @throws MachineException
     */
    IMachine removeMachine(IMachine machine) throws MachineException;

    /**
     * Removes a machine from the rack if it exists.
     * 
     * @param index The machine index assigned during creation of the machine.
     * @return The machine removed, <code>null</code> if not found.
     * @throws MachineException
     */
    IMachine removeMachineAt(int index) throws MachineException;

    /**
     * Return the machine at the IRack index, this does not relate to the
     * {@link #getTrackMap()} list.
     * 
     * @param index The rack index.
     */
    IMachine getMachine(int index);

    /**
     * Returns whether the machines by index exists in the rack.
     * <p>
     * NOTE: The indexes do not have to be consecutive, this is not a mapping
     * the the {@link #getTrackMap()} list. This index is set when the machine
     * is created.
     * </p>
     * 
     * @param index The machine index.
     */
    boolean hasMachine(int index);

    /**
     * Searches the core's machine list, if found returns the machine name at
     * the index, returns <code>null</code> if not found.
     * 
     * @param index The core machine index to search.
     */
    String queryMachineName(int index);

    /**
     * Searches the core's machine list, if found returns the machine type at
     * the index, returns <code>null</code> if not found.
     * 
     * @param index The core machine index to search.
     */
    String queryMachineType(int index);

    //--------------------------------------------------------------------------
    //
    // Listeners
    //
    //--------------------------------------------------------------------------

    /**
     * Sets the song state change listener for the rack.
     * 
     * @param l The state change listener.
     */
    void setOnSongStateChangeListener(OnSongStateChangeListener l);

    /**
     * A single listener that will be notified when the {@link ISong} in the
     * {@link IRack} goes through it's {@link SongStateChangeKind}s.
     */
    public interface OnSongStateChangeListener {

        /**
         * A callback for the {@link ISong}s {@link SongStateChangeKind}.
         * 
         * @param kind The current state notification.
         */
        void onSongStateChanged(SongStateChangeKind kind);
    }

    /**
     * The state changes an {@link ISong} will experience in the {@link IRack}.
     */
    public enum SongStateChangeKind {

        /**
         * The {@link ISong} has just been added to the {@link IRack}.
         */
        ADDED,

        /**
         * The {@link ISong} has just been removed from the {@link IRack}.
         */
        REMOVED,

        /**
         * The {@link ISong} has just been preloaded which means it is in the
         * core memory but the {@link IRack#restore()} has not been called.
         */
        PRELOADED,

        /**
         * The {@link ISong} has just been loaded which means it is in the core
         * memory but the {@link IRack#restore()} has not been called.
         */
        LOADED,

        /**
         * The {@link IRack#restore()} has been called and the song is
         * successfully resotored to it's previous saved state.
         */
        RESTORED,

        /**
         * The {@link ISong} has been saved to disk.
         */
        SAVED
    }

    /**
     * Adds a machine change listener to the rack.
     * 
     * @param l The state change listener.
     */
    void addOnMachineChangeListener(OnMachineChangeListener l);

    /**
     * Removes a machine change listener from the rack.
     * 
     * @param l The state change listener.
     */
    void removeOnMachineChangeListener(OnMachineChangeListener l);

    /**
     * A single listener that will be notified when the {@link Imachine} in the
     * {@link IRack} goes through it's {@link MachineChangeKind}s.
     */
    public interface OnMachineChangeListener {

        /**
         * A callback for a machine change in the {@link IRack}.
         * 
         * @param machine The {@link IMachine} that has changed.
         * @param kind The {@link MachineChangeKind} change kind.
         */
        void onMachineChanged(IMachine machine, MachineChangeKind kind);
    }

    /**
     * The changes an {@link IMachine} will experience in the {@link IRack}.
     */
    public enum MachineChangeKind {

        /**
         * An {@link IMachine} has been added to the {@link IRack}.
         */
        ADDED,

        /**
         * An {@link IMachine} was loaded from the
         * {@link IRack#loadSong(String)} call.
         */
        LOADED,

        /**
         * An {@link IMachine} has been removed to the {@link IRack}.
         */
        REMOVED,
    }

}
