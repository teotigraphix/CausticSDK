
package com.teotigraphix.caustk.sequencer.track;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.androidtransfuse.event.EventObserver;
import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.core.ControllerComponent;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.sequencer.ISystemSequencer;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.OnSystemSequencerBeatChange;
import com.teotigraphix.caustk.sequencer.ITrackSequencer;
import com.teotigraphix.caustk.sound.source.SoundSource.OnSoundSourceToneAdd;
import com.teotigraphix.caustk.sound.source.SoundSource.OnSoundSourceToneRemove;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.components.PatternSequencerComponent;
import com.teotigraphix.caustk.utils.PatternUtils;

/**
 * @see OnTrackSequencerLoad
 */
public class TrackSequencer extends ControllerComponent implements ITrackSequencer {

    @SuppressWarnings("unused")
    private TrackSequencerHandlers handlers;

    //----------------------------------
    // trackSong
    //----------------------------------

    private TrackSong trackSong;

    @Override
    public final TrackSong getTrackSong() {
        return trackSong;
    }

    //----------------------------------
    // currentTrack
    //----------------------------------

    @Override
    public int getCurrentTrack() {
        return trackSong.getCurrentTrack();
    }

    @Override
    public void setCurrentTrack(int value) {
        trackSong.setCurrentTrack(value);
    }

    //----------------------------------
    // currentBank
    //----------------------------------

    @Override
    public int getCurrentBank() {
        return trackSong.getCurrentBank();
    }

    @Override
    public int getCurrentBank(int trackIndex) {
        return trackSong.getCurrentBank(trackIndex);
    }

    //----------------------------------
    // currentPattern
    //----------------------------------

    @Override
    public int getCurrentPattern() {
        return trackSong.getCurrentPattern();
    }

    @Override
    public int getCurrentPattern(int trackIndex) {
        return trackSong.getCurrentPattern(trackIndex);
    }

    //----------------------------------
    // track
    //----------------------------------

    @Override
    public boolean hasTracks() {
        return trackSong.hasTracks();
    }

    @Override
    public Collection<Track> getTracks() {
        return trackSong.getTracks();
    }

    @Override
    public Track getSelectedTrack() {
        return trackSong.getSelectedTrack();
    }

    @Override
    public Track getTrack(Tone tone) {
        return getTrack(tone.getIndex());
    }

    @Override
    public Track getTrack(int index) {
        return trackSong.getTrack(index);
    }

    public Phrase getPhrase(int toneIndex, int bankIndex, int patterIndex) {
        return trackSong.getPhrase(toneIndex, bankIndex, patterIndex);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public TrackSequencer(ICaustkController controller) {
        super(controller);
        handlers = new TrackSequencerHandlers(this);
        // XXX Need to figure out how to avoid this (remove the facade?)
        // straight access into the TrackSong
        trackSong = new TrackSong();
        trackSong.wakeup(controller);
    }

    @Override
    public void onRegister() {
        getController().register(OnSoundSourceToneAdd.class,
                new EventObserver<OnSoundSourceToneAdd>() {
                    @Override
                    public void trigger(OnSoundSourceToneAdd object) {
                        trackAdd(object.getTone());
                    }
                });

        getController().register(OnSoundSourceToneRemove.class,
                new EventObserver<OnSoundSourceToneRemove>() {
                    @Override
                    public void trigger(OnSoundSourceToneRemove object) {
                        trackRemove(object.getTone());
                    }
                });

        final ISystemSequencer systemSequencer = getController().getSystemSequencer();

        systemSequencer.register(OnSystemSequencerBeatChange.class,
                new EventObserver<OnSystemSequencerBeatChange>() {
                    @Override
                    public void trigger(OnSystemSequencerBeatChange object) {
                        //System.err.println(object.getBeat());
                        getSelectedTrack().getPhrase().onBeatChange(object.getBeat());
                    }
                });
    }

    @Override
    public void load(File absoluteCausticFile) throws IOException {
        if (!absoluteCausticFile.exists())
            throw new IOException("File does not exist:" + absoluteCausticFile);

        // clear sound source model
        getController().getRack().clearAndReset();

        // load the machines, no restore
        try {
            getController().getRack().loadSong(absoluteCausticFile);
        } catch (CausticException e) {
            e.printStackTrace();
        }

        String name = absoluteCausticFile.getName().replace(".caustic", ".ctks");
        trackSong = createSong(new File(name));

        // load all tone patterns into TrackPhrase/PhraseNote
        for (Tone tone : getController().getRack().getTones()) {
            int toneIndex = tone.getIndex();
            Track channel = getTrack(toneIndex);
            PatternSequencerComponent sequencer = tone.getPatternSequencer();
            List<String> patterns = sequencer.getPatternListing();
            for (String patternName : patterns) {
                int bank = PatternUtils.toBank(patternName);
                int pattern = PatternUtils.toPattern(patternName);
                // get the note data
                Collection<Note> notes = sequencer.getNotes(bank, pattern);
                Phrase phrase = channel.getPhrase(bank, pattern);
                phrase.setLength(sequencer.getLength(bank, pattern));
                phrase.addNotes(notes);
            }
            tone.restore();
        }

        // load all song sequencer patterns into TrackItems
        String patterns = SequencerMessage.QUERY_PATTERN_EVENT.queryString(getController());
        if (patterns != null && !patterns.equals("")) {
            String[] items = patterns.split("\\|");
            for (String item : items) {
                String[] spilt = item.split(" ");
                int toneIndex = Integer.valueOf(spilt[0]);
                int start = Integer.valueOf(spilt[1]);
                int bank = Integer.valueOf(spilt[2]);
                int pattern = Integer.valueOf(spilt[3]);
                int end = Integer.valueOf(spilt[4]);

                Track channel = getTrack(toneIndex);
                Phrase phrase = channel.getPhrase(bank, pattern);
                int phraseLength = phrase.getLength();

                int span = end - start;
                // multiple patterns
                int loops = span / phraseLength;
                int remainder = span % phraseLength;
                if (remainder != 0) {
                    throw new RuntimeException("IMPL, remainder on Song sequencer");
                } else {
                    for (int i = start; i < start + loops; i++) {
                        try {
                            channel.addPhraseAt(i, 1, phrase, false);
                        } catch (CausticException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        //
        //        MasterMixer masterMixer = new MasterMixer(getController());
        //        getController().getSoundMixer().setMasterMixer(masterMixer);
        //
        //        // load all mixer channels
        //        for (Tone tone : getController().getSoundSource().getTones()) {
        //            masterMixer.addTone(tone);
        //        }
        //        // Restores volume, equalizer, limiter, delay, reverb
        //        // all channel's eq and effects
        //        masterMixer.restore();

    }

    @Override
    public TrackSong createSong(String relativePath) throws IOException {
        return createSong(new File(relativePath));
    }

    @Override
    public TrackSong createSong(File songFile) throws IOException {
        if (trackSong != null) {
            trackSong.dispose();
        }

        // all songs are relative to the current projects location
        // /MyProject/songs/MySong.ctks
        File localFile = getController().getProjectManager().getProject()
                .getAbsoluteResource(new File("songs", songFile.getPath()).getPath());

        // platform independent location
        File absoluteSongDir = localFile.getAbsoluteFile().getParentFile();

        // make the songs directory
        if (!absoluteSongDir.exists())
            FileUtils.forceMkdir(absoluteSongDir);

        trackSong = new TrackSong(songFile);
        trackSong.wakeup(getController());
        trigger(new OnTrackSongChange(TrackSongChangeKind.Create, trackSong));

        saveTrackSong();

        return trackSong;
    }

    private void saveTrackSong() throws IOException {
        if (!trackSong.exists())
            return;

        File absoluteTargetSongFile = getAbsoluteSongFile();
        getController().getSerializeService().save(absoluteTargetSongFile, trackSong);
        trigger(new OnTrackSongChange(TrackSongChangeKind.Save, trackSong));
    }

    protected File getAbsoluteSongFile() {
        File localFile = getController().getProjectManager().getProject()
                .getAbsoluteResource(new File("songs", trackSong.getFile().getPath()).getPath());
        File absoluteTargetSongFile = localFile.getAbsoluteFile();
        return absoluteTargetSongFile;
    }

    protected void trackAdd(Tone tone) {
        trackSong.toneAdd(tone);
    }

    protected void trackRemove(Tone tone) {
        trackSong.toneRemove(tone);
    }

    //--------------------------------------------------------------------------
    // Song
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // State
    //--------------------------------------------------------------------------

    //    @Override
    //    protected void loadState(Project project) {
    //        super.loadState(project);
    //        if (!trackSong.exists())
    //            return;
    //
    //        File file = getState().getSongFile();
    //        // XXX refactor and use with createSong() logic
    //        File localFile = getController().getProjectManager().getProject()
    //                .getAbsoluteResource(new File("songs", file.getPath()).getPath());
    //        File absoluteTargetSongFile = localFile.getAbsoluteFile();
    //        if (absoluteTargetSongFile != null) {
    //
    //            trackSong = getController().getSerializeService().fromFile(absoluteTargetSongFile,
    //                    TrackSong.class);
    //            getDispatcher().trigger(
    //                    new OnTrackSequencerTrackSongChange(TrackSongChangeKind.Load, trackSong));
    //        }
    //        getDispatcher().trigger(new OnTrackSequencerLoad());
    //    }
    //
    //    @Override
    //    protected void loadComplete(Project project) {
    //        if (trackSong.exists()) {
    //            File causticFile = trackSong.getAbsoluteCausticFile();
    //            try {
    //                getController().getSoundSource().loadSong(causticFile);
    //            } catch (CausticException e) {
    //                e.printStackTrace();
    //            }
    //        }
    //    }
    //
    //    @Override
    //    protected void saveState(Project project) {
    //        // save the state object
    //        super.saveState(project);
    //        try {
    //            saveTrackSong();
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    }
}
