////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.part;

import java.io.File;
import java.util.Collection;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.rack.IRack;

/**
 * The {@link ISoundGenerator} API is the toplevel interface for creating and
 * managing sound generators IE {@link IMachine}s.
 * <p>
 * This management is achieved through {@link IPart} collections that can be
 * accessed through this API.
 * </p>
 * <p>
 * By the time a client wants to create an application with an {@link IPart},
 * the setup and tear down of parts gets pretty complicated. This interface
 * simplifies this process by encapsulating the creation and destruction process
 * along with the management, muting, soloing etc of the part collection.
 * </p>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ISoundGenerator {

    //--------------------------------------------------------------------------
    // 
    //  Properties
    // 
    //--------------------------------------------------------------------------

    /**
     * Returns an {@link IPart} by id, if the part is not found
     * <code>null</code> is returned.
     * <p>
     * The part index does not correspond to the tone index within a part. The
     * tone index corresponds to the machine id found within the rack. The sound
     * generator sets the index of the part.
     * 
     * @param partId The unique string part id.
     */
    IPart getPart(int index);

    /**
     * Returns an unmodifiable List of the current parts created.
     * <p>
     * Note: Since the {@link IRack} can have inconsecutive machine placements
     * within it's map, this list is only a collection of {@link IPart}s. Use
     * the {@link IPart#getIndex()} to find the real location within the rack of
     * the part's sound source's tone.
     * </p>
     */
    Collection<IPart> getParts();

    /**
     * Returns an unmodifiable list of {@link IPart}s that have been muted.
     */
    Collection<IPart> getMuteParts();

    /**
     * Returns an unmodifiable list of {@link IPart}s that have been soled.
     */
    Collection<IPart> getSoloParts();

    /**
     * Loads an existing {@link IMachine} into a new {@link IPart} instance.
     * <p>
     * Any patterns existing in the machine's pattern banks will be used to
     * create {@link ISequencerPhrase} for each pattern.
     * </p>
     * <p>
     * When the loading process is complete, the initialized property of the
     * part is set to <code>true</code>.
     * </p>
     * 
     * @param machine The machine to use as the part's {@link ITone}.
     * @see IRack#loadSong(String)
     * @throws CausticException
     */
    IPart load(IMachine machine) throws CausticException;

    /**
     * Creates a new {@link IPart} with tone depending on the
     * {@link MachineType} passed.
     * 
     * @param partId The unique id of the new part.
     * @param name The human readable part name.
     * @param type The type of {@link ITone} to create for the part.
     * @param patch The {@link IPatch} to load into the {@link IPart}.
     * @return A new {@link IPart}.
     * @throws CausticException
     */
    IPart create(String partId, String name, MachineType type, IPatch patch)
            throws CausticException;

    void loadPreset(IPart part, File file) throws CausticException;

    void savePreset(IPart part, File file) throws CausticException;

    void savePreset(IPart part) throws CausticException;

    /**
     * Mutes all parts.
     */
    void allMute();

    /**
     * Unmutes all parts.
     */
    void noMute();

    /**
     * Inverses the mute status of all parts.
     */
    void inverseMute();

    //void clearParts();
}
