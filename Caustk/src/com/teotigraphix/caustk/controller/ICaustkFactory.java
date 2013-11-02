
package com.teotigraphix.caustk.controller;

import java.io.File;
import java.io.FileNotFoundException;

import com.teotigraphix.caustk.controller.command.ICommandManager;
import com.teotigraphix.caustk.controller.core.CaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.live.ComponentInfo;
import com.teotigraphix.caustk.live.ComponentType;
import com.teotigraphix.caustk.live.Effect;
import com.teotigraphix.caustk.live.ICaustkComponent;
import com.teotigraphix.caustk.live.Library;
import com.teotigraphix.caustk.live.LiveSet;
import com.teotigraphix.caustk.live.Machine;
import com.teotigraphix.caustk.live.MachineType;
import com.teotigraphix.caustk.live.MasterMixer;
import com.teotigraphix.caustk.live.MasterSequencer;
import com.teotigraphix.caustk.live.Patch;
import com.teotigraphix.caustk.live.Phrase;
import com.teotigraphix.caustk.live.RackSet;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.effect.EffectType;
import com.teotigraphix.caustk.rack.tone.RackTone;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;
import com.teotigraphix.caustk.service.ISerializeService;

/**
 * @author Michael Schmalle
 */
public interface ICaustkFactory {

    /**
     * Proxied from {@link ICaustkApplication#getRack()}.
     */
    IRack getRack();

    /**
     * The main {@link CaustkController} instance that instrumentates the whole
     * application sequencing from patterns, parts, presets, memory and all
     * other things needing controlling.
     */
    ICaustkController createController();

    /**
     * Creates the single {@link ISerializeService} for the application's
     * controller.
     * 
     * @return An instance of the {@link ISerializeService}
     */
    ISerializeService createSerializeService();

    /**
     * Creates the single {@link ICommandManager} for the application's
     * controller.
     * 
     * @return An instance of the {@link ICommandManager}
     */
    ICommandManager createCommandManager();

    /**
     * Creates the single {@link IProjectManager} for the application's
     * controller.
     * 
     * @return An instance of the {@link IProjectManager}
     */
    IProjectManager createProjectManager();

    IRack createRack();

    Library createLibrary(String name);

    LiveSet createLiveSet(ComponentInfo info);

    RackSet createRackSet(ComponentInfo info);

    RackSet createRackSet(ComponentInfo info, File absoluteCausticFile);

    Machine createMachine(ComponentInfo info, MachineType machineType, String machineName);

    Machine createMachine(RackSet rackSet, int index, MachineType machineType, String machineName);

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

    RackTone createTone(Machine machine, ToneDescriptor descriptor) throws CausticException;

}
