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

package com.teotigraphix.caustk.rack;

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
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.library.item.LibraryScene;
import com.teotigraphix.caustk.rack.tone.BasslineTone;
import com.teotigraphix.caustk.rack.tone.BeatboxTone;
import com.teotigraphix.caustk.rack.tone.EightBitSynth;
import com.teotigraphix.caustk.rack.tone.FMSynthTone;
import com.teotigraphix.caustk.rack.tone.ModularTone;
import com.teotigraphix.caustk.rack.tone.OrganTone;
import com.teotigraphix.caustk.rack.tone.PCMSynthTone;
import com.teotigraphix.caustk.rack.tone.PadSynthTone;
import com.teotigraphix.caustk.rack.tone.SubSynthTone;
import com.teotigraphix.caustk.rack.tone.Tone;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;
import com.teotigraphix.caustk.rack.tone.ToneType;
import com.teotigraphix.caustk.rack.tone.ToneUtils;
import com.teotigraphix.caustk.rack.tone.VocoderTone;
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

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

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

    @Override
    public List<Tone> findToneStartsWith(String name) {
        List<Tone> result = new ArrayList<Tone>();
        for (Tone tone : getTones()) {
            if (tone.getName().startsWith(name))
                result.add(tone);
        }
        return result;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    SoundSource() {
    }

    public SoundSource(Rack rack) {
        super(rack);
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void createScene(LibraryScene libraryScene) throws CausticException {
        //        // make tones
        //        for (ToneDescriptor descriptor : libraryScene.getSoundSourceDescriptor().getDescriptors()
        //                .values()) {
        //            Tone tone = getRack().createTone(descriptor);
        //            UUID patchId = descriptor.getPatchId();
        //            Library library = getRack().getLibrary();
        //            if (library != null && patchId != null) {
        //                LibraryPatch libraryPatch = library.findPatchById(patchId);
        //                library.assignPatch(tone, libraryPatch);
        //            }
        //
        //            //            SoundMixerChannelDescriptor channelDescriptor = libraryScene.getSoundSourceDescriptor()
        //            //                    .getChannels().get(tone.getIndex());
        //            //
        //            //            channelDescriptor.update(tone);
        //        }
        //
        //        //        SoundMixerState mixerState = scene.getSoundMixerState();
        //        //        SoundMixerModel model = controller.getSerializeService().fromString(
        //        //                mixerState.getData(), SoundMixerModel.class);
        //        //        model.update();
    }

    @SuppressWarnings("unchecked")
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

    public <T extends Tone> T createTone(String name, Class<? extends Tone> toneClass)
            throws CausticException {
        return createTone(nextIndex(), name, toneClass);
    }

    public Tone createTone(String name, ToneType toneType) throws CausticException {
        return createTone(new ToneDescriptor(nextIndex(), name, toneType));
    }

    public Tone createTone(int index, String name, ToneType toneType) throws CausticException {
        return createTone(new ToneDescriptor(index, name, toneType));
    }

    public Tone createTone(ToneDescriptor descriptor) throws CausticException {
        Tone tone = createSynthChannel(descriptor.getIndex(), descriptor.getName(),
                descriptor.getToneType());
        return tone;
    }

    public void destroyTone(int index) {
        destroyTone(getTone(index));
    }

    public void destroyTone(Tone tone) {
        int index = tone.getIndex();
        RackMessage.REMOVE.send(getRack(), index);
        toneRemove(tone);
    }

    public void loadSongRaw(File causticFile) throws CausticException {
        RackMessage.LOAD_SONG.send(getRack(), causticFile.getAbsolutePath());
    }

    public void loadSong(File causticFile) throws CausticException {
        loadSongRaw(causticFile);
        loadMachines();
    }

    public File saveSong(String name) {
        RackMessage.SAVE_SONG.send(getRack(), name);
        return RuntimeUtils.getCausticSongFile(name);
    }

    public File saveSongAs(File file) throws IOException {
        File song = saveSong(file.getName().replace(".caustic", ""));
        FileUtils.copyFileToDirectory(song, file.getParentFile(), true);
        song.delete();
        return file;
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
                ToneDescriptor descriptor = new ToneDescriptor(i, name, toneType);
                // have to call from rack for callbacks on mixer
                // XXX this is wrong, need to figure out proper callback mechanism
                tone = getRack().createTone(descriptor);
            } catch (CausticException e) {
                e.printStackTrace();
            }
        }
        restoring = false;
    }

    //--------------------------------------------------------------------------
    // Private Methods
    //--------------------------------------------------------------------------

    private void initializeTone(Tone tone, String toneName, ToneType toneType, int index) {
        tone.setId(UUID.randomUUID());
        tone.setIndex(index);
        ToneUtils.setName(tone, toneName);
    }

    private void toneAdd(int index, Tone tone) {
        tones.put(index, tone);
    }

    private void toneRemove(Tone tone) {
        tones.remove(tone.getIndex());
    }

    private int nextIndex() {
        int index = 0;
        for (index = 0; index < 15; index++) {
            if (!tones.containsKey(index))
                break;
        }
        return index;
    }

    @Override
    public void restore() {
    }

}
