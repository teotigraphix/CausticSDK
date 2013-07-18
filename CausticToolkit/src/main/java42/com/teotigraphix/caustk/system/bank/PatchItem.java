////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.system.bank;

import java.io.IOException;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.BeatboxTone;
import com.teotigraphix.caustk.tone.PCMSynthTone;
import com.teotigraphix.caustk.tone.SubSynthTone;
import com.teotigraphix.caustk.tone.Tone;

public class PatchItem extends MemorySlotItem {

    private Tone tone;

    /**
     * The machine is only active
     * 
     * @return
     */
    public Tone getTone() {
        return tone;
    }

    private String name;

    //----------------------------------
    // id
    //----------------------------------

    private String id;

    public String getId() {
        return id;
    }

    //    public void setId(String value) {
    //        id = value;
    //    }

    //----------------------------------
    // memento
    //----------------------------------

    private IMemento memento;

    public IMemento getMemento() {
        return memento;
    }

    public void setMemento(IMemento value) {
        memento = value;
    }

    /**
     * @param id The machine name.
     */
    public PatchItem(String name, String id) {
        super();
        this.name = name;
        this.id = id;
    }

    public void copyData(ICaustkController controller, int machineIndex, MachineType machineType)
            throws IOException {
        //        machine = null;
        //        switch (machineType) {
        //            case BASSLINE:
        //                machine = new Bassline(id);
        //                machine.setType(MachineType.BASSLINE);
        //                break;
        //            case BEATBOX:
        //                machine = new Beatbox(id);
        //                machine.setType(MachineType.BEATBOX);
        //                break;
        //            case PCMSYNTH:
        //                machine = new PCMSynth(id);
        //                machine.setType(MachineType.PCMSYNTH);
        //                break;
        //            case SUBSYNTH:
        //                machine = new SubSynth(id);
        //                machine.setType(MachineType.SUBSYNTH);
        //                break;
        //        }
        //
        //        machine.setName(name);
        //        machine.setFactory(controller.getFactory());
        //        machine.setEngine(controller);
        //        machine.setIndex(machineIndex);
        //
        //        memento = XMLMemento.createWriteRoot("patch");
        //
        //        switch (machineType) {
        //            case BASSLINE:
        //                copyBassline(controller, (Bassline)machine, memento);
        //                break;
        //            case BEATBOX:
        //                copyBeatbox(controller, (Beatbox)machine, memento);
        //                break;
        //            case PCMSYNTH:
        //                copyPCMSynth(controller, (PCMSynth)machine, memento);
        //                break;
        //            case SUBSYNTH:
        //                copySubSynth(controller, (SubSynth)machine, memento);
        //                break;
        //        }
        //
        //        copyMixerChannel(controller, machine, memento);
        //        copyEffectChannel(controller, machine, memento);
        //
        //        machine.savePreset(id);
        //        // XXX Copy and delete from the presets directory to the project
        //        // root dir
        //        File presetFile = RuntimeUtils.getCausticPresetsFile(machineType.toString(), id);
        //        if (presetFile.exists())
        //            throw new IOException("Preset file does not exist");
        //
        //        File destFile = new File("");
        //        FileUtils.copyFile(presetFile, destFile);
        //
        //        presetFile.delete();
        //
        //        machine.copy(memento);
        //
        //        machine.setEngine(null);
    }

    void copyBassline(ICaustkController controller, BasslineTone tone, IMemento memento) {
        //machine.getSequencer().restore();
        tone.getOsc1().restore();
        tone.getVolume().restore();
        tone.getFilter().restore();
        tone.getLFO1().restore();
        tone.getDistortion().restore();
    }

    void copySubSynth(ICaustkController controller, SubSynthTone tone, IMemento memento) {
        //machine.getSequencer().restore();
        tone.getFilter().restore();
        tone.getLFO1().restore();
        tone.getLFO2().restore();
        tone.getOsc1().restore();
        tone.getOsc2().restore();
        tone.getVolume().restore();
    }

    void copyPCMSynth(ICaustkController controller, PCMSynthTone tone, IMemento memento) {
        //machine.getSequencer().restore();
        tone.getFilter().restore();
        tone.getLFO1().restore();
        tone.getTuner().restore();
        tone.getSampler().restore();
        tone.getVolume().restore();
    }

    void copyBeatbox(ICaustkController controller, BeatboxTone tone, IMemento memento) {
        //machine.getSequencer().restore();
        tone.getSampler().restore();
        tone.getVolume().restore();
    }

    //    private void copyMixerChannel(ICaustkController controller, Machine machine, IMemento memento) {
    //        MixerPanel panel = (MixerPanel)controller.getFactory().createMixerPanel();
    //        panel.setEngine(controller);
    //        panel.addMachine(machine);
    //
    //        MixerDelay delay = (MixerDelay)controller.getFactory().createMixerEffect(panel,
    //                MixerEffectType.DELAY);
    //        MixerReverb reverb = (MixerReverb)controller.getFactory().createMixerEffect(panel,
    //                MixerEffectType.REVERB);
    //
    //        delay.setDevice(panel);
    //        reverb.setDevice(panel);
    //
    //        panel.setDelay(delay);
    //        panel.setReverb(reverb);
    //
    //        panel.restore();
    //        panel.copyChannel(machine, memento);
    //    }
    //
    //    private void copyEffectChannel(ICaustkController controller, Machine machine, IMemento memento) {
    //        EffectsRack effectsRack = (EffectsRack)controller.getFactory().createEffectRack();
    //        effectsRack.setEngine(controller);
    //        effectsRack.addMachine(machine);
    //        effectsRack.restore();
    //        effectsRack.copyChannel(machine, memento);
    //    }

    @Override
    public String toString() {
        return memento.toString();
    }

}
