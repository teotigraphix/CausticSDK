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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.application.Dispatcher;
import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerBase;
import com.teotigraphix.caustk.controller.SubControllerModel;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.BeatboxTone;
import com.teotigraphix.caustk.tone.PCMSynthTone;
import com.teotigraphix.caustk.tone.SubSynthTone;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.ToneDescriptor;
import com.teotigraphix.caustk.tone.ToneType;

public class SoundSource extends SubControllerBase implements ISoundSource {

    private int maxNumTones = 6; // 14

    @Override
    protected Class<? extends SubControllerModel> getModelType() {
        return SoundSourceModel.class;
    }

    SoundSourceModel getModel() {
        return (SoundSourceModel)getInternalModel();
    }

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // dispatcher
    //----------------------------------

    private final IDispatcher dispatcher;

    @Override
    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    //----------------------------------
    // transpose
    //----------------------------------

    private int transpose;

    @Override
    public int getTranspose() {
        return transpose;
    }

    @Override
    public void setTranspose(int value) {
        transpose = value;
    }

    //----------------------------------
    // tones
    //----------------------------------

    @Override
    public int getToneCount() {
        return getModel().getTones().size();
    }

    @Override
    public Collection<Tone> getTones() {
        return Collections.unmodifiableCollection(getModel().getTones().values());
    }

    @Override
    public Tone getTone(int index) {
        return getModel().getTones().get(index);
    }

    @Override
    public Tone getToneByName(String value) {
        for (Tone tone : getModel().getTones().values()) {
            if (tone.getName().equals(value))
                return tone;
        }
        return null;
    }

    public SoundSource(ICaustkController controller) {
        super(controller);

        dispatcher = new Dispatcher();

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

    @Override
    public Tone createTone(String name, ToneType toneType) throws CausticException {
        return createTone(new ToneDescriptor(nextIndex(), name, toneType));
    }

    @Override
    public Tone createTone(int index, String name, ToneType toneType) throws CausticException {
        return createTone(new ToneDescriptor(index, name, toneType));
    }

    @Override
    public Tone createTone(ToneDescriptor descriptor) throws CausticException {
        Tone tone = createSynthChannel(nextIndex(), descriptor.getName(), descriptor.getToneType());
        return tone;
    }

    @Override
    public void destroyTone(int index) {
        destroyTone(getTone(index));
    }

    public void destroyTone(Tone tone) {
        int index = tone.getIndex();
        RackMessage.REMOVE.send(getController(), index);
        toneRemove(tone);
    }

    @Override
    public void clearAndReset() {
        getController().getDispatcher().trigger(new OnSoundSourceClear());

        ArrayList<Tone> remove = new ArrayList<Tone>(getModel().getTones().values());
        for (Tone tone : remove) {
            destroyTone(tone);
        }

        RackMessage.BLANKRACK.send(getController());

        getController().getDispatcher().trigger(new OnSoundSourceReset());
    }

    //--------------------------------------------------------------------------
    // Protected Method API
    //--------------------------------------------------------------------------

    Tone createSynthChannel(int index, String toneName, ToneType toneType) throws CausticException {
        if (index > 13)
            throw new CausticException("Only 14 machines allowed in a rack");

        if (getModel().getTones().containsKey(index))
            throw new CausticException("{" + index + "} tone is already defined");

        RackMessage.CREATE.send(getController(), toneType.getValue(), toneName, index);

        Tone tone = null;
        switch (toneType) {
            case Bassline:
                tone = new BasslineTone(getController());
                initializeTone(tone, toneName, toneType, index);
                SoundSourceUtils.setup((BasslineTone)tone);
                break;
            case Beatbox:
                tone = new BeatboxTone(getController());
                initializeTone(tone, toneName, toneType, index);
                SoundSourceUtils.setup((BeatboxTone)tone);
                break;
            case PCMSynth:
                tone = new PCMSynthTone(getController());
                initializeTone(tone, toneName, toneType, index);
                SoundSourceUtils.setup((PCMSynthTone)tone);
                break;
            case SubSynth:
                tone = new SubSynthTone(getController());
                initializeTone(tone, toneName, toneType, index);
                SoundSourceUtils.setup((SubSynthTone)tone);
                break;
            case PadSynth:
                break;
            case Organ:
                break;
            case Vocoder:
                break;
            case EightBitSynth:
                break;
            case Modular:
                break;
            case FMSynth:
                break;
            default:
                break;
        }

        toneAdd(index, tone);

        return tone;
    }

    private void initializeTone(Tone tone, String toneName, ToneType toneType, int index) {
        tone.setId(UUID.randomUUID());
        tone.setName(toneName);
        tone.setToneType(toneType);
        tone.setIndex(index);
    }

    private void toneAdd(int index, Tone tone) {
        getModel().getTones().put(index, tone);
        getController().getDispatcher().trigger(new OnSoundSourceToneAdd(tone));
    }

    private void toneRemove(Tone tone) {
        getModel().getTones().remove(tone.getIndex());
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
        for (index = 0; index < 15; index++) {
            if (!getModel().getTones().containsKey(index))
                break;
        }
        return index;
    }

    // XXX implement saveSong() if absolutePath, move file from caustic/songs to location

    @Override
    public void loadSong(File causticFile) throws CausticException {
        clearAndReset();

        RackMessage.LOAD_SONG.send(getController(), causticFile.getAbsolutePath());

        restore();

        getDispatcher().trigger(new OnSoundSourceSongLoad());
    }

    @Override
    public void restore() {
        for (int i = 0; i < maxNumTones; i++) {
            String name = RackMessage.QUERY_MACHINE_NAME.queryString(getController(), i);
            String type = RackMessage.QUERY_MACHINE_TYPE.queryString(getController(), i);
            if (name == null || name.equals(""))
                continue;

            ToneType toneType = ToneType.fromString(type);
            Tone tone = null;
            try {
                tone = createTone(i, name, toneType);
            } catch (CausticException e) {
                e.printStackTrace();
            }
            tone.restore();
        }
    }

}
