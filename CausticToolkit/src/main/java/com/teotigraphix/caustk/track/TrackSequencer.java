
package com.teotigraphix.caustk.track;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ControllerComponent;
import com.teotigraphix.caustk.controller.ControllerComponentState;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.sound.SoundSource.OnSoundSourceToneAdd;
import com.teotigraphix.caustk.sound.SoundSource.OnSoundSourceToneRemove;
import com.teotigraphix.caustk.tone.Tone;

/**
 * @see OnTrackSequencerLoad
 */
public class TrackSequencer extends ControllerComponent implements ITrackSequencer {

    @SuppressWarnings("unused")
    private TrackSequencerHandlers handlers;

    private Map<Integer, TrackChannel> tracks = new HashMap<Integer, TrackChannel>();

    @Override
    protected Class<? extends ControllerComponentState> getStateType() {
        return TrackSequencerState.class;
    }

    //----------------------------------
    // currentTrack
    //----------------------------------

    private int currentTrack = -1;

    @Override
    public int getCurrentTrack() {
        return currentTrack;
    }

    @Override
    public void setCurrentTrack(int value) {
        if (currentTrack < 0 || currentTrack > 13)
            throw new IllegalArgumentException("Illigal track index");
        if (tracks.containsKey(value))
            throw new IllegalArgumentException("Track index does not exist;" + value);
        if (value == currentTrack)
            return;
        currentTrack = value;
        getDispatcher().trigger(new OnTrackSequencerCurrentTrackChange(currentTrack));
    }

    //----------------------------------
    // currentBank
    //----------------------------------

    @Override
    public int getCurrentBank() {
        return getCurrentBank(currentTrack);
    }

    @Override
    public int getCurrentBank(int trackIndex) {
        return getTrack(trackIndex).getCurrentBank();
    }

    //----------------------------------
    // currentPattern
    //----------------------------------

    @Override
    public int getCurrentPattern() {
        return getCurrentPattern(currentTrack);
    }

    @Override
    public int getCurrentPattern(int trackIndex) {
        return getTrack(trackIndex).getCurrentPattern();
    }

    //----------------------------------
    // track
    //----------------------------------

    @Override
    public Collection<TrackChannel> getTracks() {
        return tracks.values();
    }

    @Override
    public TrackChannel getSelectedTrack() {
        return getTrack(currentTrack);
    }

    @Override
    public TrackChannel getTrack(int index) {
        TrackChannel track = getState().getTracks().get(index);
        if (track == null) {
            track = new TrackChannel(getController(), index);
            getState().getTracks().put(index, track);
        }
        return track;
    }

    public TrackChannel findTrack(int index) {
        return tracks.get(index);
    }

    public TrackPhrase getPhrase(int toneIndex, int bankIndex, int patterIndex) {
        return getTrack(toneIndex).getPhrase(bankIndex, patterIndex);
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
    }

    protected void trackAdd(Tone tone) {
        TrackChannel channel = getTrack(tone.getIndex());
        tracks.put(tone.getIndex(), channel);
        channel.onAdded();
    }

    protected void trackRemove(Tone tone) {
        TrackChannel channel = tracks.remove(tone.getIndex());
        channel.onRemoved();
    }

    //--------------------------------------------------------------------------
    // Song
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // State
    //--------------------------------------------------------------------------

    @Override
    protected void loadState(Project project) {
        super.loadState(project);
        getDispatcher().trigger(new OnTrackSequencerLoad());
    }

    public static class TrackSequencerState extends ControllerComponentState {
        private Map<Integer, TrackChannel> tracks = new TreeMap<Integer, TrackChannel>();

        public Map<Integer, TrackChannel> getTracks() {
            return tracks;
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
            for (TrackChannel item : tracks.values()) {
                item.wakeup(controller);
            }
        }
    }

    @Override
    public boolean hasTracks() {
        return tracks.size() > 0;
    }

    /*
     * - dispatcher
     * 
     * - currentTrack
     * - currentBank
     * - currentPattern
     * 
     * -
     * 
     */

    /*
        trackSequencer.setCurrentTrack(0);
        TrackItem track = trackSequencer.getTrack();
        
        track.setCurrentBank(0);
        track.setCurrentPattern(2);
        
        Phrase phrase = track.getPhrase();
        phrase.setLength(2);
        phrase.setNoteData(2)
        phrase.setPlayMeasure(2)
        phrase.setEditMeasure(2)
    */
}
