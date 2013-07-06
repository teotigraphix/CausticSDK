
package com.teotigraphix.caustk.project;

import java.io.File;
import java.util.List;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.internal.sequencer.PatternSequencerUtils;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.sequencer.IPatternSequencer2;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryScene;
import com.teotigraphix.caustk.library.LibrarySerializerUtils;
import com.teotigraphix.caustk.library.vo.EffectRackInfo;
import com.teotigraphix.caustk.library.vo.MixerPanelInfo;
import com.teotigraphix.caustk.library.vo.RackInfo;
import com.teotigraphix.caustk.library.vo.RackInfo.RackInfoItem;
import com.teotigraphix.caustk.sound.ICaustkSoundSource;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.ToneDescriptor;

public class TrackUtils {

    public static void refreshTrackSongInfos(ICaustkController controller, TrackSong trackSong) {
        trackSong.setRackInfo(LibrarySerializerUtils.createRackInfo(controller));
        trackSong.setMixerInfo(LibrarySerializerUtils.createMixerPanelInfo(controller
                .getSoundMixer().getMixerPanel()));
        trackSong.setEffectRackInfo(LibrarySerializerUtils.createEffectRackInfo(controller
                .getSoundMixer().getEffectsRack()));
    }

    /**
     * Copies the {@link LibraryScene} into the core {@link ICaustkSoundSource}
     * initializing the tones, mixer, effects rack.
     * 
     * @param scene The {@link LibraryScene} to copy.
     */
    public static void assignScene(ICaustkController controller, TrackSong trackSong,
            LibraryScene libraryScene) {
        RackInfo rackInfo = libraryScene.getRackInfo();
        MixerPanelInfo mixerInfo = libraryScene.getMixerInfo();
        EffectRackInfo effectRackInfo = libraryScene.getEffectRackInfo();

        // Restore the master channel mixer settings
        controller.getSoundMixer().pasteMasterChannel(mixerInfo.getMasterMemento());

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
                tone = controller.getSoundSource().create(descriptor);
            } catch (CausticException e) {
                e.printStackTrace();
            }

            // 2) Set the mixer settings
            IMemento[] channels = mixerInfo.getMemento().getChild("channels")
                    .getChildren("channel");
            if (channels.length > 0)
                controller.getSoundMixer().pasteMixerChannel(tone.getMachine(), channels[index]);

            // 3) Add the effects
            IMemento[] children = effectRackInfo.getMemento().getChildren("channel");
            if (children.length > 0)
                controller.getSoundMixer().pasteEffectChannel(tone.getMachine(), children[index]);
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
        IMachine machine = getMachine(controller, track);
        // load the Machine witht eh preset File
        machine.loadPreset(file.getAbsolutePath());
        // restore the Machine's values
        machine.restore();
    }

    public static void assignNotes(ICaustkController controller, Track track,
            TrackPhrase trackPhrase) {
        IPatternSequencer2 sequencer = getPatternSequencer(controller, track);

        int lastBank = sequencer.getSelectedBank();
        int lastPattern = sequencer.getSelectedIndex();
        sequencer.setSelectedPattern(trackPhrase.getBankIndex(), trackPhrase.getPatternIndex());

        sequencer.setLength(trackPhrase.getNumMeasures());

        PatternSequencerUtils.applyNoteData(sequencer, trackPhrase.getNoteData());

        sequencer.setSelectedPattern(lastBank, lastPattern);
    }

    public static Tone getTone(ICaustkController controller, Track track) {
        Tone tone = controller.getSoundSource().getTone(track.getIndex());
        return tone;
    }

    public static IMachine getMachine(ICaustkController controller, Track track) {
        return getTone(controller, track).getMachine();
    }

    public static IPatternSequencer2 getPatternSequencer(ICaustkController controller, Track track) {
        return getMachine(controller, track).getPatternSequencer();
    }
}
