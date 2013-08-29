
package com.teotigraphix.caustk.track;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.track.TrackChannel.OnTrackChannelBankChange;
import com.teotigraphix.caustk.track.TrackChannel.OnTrackChannelPatternChange;

public class TrackSequencerHandlers {

    private final TrackSequencer trackSequencer;

    private final IDispatcher getDispatcher() {
        return trackSequencer.getDispatcher();
    }

    public TrackSequencerHandlers(TrackSequencer trackSequencer) {
        this.trackSequencer = trackSequencer;

        registerObservers();
    }

    void registerObservers() {
        getDispatcher().register(OnTrackChannelBankChange.class, bankChange);
        getDispatcher().register(OnTrackChannelPatternChange.class, patternChange);
    }

    void unregisterObservers() {
        getDispatcher().unregister(bankChange);
        getDispatcher().unregister(patternChange);
    }

    //--------------------------------------------------------------------------
    // Handlers
    //--------------------------------------------------------------------------

    private EventObserver<OnTrackChannelBankChange> bankChange = new EventObserver<OnTrackChannelBankChange>() {
        @Override
        public void trigger(OnTrackChannelBankChange object) {
            final TrackChannel channel = object.getTrack();
            int bank = channel.getCurrentBank();
            channel.getTone().getPatternSequencer().setSelectedBank(bank);
        }
    };

    private EventObserver<OnTrackChannelPatternChange> patternChange = new EventObserver<OnTrackChannelPatternChange>() {
        @Override
        public void trigger(OnTrackChannelPatternChange object) {
            final TrackChannel channel = object.getTrack();
            int pattern = channel.getCurrentPattern();
            channel.getTone().getPatternSequencer().setSelectedPattern(pattern);
        }
    };
}
