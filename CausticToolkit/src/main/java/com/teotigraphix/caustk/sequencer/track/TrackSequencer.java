
package com.teotigraphix.caustk.sequencer.track;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.androidtransfuse.event.EventObserver;
import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.controller.ControllerComponent;
import com.teotigraphix.caustk.controller.ControllerComponentState;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.sequencer.ISystemSequencer;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.OnSystemSequencerBeatChange;
import com.teotigraphix.caustk.sequencer.ITrackSequencer;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.sound.source.SoundSource.OnSoundSourceToneAdd;
import com.teotigraphix.caustk.sound.source.SoundSource.OnSoundSourceToneRemove;
import com.teotigraphix.caustk.tone.Tone;

/**
 * @see OnTrackSequencerLoad
 */
public class TrackSequencer extends ControllerComponent implements ITrackSequencer {

    @SuppressWarnings("unused")
    private TrackSequencerHandlers handlers;

    @Override
    protected Class<? extends ControllerComponentState> getStateType() {
        return TrackSequencerState.class;
    }

    //----------------------------------
    // trackSong
    //----------------------------------

    private TrackSong trackSong;

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
    public Collection<TrackChannel> getTracks() {
        return trackSong.getTracks();
    }

    @Override
    public TrackChannel getSelectedTrack() {
        return trackSong.getSelectedTrack();
    }

    @Override
    public TrackChannel getTrack(int index) {
        return trackSong.getTrack(index);
    }

    public TrackChannel findTrack(int index) {
        return trackSong.findTrack(index);
    }

    public TrackPhrase getPhrase(int toneIndex, int bankIndex, int patterIndex) {
        return trackSong.getPhrase(toneIndex, bankIndex, patterIndex);
    }

    //----------------------------------
    // state
    //----------------------------------

    final TrackSequencerState getState() {
        return (TrackSequencerState)getInternalState();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public TrackSequencer(ICaustkController controller) {
        super(controller);
        handlers = new TrackSequencerHandlers(this);
        // DUMMY
        trackSong = new TrackSong();
    }

    @Override
    public void onRegister() {
        super.onRegister();

        final ISoundSource soundSource = getController().getSoundSource();
        soundSource.getDispatcher().register(OnSoundSourceToneAdd.class,
                new EventObserver<OnSoundSourceToneAdd>() {
                    @Override
                    public void trigger(OnSoundSourceToneAdd object) {
                        trackAdd(object.getTone());
                    }
                });

        soundSource.getDispatcher().register(OnSoundSourceToneRemove.class,
                new EventObserver<OnSoundSourceToneRemove>() {
                    @Override
                    public void trigger(OnSoundSourceToneRemove object) {
                        trackRemove(object.getTone());
                    }
                });

        final ISystemSequencer systemSequencer = getController().getSystemSequencer();

        systemSequencer.getDispatcher().register(OnSystemSequencerBeatChange.class,
                new EventObserver<OnSystemSequencerBeatChange>() {
                    @Override
                    public void trigger(OnSystemSequencerBeatChange object) {
                        //System.out.println(object.getBeat());
                        getSelectedTrack().getPhrase().onBeatChange(object.getBeat());
                    }
                });
    }

    @Override
    protected void closeProject(Project project) {

    }

    @Override
    public void create(File songFile) throws IOException {
        if (trackSong != null) {

        }
        // all songs are relative to the current projects location
        File localFile = getController().getProjectManager().getProject()
                .getResource(songFile.getPath());
        File absoluteTargetSongFile = localFile.getAbsoluteFile();

        // save the project relative path of the song
        getState().setSongFile(localFile);
        File absoluteSongDir = absoluteTargetSongFile.getParentFile();

        if (!absoluteSongDir.exists())
            FileUtils.forceMkdir(absoluteSongDir);

        trackSong = new TrackSong(songFile);
        trackSong.wakeup(getController());
        getDispatcher().trigger(
                new OnTrackSequencerTrackSongChange(TrackSongChangeKind.Create, trackSong));
        saveTrackSong();
    }

    private void saveTrackSong() throws IOException {
        if (!trackSong.exists())
            return;

        File localFile = getController().getProjectManager().getProject()
                .getResource(trackSong.getFile().getPath());
        File absoluteTargetSongFile = localFile.getAbsoluteFile();
        getController().getSerializeService().save(absoluteTargetSongFile, trackSong);
        getDispatcher().trigger(
                new OnTrackSequencerTrackSongChange(TrackSongChangeKind.Save, trackSong));
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

    @Override
    protected void createProject(Project project) {
    }

    @Override
    protected void loadState(Project project) {
        super.loadState(project);
        File file = getState().getSongFile();
        if (file != null) {
            trackSong = getController().getSerializeService().fromFile(file, TrackSong.class);
            getDispatcher().trigger(
                    new OnTrackSequencerTrackSongChange(TrackSongChangeKind.Load, trackSong));
        }
        getDispatcher().trigger(new OnTrackSequencerLoad());
    }

    @Override
    protected void loadComplete(Project project) {
        if (trackSong.exists()) {
            File causticFile = trackSong.getAbsoluteCausticFile();
            try {
                getController().getSoundSource().loadSong(causticFile);
            } catch (CausticException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void saveState(Project project) {
        // save the state object
        super.saveState(project);
        try {
            saveTrackSong();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class TrackSequencerState extends ControllerComponentState {

        private File songFile;

        public File getSongFile() {
            return songFile;
        }

        public void setSongFile(File value) {
            songFile = value;
        }

        public TrackSequencerState() {
            super();
        }

        public TrackSequencerState(ICaustkController controller) {
            super(controller);
        }

        @Override
        public void wakeup(ICaustkController controller) {
            super.wakeup(controller);
        }
    }

}
