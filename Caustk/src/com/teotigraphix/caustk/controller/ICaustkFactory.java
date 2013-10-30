
package com.teotigraphix.caustk.controller;

import java.io.File;
import java.io.FileNotFoundException;

import com.teotigraphix.caustk.controller.command.ICommandManager;
import com.teotigraphix.caustk.controller.core.CaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.machine.ComponentInfo;
import com.teotigraphix.caustk.machine.ComponentType;
import com.teotigraphix.caustk.machine.Effect;
import com.teotigraphix.caustk.machine.ICaustkComponent;
import com.teotigraphix.caustk.machine.Library;
import com.teotigraphix.caustk.machine.Machine;
import com.teotigraphix.caustk.machine.MachineType;
import com.teotigraphix.caustk.machine.MasterMixer;
import com.teotigraphix.caustk.machine.MasterSequencer;
import com.teotigraphix.caustk.machine.Patch;
import com.teotigraphix.caustk.machine.Phrase;
import com.teotigraphix.caustk.machine.Scene;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.effect.EffectType;
import com.teotigraphix.caustk.rack.tone.Tone;
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

    Scene createScene(ComponentInfo info);

    Scene createScene(ComponentInfo info, File absoluteCausticFile);

    Machine createMachine(ComponentInfo info, MachineType machineType, String machineName);

    Machine createMachine(int index, MachineType machineType, String machineName);

    Patch createPatch(ComponentInfo info, MachineType machineType);

    Patch createPatch(Machine machine);

    Phrase createPhrase(ComponentInfo info, MachineType machineType, int bankIndex, int patternIndex);

    Phrase createPhrase(Machine caustkMachine, int bankIndex, int patternIndex);

    Effect createEffect(ComponentInfo info, EffectType effectType);

    Effect createEffect(int slot, EffectType effectType);

    Effect createEffect(int slot, EffectType effectType, Patch caustkPatch);

    MasterMixer createMasterMixer(Scene caustkScene);

    MasterSequencer createMasterSequencer(Scene caustkScene);

    ComponentInfo createInfo(ComponentType type);

    ComponentInfo createInfo(ComponentType type, String name);

    ComponentInfo createInfo(ComponentType type, String relativePath, String name);

    ComponentInfo createInfo(ComponentType type, File relativePath, String name);

    ICaustkComponent create(File componentFile, Class<? extends ICaustkComponent> clazz)
            throws FileNotFoundException;

    Tone createTone(ToneDescriptor descriptor) throws CausticException;

}
