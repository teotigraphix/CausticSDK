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

import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.effect.EffectNode;
import com.teotigraphix.caustk.node.effect.EffectType;
import com.teotigraphix.caustk.node.effect.EffectsChannel;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public interface ICaustkFactory {

    ICaustkRuntime getRuntime();

    /**
     * Creates and returns a new initialized {@link RackNode}.
     */
    RackNode createRack();

    /**
     * Creates and returns a {@link RackNode} that wraps a <code>.caustic</code>
     * file.
     * 
     * @param relativeOrAbsolutePath The relative or absolute
     *            <code>.caustic</code> file location.
     */
    RackNode createRack(String relativeOrAbsolutePath);

    /**
     * Creates and returns a {@link RackNode} that wraps a <code>.caustic</code>
     * file.
     * 
     * @param file The <code>.caustic</code> file location.
     */
    RackNode createRack(File file);

    /**
     * Creates an {@link EffectNode} subclass using the {@link EffectType}.
     * 
     * @param machineNode The machine for the new effect.
     * @param slot The effect slot within the {@link EffectsChannel}.
     * @param effectType The {@link EffectType} of the effect to be created.
     * @return A new {@link EffectNode}, has not been added to an
     *         {@link EffectsChannel}.
     */
    <T extends EffectNode> T createEffect(MachineNode machineNode, int slot, EffectType effectType);

    /**
     * Creates a {@link MachineNode} subclass using the {@link MachineType}.
     * 
     * @param index The machine index lost in the native rack.
     * @param type The {@link MachineType} of machine to create.
     * @param name The machine name (10 character alpha numeric).
     * @return A new {@link MachineNode}, added to the native rack.
     */

    <T extends MachineNode> T createMachine(RackNode rackNode, int index, MachineType type,
            String name);

    LibraryEffect createLibraryEffect(LibraryProduct product, String name, String relativePath,
            EffectNode efffect0, EffectNode efffect1);

    LibraryInstrument createLibraryInstrument(LibraryProduct product, String name,
            String relativePath, MachineNode machineNode);

    LibraryGroup createLibraryGroup(LibraryProduct product, String name, String relativePath);

    LibrarySound createLibrarySound(LibraryProduct product, String name, String relativePath);

    /**
     * Deserializes a JSON String into a {@link Object} of type T.
     * 
     * @param json The valid JSON formated serial String.
     * @param clazz The T implementation result.
     * @return A new instance of the T type deserialized.
     * @throws CausticException Serialization exception.
     */
    <T> T deserialize(String json, Class<? extends Object> clazz) throws CausticException;

    /**
     * Serializes an Object into a JSON String.
     * 
     * @param node The object to serialize.
     * @param prettyPrint Whether to pretty print for debugging.
     * @return A serialized JSON String of the object.
     */
    String serialize(Object node, boolean prettyPrint);

    /**
     * Serializes an Object into a JSON String.
     * <p>
     * Does not pretty print json string.
     * 
     * @param node The object to serialize.
     * @return A serialized JSON String of the object.
     */
    String serialize(Object node);

}
