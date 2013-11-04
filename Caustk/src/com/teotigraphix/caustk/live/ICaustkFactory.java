
package com.teotigraphix.caustk.live;

import java.io.File;
import java.io.FileNotFoundException;

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
     * Proxied from {@link ICaustkApplication#getRack()}.
     */
    IRack getRack();

    ICaustkApplicationContext createContext();

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

    Effect createEffect(int slot, EffectType effectType, Patch caustkPatch);

    MasterMixer createMasterMixer(RackSet caustkScene);

    MasterSequencer createMasterSequencer(RackSet caustkScene);

    ComponentInfo createInfo(ComponentType type);

    ComponentInfo createInfo(ComponentType type, String name);

    ComponentInfo createInfo(ComponentType type, String relativePath, String name);

    ComponentInfo createInfo(ComponentType type, File relativePath, String name);

    ICaustkComponent create(File componentFile, Class<? extends ICaustkComponent> clazz)
            throws FileNotFoundException;

    RackTone createRackTone(ToneDescriptor descriptor) throws CausticException;

    RackTone createRackTone(Machine machine, ToneDescriptor descriptor) throws CausticException;

}
