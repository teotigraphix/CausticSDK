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

package com.teotigraphix.caustk.sequencer;

import java.io.File;
import java.util.List;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.components.SynthComponent;
import com.teotigraphix.caustk.library.EffectMixerState;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryScene;
import com.teotigraphix.caustk.library.SoundMixerState;
import com.teotigraphix.caustk.library.SoundSourceState;
import com.teotigraphix.caustk.library.SoundSourceState.RackInfoItem;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.ToneDescriptor;

public class TrackUtils {

    //    public static void refreshTrackSongInfos(ICaustkController controller, TrackSong trackSong) {
    //        trackSong.setRackInfo(LibrarySerializerUtils.createRackInfo(controller));
    //        trackSong.setMixerInfo(LibrarySerializerUtils.createMixerPanelInfo(controller
    //                .getSoundMixer().getMixerPanel()));
    //        trackSong.setEffectRackInfo(LibrarySerializerUtils.createEffectRackInfo(controller
    //                .getSoundMixer().getEffectsRack()));
    //    }

    /**
     * Copies the {@link LibraryScene} into the core {@link ICaustkSoundSource}
     * initializing the tones, mixer, effects rack.
     * 
     * @param scene The {@link LibraryScene} to copy.
     */
    @SuppressWarnings("unused")
    public static void assignScene(ICaustkController controller, TrackSong trackSong,
            LibraryScene libraryScene) {
        SoundSourceState rackInfo = libraryScene.getSoundSourceState();
        SoundMixerState mixerInfo = libraryScene.getSoundMixerState();
        EffectMixerState effectRackInfo = libraryScene.getEffectMixerState();

        // Restore the master channel mixer settings
        //        controller.getSoundMixer().pasteMasterChannel(mixerInfo.getMasterMemento());

        // Create the scene Tones
        List<RackInfoItem> items = rackInfo.getItems();

        trackSong.setNumTracks(6);

        // loop through the Track instances and get the info item for the index
        for (Track track : trackSong.getTracks()) {
            int index = track.getIndex();
            RackInfoItem item = items.get(index);

            // no machine existed in the scene when created
            if (!item.isActive())
                continue;

            // 1) Create the Tone for the Track
            ToneDescriptor descriptor = item.createDescriptor();
            Tone tone = null;
            try {
                tone = controller.getSoundSource().createTone(descriptor);
            } catch (CausticException e) {
                e.printStackTrace();
            }

            //            // 2) Set the mixer settings
            //            IMemento[] channels = mixerInfo.getMemento().getChild("channels")
            //                    .getChildren("channel");
            //            if (channels.length > 0)
            //                controller.getSoundMixer().pasteMixerChannel(tone.getMachine(), channels[index]);
            //
            //            // 3) Add the effects
            //            IMemento[] children = effectRackInfo.getMemento().getChildren("channel");
            //            if (children.length > 0)
            //                controller.getSoundMixer().pasteEffectChannel(tone.getMachine(), children[index]);
        }
    }

    /**
     * @param controller
     * @param track
     * @param libraryPatch
     */
    public static void assignPatch(ICaustkController controller, Track track,
            LibraryPatch libraryPatch) {
        // get the current Library
        Library library = controller.getLibraryManager().getSelectedLibrary();
        // get the absolute preset File for the LibraryPatch
        File file = library.getPresetFile(libraryPatch.getPresetFile());
        // get the Machine for the Tone
        Tone machine = getTone(controller, track);
        // load the Machine witht eh preset File
        machine.getComponent(SynthComponent.class).loadPreset(file.getAbsolutePath());
        // restore the Machine's values
        machine.restore();
    }

    public static void assignNotes(ICaustkController controller, Track track,
            TrackPhrase trackPhrase) {
        //        IPatternSequencer2 sequencer = getPatternSequencer(controller, track);
        //
        //        int lastBank = sequencer.getSelectedBank();
        //        int lastPattern = sequencer.getSelectedIndex();
        //        sequencer.setSelectedPattern(trackPhrase.getBankIndex(), trackPhrase.getPatternIndex());
        //
        //        sequencer.setLength(trackPhrase.getNumMeasures());
        //
        //        PatternSequencerUtils.applyNoteData(sequencer, trackPhrase.getNoteData());
        //
        //        sequencer.setSelectedPattern(lastBank, lastPattern);
    }

    public static Tone getTone(ICaustkController controller, Track track) {
        Tone tone = controller.getSoundSource().getTone(track.getIndex());
        return tone;
    }

    //    public static IPatternSequencer2 getPatternSequencer(ICaustkController controller, Track track) {
    //        return getMachine(controller, track).getPatternSequencer();
    //    }
}
