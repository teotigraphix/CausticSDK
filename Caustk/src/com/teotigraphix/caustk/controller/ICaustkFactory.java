
package com.teotigraphix.caustk.controller;

import java.io.File;
import java.io.FileNotFoundException;

import com.teotigraphix.caustk.controller.command.ICommandManager;
import com.teotigraphix.caustk.controller.core.CaustkController;
import com.teotigraphix.caustk.machine.CastkMasterMixer;
import com.teotigraphix.caustk.machine.CaustkEffect;
import com.teotigraphix.caustk.machine.CaustkLibrary;
import com.teotigraphix.caustk.machine.CaustkMachine;
import com.teotigraphix.caustk.machine.CaustkMasterSequencer;
import com.teotigraphix.caustk.machine.CaustkPatch;
import com.teotigraphix.caustk.machine.CaustkPhrase;
import com.teotigraphix.caustk.machine.CaustkScene;
import com.teotigraphix.caustk.machine.ComponentInfo;
import com.teotigraphix.caustk.machine.ComponentType;
import com.teotigraphix.caustk.machine.ICaustkComponent;
import com.teotigraphix.caustk.machine.MachineType;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.effect.EffectType;
import com.teotigraphix.caustk.service.ISerializeService;

/**
 * @author Michael Schmalle
 */
public interface ICaustkFactory {

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

    CaustkLibrary createLibrary(String name);

    CaustkScene createScene(ComponentInfo info);

    CaustkScene createScene(ComponentInfo info, File absoluteCausticFile);

    CaustkMachine createMachine(ComponentInfo info, MachineType machineType, String machineName);

    CaustkMachine createMachine(int index, MachineType machineType, String machineName);

    CaustkPatch createPatch(ComponentInfo info, MachineType machineType);

    CaustkPatch createPatch(CaustkMachine machine);

    CaustkPhrase createPhrase(ComponentInfo info, MachineType machineType, int bankIndex,
            int patternIndex);

    CaustkPhrase createPhrase(CaustkMachine caustkMachine, int bankIndex, int patternIndex);

    CaustkEffect createEffect(ComponentInfo info, EffectType effectType);

    CaustkEffect createEffect(int slot, EffectType effectType);

    CaustkEffect createEffect(int slot, EffectType effectType, CaustkPatch caustkPatch);

    CastkMasterMixer createMasterMixer(CaustkScene caustkScene);

    CaustkMasterSequencer createMasterSequencer(CaustkScene caustkScene);

    ComponentInfo createInfo(ComponentType type);

    ComponentInfo createInfo(ComponentType type, String name);

    ComponentInfo createInfo(ComponentType type, String relativePath, String name);

    ComponentInfo createInfo(ComponentType type, File relativePath, String name);

    ICaustkComponent create(File componentFile, Class<? extends ICaustkComponent> clazz)
            throws FileNotFoundException;
}
