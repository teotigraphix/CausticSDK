
package com.teotigraphix.caustk.track;

import java.util.Collection;

import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.controller.IControllerComponent;
import com.teotigraphix.caustk.sound.SoundSource.OnSoundSourceToneAdd;
import com.teotigraphix.caustk.sound.SoundSource.OnSoundSourceToneRemove;

public interface ITrackSequencer extends IControllerComponent {

    @Override
    IDispatcher getDispatcher();

    int getCurrentTrack();

    void setCurrentTrack(int currentTrack);

    int getCurrentBank();

    int getCurrentBank(int trackIndex);

    int getCurrentPattern();

    int getCurrentPattern(int trackIndex);

    /**
     * Returns whether the sequencer contains tracks.
     * <p>
     * A track is created in response to the {@link OnSoundSourceToneAdd} event,
     * a track is removed in response to the {@link OnSoundSourceToneRemove}
     * Event.
     */
    boolean hasTracks();

    Collection<TrackChannel> getTracks();

    TrackChannel getTrack();

    TrackChannel getTrack(int index);

    public static class OnTrackSequencerLoad {

    }

    public static class OnTrackSequencerCurrentTrackChange {

        private int index;

        public int getIndex() {
            return index;
        }

        private int oldIndex;

        public int getOldIndex() {
            return oldIndex;
        }

        public OnTrackSequencerCurrentTrackChange(int index, int oldIndex) {
            this.index = index;
            this.oldIndex = oldIndex;
        }
    }

}
