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

package com.teotigraphix.caustk.sound;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.core.Dispatcher;
import com.teotigraphix.caustic.core.ICausticEngine;
import com.teotigraphix.caustic.core.IDispatcher;
import com.teotigraphix.caustic.internal.machine.Machine;
import com.teotigraphix.caustic.machine.IBassline;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.machine.ISynth;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.osc.RackMessage;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.PCMSynthTone;
import com.teotigraphix.caustk.tone.RhythmSet;
import com.teotigraphix.caustk.tone.SubSynthTone;
import com.teotigraphix.caustk.tone.SynthTone;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.ToneDescriptor;

public class CaustkSoundSource implements ICaustkSoundSource {

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // soundMode
    //----------------------------------

    private SoundMode soundMode;

    @Override
    public SoundMode getSoundMode() {
        return soundMode;
    }

    @Override
    public void setSoundMode(SoundMode value) {
        soundMode = value;
    }

    //----------------------------------
    // octave
    //----------------------------------

    private int octave;

    @Override
    public int getOctave() {
        return octave;
    }

    @Override
    public void setOctave(int value) {
        octave = value;
    }

    //----------------------------------
    // dispatcher
    //----------------------------------

    private final IDispatcher dispatcher;

    @Override
    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    //----------------------------------
    // controller
    //----------------------------------

    private ICaustkController controller;

    protected ICaustkController getController() {
        return controller;
    }

    protected ICausticEngine getEngine() {
        return controller.getSoundGenerator();
    }

    //----------------------------------
    // tones
    //----------------------------------

    private static Map<Integer, Tone> tones = new HashMap<Integer, Tone>();

    @Override
    public int getToneCount() {
        return tones.size();
    }

    @Override
    public Collection<Tone> getTones() {
        return Collections.unmodifiableCollection(tones.values());
    }

    @Override
    public Tone getTone(int index) {
        return tones.get(index);
    }

    private static Map<Integer, IMachine> machines = new HashMap<Integer, IMachine>();

    public static IMachine getMachine(int index) {
        return machines.get(index);
    }

    public static Collection<IMachine> machines() {
        return machines.values();
    }

    public CaustkSoundSource(ICaustkController controller) {
        this.controller = controller;

        controller.registerAPI(SoundMixerAPI.class, new SoundMixerAPI(controller));

        dispatcher = new Dispatcher();

        tones = new HashMap<Integer, Tone>();
        machines = new HashMap<Integer, IMachine>();

        getDispatcher().register(OnSoundSourceInitialValue.class,
                new EventObserver<OnSoundSourceInitialValue>() {
                    @Override
                    public void trigger(OnSoundSourceInitialValue object) {
                        System.out.println("Original value:" + object.getValue());
                    }
                });
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    /**
     * Triggers a note on for the part's tone.
     * <p>
     * Using this method automatically adjusts the pitch of the tone based on
     * the current {@link #getOctave()} of the sound source.
     * 
     * @param part
     * @param pitch
     * @param velocity
     */
    @Override
    public void noteOn(Tone tone, int pitch, float velocity) {
        int semitones = getOctave() * 12;
        ISynth synth = (ISynth)tone.getMachine();
        synth.getSynth().noteOn(pitch + semitones, velocity);
    }

    @Override
    public void noteOff(Tone tone, int pitch) {
        int semitones = getOctave() * 12;
        ISynth synth = (ISynth)tone.getMachine();
        synth.getSynth().noteOff(pitch + semitones);
    }

    @Override
    public Tone create(ToneDescriptor descriptor) throws CausticException {
        Tone tone = null;
        MachineType machineType = descriptor.getMachineType();
        switch (machineType) {
            case BASSLINE:
                tone = createBassline(descriptor.getId());
                break;
            case BEATBOX:
                tone = createBeatbox(descriptor.getId());
                break;
            case PCMSYNTH:
                tone = createPCMSynth(descriptor.getId());
                break;
            case SUBSYNTH:
                tone = createSubSynth(descriptor.getId());
                break;
            default:
                break;
        }
        return tone;
    }

    BasslineTone createBassline(String toneId) throws CausticException {
        return (BasslineTone)createSynthChannel(nextIndex(), toneId, MachineType.BASSLINE);
    }

    RhythmSet createBeatbox(String toneId) throws CausticException {
        return (RhythmSet)createSynthChannel(nextIndex(), toneId, MachineType.BEATBOX);
    }

    SynthTone createPCMSynth(String toneId) throws CausticException {
        return (PCMSynthTone)createSynthChannel(nextIndex(), toneId, MachineType.PCMSYNTH);
    }

    SynthTone createSubSynth(String toneId) throws CausticException {
        return (SubSynthTone)createSynthChannel(nextIndex(), toneId, MachineType.SUBSYNTH);
    }

    public void destroyTone(int index) {
        destroyTone(getTone(index));
    }

    public void destroyTone(Tone tone) {
        int index = tone.getIndex();
        RackMessage.REMOVE.send(getEngine(), index);
        toneRemove(tone);
    }

    public void clearAndReset() {
        getController().getDispatcher().trigger(new OnSoundSourceClear());

        ArrayList<Tone> remove = new ArrayList<Tone>(tones.values());
        for (Tone tone : remove)
            destroyTone(tone);

        RackMessage.BLANKRACK.send(getEngine());

        getController().getDispatcher().trigger(new OnSoundSourceReset());
    }

    //--------------------------------------------------------------------------
    // Protected Method API
    //--------------------------------------------------------------------------

    Tone createSynthChannel(String machineId, MachineType machineType) throws CausticException {
        return createSynthChannel(nextIndex(), machineId, machineType);
    }

    Tone createSynthChannel(int index, String machineId, MachineType machineType)
            throws CausticException {
        if (tones.containsKey(index)) {
            throw new CausticException("{" + index + "} tone is already defined");
        }

        Machine machine = (Machine)getController().getFactory().create(machineId, machineType);

        if (machine == null) {
            throw new CausticException("{" + machineType + "} IMachine type not defined");
        }

        RackMessage.CREATE.send(getEngine(), machineType.getValue(), machineId, index);

        machine.setIndex(index);
        machine.setType(machineType);
        machine.setEngine(getEngine());

        Tone tone = null;

        if (machineType == MachineType.BASSLINE) {
            tone = new BasslineTone((IBassline)machine);
        } else if (machineType == MachineType.BEATBOX) {
            tone = new RhythmSet(machine);
        } else if (machineType == MachineType.PCMSYNTH) {
            tone = new PCMSynthTone(machine);
        } else if (machineType == MachineType.SUBSYNTH) {
            tone = new SubSynthTone(machine);
        }

        toneAdd(index, tone);

        return tone;
    }

    private void toneAdd(int index, Tone tone) {
        tones.put(index, tone);
        machines.put(index, tone.getMachine());

        getController().getDispatcher().trigger(new OnSoundSourceToneAdd(tone));
    }

    private void toneRemove(Tone tone) {
        tones.remove(tone.getIndex());
        machines.remove(tone.getIndex());

        getController().getDispatcher().trigger(new OnSoundSourceToneRemove(tone));
    }

    //--------------------------------------------------------------------------
    // Public Observer API
    //--------------------------------------------------------------------------

    public static class OnSoundSourceToneAdd {
        private Tone tone;

        public Tone getTone() {
            return tone;
        }

        public OnSoundSourceToneAdd(Tone tone) {
            this.tone = tone;
        }
    }

    public static class OnSoundSourceToneRemove {
        private Tone tone;

        public Tone getTone() {
            return tone;
        }

        public OnSoundSourceToneRemove(Tone tone) {
            this.tone = tone;
        }
    }

    public static class OnSoundSourceInitialValue {
        private Object value;

        public Object getValue() {
            return value;
        }

        public OnSoundSourceInitialValue(Object value) {
            this.value = value;
        }
    }

    public static class OnSoundSourceInitialValueReset {
    }

    public static class OnSoundSourceClear {
    }

    public static class OnSoundSourceReset {
    }

    //--------------------------------------------------------------------------
    // Public Observer
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // Private Methods
    //--------------------------------------------------------------------------

    private int nextIndex() {
        int index = 0;
        for (index = 0; index < 10; index++) {
            if (!tones.containsKey(index))
                break;
        }
        return index;
    }

}
