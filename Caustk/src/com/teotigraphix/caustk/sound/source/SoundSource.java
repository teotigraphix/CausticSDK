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

package com.teotigraphix.caustk.sound.source;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IRack;
import com.teotigraphix.caustk.controller.core.Rack;
import com.teotigraphix.caustk.controller.core.RackComponent;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.library.core.Library;
import com.teotigraphix.caustk.library.item.LibraryPatch;
import com.teotigraphix.caustk.library.item.LibraryScene;
import com.teotigraphix.caustk.library.item.SoundMixerChannelDescriptor;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.BeatboxTone;
import com.teotigraphix.caustk.tone.EightBitSynth;
import com.teotigraphix.caustk.tone.FMSynthTone;
import com.teotigraphix.caustk.tone.ModularTone;
import com.teotigraphix.caustk.tone.OrganTone;
import com.teotigraphix.caustk.tone.PCMSynthTone;
import com.teotigraphix.caustk.tone.PadSynthTone;
import com.teotigraphix.caustk.tone.SubSynthTone;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.ToneDescriptor;
import com.teotigraphix.caustk.tone.ToneType;
import com.teotigraphix.caustk.tone.ToneUtils;
import com.teotigraphix.caustk.tone.VocoderTone;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public class SoundSource extends RackComponent implements ISoundSource {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private transient int maxNumTones = 14;

    private transient boolean restoring;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private Map<Integer, Tone> tones = new HashMap<Integer, Tone>();

    @Tag(101)
    private int transpose;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // transpose
    //----------------------------------

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
        return tones.size();
    }

    @Override
    public boolean hasTone(int index) {
        return tones.containsKey(index);
    }

    @Override
    public Collection<Tone> getTones() {
        return Collections.unmodifiableCollection(tones.values());
    }

    @Override
    public Tone getTone(int index) {
        return tones.get(index);
    }

    @Override
    public Tone getToneByName(String value) {
        for (Tone tone : tones.values()) {
            if (tone.getName().equals(value))
                return tone;
        }
        return null;
    }

    public SoundSource() {
    }

    public SoundSource(Rack rack) {
        super(rack);
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    @Override
    public List<Tone> findToneStartsWith(String name) {
        List<Tone> result = new ArrayList<Tone>();
        for (Tone tone : getTones()) {
            if (tone.getName().startsWith(name))
                result.add(tone);
        }
        return result;
    }

    @Override
    public void createScene(LibraryScene scene) throws CausticException {
        // make tones
        for (ToneDescriptor descriptor : scene.getSoundSourceDescriptor().getDescriptors().values()) {
            Tone tone = createTone(descriptor);
            UUID patchId = descriptor.getPatchId();
            Library library = getRack().getLibrary();
            if (library != null && patchId != null) {
                LibraryPatch libraryPatch = library.findPatchById(patchId);
                library.assignPatch(tone, libraryPatch);
            }

            SoundMixerChannelDescriptor channelDescriptor = scene.getSoundSourceDescriptor()
                    .getChannels().get(tone.getIndex());

            channelDescriptor.update(tone);
        }

        //        SoundMixerState mixerState = scene.getSoundMixerState();
        //        SoundMixerModel model = controller.getSerializeService().fromString(
        //                mixerState.getData(), SoundMixerModel.class);
        //        model.update();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Tone> T createTone(String data) throws CausticException {
        Tone tone = null;
        //        try {
        //            tone = getController().getSerializeService().fromString(data,
        //                    ToneUtils.getToneClass(data));
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        //
        //        int index = nextIndex();
        //        tone.setIndex(index);
        //
        //        RackMessage.CREATE.send(getController(), tone.getToneType().getValue(), tone.getName(),
        //                index);
        //
        //        toneAdd(index, tone);
        //
        return (T)tone;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Tone> T createTone(int index, String name, Class<? extends Tone> toneClass)
            throws CausticException {
        T tone = null;
        try {
            Constructor<? extends Tone> constructor = toneClass.getConstructor(IRack.class);
            tone = (T)constructor.newInstance(getRack());
            initializeTone(tone, name, tone.getToneType(), index);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        SoundSourceUtils.setup(toneClass.cast(tone));

        RackMessage.CREATE.send(getRack(), tone.getToneType().getValue(), tone.getName(),
                tone.getIndex());

        toneAdd(index, tone);

        return tone;
    }

    @Override
    public <T extends Tone> T createTone(String name, Class<? extends Tone> toneClass)
            throws CausticException {
        return createTone(nextIndex(), name, toneClass);
    }

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
        Tone tone = createSynthChannel(descriptor.getIndex(), descriptor.getName(),
                descriptor.getToneType());
        return tone;
    }

    @Override
    public void destroyTone(int index) {
        destroyTone(getTone(index));
    }

    public void destroyTone(Tone tone) {
        int index = tone.getIndex();
        RackMessage.REMOVE.send(getRack(), index);
        toneRemove(tone);
    }

    @Override
    public void clearAndReset() {
        getRack().trigger(new OnSoundSourceClear());

        ArrayList<Tone> remove = new ArrayList<Tone>(tones.values());
        for (Tone tone : remove) {
            toneRemove(tone);
        }

        RackMessage.BLANKRACK.send(getRack());

        getRack().trigger(new OnSoundSourceReset());
    }

    //--------------------------------------------------------------------------
    // Protected Method API
    //--------------------------------------------------------------------------

    Tone createSynthChannel(int index, String toneName, ToneType toneType) throws CausticException {
        if (index > 13)
            throw new CausticException("Only 14 machines allowed in a rack");

        if (tones.containsKey(index))
            throw new CausticException("{" + index + "} tone is already defined");

        if (!restoring)
            RackMessage.CREATE.send(getRack(), toneType.getValue(), toneName, index);

        final IRack rack = getRack();

        Tone tone = null;
        switch (toneType) {
            case Bassline:
                tone = new BasslineTone(rack);
                initializeTone(tone, toneName, toneType, index);
                SoundSourceUtils.setup(tone);
                break;
            case Beatbox:
                tone = new BeatboxTone(rack);
                initializeTone(tone, toneName, toneType, index);
                SoundSourceUtils.setup(tone);
                break;
            case PCMSynth:
                tone = new PCMSynthTone(rack);
                initializeTone(tone, toneName, toneType, index);
                SoundSourceUtils.setup(tone);
                break;
            case SubSynth:
                tone = new SubSynthTone(rack);
                initializeTone(tone, toneName, toneType, index);
                SoundSourceUtils.setup(tone);
                break;
            case PadSynth:
                tone = new PadSynthTone(rack);
                initializeTone(tone, toneName, toneType, index);
                SoundSourceUtils.setup(tone);
                break;
            case Organ:
                tone = new OrganTone(rack);
                initializeTone(tone, toneName, toneType, index);
                SoundSourceUtils.setup(tone);
                break;
            case Vocoder:
                tone = new VocoderTone(rack);
                initializeTone(tone, toneName, toneType, index);
                SoundSourceUtils.setup(tone);
                break;
            case EightBitSynth:
                tone = new EightBitSynth(rack);
                initializeTone(tone, toneName, toneType, index);
                SoundSourceUtils.setup(tone);
                break;
            case Modular:
                tone = new ModularTone(rack);
                initializeTone(tone, toneName, toneType, index);
                SoundSourceUtils.setup(tone);
                break;
            case FMSynth:
                tone = new FMSynthTone(rack);
                initializeTone(tone, toneName, toneType, index);
                SoundSourceUtils.setup(tone);
                break;
            default:
                break;
        }

        toneAdd(index, tone);

        return tone;
    }

    private void initializeTone(Tone tone, String toneName, ToneType toneType, int index) {
        tone.setId(UUID.randomUUID());
        tone.setIndex(index);
        ToneUtils.setName(tone, toneName);
    }

    private void toneAdd(int index, Tone tone) {
        tones.put(index, tone);
        getRack().getLogger().err("SoundSource", "toneAdd()");
        getRack().trigger(new OnSoundSourceToneAdd(tone));
    }

    private void toneRemove(Tone tone) {
        tones.remove(tone.getIndex());
        getRack().trigger(new OnSoundSourceToneRemove(tone));
    }

    //--------------------------------------------------------------------------
    // Public Observer API
    //--------------------------------------------------------------------------

    /**
     * Dispatcher: {@link ICaustkController}
     */
    public static class OnSoundSourceToneAdd {
        private Tone tone;

        public Tone getTone() {
            return tone;
        }

        public OnSoundSourceToneAdd(Tone tone) {
            this.tone = tone;
        }
    }

    /**
     * Dispatcher: {@link ICaustkController}
     */
    public static class OnSoundSourceToneRemove {
        private Tone tone;

        public Tone getTone() {
            return tone;
        }

        public OnSoundSourceToneRemove(Tone tone) {
            this.tone = tone;
        }
    }

    /**
     * Dispatcher: {@link ICaustkController}
     */
    public static class OnSoundSourceInitialValue {
        private Object value;

        public Object getValue() {
            return value;
        }

        public OnSoundSourceInitialValue(Object value) {
            this.value = value;
        }
    }

    /**
     * Dispatcher: {@link ICaustkController}
     */
    public static class OnSoundSourceInitialValueReset {
    }

    /**
     * Dispatcher: {@link ICaustkController}
     */
    public static class OnSoundSourceClear {
    }

    /**
     * Dispatcher: {@link ICaustkController}
     */
    public static class OnSoundSourceReset {
    }

    //--------------------------------------------------------------------------
    // Private Methods
    //--------------------------------------------------------------------------

    private int nextIndex() {
        int index = 0;
        for (index = 0; index < 15; index++) {
            if (!tones.containsKey(index))
                break;
        }
        return index;
    }

    @Override
    public void loadSongRaw(File causticFile) throws CausticException {
        RackMessage.LOAD_SONG.send(getRack(), causticFile.getAbsolutePath());
    }

    @Override
    public void loadSong(File causticFile) throws CausticException {
        loadSongRaw(causticFile);
        loadMachines();
        getRack().trigger(new OnSoundSourceSongLoad(causticFile));
    }

    @Override
    public File saveSong(String name) {
        RackMessage.SAVE_SONG.send(getRack(), name);
        return RuntimeUtils.getCausticSongFile(name);
    }

    @Override
    public File saveSongAs(File file) throws IOException {
        File song = saveSong(file.getName().replace(".caustic", ""));
        FileUtils.copyFileToDirectory(song, file.getParentFile(), true);
        song.delete();
        return file;
    }

    @Override
    public void restore() {

    }

    protected void loadMachines() {
        restoring = true;
        for (int i = 0; i < maxNumTones; i++) {
            String name = RackMessage.QUERY_MACHINE_NAME.queryString(getRack(), i);
            String type = RackMessage.QUERY_MACHINE_TYPE.queryString(getRack(), i);
            if (name == null || name.equals(""))
                continue;

            name = name.replace(" ", "_");
            ToneType toneType = ToneType.fromString(type);

            @SuppressWarnings("unused")
            Tone tone = null;
            try {
                getRack().getLogger().log("SoundSource",
                        "Restore machine from load: " + name + ":" + type);
                tone = createTone(i, name, toneType);
            } catch (CausticException e) {
                e.printStackTrace();
            }
        }
        restoring = false;
    }
}
