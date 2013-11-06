
package com.teotigraphix.caustk.live;

import java.io.File;
import java.io.FileNotFoundException;

import com.esotericsoftware.kryo.Kryo;
import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.effect.EffectType;
import com.teotigraphix.caustk.rack.tone.RackTone;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;

/**
 * @author Michael Schmalle
 */
public interface ICaustkFactory {

    /**
     * Returns the absolute location of the component in the library using the
     * <code>rootDirectory</code> and relative file path of the component.
     * 
     * @param component The component to resolve absolute location.
     * @param rootDirectory The root directory to resolve the
     *            {@link ComponentInfo} from.
     */
    File resolveLocation(ICaustkComponent component, File rootDirectory);

    /**
     * Proxied from {@link ICaustkApplication#getRack()}.
     */
    IRack getRack();

    /**
     * Creates an application context that can be passed during component
     * creation.
     */
    ICaustkApplicationContext createContext();

    /**
     * Creates a new {@link ComponentInfo} instance empty.
     * <p>
     * The id, type, creation and modified date are populated during
     * construction.
     * 
     * @param type The {@link ComponentType} being created.
     */
    ComponentInfo createInfo(ComponentType type);

    /**
     * Creates a new {@link ComponentInfo} instance with type and display name.
     * 
     * @param type The {@link ComponentType} being created.
     * @param name The display name of the component, is used as the file name
     *            also.
     */
    ComponentInfo createInfo(ComponentType type, String name);

    /**
     * Creates a new info instance constructing the {@link File} instance from
     * the relativePath and name using the {@link ComponentType#getExtension()}
     * of the type.
     * 
     * @param type The type of component.
     * @param relativePath The relative path of the component.
     * @param name The display name of the component, is used as the file name
     *            also.
     */
    ComponentInfo createInfo(ComponentType type, String relativePath, String name);

    /**
     * Creates a new info instance constructing the {@link File} instance from
     * the relativePath and name using the {@link ComponentType#getExtension()}
     * of the type.
     * 
     * @param type The type of component.
     * @param relativePath The relative path File of the component.
     * @param name The display name of the component, is used as the file name
     *            also.
     */
    ComponentInfo createInfo(ComponentType type, File relativePath, String name);

    IRack createRack();

    Library createLibrary(String name);

    LiveSet createLiveSet(ComponentInfo info);

    RackSet createRackSet(ComponentInfo info);

    RackSet createRackSet(ComponentInfo info, File absoluteCausticFile);

    Machine createMachine(ComponentInfo info, int machineIndex, MachineType machineType,
            String machineName);

    Machine createMachine(RackSet rackSet, int machineIndex, MachineType machineType,
            String machineName);

    Patch createPatch(ComponentInfo info, MachineType machineType);

    Patch createPatch(Machine machine);

    Phrase createPhrase(ComponentInfo info, MachineType machineType, int bankIndex, int patternIndex);

    Phrase createPhrase(Machine caustkMachine, int bankIndex, int patternIndex);

    Effect createEffect(ComponentInfo info, EffectType effectType);

    Effect createEffect(int slot, EffectType effectType);

    Effect createEffect(int slot, EffectType effectType, Patch patch);

    MasterMixer createMasterMixer(RackSet caustkScene);

    MasterSequencer createMasterSequencer(RackSet caustkScene);

    <T extends RackTone> T createRackTone(String machineName, MachineType machineType,
            int machineIndex) throws CausticException;

    RackTone createRackTone(ToneDescriptor descriptor) throws CausticException;

    RackTone createRackTone(Machine machine, ToneDescriptor descriptor) throws CausticException;

    /**
     * Loads and creates a {@link ICaustkComponent} from the serialized
     * component File passed using the <code>clazz</code> type to instantiate
     * the deserialized instance.
     * 
     * @param componentFile The serialized Caustk component.
     * @param clazz The class type of the component to deserialize.
     * @throws FileNotFoundException
     */
    <T extends ICaustkComponent> T load(File componentFile, Class<T> clazz)
            throws FileNotFoundException;

    /**
     * Saves the caustk component to it's specific File location on disk.
     * <p>
     * Uses {@link #resolveLocation(ICaustkComponent, File)} to calculate where
     * the component is saved. The component is serialized into it's binary
     * format.
     * 
     * @param component The caustk component.
     * @return A File pointing to the serialized component if serialization was
     *         successful.
     * @throws FileNotFoundException
     */
    File save(ICaustkComponent component, File rootDirectory) throws FileNotFoundException;

    <T> T copy(T instance);

    Kryo getKryo();

}
