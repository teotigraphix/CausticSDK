
package com.teotigraphix.caustk.system.bank;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.core.XMLMemento;
import com.teotigraphix.caustic.internal.effect.EffectsRack;
import com.teotigraphix.caustic.internal.machine.Bassline;
import com.teotigraphix.caustic.internal.machine.Beatbox;
import com.teotigraphix.caustic.internal.machine.Machine;
import com.teotigraphix.caustic.internal.machine.PCMSynth;
import com.teotigraphix.caustic.internal.machine.SubSynth;
import com.teotigraphix.caustic.internal.mixer.MixerDelay;
import com.teotigraphix.caustic.internal.mixer.MixerPanel;
import com.teotigraphix.caustic.internal.mixer.MixerReverb;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.mixer.MixerEffectType;
import com.teotigraphix.caustk.controller.ICaustkController;

public class PatchItem extends MemorySlotItem {

    private String name;

    //----------------------------------
    // memento
    //----------------------------------

    private IMemento memento;

    public IMemento getMemento() {
        return memento;
    }

    /**
     * @param name The machine name.
     */
    public PatchItem(String name) {
        super();
        this.name = name;
    }

    public void loadData(ICaustkController controller, int machineIndex, MachineType machineType) {
        // all machine components
        Machine machine = null;
        switch (machineType) {
            case BASSLINE:
                machine = new Bassline(name);
                machine.setType(MachineType.BASSLINE);
                break;
            case BEATBOX:
                machine = new Beatbox(name);
                machine.setType(MachineType.BEATBOX);
                break;
            case PCMSYNTH:
                machine = new PCMSynth(name);
                machine.setType(MachineType.PCMSYNTH);
                break;
            case SUBSYNTH:
                machine = new SubSynth(name);
                machine.setType(MachineType.SUBSYNTH);
                break;
        }

        machine.setFactory(controller.getFactory());
        machine.setEngine(controller);
        machine.setIndex(machineIndex);

        memento = XMLMemento.createWriteRoot("patch");

        switch (machineType) {
            case BASSLINE:
                copyBassline(controller, (Bassline)machine, memento);
                break;
            case BEATBOX:
                copyBeatbox(controller, (Beatbox)machine, memento);
                break;
            case PCMSYNTH:
                copyPCMSynth(controller, (PCMSynth)machine, memento);
                break;
            case SUBSYNTH:
                copySubSynth(controller, (SubSynth)machine, memento);
                break;
        }

        machine.copy(memento);

        copyMixerChannel(controller, machine, memento);
        copyEffectChannel(controller, machine, memento);

        machine.setEngine(null);
    }

    void copyBassline(ICaustkController controller, Bassline machine, IMemento memento) {
        //machine.getSequencer().restore();
        machine.getOsc1().restore();
        machine.getVolume().restore();
        machine.getFilter().restore();
        machine.getLFO1().restore();
        machine.getDistortion().restore();
    }

    void copySubSynth(ICaustkController controller, SubSynth machine, IMemento memento) {
        //machine.getSequencer().restore();
        machine.getFilter().restore();
        machine.getLFO1().restore();
        machine.getLFO2().restore();
        machine.getOsc1().restore();
        machine.getOsc2().restore();
        machine.getVolume().restore();
    }

    void copyPCMSynth(ICaustkController controller, PCMSynth machine, IMemento memento) {
        //machine.getSequencer().restore();
        machine.getFilter().restore();
        machine.getLFO1().restore();
        machine.getPitch().restore();
        machine.getSampler().restore();
        machine.getVolume().restore();
    }

    void copyBeatbox(ICaustkController controller, Beatbox machine, IMemento memento) {
        //machine.getSequencer().restore();
        machine.getSampler().restore();
        machine.getVolume().restore();
    }

    private void copyMixerChannel(ICaustkController controller, Machine machine, IMemento memento) {
        MixerPanel panel = (MixerPanel)controller.getFactory().createMixerPanel();
        panel.setEngine(controller);
        panel.addMachine(machine);

        MixerDelay delay = (MixerDelay)controller.getFactory().createMixerEffect(panel,
                MixerEffectType.DELAY);
        MixerReverb reverb = (MixerReverb)controller.getFactory().createMixerEffect(panel,
                MixerEffectType.REVERB);

        delay.setDevice(panel);
        reverb.setDevice(panel);

        panel.setDelay(delay);
        panel.setReverb(reverb);

        panel.restore();
        panel.copyChannel(machine, memento);
    }

    private void copyEffectChannel(ICaustkController controller, Machine machine, IMemento memento) {
        EffectsRack effect = (EffectsRack)controller.getFactory().createEffectRack();
        effect.setEngine(controller);
        effect.addMachine(machine);
        effect.restore();

        effect.copyChannel(machine, memento);
    }

    @Override
    public String toString() {
        return memento.toString();
    }
}
